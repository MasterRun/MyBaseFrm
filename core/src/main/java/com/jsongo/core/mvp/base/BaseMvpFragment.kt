package com.jsongo.core.mvp.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.jsongo.core.annotations.PresenterBinder
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

        if (showEnterPageLoading) {
            onPageLoading()
        }
        initView()
        basePresenter?.start()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        PresenterBinder.bind(this)
        initPresenter()
    }

    //region page pageStatus
    override fun onPageLoading() {
        pageStatus = Status.LOADING
        emptyView?.show(true, "加载中...", null, null, null)
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
        inflateEmptyView()?.show(true, "加载中...", null, null, null)
    }

    override fun onPageEmpty() {
        pageStatus = Status.NO_DATA
        smartRefreshLayout.finishRefresh()
        inflateEmptyView()?.show(false, "暂无数据", "这里什么都没有哦", "重试") {
            onPageReloading()
        }
    }

    override fun onPageError(msg: String?) {
        pageStatus = Status.ERROR
        smartRefreshLayout.finishRefresh()
        val message = if (msg.isNullOrEmpty()) "数据找不到了" else msg
        inflateEmptyView()?.show(false, "出错喽", message, "重试") {
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
