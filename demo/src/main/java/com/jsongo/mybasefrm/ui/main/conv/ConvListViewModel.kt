package com.jsongo.mybasefrm.ui.main.conv

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.google.gson.reflect.TypeToken
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.core.constant.gson
import com.jsongo.core.plugin.AppPlugin
import com.jsongo.core.plugin.MobileIM
import com.jsongo.core.util.CommonCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author ： jsongo
 * @date ： 2020/1/18 17:03
 * @desc : 会话列表的ViewModel
 */
class ConvListViewModel : BaseViewModel() {

    val convs = MutableLiveData<ObservableArrayList<MutableMap<String, Any?>>>()

    val errorMessage = MutableLiveData<String>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun getConvList() {
        if (AppPlugin.isEnabled(MobileIM)) {
            AppPlugin.invoke(
                MobileIM,
                "_getConvs",
                hashMapOf(Pair("scope", mainScope)),
                object : CommonCallBack {
                    override fun success(data: Map<String, Any?>?) {
                        mainScope.launch(Dispatchers.IO) {
                            try {
                                val type = object :
                                    TypeToken<ObservableArrayList<MutableMap<String, Any?>>>() {}.type
                                val convsList =
                                    gson.fromJson<ObservableArrayList<MutableMap<String, Any?>>>(
                                        data?.get("convs") as String,
                                        type
                                    )
                                convs.postValue(convsList)
                            } catch (e: Exception) {
                                failed(-1, e.message ?: "", e)
                            }
                        }
                    }

                    override fun failed(code: Int, msg: String, throwable: Throwable?) {
                        errorMessage.value = msg
                    }
                })
        }
    }


}