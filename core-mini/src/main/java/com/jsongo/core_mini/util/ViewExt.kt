package com.jsongo.core_mini.util

import android.view.View
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * @author ： jsongo
 * @date ： 2020/7/26 21:05
 * @desc :
 */

/**
 * 添加状态栏高度的padding
 */
fun View.addStatusBarHeightPadding() {
    val statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(context)
    setPadding(paddingLeft, paddingTop + statusbarHeight, paddingRight, paddingBottom)
}