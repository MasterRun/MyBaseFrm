package com.jsongo.mybasefrm.ui.mypage.mypage

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.constant.gson
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.util.toGsonBean
import com.jsongo.mybasefrm.bean.User
import com.jsongo.mybasefrm.data.repository.UserHttpRequestManager
import kotlinx.coroutines.launch

/**
 * @author ： jsongo
 * @date ： 2019/12/29 20:33
 * @desc :
 */
class MyPageViewModel : BaseViewModel() {

    val userInfo = MutableLiveData<User>()

    var errorMsg = MutableLiveData<String>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun requestUserInfo() {
        mainScope.launch {
            try {
                userInfo.value =
                    CommonDbOpenHelper.getValue(CommonDbKeys.USER_INFO).toGsonBean(User::class.java)
                val user = UserHttpRequestManager.getUserInfo()
                CommonDbOpenHelper.setKeyValue(CommonDbKeys.USER_INFO, gson.toJson(user))
                userInfo.value = user
            } catch (e: Exception) {
                e.printStackTrace()
                errorMsg.value = e.message
            }
        }
    }
}