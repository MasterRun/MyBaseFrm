package com.jsongo.core_mini.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jsongo.core_mini.CoreMini
import com.qmuiteam.qmui.util.QMUIDisplayHelper

/**
 * @author ： jsongo
 * @date ： 2019/11/23 11:06
 * @desc :
 */
object KeyboardUtil {

    /**
     * 修复透明状态栏导致输入法弹起时输入框不弹起的问题
     */
    @JvmStatic
    fun fixSoftInput(activity: FragmentActivity) {
        val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = QMUIDisplayHelper.getScreenHeight(CoreMini.context)
            val heightDiff = screenHeight - rect.bottom
            activity.findViewById<View>(android.R.id.content).run {
                if (layoutParams is ViewGroup.MarginLayoutParams) {
                    (layoutParams as ViewGroup.MarginLayoutParams).setMargins(0, 0, 0, heightDiff)
                }
                requestLayout()
            }
        }
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                //设置透明状态栏以及导航栏与软键盘弹出的冲突问题（软键盘弹出收回监听）
                activity.window.decorView.viewTreeObserver.addOnGlobalLayoutListener(
                    onGlobalLayoutListener
                )
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                //解除监听
                activity.window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(
                    onGlobalLayoutListener
                )
            }
        })
    }

    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    fun hideSoftInput(activity: Activity) {
        val view: View? = activity.window.peekDecorView()
        if (view != null) {
            val inputmanger: InputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmanger.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    /**
     * 动态隐藏软键盘
     *
     * @param context 上下文
     * @param edit    输入框
     */
    fun hideSoftInput(context: Context, edit: EditText) {
        edit.clearFocus()
        val inputmanger = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputmanger.hideSoftInputFromWindow(edit.windowToken, 0)
    }


}