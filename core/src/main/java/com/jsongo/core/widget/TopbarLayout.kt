package com.jsongo.core.widget

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.TextView
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.jsongo.core.util.ActivityCollector
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUITopBar
import com.qmuiteam.qmui.widget.QMUITopBarLayout

/**
 * author ： jsongo
 * createtime ： 2019/7/11 22:41
 * desc : 标题栏 modify from QMUITopBarLayout
 */
class TopbarLayout(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.QMUITopBarStyle
) : QMUITopBarLayout(mContext, attrs, defStyleAttr) {

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
        //获取QMUITopBarLayout中topbar等字段
        QMUITopBarLayout::class.java.apply {
            getDeclaredField("mTopBar").apply {
                isAccessible = true
                topBar = get(this@TopbarLayout) as QMUITopBar
            }
            getDeclaredField("mTopBarSeparatorColor").apply {
                isAccessible = true
                topBarSeparatorColor = get(this@TopbarLayout) as Int
            }
            getDeclaredField("mTopBarBgColor").apply {
                isAccessible = true
                topBarBgColor = get(this@TopbarLayout) as Int
            }
            getDeclaredField("mTopBarSeparatorHeight").apply {
                isAccessible = true
                topBarSeparatorHeight = get(this@TopbarLayout) as Int
            }
        }

        //设置默认样式
        setBackgroundColor(ContextCompat.getColor(context, R.color.app_color_theme))
        tvTitle = setTitle(BaseCore.context.getString(R.string.topbar_title))
        tvTitle.setTextColor(Color.WHITE)

        backImageButton = addLeftBackImageButton()
        backImageButton.setOnClickListener {
            ActivityCollector.topActivity.onBackPressed()
        }
    }

    /**
     * 添加状态栏的高度
     */
    fun addStatusBarHeight() {
        val statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(context)
        setPadding(paddingLeft, paddingTop + statusbarHeight, paddingRight, paddingBottom)
    }
}