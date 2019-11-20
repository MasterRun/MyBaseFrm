package com.jsongo.mybasefrm.aop

import android.os.SystemClock
import kotlin.math.abs

/**
 * @author ： jsongo
 * @date ： 19-9-22 下午11:10
 * @desc :
 */
object AopClickUtil {
    /**
     * 最近一次点击的时间
     */
    private var mLastClickTime: Long = 0

    /**
     * 是否是快速点击
     *
     * @param intervalMillis  时间间期（毫秒）
     * @return  true:是，false:不是
     */
    fun isFastDoubleClick(intervalMillis: Long): Boolean {
        //        long time = System.currentTimeMillis();
        val time = SystemClock.elapsedRealtime()
        val timeInterval = abs(time - mLastClickTime)
        return if (timeInterval < intervalMillis) {
            true
        } else {
            mLastClickTime = time
            false
        }
    }
}