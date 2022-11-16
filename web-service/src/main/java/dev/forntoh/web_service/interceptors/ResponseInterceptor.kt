package dev.forntoh.web_service.interceptors

import com.google.gson.Gson
import dev.forntoh.common.lib.OfferWallHashKeyUtility
import dev.forntoh.web_service.dto.OffersDTO
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

            val offersDTO = Gson().fromJson(responseString, OffersDTO::class.java)

            // Only check signature if response is successful
            if (!offersDTO.code.startsWith("ERROR")) {

                val signature = response.header("X-Sponsorpay-Response-Signature")

                println(offersDTO)

                val isValid = offerWallHashKeyUtility.validate(responseString, signature)

                if (!isValid)
                    return response
                        .newBuilder()
                        .message("WARNING: Response has been altered")
                        .code(401)
                        .build()
            }
        }
        return response
    }

}