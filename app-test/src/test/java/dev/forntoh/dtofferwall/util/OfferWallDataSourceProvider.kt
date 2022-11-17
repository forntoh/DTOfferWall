package dev.forntoh.dtofferwall.util

import dev.forntoh.web_service.api.MainApiService
import dev.forntoh.web_service.datasources.OfferWallNetworkDataSource
import dev.forntoh.web_service.interceptors.RequestInterceptor
import dev.forntoh.web_service.interceptors.ResponseInterceptor
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class OfferWallDataSourceProvider {

    val mockWebServer = MockWebServer()

    private val mockOkhttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(RequestInterceptor(offerWallHashKeyUtility))
        .addInterceptor(ResponseInterceptor(offerWallHashKeyUtility))
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(mockOkhttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MainApiService::class.java)

    val owNetworkDataSource = OfferWallNetworkDataSource(ApiManagerImplTest(api))

}