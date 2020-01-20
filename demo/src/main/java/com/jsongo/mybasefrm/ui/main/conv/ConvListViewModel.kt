package com.jsongo.mybasefrm.ui.main.conv

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.jsongo.core.arch.mvvm.BaseViewModel
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

    /**
     * 消息数据列表
     */
    val convs = MutableLiveData<MutableList<MutableMap<String, Any?>>>()

    /**
     * 未读消息数
     */
    val totalUnreadCount = MutableLiveData(0)

    /**
     * 错误信息
     */
    val errorMessage = MutableLiveData<String>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
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
                                //转换数据
                                val convsList = data?.get("convs") as List<*>
                                //计算未读消息总数
                                var totalCount = 0
                                for (mutableMap in convsList) {
                                    if (mutableMap is Map<*, *>) {
                                        val any = mutableMap["unreadCount"] ?: "0"
                                        try {
                                            totalCount += Integer.valueOf(any.toString())
                                        } catch (e: Exception) {
                                        }
                                    }
                                }
                                //post数据
                                convs.postValue(convsList as MutableList<MutableMap<String, Any?>>)
                                totalUnreadCount.postValue(totalCount)
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