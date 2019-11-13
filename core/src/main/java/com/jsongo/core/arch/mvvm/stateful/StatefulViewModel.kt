package com.jsongo.core.arch.mvvm.stateful

import android.arch.lifecycle.MutableLiveData
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.core.bean.ErrorDataWrapper

/**
 * @author ： jsongo
 * @date ： 2019/11/10 0:10
 * @desc :
 */
open class StatefulViewModel : BaseViewModel() {

    /**
     * 数据异常的LiveData
     */
    val errorLiverData: MutableLiveData<ErrorDataWrapper> by lazy {
        val data = MutableLiveData<ErrorDataWrapper>()
        data
    }

}