package com.jsongo.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author  jsongo
 * @date 2019/3/26 16:03
 */
class NetCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val onlineCacheTime = 60
        return response.newBuilder()
            // TODO: 2019/3/26 net cache
            .header("Cache-Control", "public, max-age=$onlineCacheTime")
            .removeHeader("Pragma")
            .build()
    }

}