package com.jsongo.core.mvp.base

import android.os.Bundle
import android.view.View
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import kotlinx.android.synthetic.*

/**
 * @author jsongo
 * @date 2019/3/26 10:32
 */
abstract class BaseMvpFragment<out M : IBaseMvp.IBaseModel, out V : IBaseMvp.IBaseView>
    : BaseFragment(), IBaseMvp.IBaseView {

    protected abstract val basePresenter: BasePresenter<M, V>?
    /**
     * 页面状态
     */
    override lateinit var pageStatus: Status
    /**
     * 进入页面显示加载
     */
    open val showEnterPageLoading = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPresenter()

        if (showEnterPageLoading) {
            onPageLoading()
        }
        initView()
        basePresenter?.start()
    }

    //region page pageStatus
    override fun onPageLoading() {
        pageStatus = Status.LOADING
        emptyView?.show(
            true,
            BaseCore.context.getString(R.string.empty_view_loading),
            null,
            null,
            null
        )
    }

    override fun onPageLoaded() {
        pageStatus = Status.SHOWING_DATA
        smartRefreshLayout.finishRefresh()
        if (emptyView?.visibility != View.GONE) {
            emptyView?.hide()
        }
    }

    override fun onPageReloading() {
        pageStatus = Status.RELOADING
        inflateEmptyView()?.show(
            true,
            BaseCore.context.getString(R.string.empty_view_loading),
            null, null, null
        )
    }

    override fun onPageEmpty() {
        pageStatus = Status.NO_DATA
        smartRefreshLayout.finishRefresh()
        inflateEmptyView()?.show(
            false,
            BaseCore.context.getString(R.string.empty_view_nodata_title),
            BaseCore.context.getString(R.string.empty_view_nodata_detail),
            BaseCore.context.getString(R.string.empty_view_retry)
        ) {
            onPageReloading()
        }
    }

    override fun onPageError(msg: String?) {
        pageStatus = Status.ERROR
        smartRefreshLayout.finishRefresh()
        val message =
            if (msg.isNullOrEmpty()) BaseCore.context.getString(R.string.empty_view_error_detail) else msg
        inflateEmptyView()?.show(
            false,
            BaseCore.context.getString(R.string.empty_view_error_title),
            message,
            BaseCore.context.getString(R.string.empty_view_retry)
        ) {
            onPageReloading()
        }
    }
    //endregion

    override fun onDetach() {
        view?.clearFindViewByIdCache()
        clearFindViewByIdCache()
        basePresenter?.onDestory()
        super.onDetach()
    }
}
