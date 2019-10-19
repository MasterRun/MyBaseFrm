package com.jsongo.ui.util

import android.view.View
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午9:03
 * @desc :
 */

/**
 * 添加状态栏高度的padding
 */
fun View.addStatusBarHeightPadding() {
    val statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(context)
    setPadding(paddingLeft, paddingTop + statusbarHeight, paddingRight, paddingBottom)
}