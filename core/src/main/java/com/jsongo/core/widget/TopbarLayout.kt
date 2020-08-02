package com.jsongo.core.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.jsongo.core_mini.common.ActivityCollector
import com.jsongo.core_mini.widget.ITopbar
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.qqface.QMUIQQFaceView
import com.qmuiteam.qmui.widget.MyQMUITopBarLayout
import com.qmuiteam.qmui.widget.QMUITopBar

/**
 * author ： jsongo
 * createtime ： 2019/7/11 22:41
 * desc : 标题栏 modify from QMUITopBarLayout
 */
class TopbarLayout @JvmOverloads constructor(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.QMUITopBarStyle
) : MyQMUITopBarLayout(mContext, attrs, defStyleAttr), ITopbar {

    val topBar: QMUITopBar

    val tvTitle: QMUIQQFaceView
    override val backImageButton: QMUIAlphaImageButton

    override var titleText: CharSequence
        get() = tvTitle.text.toString()
        set(value) {
            tvTitle.text = value
        }

    override fun setTitleColor(colorInt: Int) {
        tvTitle.setTextColor(colorInt)
    }

    override var titleSize: Int
        get() = tvTitle.textSize
        set(value) {
            tvTitle.textSize = value
        }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }

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
    override fun hideBottomDivider() {
        //updateBottomSeparatorColor(Color.TRANSPARENT);
        //默认设置底部分割线的高度为0（隐藏）
        updateBottomDivider(0, 0, 0, Color.TRANSPARENT)
    }
}