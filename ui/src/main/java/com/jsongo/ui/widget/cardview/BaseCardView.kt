package com.jsongo.ui.widget.cardview

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import com.jsongo.ui.R

/**
 * @author ： jsongo
 * @date ： 19-10-9 下午10:27
 * @desc : 卡片布局的父布局
 */
abstract class BaseCardView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    CardView(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.cardViewStyle
    )
}