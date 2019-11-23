package com.jsongo.core.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author ： jsongo
 * @date ： 2019/11/23 11:06
 * @desc :
 */
object KeyboardUtil {
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