package com.jsongo.core

import android.os.Environment
import com.vondear.rxtool.RxFileTool
import java.io.File

/**
 * author ： jsongo
 * createtime ： 2019/7/24 23:21
 * desc : 常量
 */
object Constants {

    var BASE_DIR: String
        private set
    val HTTP_CACHE_DIR: String
    val CRASH_LOG_DIR: String

    init {
        try {
            val packageManager = BaseCore.context.getPackageManager()
            val packageInfo = packageManager.getPackageInfo(BaseCore.context.getPackageName(), 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            val name = BaseCore.context.getResources().getString(labelRes)
            BASE_DIR = RxFileTool.getRootPath().toString() + File.separator + name + File.separator
        } catch (e: Exception) {
            BASE_DIR = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                BaseCore.context.getExternalCacheDir()!!.getPath() + File.separator
            } else {
                BaseCore.context.getCacheDir().getPath() + File.separator
            }
            e.printStackTrace()
        }
        HTTP_CACHE_DIR = BASE_DIR + "netcache" + File.separator
        CRASH_LOG_DIR = BASE_DIR + "crash" + File.separator
    }
}