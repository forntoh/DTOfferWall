package dev.forntoh.dtofferwall

import com.google.gson.Gson
import dev.forntoh.common.entities.Offer
import dev.forntoh.common.entities.OfferFilter
import dev.forntoh.common.lib.OfferWallHashKeyUtility
import dev.forntoh.common.lib.hash
import dev.forntoh.web_service.api.MainApiService
import dev.forntoh.web_service.datasources.OfferWallNetworkDataSource
import dev.forntoh.web_service.dto.OffersDTO
import dev.forntoh.web_service.interceptors.RequestInterceptor
import dev.forntoh.web_service.interceptors.ResponseInterceptor
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class OfferWallNetworkDataSourceTest {

    private val gson = Gson()

    private val mockWebServer = MockWebServer()

    private val offerWallHashKeyUtility = OfferWallHashKeyUtility()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .addInterceptor(RequestInterceptor(offerWallHashKeyUtility))
        .addInterceptor(ResponseInterceptor(offerWallHashKeyUtility))
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MainApiService::class.java)

    private val owNetworkDataSource = OfferWallNetworkDataSource(ApiManagerImplTest(api))

    private val testOfferFilter = OfferFilter(
        appId = "1234",
        userId = "testUser",
        token = "8a85q89",
    )

    private val testOffersDTO = OffersDTO(
        code = "OK",
        message = "Test Message",
        count = 1,
        pages = 10,
        offers = listOf(
            Offer(
                link = "test-ink",
                offerId = 10,
                payout = 100,
                requiredActions = "test-requiredActions",
                teaser = "test-teaser",
                title = "test-title",
                offerTypes = emptyList(),
                timeToPayout = Offer.TimeToPayout(1800, "30 Minutes"),
                thumbnail = Offer.Thumbnail("test-hires", "test-low-res")
            )
        ),
    )

    private val signature = "${gson.toJson(testOffersDTO)}${testOfferFilter.token}".hash()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `no error when signature can be validated`() = runBlocking {

        val signature = "${gson.toJson(testOffersDTO)}${testOfferFilter.token}".hash()

        mockWebServer.enqueue(
            MockResponse()
                .setBody(gson.toJson(testOffersDTO))
                .addHeader("X-Sponsorpay-Response-Signature", signature)
        )

        owNetworkDataSource.fetchOffers(testOfferFilter)

        assertNull(owNetworkDataSource.error.value)
    }

    @Test
    fun `notify when data is tampered`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(gson.toJson(testOffersDTO.copy(message = "MODIFIED"))) // Response modified here
                .addHeader("X-Sponsorpay-Response-Signature", signature)
        )

        owNetworkDataSource.fetchOffers(testOfferFilter)

        assertEquals("WARNING: Response has been altered", owNetworkDataSource.error.value)
    }

    @Test
    fun `should fetch offers correctly`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(gson.toJson(testOffersDTO))
                .addHeader("X-Sponsorpay-Response-Signature", signature)
        )

        owNetworkDataSource.fetchOffers(testOfferFilter)

        assertEquals(testOffersDTO, owNetworkDataSource.offersFlow.value)
    }

    @Test
    fun `offers cleared when filter clearPrev is true`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(gson.toJson(testOffersDTO))
                .addHeader("X-Sponsorpay-Response-Signature", signature)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setBody(gson.toJson(testOffersDTO))
                .addHeader("X-Sponsorpay-Response-Signature", signature)
        )

        owNetworkDataSource.fetchOffers(testOfferFilter)
        owNetworkDataSource.fetchOffers(testOfferFilter.copy(clearPrev = true))

        assertEquals(1, owNetworkDataSource.offersFlow.value?.offers?.size)
    }

    @Test
    fun `error message exist when request is unsuccessful`(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(400)
        )

        owNetworkDataSource.fetchOffers(OfferFilter())

        assertNotNull(owNetworkDataSource.error.value)
    }

    @Test
    fun `error message exist when result is unsuccessful`(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse().setBody(gson.toJson(testOffersDTO.copy(code = "ERROR_")))
        )

        owNetworkDataSource.fetchOffers(testOfferFilter)

        assertEquals("Test Message", owNetworkDataSource.error.value) // Message from response should be sent to error flow
    }

    @Test
    fun `no error message when response is successful`(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(gson.toJson(testOffersDTO))
                .addHeader("X-Sponsorpay-Response-Signature", signature)
        )

        owNetworkDataSource.fetchOffers(testOfferFilter)

        assertNull(owNetworkDataSource.error.value)
    }
}