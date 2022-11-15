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

package dev.forntoh.web_service.api

import dev.forntoh.web_service.dto.OffersDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * DT Offer Wall API Service Interface
 * all the REST requests for the DT Offer Wall API are defined here
 */
interface MainApiService {

    @GET("/feed/v1/offers.json")
    suspend fun fetchOffers(
        @Query("appid") appId: String,
        @Query("uid") userId: String,
        @Query("token") token: String,
    ): Response<OffersDTO>

}