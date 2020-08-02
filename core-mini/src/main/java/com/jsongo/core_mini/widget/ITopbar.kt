package com.jsongo.core_mini.widget

import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author ： jsongo
 * @date ： 2020/7/26 20:50
 * @desc :
 */
interface ITopbar {

    var title: AppCompatTextView

    var backImageButton: View

    var visibility: Int

    fun setBackgroundColor(@ColorInt colorInt: Int)

    fun hideBottomDivider()
}