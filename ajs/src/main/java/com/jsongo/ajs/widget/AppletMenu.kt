package com.jsongo.ajs.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.jsongo.ajs.R
import kotlinx.android.synthetic.main.applet_menu.view.*

/**
 * @author ： jsongo
 * @date ： 2020/2/23 13:23
 * @desc : 小程序右上角按钮
 */
class AppletMenu : LinearLayout {
    /**
     * 跟布局
     */
    lateinit var menuRoot: LinearLayout
        protected set
    /**
     * 更多
     */
    lateinit var ivMore: ImageView
        protected set
    /**
     * 关闭
     */
    lateinit var ivClose: ImageView
        protected set
    /**
     * 样式风格（指背景色）：true 深色 false 浅色  默认浅色
     */
    private var isDark: Boolean = false

    constructor(context: Context) : this(context, false)

    constructor(
        context: Context,
        isDark: Boolean
    ) : super(context) {
        initView()
        setStyle(isDark)
    }

    /**
     * 初始化
     */
    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.applet_menu, this)
        menuRoot = view.ll_menu_root
        ivClose = view.iv_close
        ivMore = view.iv_more
    }

    /**
     * 设置风格
     */
    fun setStyle(isDark: Boolean) {
        this.isDark = isDark
        //根据风格设置控件颜色
        val drawable =
            resources.getDrawable(R.drawable.applet_menu_bg) as GradientDrawable
        if (isDark) {
            drawable.setColor(context.resources.getColor(R.color.applet_menu_darkStyle_bg))
            val color = context.resources.getColor(R.color.applet_menu_darkStyle_fore)
            ivClose.setColorFilter(color)
            ivMore.setColorFilter(color)
        } else {
            drawable.setColor(context.resources.getColor(R.color.applet_menu_lightStyle_bg))
            val color = context.resources.getColor(R.color.applet_menu_lightStyle_fore)
            ivClose.setColorFilter(color)
            ivMore.setColorFilter(color)
        }
        menuRoot.setBackgroundDrawable(drawable)
    }

}
