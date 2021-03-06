package com.jsongo.core.aspect

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jsongo.annotation.anno.permission.PermissionDeny
import com.jsongo.annotation.anno.permission.PermissionNeed
import com.jsongo.core_mini.common.ActivityCollector
import com.jsongo.core_mini.util.rxpermissions2.RxPermissionManager
import com.jsongo.core_mini.widget.RxToast
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
            val activity = ActivityCollector.myForegroundActivity
            if (activity !is FragmentActivity) {
                RxToast.error("请求权限失败!")
                return
            }
            val rxPermissions = RxPermissionManager.get(activity)
            if (rxPermissions == null) {
                RxToast.error("error when request permission")
            } else {
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
                //添加生命周期回调
                activity.lifecycle.addObserver(object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onDestroy() {
                        if (disposable != null && !disposable.isDisposed) {
                            disposable.dispose()
                        }
                    }
                })
            }
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