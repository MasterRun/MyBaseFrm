package com.jsongo.mybasefrm.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.BaseActivity
import com.jsongo.core.arch.mvvm.IMvvmView
import com.jsongo.core.ui.splash.SplashActivity
import com.jsongo.core.util.ActivityCollector
import com.jsongo.core.widget.RxToast
import com.jsongo.mybasefrm.R
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author ： jsongo
 * @date ： 2019/11/24 12:38
 * @desc : 登录页
 */
@Page(R.layout.activity_login, 0)
class LoginActivity : BaseActivity(), IMvvmView {

    lateinit var viewModel: LoginViewModel

    lateinit var eventProxy: EventProxy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        initView()

        observeLiveData()

        regEventListener()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun initView() {
        setSwipeBackEnable(false)

        //结束启动页
        ActivityCollector.finish(SplashActivity::class.java)
    }

    override fun observeLiveData() {
        viewModel.showPassword.observe(this, Observer {
            if (it) {
                //如果选中，显示密码
                et_user_pwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                iv_showpwd.imageTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.darker_gray))
            } else {
                //否则隐藏密码
                et_user_pwd.transformationMethod = PasswordTransformationMethod.getInstance()
                iv_showpwd.imageTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.lighter_gray))
            }
            //设置光标位置
            et_user_pwd.setSelection(et_user_pwd.text.toString().length)
        })

        viewModel.loginResult.observe(this, Observer {
            RxToast.success(it)
        })
    }

    fun regEventListener() {
        eventProxy = EventProxy(this)
        iv_showpwd.setOnClickListener {
            eventProxy.clickShowPwd(it)
        }
        btn_login.setOnClickListener {
            eventProxy.clickLogin(it)
        }
    }

    class EventProxy(private val loginActivity: LoginActivity) {

        var viewModel: LoginViewModel = loginActivity.viewModel

        /**
         * 点击显示密码
         */
        fun clickShowPwd(it: View?) {
            val value = viewModel.showPassword.value
            viewModel.showPassword.value = value?.not()
        }

        /**
         * 点击登录
         */
        fun clickLogin(it: View?) {
            viewModel.login()
        }

    }
}