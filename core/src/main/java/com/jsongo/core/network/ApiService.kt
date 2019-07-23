package com.jsongo.core.network

import com.jsongo.core.BaseCore

/**
 * @author  jsongo
 * @date 2019/3/26 15:54
 */

// TODO: 2019/3/26 ip 配置
object ServerAddr {
    /** todo 正式服务器地址  */
    val SERVER_ADDRESS_RELEASE: String = ""

    /**  测试服务器地址  */
    var SERVER_ADDRESS_DEBUG = ""

    /** 服务器域名  */
    var SERVER_ADDRESS = if (BaseCore.isDebug) SERVER_ADDRESS_DEBUG else SERVER_ADDRESS_RELEASE
}

interface ApiService {

/*    @POST("confession/oneConfession")
    fun getConfession(@Query("id") id: Long): Observable<Result<Confession?>>*/
}