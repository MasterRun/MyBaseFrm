package com.jsongo.mybasefrm.ui.login

import androidx.lifecycle.MutableLiveData
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.core.widget.RxToast
import com.jsongo.mybasefrm.data.repository.HttpRequestManager
import com.jsongo.mybasefrm.data.repository.NetFailedException
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

    val loginResult = MutableLiveData<String>()

    val loading = MutableLiveData<Boolean>()

    val showThirdPartLogin = MutableLiveData<Boolean>(false)

    fun login(account: String, password: String) {
        loading.value = true
        this.account.value = account
        this.password.value = password
        mainScope.launch {
            try {
                val checkUser = HttpRequestManager.checkUser(account, password)
                loginResult.value = checkUser
            } catch (e: NetFailedException) {
                e.printStackTrace()
                loading.value = false
                RxToast.error(e.message ?: "出错了！")
            }
        }
    }


}
