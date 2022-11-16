package dev.forntoh.dtofferwall.ui

import dev.forntoh.repository.OfferWallRepoImpl
import dev.forntoh.web_service.datasources.OfferWallNetworkDataSource
import dev.forntoh.web_service_test.api.ApiManagerImplTest
import dev.forntoh.web_service_test.api.MainApiServiceImplTest
import org.junit.Before
import org.junit.Test

class OfferWallViewModelTest {

    private lateinit var testMainApi: MainApiServiceImplTest
    private lateinit var viewModel: OfferWallViewModel

    @Before
    fun setUp() {
        viewModel = OfferWallViewModel(
            OfferWallRepoImpl(
                OfferWallNetworkDataSource(ApiManagerImplTest(testMainApi))
            )
        )
    }

    @Test
    fun nextPage() {
    }

    @Test
    fun updateFilters() {
    }
}