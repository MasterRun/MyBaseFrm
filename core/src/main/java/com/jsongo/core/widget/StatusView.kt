package com.jsongo.core.widget

import android.content.Context
import android.util.AttributeSet
import com.jsongo.core.R
import com.jsongo.core_mini.widget.IStatusView
import com.qmuiteam.qmui.widget.QMUIEmptyView

/**
 * @author ： jsongo
 * @date ： 2020/8/2 18:08
 * @desc :
 */
class StatusView @JvmOverloads constructor(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.QMUITopBarStyle
) : QMUIEmptyView(mContext, attrs, defStyleAttr), IStatusView