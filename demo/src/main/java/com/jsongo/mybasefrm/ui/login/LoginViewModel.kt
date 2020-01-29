package com.jsongo.mybasefrm.ui.login

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.constant.gson
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.plugin.AppPlugin
import com.jsongo.core.plugin.MobileIM
import com.jsongo.core.common.CommonCallBack
import com.jsongo.core.widget.RxToast
import com.jsongo.core.network.NetFailedException
import com.jsongo.mybasefrm.data.repository.UserHttpRequestManager
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
                val userguid = UserHttpRequestManager.checkUser(account, password)
                loginIM(userguid, password, object :
                    CommonCallBack {
                    override fun success(data: Map<String, Any?>?) {
                        L.e("login success")
                        //保存userguid和password
                        CommonDbOpenHelper.setKeyValue(CommonDbKeys.USER_GUID, userguid)
                        CommonDbOpenHelper.setKeyValue(CommonDbKeys.USER_PASSWORD, password)
                        requestUserInfo()
                    }

                    override fun failed(code: Int, msg: String, throwable: Throwable?) {
                        onLoginError(msg, throwable)
                    }
                })
            } catch (e: NetFailedException) {
                e.printStackTrace()
                onLoginError(null, e)
            }
        }

    }

    /**
     * 请求用户信息
     */
    fun requestUserInfo() {
        mainScope.launch {
            try {
                val userInfo = UserHttpRequestManager.getUserInfo()
                //保存用户信息
                CommonDbOpenHelper.setKeyValue(
                    CommonDbKeys.USER_INFO,
                    gson.toJson(userInfo)
                )
                //登录成功
                loginResult.value = true
            } catch (e: NetFailedException) {
                e.printStackTrace()
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
