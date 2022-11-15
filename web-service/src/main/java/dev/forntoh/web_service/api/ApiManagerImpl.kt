package dev.forntoh.web_service.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * API Manager class to create and provide the API service
 */
@Singleton
class ApiManagerImpl @Inject constructor(
    retrofitBuilder: Retrofit.Builder,
    baseOkHttpClient: OkHttpClient,
) : ApiManager {

    private val baseRetrofit: Retrofit = retrofitBuilder
        .client(baseOkHttpClient)
        .build()

    /**
     * Get the Main API service
     */
    override val mainApi: MainApiService = baseRetrofit.create(MainApiService::class.java)

}