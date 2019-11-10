package com.jsongo.ajs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk

/**
 * author ： jsongo
 * createtime ： 2019/7/23 9:26
 * desc :
 * //x5内核加载失败可能原因
 * https://www.yimenapp.com/info/X5-nei-he-kai-qi-hou-dan-shi-bu-sheng-xiao-143.html
 *
 */
@SuppressLint("StaticFieldLeak")
object AJs {
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context

        initX5()//启动预加载的服务
    }

    private fun initX5() {

        val tbsSettingMap =
            hashMapOf(Pair(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true as Any))
        QbSdk.initTbsSettings(tbsSettingMap)
        val intent = Intent(context, PreLoadX5Service::class.java)
        context.startService(intent)
    }
}