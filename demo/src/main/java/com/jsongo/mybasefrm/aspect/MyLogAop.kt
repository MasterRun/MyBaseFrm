package com.jsongo.mybasefrm.aspect

/**
 * @author ： jsongo
 * @date ： 19-9-22 下午11:26
 * @desc :
 */
//标识类为切面
//@Aspect
class MyLogAop {

    /**
     * 定义切点
     */
    //@Pointcut("execution(* com.jsongo.mybasefrm.view..*.onC*(..))")  //  --  ok
    //@Pointcut("execution(* com.jsongo.mybasefrm.presenter.DemoPresenter.*(..))")
    //@Pointcut("execution(* com.jsongo.mybasefrm.view.activity.DemoActivity.onGetDailyGank(..))")//  --ok
    //@Pointcut("execution(* com.jsongo.mybasefrm.presenter.DemoPresenter.getAuthtypes(..))")
    //@Pointcut("execution(* com.jsongo.mybasefrm.presenter..*(..))")
    //@Pointcut("execution(android.view.View.OnClickListener.onClick(..))")
//    @Pointcut("execution(* com.jsongo.core.mvp.base.BaseActivity.onCreate(..))")  // -- ok
/*    @Pointcut("execution(* android.app.Activity.on**(..))")  // --  ok
    fun methodLog() {
    }*/


    /**
     * 定义一个切面方法，包裹切点
     */
    /*@Around("methodLog()")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint): Any? {
        //获取方法
        val methodSignature = joinPoint.signature as MethodSignature
        //方法所在类名
        val className = methodSignature.declaringType.simpleName
        //方法名
        val methodName = methodSignature.name
        //方法参数名
        val parameterNames = methodSignature.parameterNames
        //方法参数类型
        val parameterTypes = methodSignature.parameterTypes

        //切点（方法）参数
        val args = joinPoint.args
        //切点类型（方法）
        val kind = joinPoint.kind
        //切点（方法）源码位置
        val sourceLocation = joinPoint.sourceLocation

        //打印日志
        L.w("before --  ${className}#${methodName}")
        //执行方法
        val proceed = joinPoint.proceed()
        //打印日志
        L.w("after --  ${className}#${methodName}")

        //返回执行结果
        return proceed
    }*/

    /* @Before("execution(* android.app.Activity.on**(..))")
     fun beforeOnMethod(joinPoint: JoinPoint) {
         val methodSignature = joinPoint.signature as MethodSignature
         val className = methodSignature.declaringType.simpleName
         val methodName = methodSignature.name
         L.w("before --  ${className}#${methodName}")
     }

     @After("execution(* android.app.Activity.on**(..))")
     fun afterOnMethod(joinPoint: JoinPoint) {
         val methodSignature = joinPoint.signature as MethodSignature
         val className = methodSignature.declaringType.simpleName
         val methodName = methodSignature.name
         L.w("after --  ${className}#${methodName}")
     }*/
}