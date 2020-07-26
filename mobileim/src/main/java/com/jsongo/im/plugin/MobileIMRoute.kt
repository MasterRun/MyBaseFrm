package com.jsongo.im.plugin

import android.app.Activity
import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.bean.ErrorPluginWrapper
import com.jsongo.core_mini.common.ActivityCollector
import com.jsongo.im.imui.ChatActivity

/**
 * @author ： jsongo
 * @date ： 2020/1/16 18:14
 * @desc :
 */
object MobileIMRoute {
    fun goChat(params: Map<String, Any?>?): DataWrapper<MutableMap<String, Any?>> {
        val activity =
            params?.get("activity") as Activity? ?: ActivityCollector.myForegroundActivity
            ?: ActivityCollector.topActivity
        val convid = params?.get("convid") as String? ?: ""
        val aimUserguid =
            params?.get("aimUserguid") as String? ?: ""

        if (convid.isEmpty() && aimUserguid.isEmpty()) {
            return ErrorPluginWrapper("参数错误！")
        }
        ChatActivity.go(activity, aimUserguid, convid)
        return DataWrapper.PLUGIN_WRAP_NO_RESULT
    }

}