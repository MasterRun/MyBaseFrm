package com.jsongo.core.mvp.base

import android.os.Bundle
import android.view.View
import com.jsongo.core.R
import kotlinx.android.synthetic.*

/**
 * author ： jsongo
 * createtime ： 2019/7/22 17:19
 * desc :
 */
abstract class BaseMvpActivity<out M : IBaseMvp.IBaseModel, out V : IBaseMvp.IBaseView>
    : BaseActivity(), IBaseMvp.IBaseView {
    /**
     * 页面状态
     */
    override lateinit var pageStatus: Status
    /**
     * 进入页面显示加载
     */
    open val showEnterPageLoading = true

    protected abstract val basePresenter: BasePresenter<M, V>?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (showEnterPageLoading) {
            onPageLoading()
        } else {
            onPageLoaded()
        }

        initPresenter()

        initView()
        basePresenter?.start()
    }


    //region page pageStatus
    override fun onPageLoading() {
        pageStatus = Status.LOADING
        loadingDialog.show()
        if (emptyView?.visibility != View.GONE) {
            emptyView?.hide()
        }
    }

    override fun onPageLoaded() {
        pageStatus = Status.SHOWING_DATA
        loadingDialog.dismiss()
        smartRefreshLayout.finishRefresh()
        if (emptyView?.visibility != View.GONE) {
            emptyView?.hide()
        }
    }

    override fun onPageReloading() {
        pageStatus = Status.RELOADING
        loadingDialog.dismiss()
        inflateEmptyView()?.show(true, getString(R.string.empty_view_loading), null, null, null)
    }

    override fun onPageEmpty() {
        pageStatus = Status.NO_DATA
        smartRefreshLayout.finishRefresh()
        loadingDialog.dismiss()
        inflateEmptyView()?.show(
            false,
            getString(R.string.empty_view_nodata_title),
            getString(R.string.empty_view_nodata_detail),
            getString(R.string.empty_view_retry)
        ) {
            onPageReloading()
        }
    }

    override fun onPageError(msg: String?) {
        pageStatus = Status.ERROR
        smartRefreshLayout.finishRefresh()
        loadingDialog.dismiss()
        val message = if (msg.isNullOrEmpty()) getString(R.string.empty_view_error_detail) else msg
        inflateEmptyView()?.show(
            false,
            getString(R.string.empty_view_error_title),
            message,
            getString(R.string.empty_view_retry)
        ) {
            onPageReloading()
        }
    }
    //endregion

    override fun onDestroy() {
        clearFindViewByIdCache()
        basePresenter?.onDestory()
        super.onDestroy()
    }
}