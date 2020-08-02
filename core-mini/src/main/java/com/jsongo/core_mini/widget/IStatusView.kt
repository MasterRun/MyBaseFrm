package com.jsongo.core_mini.widget

import android.view.View

/**
 * @author ： jsongo
 * @date ： 2020/7/26 21:49
 * @desc :
 */
interface IStatusView {

    fun show(
        loading: Boolean,
        titleText: String?,
        detailText: String?,
        buttonText: String?,
        onButtonClickListener: View.OnClickListener?
    )

    fun isShowing(): Boolean

    fun hide()
}