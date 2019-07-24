package com.jsongo.core.crash

import android.content.Intent
import android.os.Process
import com.jsongo.core.BaseCore
import java.io.PrintWriter
import java.io.StringWriter

/**
 * author ： jsongo
 * createtime ： 2019/7/24 9:11
 * desc : 全局crash捕获
 */
class CrashHandler : Thread.UncaughtExceptionHandler {

    companion object {
        private val crashHandler: CrashHandler by lazy {
            CrashHandler()
        }

        private var defaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

        fun init() {
            defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(crashHandler)
        }
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (e == null) {
            killProgress()
            return
        }

        e.printStackTrace()

        //获取错误原因
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        e.printStackTrace(printWriter)
        var cause = e.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()

        //启动CrashActivity 显示弹窗
        BaseCore.context?.let {
            val intent = Intent(it, CrashActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            if (result.isNotEmpty()) {
                intent.putExtra("crash_log", result)
            }
            it.startActivity(intent)
        }

        defaultUncaughtExceptionHandler?.uncaughtException(t, e)
        killProgress()
    }

    private fun killProgress() {
        //如果不关闭程序,会导致程序无法启动,需要完全结束进程才能重新启动
        Process.killProcess(Process.myPid())
        System.exit(10)
    }

}