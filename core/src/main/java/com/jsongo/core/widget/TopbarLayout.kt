package com.jsongo.core.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.jsongo.core.util.ActivityCollector
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.widget.MyQMUITopBarLayout
import com.qmuiteam.qmui.widget.QMUITopBar

/**
 * author ： jsongo
 * createtime ： 2019/7/11 22:41
 * desc : 标题栏 modify from QMUITopBarLayout
 */
class TopbarLayout(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.QMUITopBarStyle
) : MyQMUITopBarLayout(mContext, attrs, defStyleAttr) {

    // TODO: 2019/8/9 封装设置返回按钮颜色  添加topbar底部横线

    constructor(mContext: Context, attrs: AttributeSet?) : this(
        mContext,
        attrs,
        R.attr.QMUITopBarStyle
    )

    constructor(mContext: Context) : this(mContext, null)

    val topBar: QMUITopBar
    val topBarSeparatorColor: Int
    val topBarBgColor: Int
    val topBarSeparatorHeight: Int

    val tvTitle: TextView
    val backImageButton: QMUIAlphaImageButton

    init {
        topBar = mTopBar
        topBarBgColor = mTopBarBgColor
        topBarSeparatorColor = mTopBarSeparatorColor
        topBarSeparatorHeight = mTopBarSeparatorHeight

        //设置默认样式
        setBackgroundColor(ContextCompat.getColor(context, R.color.app_color_theme))
        tvTitle = setTitle(BaseCore.context.getString(R.string.topbar_title))
        tvTitle.setTextColor(Color.WHITE)

        backImageButton = addLeftBackImageButton()
        backImageButton.setOnClickListener {
            ActivityCollector.topActivity.onBackPressed()
        }
    }
}