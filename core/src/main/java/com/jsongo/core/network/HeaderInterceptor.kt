package com.jsongo.core.network

import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.db.CommonDbOpenHelper
import okhttp3.Interceptor

/**
 * @author  jsongo
 * @date 2019/3/26 16:07
 */
class HeaderInterceptor(private val addAuth: Boolean) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) = chain.proceed(
        chain.request().newBuilder()
            .apply {
                if (addAuth) {
                    val userguid = CommonDbOpenHelper.getValue(CommonDbKeys.USER_GUID)
                    addHeader("Authorization", "Bearer $userguid")
                }
            }.build()
    )
}