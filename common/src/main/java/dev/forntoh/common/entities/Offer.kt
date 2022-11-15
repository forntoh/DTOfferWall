package dev.forntoh.common.entities


import com.google.gson.annotations.SerializedName

data class Offer(
    val link: String,
    @SerializedName("offer_id")
    val offerId: Int,
    @SerializedName("offer_types")
    val offerTypes: List<OfferType>,
    val payout: Int,
    @SerializedName("required_actions")
    val requiredActions: String,
    val teaser: String,
    val thumbnail: Thumbnail,
    @SerializedName("time_to_payout")
    val timeToPayout: TimeToPayout,
    val title: String
) {
    data class OfferType(
        @SerializedName("offer_type_id")
        val offerTypeId: Int,
        val readable: String
    )

    data class Thumbnail(
        val hires: String,
        val lowres: String
    )

    data class TimeToPayout(
        val amount: Int,
        val readable: String
    )
}