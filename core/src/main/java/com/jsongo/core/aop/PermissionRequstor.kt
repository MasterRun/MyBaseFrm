package com.jsongo.core.aop

import com.jsongo.annotation.anno.PermissionNeed
import com.jsongo.core.util.ActivityCollector
import com.tbruyelle.rxpermissions2.RxPermissions
import com.vondear.rxtool.view.RxToast
import io.reactivex.android.schedulers.AndroidSchedulers
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * @author ： jsongo
 * @date ： 2019/11/20 19:50
 * @desc : 动态权限申请
 */

@Aspect
class PermissionRequstor {
    //此处参数是注解对象
    @Pointcut("execution(@com.jsongo.annotation.anno.PermissionNeed * *(..)) && @annotation(permissionNeed)")
    fun needPermission(permissionNeed: PermissionNeed) {
    }

    /**
     * 切面方法（只适用于无返回值的方法），用于请求权限,
     */
    @Around("needPermission(permissionNeed)")
    @Throws(Throwable::class)
    fun requestPermission(joinPoint: ProceedingJoinPoint, permissionNeed: PermissionNeed) {

        val permissions = permissionNeed.permissions
        if (permissions.isEmpty()) {
            joinPoint.proceed()
        } else {
            val rxPermissions = RxPermissions(ActivityCollector.topActivity)
            val disposable = rxPermissions.request(*permissions)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { granted ->
                    if (!granted) {
                        //权限未通过
                        RxToast.error("no permission!")
                    } else {
                        //权限通过
                        joinPoint.proceed()
                        RxToast.success("permission ok!")
                    }
                }
        }

    }

}