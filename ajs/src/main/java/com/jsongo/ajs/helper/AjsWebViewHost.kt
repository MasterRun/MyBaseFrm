package com.jsongo.ajs.helper

import android.app.Activity
import android.content.Intent
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jsongo.core.arch.BaseActivityWrapper
import com.jsongo.core.arch.BaseFragmentWrapper
import com.jsongo.core.arch.IPageWrapper
import io.reactivex.disposables.CompositeDisposable

/**
 * @author ： jsongo
 * @date ： 19-10-13 下午9:08
 * @desc : AjsWebView的持有者 实现此接口设置给ajswebview，可实现activity/fragment调用ajs api
 *        可用于长回调接口，一般为AJsWebLoader
 *        （当 ajswebview作为view单独使用时，需要设置此对象为FragmentActivity/Fragment）
 */
interface AjsWebViewHost {
    companion object {
        /**
         * 长回调所有的集合，map的key为类名，SparseArray的key为requestCode
         */
        val longCallBackMap = HashMap<String, SparseArray<LongCallback<Intent>>>()
    }

    val compositeDisposable: CompositeDisposable

    /**
     * 长回调
     */
    val longCallbacks: SparseArray<LongCallback<Intent>>
        get() {
            //获取到对应的长回调,目前key为类名（Activity/Fragment）
            val name = this::class.java.name
            var sparseArray = longCallBackMap[name]
            if (sparseArray == null) {
                sparseArray = SparseArray()
                longCallBackMap[name] = sparseArray
            }
            return sparseArray
        }

    /**
     * 所在的activity
     */
    val hostActivity: FragmentActivity?
        get() = hostFragment?.activity
    /**
     * 所在的fragment
     */
    val hostFragment: Fragment?
        get() = null

    /**
     * 当前的IPage  用于设置topbar smaerrefresh等
     */
    val hostIPage: IPageWrapper?
        get() = when {
            hostFragment is BaseFragmentWrapper -> hostFragment as BaseFragmentWrapper
            hostActivity is BaseActivityWrapper -> hostActivity as BaseActivityWrapper
            else -> null
        }

    /**
     * 添加长回调
     */
    fun addLongCallback(requestCode: Int, longCallback: LongCallback<Intent>) {
        longCallbacks.put(requestCode, longCallback)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    /**
     * 回调  在onactivitresult中调用
     */
    fun onAjsLongCallBack(requestCode: Int, resultCode: Int, data: Intent?) {
        val longCallback = longCallbacks.get(requestCode)
        longCallback?.let {
            if (resultCode == Activity.RESULT_OK) {
                it.success(data)
            } else {
                it.failed(data)
            }
            longCallbacks.remove(requestCode)
        }
    }
}