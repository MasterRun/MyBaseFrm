package com.jsongo.mybasefrm.aspect

import android.os.SystemClock
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import kotlin.math.abs

/**
 * @author ： jsongo
 * @date ： 19-9-22 下午11:11
 * @desc :
 */

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AopOnclick(
    /**
     * 点击间隔时间
     */
    val value: Long = 1000
)

@Aspect
class ClickAspect {

    /**
     * 定义切点，标记切点为所有被@AopOnclick注解的方法
     * 注意：这里com.xxx.xxx.AopOnclick需要替换成
     * 你自己项目中AopOnclick这个类的全路径
     */
    @Pointcut("execution(@com.jsongo.mybasefrm.aspect.AopOnclick * *(..))")
    fun methodAnnotated() {
    }

    /**
     * 定义一个切面方法，包裹切点方法
     */
    @Around("methodAnnotated()")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint) {
        // 取出方法的注解
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        if (!method.isAnnotationPresent(AopOnclick::class.java)) {
            return
        }
        val aopOnclick = method.getAnnotation(AopOnclick::class.java)
        // 判断是否快速点击
        if (!isFastDoubleClick(aopOnclick.value)) {
            // 不是快速点击，执行原方法
            joinPoint.proceed()
        }
    }

    companion object {

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
}