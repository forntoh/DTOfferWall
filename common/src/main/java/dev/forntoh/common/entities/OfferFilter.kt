package dev.forntoh.common.entities

data class OfferFilter(
    var appId: String = "",
    var userId: String = "",
    var token: String = "",
    var page: Int = 1,
    val clearPrev: Boolean = false,
)
