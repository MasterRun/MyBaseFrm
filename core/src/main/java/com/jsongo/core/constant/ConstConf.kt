package com.jsongo.core.constant

import android.os.Environment
import com.jsongo.core.BaseCore
import com.jsongo.core.util.FileUtil
import java.io.File

/**
 * author ： jsongo
 * createtime ： 2019/7/24 23:21
 * desc : 常量
 */
object ConstConf {

    //app文件基础路径
    var BASE_DIR: String
        private set
    //网络请求缓存路径
    val HTTP_CACHE_DIR: String
    //崩溃日志保存路径
    val CRASH_LOG_DIR: String

    init {
        try {
            val packageManager = BaseCore.context.packageManager
            val packageInfo = packageManager.getPackageInfo(BaseCore.context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            val name = BaseCore.context.resources.getString(labelRes)
            BASE_DIR = FileUtil.getRootPath().toString() + File.separator + name + File.separator
        } catch (e: Exception) {
            BASE_DIR = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                BaseCore.context.externalCacheDir!!.path + File.separator
            } else {
                BaseCore.context.cacheDir.path + File.separator
            }
            e.printStackTrace()
        }
        HTTP_CACHE_DIR = BASE_DIR + "netcache" + File.separator
        CRASH_LOG_DIR = BASE_DIR + "crash" + File.separator

    }
}