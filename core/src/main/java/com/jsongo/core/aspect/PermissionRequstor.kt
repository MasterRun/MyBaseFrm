package com.jsongo.core.aspect

import com.jsongo.annotation.anno.permission.PermissionDeny
import com.jsongo.annotation.anno.permission.PermissionNeed
import com.jsongo.core.common.ActivityCollector
import com.jsongo.core.widget.RxToast
import com.tbruyelle.rxpermissions2.RxPermissions
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import java.lang.reflect.Method

/**
 * @author ： jsongo
 * @date ： 2019/11/20 19:50
 * @desc : 动态权限申请
 */

@Aspect
class PermissionRequstor {
    //此处参数是注解对象
    @Pointcut("execution(@com.jsongo.annotation.anno.permission.PermissionNeed * *(..)) && @annotation(permissionNeed)")
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
                .subscribe({ granted ->
                    if (granted) {
                        //权限通过
                        joinPoint.proceed()
                    } else {
                        //权限未通过
                        permissionDeny(joinPoint)
                    }
                }, {
                    RxToast.error(it.message ?: "error in request permissions")
                })
        }

    }

    /**
     * 权限拒绝，找到被@PermissionDeny注解的方法，并调用
     */
    private fun permissionDeny(joinPoint: ProceedingJoinPoint) {
        //获取到被注解的方法所在类的对象
        val clazzObj = joinPoint.`this`
        //获取签名（被注解的对象）
        val signature = joinPoint.signature as MethodSignature
        //获取到声明ta的类的Class对象
        val declaringType = signature.declaringType
        //获取同类的所有方法
        val methods = declaringType.methods
        var permissionDenyCallback: Method? = null
        for (method in methods) {
            //找到被注解的方法
            val annotation = method.getAnnotation(PermissionDeny::class.java)
            if (annotation != null) {
                permissionDenyCallback = method
                break
            }
        }
        //调用权限拒绝的方法
        permissionDenyCallback?.isAccessible = true
        permissionDenyCallback?.invoke(clazzObj)
    }

}