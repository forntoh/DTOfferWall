package dev.forntoh.common.lib

import java.security.MessageDigest
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfferWallHashKeyUtility @Inject constructor() {

    private val digest: MessageDigest = MessageDigest.getInstance("SHA-1")

    private var token: String? = null

    private fun toQueryMap(query: String?): SortedMap<String, String> = sortedMapOf<String, String>().apply {
        query?.split("&")?.forEach {
            val keyValue = it.split("=")
            this[keyValue[0]] = keyValue[1]
        }
    }

    fun compute(query: String?): String? = with(toQueryMap(query)) {
        remove("hashkey")
        token = remove("token") ?: return null

        val joinedParams = map { "${it.key}=${it.value}" }.joinToString(separator = "&") { it }
        return "$joinedParams&$token".hash(digest)
    }

    fun validate(response: String, signature: String?): Boolean {
        val responseHash = "$response$token".hash(digest)
        return responseHash == signature
    }

}

fun String.hash(digest: MessageDigest = MessageDigest.getInstance("SHA-1")): String {
    val result = digest.digest(toByteArray(Charsets.UTF_8))
    return result.joinToString(separator = "") { String.format("%02X", it) }.lowercase(Locale.ROOT)
}