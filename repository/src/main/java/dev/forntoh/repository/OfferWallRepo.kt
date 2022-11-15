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
import kotlinx.coroutines.flow.Flow

/**
 * Repository for Movies
 */
abstract class OfferWallRepo {

    /**
     * Flow of Offers
     */
    abstract val offers: Flow<List<Offer>>

    abstract suspend fun updateFilter(newFilter: OfferFilter): OfferFilter

    abstract suspend fun clearOffers()

}