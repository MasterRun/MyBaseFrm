package com.jsongo.mybasefrm.ui.demo

import android.arch.lifecycle.MutableLiveData
import com.jsongo.core.base.mvvm.stateful.StatefulViewModel
import com.jsongo.core.bean.toErrorDataWrapper
import com.jsongo.mybasefrm.data.repository.HttpRequestManager
import kotlinx.coroutines.launch

/**
 * author ： jsongo
 * createtime ： 2019/7/23 13:52
 * desc :
 */
class DemoViewModel : StatefulViewModel() {

    val txtContent = MutableLiveData<String>()

    init {
        txtContent.value = ""
    }

    fun getAuthtypes() {
        mainScope.launch {
            try {
                val data = HttpRequestManager.getAuthtypes().getAsJsonObject("data").toString()
                txtContent.value = data
            } catch (e: Exception) {
                errorLiverData.value = e.message.toErrorDataWrapper()
            }
        }
    }

    fun getDaliyDank() {
        mainScope.launch {
            try {
//                val category = HttpRequestManager.getDailyGank().data.getAsJsonArray("category").toString()
//                txtContent.value = category
            } catch (e: Exception) {
                errorLiverData.value = e.message.toErrorDataWrapper()
            }
        }
    }
}