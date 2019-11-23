package com.jsongo.core.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException


/**
 * @author ： jsongo
 * @date ： 2019/11/23 10:47
 * @desc : device工具类
 */
object DeviceUtil {
    fun getAppVersionNo(context: Context): Int {
        // 获取packagemanager的实例
        val packageManager: PackageManager = context.packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        var packInfo: PackageInfo? = null
        try {
            packInfo = packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return packInfo?.versionCode ?: -1
    }

    fun getAppVersionName(context: Context): String? {
        // 获取packagemanager的实例
        val packageManager = context.packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        var packInfo: PackageInfo? = null
        try {
            packInfo = packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return packInfo?.versionName
    }
}