package dev.forntoh.web_service_test.datasources

import dev.forntoh.common.entities.OfferFilter
import dev.forntoh.web_service.datasources.OfferWallNetworkDataSource
import dev.forntoh.web_service_test.api.ApiManagerImplTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class OfferWallNetworkDataSourceTest {

    private lateinit var testMainApi: dev.forntoh.web_service_test.api.MainApiServiceImplTest
    private lateinit var owNetworkDataSource: OfferWallNetworkDataSource

    @Before
    fun setUp() {
        testMainApi = dev.forntoh.web_service_test.api.MainApiServiceImplTest()
        owNetworkDataSource = OfferWallNetworkDataSource(ApiManagerImplTest(testMainApi))
    }

    @Test
    fun `offers cleared when filter clearPrev is true`() = runBlocking {
        owNetworkDataSource.fetchOffers(
            OfferFilter(
                appId = "1234",
                userId = "testUser",
                token = "8a85q89",
            )
        )
        owNetworkDataSource.fetchOffers(
            OfferFilter(
                appId = "1234",
                userId = "testUser",
                token = "8a85q89",
                clearPrev = true
            )
        )
        assertEquals(1, owNetworkDataSource.offersFlow.value?.offers?.size)
    }

    @Test
    fun `error message exist when request is unsuccessful`(): Unit = runBlocking {
        testMainApi.shouldRequestSucceed = false
        owNetworkDataSource.fetchOffers(OfferFilter())

        assertNotNull(owNetworkDataSource.error.value)
    }

    @Test
    fun `error message exist when response is unsuccessful`(): Unit = runBlocking {
        testMainApi.shouldResponseSucceed = false
        owNetworkDataSource.fetchOffers(OfferFilter())

        assertNotNull(owNetworkDataSource.error.value)
    }

    @Test
    fun `no error message when response is successful`(): Unit = runBlocking {
        testMainApi.shouldResponseSucceed = true
        testMainApi.shouldRequestSucceed = true

        owNetworkDataSource.fetchOffers(OfferFilter())

        assertNull(owNetworkDataSource.error.value)
    }
}