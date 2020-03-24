package com.jsongo.core.util

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi

/**
 * @author ： jsongo
 * @date ： 2020/1/15 15:49
 * @desc : 保活
 */
class KeepLiveUtil(private val activity: Activity) {
    /**
     * 请求忽略电池优化
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestIgnoreBatteryOptimizations() {
        try {
            val intent =
                Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:" + activity.packageName)
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 检查此APP是否忽略电池优化
     *
     * @return
     */
    @get:RequiresApi(api = Build.VERSION_CODES.M)
    val isIgnoringBatteryOptimizations: Boolean
        get() {
            var isIgnoring = false
            val powerManager =
                activity.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (powerManager != null) {
                isIgnoring = powerManager.isIgnoringBatteryOptimizations(activity.packageName)
            }
            return isIgnoring
        }

    fun goSetting4KeepLive() {
        if (isHuawei) {
            goHuaweiSetting()
        } else if (isXiaomi) {
            goXiaomiSetting()
        } else if (isOPPO) {
            goOPPOSetting()
        } else if (isVIVO) {
            goVIVOSetting()
        } else if (isMeizu) {
            goMeizuSetting()
        } else if (isSamsung) {
            goSamsungSetting()
        } else if (isLeTV) {
            goLetvSetting()
        } else if (isSmartisan) {
            goSmartisanSetting()
        }
    }

    /**
     * 跳转到指定应用的首页
     */
    fun showActivity(packageName: String) {
        val intent = activity.packageManager.getLaunchIntentForPackage(packageName)
        activity.startActivity(intent)
    }

    /**
     * 跳转到指定应用的指定页面
     */
    fun showActivity(packageName: String, activityDir: String) {
        val intent = Intent()
        intent.component = ComponentName(packageName, activityDir)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    fun goHuaweiSetting() {
        try {
            showActivity(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
            )
        } catch (e: Exception) {
            showActivity(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"
            )
        }
    }

    fun goXiaomiSetting() {
        showActivity(
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
    }

    fun goOPPOSetting() {
        try {
            showActivity("com.coloros.phonemanager")
        } catch (e1: Exception) {
            try {
                showActivity("com.oppo.safe")
            } catch (e2: Exception) {
                try {
                    showActivity("com.coloros.oppoguardelf")
                } catch (e3: Exception) {
                    showActivity("com.coloros.safecenter")
                }
            }
        }
    }

    fun goVIVOSetting() {
        showActivity("com.iqoo.secure")
    }

    fun goMeizuSetting() {
        showActivity("com.meizu.safe")
    }

    fun goSamsungSetting() {
        try {
            showActivity("com.samsung.android.sm_cn")
        } catch (e: Exception) {
            showActivity("com.samsung.android.sm")
        }
    }

    fun goLetvSetting() {
        showActivity(
            "com.letv.android.letvsafe",
            "com.letv.android.letvsafe.AutobootManageActivity"
        )
    }

    fun goSmartisanSetting() {
        showActivity("com.smartisanos.security")
    }

    companion object {

        /**
         * 华为
         *
         * @return
         */
        val isHuawei: Boolean
            get() = if (Build.BRAND == null) {
                false
            } else {
                Build.BRAND.toLowerCase() == "huawei" || Build.BRAND.toLowerCase() == "honor"
            }

        /**
         * 小米
         *
         * @return
         */
        val isXiaomi: Boolean
            get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "xiaomi"

        /**
         * oppo
         */
        val isOPPO: Boolean
            get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "oppo"

        /**
         * vivo
         */
        val isVIVO: Boolean
            get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "vivo"

        /**
         * 魅族
         */
        val isMeizu: Boolean
            get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "meizu"

        /**
         * 三星
         */
        val isSamsung: Boolean
            get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "samsung"

        /**
         * 乐视
         */
        val isLeTV: Boolean
            get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "letv"

        /**
         * 锤子
         */
        val isSmartisan: Boolean
            get() = Build.BRAND != null && Build.BRAND.toLowerCase() == "smartisan"
    }

}