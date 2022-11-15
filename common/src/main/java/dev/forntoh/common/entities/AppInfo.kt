package dev.forntoh.common.entities


import com.google.gson.annotations.SerializedName

data class AppInfo(
    @SerializedName("app_name") val appName: String,
    @SerializedName("appid") val appId: Int,
    val country: String,
    val language: String,
    @SerializedName("support_url") val supportUrl: String,
    @SerializedName("virtual_currency") val virtualCurrency: String
)