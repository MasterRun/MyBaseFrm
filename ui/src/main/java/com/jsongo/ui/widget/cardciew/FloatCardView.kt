package com.jsongo.ui.widget.cardciew

import android.content.Context
import android.util.AttributeSet
import com.jsongo.ui.R

/**
 * @author ： jsongo
 * @date ： 19-10-9 下午10:27
 * @desc : 悬浮卡片布局
 */
class FloatCardView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    BaseCardView(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.cardViewStyle
    )
}