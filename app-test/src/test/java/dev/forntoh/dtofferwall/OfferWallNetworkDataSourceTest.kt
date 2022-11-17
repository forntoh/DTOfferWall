package dev.forntoh.dtofferwall

import dev.forntoh.common.entities.OfferFilter
import dev.forntoh.common.lib.hash
import dev.forntoh.dtofferwall.util.*
import dev.forntoh.web_service.datasources.OfferWallNetworkDataSource
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class OfferWallNetworkDataSourceTest {

    private lateinit var offerWallDataSourceProvider: OfferWallDataSourceProvider
    private lateinit var mockWebServer: MockWebServer
    private lateinit var owNetworkDataSource: OfferWallNetworkDataSource

    @Before
    fun setUp() {
        offerWallDataSourceProvider = OfferWallDataSourceProvider()
        mockWebServer = offerWallDataSourceProvider.mockWebServer
        owNetworkDataSource = offerWallDataSourceProvider.owNetworkDataSource
    }

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
            MockResponse().setBody(gson.toJson(testOffersDTO.copy(code = "ERROR_", message = "TEST ERROR")))
        )

        owNetworkDataSource.fetchOffers(testOfferFilter)

        assertEquals("TEST ERROR", owNetworkDataSource.error.value) // Message from response should be sent to error flow
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