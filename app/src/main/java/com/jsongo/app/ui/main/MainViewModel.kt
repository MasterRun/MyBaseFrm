package com.jsongo.app.ui.main

import androidx.lifecycle.MutableLiveData
import com.jsongo.core.arch.mvvm.BaseViewModel

/**
 * @author ： jsongo
 * @date ： 2019/11/7 12:57
 * @desc : ViewModel 测试
 */
class MainViewModel : BaseViewModel() {

    /**
     * 首页底部tab的角标数量
     */
    val mainTabTips = MutableLiveData<IntArray>()

    init {
        //初始化角标数量
        mainTabTips.value = intArrayOf(0, 0, 0, 0)
    }

    /**
     * 设置角标数量
     */
    fun setMainTabTipCount(index: Int, count: Int) {
        val value = mainTabTips.value
        value?.set(index, count)
        mainTabTips.value = value
    }

}
