package dev.forntoh.dtofferwall.ui

import dev.forntoh.dtofferwall.ApiManagerImplTest
import dev.forntoh.dtofferwall.MainApiServiceImplTest
import dev.forntoh.repository.OfferWallRepoImpl
import dev.forntoh.web_service.datasources.OfferWallNetworkDataSource
import org.junit.Before
import org.junit.Test

class OfferWallViewModelTest {

    private lateinit var testMainApi: MainApiServiceImplTest
    private lateinit var viewModel: OfferWallViewModel

    @Before
    fun setUp() {
        testMainApi = MainApiServiceImplTest()
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