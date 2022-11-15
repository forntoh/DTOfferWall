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

package dev.forntoh.repository

import dev.forntoh.common.entities.Offer
import dev.forntoh.common.entities.OfferFilter
import dev.forntoh.web_service.datasources.OfferWallNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository that provides Offer data to ViewModels.
 * Implementation of [OfferWallRepo]
 */
class OfferWallRepoImpl @Inject constructor(
    private val offerWallNetworkDataSource: OfferWallNetworkDataSource,
) : OfferWallRepo() {

    private val totalPages = MutableStateFlow(1)

    override val offers: Flow<List<Offer>> = offerWallNetworkDataSource.offersFlow
        .map { offerDto ->
            offerDto?.let {
                totalPages.emit(it.pages)
                it.offers
            } ?: emptyList()
        }
        .flowOn(Dispatchers.IO)

    override val error: Flow<String?> = offerWallNetworkDataSource.error

    override suspend fun updateFilter(newFilter: OfferFilter) = newFilter.apply {
        if (page > totalPages.value) page = totalPages.value
        else if (page <= 0) page = 1
        else if (appId.isNotBlank() && userId.isNotBlank() && token.isNotBlank())
            offerWallNetworkDataSource.fetchOffers(this)
    }

    override suspend fun clearOffers() {
        offerWallNetworkDataSource.clearOffers()
    }

}