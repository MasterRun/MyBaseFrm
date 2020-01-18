package com.jsongo.mybasefrm.ui.login

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.constant.gson
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.plugin.AppPlugin
import com.jsongo.core.plugin.MobileIM
import com.jsongo.core.util.CommonCallBack
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

    val account = MutableLiveData<String>("test_account1")

    val password = MutableLiveData<String>("11111")

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
                loginIM(userguid, password, object : CommonCallBack {
                    override fun success(data: Map<String, Any?>?) {
                        L.e("login success")
                        CommonDbOpenHelper.setKeyValue(CommonDbKeys.USER_GUID, userguid)
                        CommonDbOpenHelper.setKeyValue(CommonDbKeys.USER_PASSWORD, password)
                        CommonDbOpenHelper.setKeyValue(
                            CommonDbKeys.USER_INFO,
                            gson.toJson(userInfo)
                        )
                        loginResult.value = true
                    }

                    override fun failed(code: Int, msg: String, throwable: Throwable?) {
                        onLoginError(msg, throwable)
                    }
                })
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
    fun loginIM(userguid: String, password: String, commonCallBack: CommonCallBack) {
        //如果没启用组件，直接成功回调
        if (!AppPlugin.isEnabled(MobileIM)) {
            commonCallBack.success(null)
            return
        }
        val chatId = userguid
        val chatPassword = password
        AppPlugin.invoke(
            MobileIM, "_login", hashMapOf(
                Pair("chatid", chatId),
                Pair("password", chatPassword)
            ), commonCallBack
        )
    }


}
