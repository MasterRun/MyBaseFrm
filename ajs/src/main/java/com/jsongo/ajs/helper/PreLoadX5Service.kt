package com.jsongo.ajs.helper

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Nullable
import com.jsongo.ajs.R
import com.jsongo.core_mini.util.LogcatUtil
import com.tencent.smtt.sdk.QbSdk

/**
         .--,       .--,
        ( (  \.---./  ) )
         '.__/o   o\__.'
            {=  ^  =}
             >  -  <
            /       \
           //       \\
          //|   .   |\\
          "'\       /'"_.-~^`'-.
             \  _  /--'         `
           ___)( )(___
          (((__) (__)))

  高山仰止,景行行止.虽不能至,心向往之。

 * @author ： jsongo
 * @date ： 2019/11/10 14:11
 * @desc : 启动服务预加载x5 {@link https://blog.csdn.net/dong19900415/article/details/82666383}
 */
class PreLoadX5Service : Service() {

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /**
     * x5加载回调
     */
    var cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {

        override fun onViewInitFinished(initResult: Boolean) {
            LogcatUtil.d(
                String.format(
                    getString(R.string.x5_view_init_finish),
                    initResult.toString()
                )
            )
        }

        override fun onCoreInitFinished() {
            LogcatUtil.d(getString(R.string.x5_core_init_finish))
        }
    }

    override fun onCreate() {
        super.onCreate()
        initX5()
        preInitX5WebCore()
    }

    /**
     * 加载x5
     */
    private fun initX5() {
        QbSdk.setDownloadWithoutWifi(true)
        LogcatUtil.d(getString(R.string.x5_init_env))
        QbSdk.initX5Environment(applicationContext, cb)
    }

    /**
     * 预加载x5
     */
    private fun preInitX5WebCore() {

        if (!QbSdk.isTbsCoreInited()) {

            // preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
            LogcatUtil.d(getString(R.string.x5_pre_init))
            // 设置X5初始化完成的回调接口
            QbSdk.preInit(applicationContext, cb)

        }
    }

}