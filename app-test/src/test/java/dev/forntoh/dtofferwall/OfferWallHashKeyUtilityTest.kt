package dev.forntoh.dtofferwall

import dev.forntoh.common.lib.OfferWallHashKeyUtility
import org.junit.Test
import kotlin.test.assertEquals

internal class OfferWallHashKeyUtilityTest {

    @Test
    fun `hash generation is okay`() = with(OfferWallHashKeyUtility()) {
        val expectedHash = "e1f1f7c5d56243584942022bc8bdb58c06297bb8" // hash of p1=1&p2=2&1234 see DT Docs
        val actualHash = compute("token=1234&p2=2&p1=1")
        assertEquals(expectedHash, actualHash)
    }

    @Test
    fun `signature is verified correctly`() = with(OfferWallHashKeyUtility()) {
        compute("token=1234&p1=1&p2=2") // Token will be set to 1234

        val response = "RESPONSE"
        val expectedHash = "58e495cc9ba7f9ccbbc3be1f334897c3d8f0ae95" // = hashOf RESPONSE1234

        assert(validate(response, expectedHash)) // Hash should be the same
    }
}