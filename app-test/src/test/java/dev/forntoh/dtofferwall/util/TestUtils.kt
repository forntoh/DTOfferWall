package dev.forntoh.dtofferwall.util

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
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val testOfferFilter = OfferFilter(
    appId = "1234",
    userId = "testUser",
    token = "8a85q89",
)

val testOffersDTO = OffersDTO(
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

val gson by lazy { Gson() }

val signature by lazy { "${gson.toJson(testOffersDTO)}${testOfferFilter.token}".hash() }

val offerWallHashKeyUtility = OfferWallHashKeyUtility()

val mockWebServer by lazy { MockWebServer() }

val mockOkhttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .addInterceptor(RequestInterceptor(offerWallHashKeyUtility))
        .addInterceptor(ResponseInterceptor(offerWallHashKeyUtility))
        .build()
}

val owNetworkDataSource by lazy {
    val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(mockOkhttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MainApiService::class.java)

    OfferWallNetworkDataSource(ApiManagerImplTest(api))
}
