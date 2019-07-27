package com.jsongo.core.mvp.base

import android.os.Bundle
import android.view.View
import com.jsongo.core.annotations.PresenterBinder
import kotlinx.android.synthetic.*

/**
 * author ： jsongo
 * createtime ： 2019/7/22 17:19
 * desc :
 */
abstract class BaseMvpActivity<out M : IBaseMvp.IBaseModel, out V : IBaseMvp.IBaseView>
    : BaseActivity(), IBaseMvp.IBaseView {
    /**
     * 页面装填
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
        }

        PresenterBinder.bind(this)
        initPresenter()

        initView()
        basePresenter?.validatePermission(this)
        basePresenter?.start()
    }


    //region page pageStatus
    override fun onPageLoading() {
        pageStatus = Status.LOADING
        loadingDialog.show()
        if (emptyView.visibility != View.GONE) {
            emptyView.hide()
        }
    }

    override fun onPageLoaded() {
        pageStatus = Status.SHOWING_DATA
        loadingDialog.dismiss()
        smartRefreshLayout.finishRefresh()
        if (emptyView.visibility != View.GONE) {
            emptyView.hide()
        }
    }

    override fun onPageReloading() {
        pageStatus = Status.RELOADING
        loadingDialog.dismiss()
        emptyView.show(true, "加载中...", null, null, null)
    }

    override fun onPageEmpty() {
        pageStatus = Status.NO_DATA
        smartRefreshLayout.finishRefresh()
        loadingDialog.dismiss()
        emptyView.show(false, "暂无数据", "这里什么都没有哦", "重试") {
            onPageReloading()
        }
    }

    override fun onPageError() {
        pageStatus = Status.ERROR
        smartRefreshLayout.finishRefresh()
        loadingDialog.dismiss()
        emptyView.show(false, "出错喽", "数据找不到了", "重试") {
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