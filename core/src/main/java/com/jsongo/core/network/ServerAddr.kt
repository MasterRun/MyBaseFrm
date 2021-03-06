package com.jsongo.core.network

import com.jsongo.core.BaseCore
import com.jsongo.core.R

/**
 * @author  jsongo
 * @date 2019/3/26 15:54
 */

// ip 配置
object ServerAddr {
    /** 正式服务器地址  */
    val SERVER_ADDRESS_RELEASE: String = BaseCore.context.getString(R.string.SERVER_ADDRESS_RELEASE)

    /**  测试服务器地址  */
    val SERVER_ADDRESS_DEBUG: String = BaseCore.context.getString(R.string.SERVER_ADDRESS_DEBUG)

    /** 服务器域名  */
    @JvmStatic
    val SERVER_ADDRESS: String =
        if (BaseCore.isDebug) SERVER_ADDRESS_DEBUG else SERVER_ADDRESS_RELEASE
}