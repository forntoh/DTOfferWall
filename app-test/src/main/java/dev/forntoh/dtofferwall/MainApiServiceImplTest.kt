package dev.forntoh.dtofferwall

import dev.forntoh.common.entities.Offer
import dev.forntoh.web_service.api.MainApiService
import dev.forntoh.web_service.dto.OffersDTO
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class MainApiServiceImplTest(
    var shouldRequestSucceed: Boolean = true,
    var shouldResponseSucceed: Boolean = true,
) : MainApiService {

    override suspend fun fetchOffers(
        appId: String,
        userId: String,
        token: String,
        page: Int
    ): Response<OffersDTO> {
        return if (shouldRequestSucceed) Response.success(
            if (shouldResponseSucceed) {
                OffersDTO(
                    code = "OK",
                    message = "OK",
                    count = 1,
                    pages = 10,
                    offers = listOf(
                        Offer(
                            link = "test-ink",
                            offerId = 10,
                            payout = 100,
                            requiredActions = "test-requiredActions",
                            teaser = "test-teaser",
                            title = "test-title",
                            offerTypes = emptyList(),
                            timeToPayout = Offer.TimeToPayout(1800, "30 Minutes"),
                            thumbnail = Offer.Thumbnail("test-hires", "test-low-res")
                        )
                    ),
                )
            } else {
                OffersDTO(
                    code = "ERROR_",
                    message = "Error",
                    count = 0,
                    pages = 0,
                )
            }
        ) else Response.error(
            400, "Error".toResponseBody()
        )
    }
}