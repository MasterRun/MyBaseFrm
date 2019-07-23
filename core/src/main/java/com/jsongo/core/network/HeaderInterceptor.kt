package com.jsongo.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author  jsongo
 * @date 2019/3/26 16:07
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            // TODO: 2019/3/26 token
            .addHeader("token", "your token")
            .build()
        return chain.proceed(request)
    }
}