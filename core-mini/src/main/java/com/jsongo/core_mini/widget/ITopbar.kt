package com.jsongo.core_mini.widget

import android.widget.ImageView
import androidx.annotation.ColorInt

/**
 * @author ： jsongo
 * @date ： 2020/7/26 20:50
 * @desc :
 */
interface ITopbar {

    var titleText: CharSequence

    var titleSize: Int

    val backImageButton: ImageView

    fun setTitleColor(@ColorInt colorInt: Int)

    fun setBackgroundColor(@ColorInt colorInt: Int)

    fun hideBottomDivider()

    fun hide()

    fun show()
}