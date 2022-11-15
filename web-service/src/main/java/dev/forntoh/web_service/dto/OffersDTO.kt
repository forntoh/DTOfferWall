package dev.forntoh.web_service.dto

import dev.forntoh.common.entities.AppInfo
import dev.forntoh.common.entities.Offer

data class OffersDTO(
    val code: String,
    val message: String,
    val count: Int,
    val pages: Int,
    val offers: List<Offer>,
    val information: AppInfo,
)
