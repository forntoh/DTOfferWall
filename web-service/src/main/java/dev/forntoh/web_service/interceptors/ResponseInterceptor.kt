package dev.forntoh.web_service.interceptors

import dev.forntoh.common.lib.OfferWallHashKeyGenerator
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ResponseInterceptor @Inject constructor() : Interceptor {

    private val offerWallHashKeyGenerator = OfferWallHashKeyGenerator()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.isSuccessful) {

            print("XAMP ")

            println(request.url.query)

//            val responseString = response.peekBody(Long.MAX_VALUE).string()
//
//            println(responseString)
        }
        return response
    }

}