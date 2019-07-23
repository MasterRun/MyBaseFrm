package com.jsongo.core.mvp.base

import android.content.Context
import android.os.Bundle
import android.view.View

/**
 * @author jsongo
 * @date 2019/3/26 10:32
 */
abstract class BaseMvpFragment<out M : IBaseMvp.IBaseModel, out V : IBaseMvp.IBaseView>
    : BaseFragment(), IBaseMvp.IBaseView {

    protected abstract val basePresenter: BasePresenter<M, V>?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        basePresenter?.start()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initPresenter()
    }

    override fun onDetach() {
        super.onDetach()
        basePresenter?.onDestory()
    }
}
