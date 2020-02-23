package com.jsongo.core.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.jsongo.core.common.ActivityCollector
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.qqface.QMUIQQFaceView
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

    constructor(mContext: Context, attrs: AttributeSet?) : this(
        mContext,
        attrs,
        R.attr.QMUITopBarStyle
    )

    constructor(mContext: Context) : this(mContext, null)

    val topBar: QMUITopBar

    val tvTitle: QMUIQQFaceView
    val backImageButton: QMUIAlphaImageButton

    init {
        topBar = mTopBar
        //设置默认样式
        setBackgroundColor(ContextCompat.getColor(context, R.color.app_color_theme))

        tvTitle = setTitle(BaseCore.context.getString(R.string.topbar_title))
        tvTitle.setTextColor(Color.WHITE)

        backImageButton = addLeftBackImageButton()
        backImageButton.setOnClickListener {
            ActivityCollector.myForegroundActivity?.onBackPressed()
        }
    }

    /**
     * 隐藏底部分割线
     */
    fun hideBottomDivider() {
        //updateBottomSeparatorColor(Color.TRANSPARENT);
        //默认设置底部分割线的高度为0（隐藏）
        updateBottomDivider(0, 0, 0, Color.TRANSPARENT)
    }
}