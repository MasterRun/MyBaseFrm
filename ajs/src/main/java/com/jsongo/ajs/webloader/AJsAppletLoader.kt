package com.jsongo.ajs.webloader

import android.graphics.Color
import android.view.View
import android.widget.RelativeLayout
import com.jsongo.ajs.widget.AppletMenu
import com.jsongo.core.widget.RxToast
import com.jsongo.ui.util.addStatusBarHeightPadding
import com.qmuiteam.qmui.kotlin.wrapContent
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIResHelper

/**
 * @author ： jsongo
 * @date ： 19-10-3 下午5:13
 * @desc : fragment 加载 小程序
 */
class AJsAppletLoader : AJsWebLoader() {
    companion object {

        /**
         * @param url 加载的utl
         * @param showTopBar 是否显示topbar  默认是
         * @param scrollable 是否可以滑动  默认否  当卡片模式时，设置true，webview可滑动
         */
        @JvmStatic
        fun newInstance(
            url: String,
            showTopBar: Boolean = true,
            showProgress: Boolean = true,
            scrollable: Boolean = false,
            fixHeight: Boolean = true,
            bgColor: Int = Color.TRANSPARENT
        ) = AJsAppletLoader().apply {
            webPath = url
            this.showTopBar = showTopBar
            this.showProgress = showProgress
            this.scrollable = scrollable
            this.fixHeight = fixHeight
            this.bgColor = bgColor
        }
    }

    lateinit var appletMenu: AppletMenu
        protected set

    override fun init() {
        //小程序隐藏标题返回按钮
        topbar.backImageButton.visibility = View.GONE
    }

    override fun initView(view: View) {
        super.initView(view)

        //右上角按钮
        appletMenu = AppletMenu(view.context)
        //添加状态栏的padding
        appletMenu.addStatusBarHeightPadding()
        //topbar的qmui配置高度
        val topbarAttrHeight =
            QMUIResHelper.getAttrDimen(view.context, com.qmuiteam.qmui.R.attr.qmui_topbar_height)
        //设置位置
        val layoutParams = RelativeLayout.LayoutParams(
            wrapContent,
            topbarAttrHeight + QMUIDisplayHelper.getStatusBarHeight(view.context)
        )
        //右边距10dp
        layoutParams.rightMargin = QMUIDisplayHelper.dp2px(view.context, 10)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        rlLayoutRoot.addView(appletMenu, layoutParams)

        appletMenu.ivClose.setOnClickListener {
            activity?.finish()
        }
        appletMenu.ivMore.setOnClickListener {
            RxToast.info("click more")
        }
    }
}
