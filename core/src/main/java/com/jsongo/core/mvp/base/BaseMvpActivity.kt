package com.jsongo.core.mvp.base

import android.os.Bundle

/**
 * author ： jsongo
 * createtime ： 2019/7/22 17:19
 * desc :
 */
abstract class BaseMvpActivity<out M : IBaseMvp.IBaseModel, out V : IBaseMvp.IBaseView>
    : BaseActivity(), IBaseMvp.IBaseView {

    protected abstract val basePresenter: BasePresenter<M, V>?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPresenter()
        initView()
        basePresenter?.validatePermission(this)
        basePresenter?.start()
    }

    override fun onDestroy() {
        basePresenter?.onDestory()
        super.onDestroy()
    }
}