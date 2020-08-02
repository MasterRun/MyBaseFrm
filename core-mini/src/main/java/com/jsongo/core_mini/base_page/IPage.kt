package com.jsongo.core_mini.base_page

import android.content.Context
import com.jsongo.core_mini.widget.ILoadingDialog
import com.jsongo.core_mini.widget.IStatusView
import com.jsongo.core_mini.widget.ITopbar

/**
 * author ： jsongo
 * createtime ： 2019/7/27 11:38
 * desc : 页面接口
 */
interface IPage {

    val topbar: ITopbar?

    val loadingDialog: ILoadingDialog?

    val statusView: IStatusView?

    val layoutId: Int

    fun initIPage(context: Context)

    fun onDestroyIPage()
}