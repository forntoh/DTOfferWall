package dev.forntoh.dtofferwall.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forntoh.common.entities.OfferFilter
import dev.forntoh.repository.OfferWallRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferWallViewModel @Inject constructor(
    private val offerWallRepo: OfferWallRepo
) : ViewModel() {

    private var filter by mutableStateOf(
        OfferFilter(
            appId = "",
            userId = "",
            token = ""
        )
    )

    /**
     * LiveData for the Movies
     */
    val offers = offerWallRepo.offers.asLiveData()

    fun nextPage() = with(filter) {
        updateFilters(this.copy(page = page + 1))
    }

    fun updateFilters(appId: String, userId: String, token: String) = with(filter) {
        updateFilters(this.copy(appId = appId, userId = userId, token = token))
    }

    private fun updateFilters(newFilter: OfferFilter) {
        viewModelScope.launch {
            filter = offerWallRepo.updateFilter(newFilter)
        }
    }

}