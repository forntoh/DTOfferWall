/*
 * Copyright (c) 2022, Forntoh Thomas (thomasforntoh@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.forntoh.web_service.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forntoh.web_service.api.ApiManager
import dev.forntoh.web_service.api.ApiManagerImpl
import dev.forntoh.web_service.base.BaseUrl
import dev.forntoh.web_service.interceptors.RequestInterceptor
import dev.forntoh.web_service.interceptors.ResponseInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Module for providing dependencies for the web service.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseOkHttpClient(
        okHttpClientBuilder: OkHttpClient.Builder,
    ): OkHttpClient = okHttpClientBuilder
        .build()

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        requestInterceptor: RequestInterceptor,
        responseInterceptor: ResponseInterceptor,
    ): OkHttpClient.Builder = OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true }
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(requestInterceptor)
        .addInterceptor(responseInterceptor)
        .retryOnConnectionFailure(true)

    /**
     * Provides a [HttpLoggingInterceptor] for logging the request and response.
     */
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BaseUrl.defaultBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())

    /**
     * Provides the implementation of the [ApiManager]
     * @return the implementation of the APi Manager
     */
    @Provides
    fun provideApiManager(
        retrofitBuilder: Retrofit.Builder,
        baseOkHttpClient: OkHttpClient,
    ): ApiManager = ApiManagerImpl(retrofitBuilder, baseOkHttpClient)
}