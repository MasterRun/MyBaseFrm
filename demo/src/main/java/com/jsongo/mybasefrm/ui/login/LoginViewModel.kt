package com.jsongo.mybasefrm.ui.login

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.constant.gson
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.plugin.AppPlugin
import com.jsongo.core.plugin.MobileIM
import com.jsongo.core.widget.RxToast
import com.jsongo.mybasefrm.data.repository.HttpRequestManager
import com.jsongo.mybasefrm.data.repository.NetFailedException
import com.safframework.log.L
import kotlinx.coroutines.launch

/**
 * @author ： jsongo
 * @date ： 2019/11/24 15:19
 * @desc : 登录viewmodel
 */
class LoginViewModel : BaseViewModel() {

    val account = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    val showPassword = MutableLiveData<Boolean>(false)

    val loginResult = MutableLiveData<Boolean>()

    val loading = MutableLiveData<Boolean>()

    val showThirdPartLogin = MutableLiveData<Boolean>(false)

    fun login(account: String, password: String) {
        loading.value = true
        this.account.value = account
        this.password.value = password
        mainScope.launch {
            try {
                val userguid = HttpRequestManager.checkUser(account, password)
                val userInfo = HttpRequestManager.getUserInfo(userguid)
                val loginIMResult = loginIM(userguid, password)
                if (loginIMResult.code > 0) {
                    L.e("login success")
                    CommonDbOpenHelper.setKeyValue(CommonDbKeys.USER_GUID, userguid)
                    CommonDbOpenHelper.setKeyValue(CommonDbKeys.USER_PASSWORD, password)
                    CommonDbOpenHelper.setKeyValue(
                        CommonDbKeys.USER_INFO,
                        gson.toJson(userInfo)
                    )
                    loginResult.value = true
                } else {
                    onLoginError(loginIMResult.message, null)
                }
            } catch (e: NetFailedException) {
                onLoginError(null, e)
            }
        }

    }

    /**
     * 登录失败回调
     */
    fun onLoginError(msg: String?, throwable: Throwable?) {
        throwable?.printStackTrace()
        val errorMsg = if (!TextUtils.isEmpty(msg)) {
            msg!!
        } else if (!TextUtils.isEmpty(throwable?.message)) {
            throwable?.message!!
        } else {
            "登录失败！"
        }
        L.e(errorMsg)
        loading.value = false
        RxToast.error(errorMsg)
    }

    /**
     * 初始化并登陆MobileIM
     */
    fun loginIM(userguid: String, password: String): DataWrapper<MutableMap<String, Any?>> {
        //如果没启用组件，直接成功回调
        if (!AppPlugin.isEnabled(MobileIM)) {
            return DataWrapper(hashMapOf(Pair("result", true as Any?)))
        }

        val chatId = userguid
        val chatPassword = password
        val result = AppPlugin.invoke(
            MobileIM, "login", hashMapOf(
                Pair("chatid", chatId),
                Pair("password", chatPassword)
            )
        )
        return result
    }


}
