package com.jsongo.mybasefrm.aspect

import android.app.Activity
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.util.toGsonBean
import com.jsongo.mybasefrm.bean.User
import com.jsongo.mybasefrm.ui.login.LoginActivity
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * @author ： jsongo
 * @date ： 2019/12/15 17:25
 * @desc : 需要登录的切面
 */
@Aspect
class LoginCheckAspect {
    @Pointcut("execution(@com.jsongo.annotation.anno.LoginCheck * *(..))")
    fun loginNeedPointCut() {
    }

    @Around("loginNeedPointCut()")
    @Throws(Throwable::class)
    fun loginCheckExecutor(joinPoint: ProceedingJoinPoint): Any? {
        val userGuid = CommonDbOpenHelper.getValue(CommonDbKeys.USER_GUID)
        val password = CommonDbOpenHelper.getValue(CommonDbKeys.USER_PASSWORD)
        val user: User? =
            CommonDbOpenHelper.getValue(CommonDbKeys.USER_INFO).toGsonBean(User::class.java)
        if (userGuid.isNullOrEmpty() || password.isNullOrEmpty() || user == null) {
            LoginActivity.go()
            val any = joinPoint.`this`
            if (any is Activity && !any.isFinishing) {
                any.finish()
            }
            return null
        } else {
            return joinPoint.proceed()
        }
    }
}