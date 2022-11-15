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

package dev.forntoh.web_service.datasources

import dev.forntoh.common.entities.OfferFilter
import dev.forntoh.web_service.api.ApiManager
import dev.forntoh.web_service.dto.OffersDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * [OfferWallNetworkDataSource] is the data source for the Fyber Cloud API.
 * It fetches the offer list from the web service and updates the flow.
 *
 * @property apiManager is the api manager to fetch the offer list.
 */
class OfferWallNetworkDataSource @Inject constructor(
    private val apiManager: ApiManager,
) {

    private val _offersFlow: MutableStateFlow<OffersDTO?> = MutableStateFlow(null)
    val offersFlow = _offersFlow as StateFlow<OffersDTO?>

    suspend fun clearOffers() {
        _offersFlow.emit(_offersFlow.value?.copy(offers = emptyList()))
    }

    suspend fun fetchOffers(offerFilter: OfferFilter) {
        val fetchedData = apiManager.mainApi.fetchOffers(
            offerFilter.appId,
            offerFilter.userId,
            offerFilter.token,
            offerFilter.page
        )
        if (fetchedData.isSuccessful && fetchedData.body() != null) with(fetchedData.body()!!) {
            // Get previous data
            val prev = _offersFlow.value?.offers?.toMutableList() ?: mutableListOf()

            // Append new data
            prev.addAll(offers)

            // Mutate result with new data
            offers = prev.toList()

            // Emit mutation
            _offersFlow.emit(this)
        }
    }

}