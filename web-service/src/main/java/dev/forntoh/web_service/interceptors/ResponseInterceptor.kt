package dev.forntoh.web_service.interceptors

import dev.forntoh.common.lib.OfferWallHashKeyUtility
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ResponseInterceptor @Inject constructor(
    private val offerWallHashKeyUtility: OfferWallHashKeyUtility
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.isSuccessful) {
            val responseString = response.peekBody(Long.MAX_VALUE).string()
            val signature = response.header("X-Sponsorpay-Response-Signature")

            val isNotTampered = offerWallHashKeyUtility.validate(responseString, signature)

            println(" - $isNotTampered")
        }
        return response
    }

}