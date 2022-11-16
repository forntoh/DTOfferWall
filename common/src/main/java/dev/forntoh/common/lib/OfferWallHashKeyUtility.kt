package dev.forntoh.common.lib

import java.security.MessageDigest
import java.util.*

class OfferWallHashKeyUtility(
    private val digest: MessageDigest = MessageDigest.getInstance("SHA-1")
) {

    private fun toQueryMap(query: String?): SortedMap<String, String> {
        val paramsMap = sortedMapOf<String, String>()
        query?.split("&")?.forEach {
            val keyValue = it.split("=")
            paramsMap[keyValue[0]] = keyValue[1]
        }
        return paramsMap
    }

    fun compute(query: String?): String? = with(toQueryMap(query)) {
        remove("hashkey")
        val token = remove("token") ?: return null

        val joinedParams = map { "${it.key}=${it.value}" }.joinToString(separator = "&") { it }
        return "$joinedParams&$token".hash(digest)
    }

}

fun String.hash(digest: MessageDigest = MessageDigest.getInstance("SHA-1")): String {
    val result = digest.digest(toByteArray(Charsets.UTF_8))
    return result.joinToString(separator = "") { String.format("%02X", it) }.lowercase(Locale.ROOT)
}