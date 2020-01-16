package com.jsongo.mybasefrm.ui.login

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jsongo.annotation.anno.Page
import com.jsongo.annotation.anno.permission.PermissionNeed
import com.jsongo.core.arch.BaseActivity
import com.jsongo.core.arch.mvvm.IMvvmView
import com.jsongo.core.plugin.AppPlugin
import com.jsongo.core.plugin.MobileIM
import com.jsongo.core.ui.splash.SplashActivity
import com.jsongo.core.util.ActivityCollector
import com.jsongo.mybasefrm.AppApplication
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.databinding.ActivityLoginBinding
import com.jsongo.mybasefrm.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author ： jsongo
 * @date ： 2019/11/24 12:38
 * @desc : 登录页
 */
@Page(R.layout.activity_login, 0)
class LoginActivity : BaseActivity(), IMvvmView {

    lateinit var viewModel: LoginViewModel

    lateinit var activityLoginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        initPermission()

        initView()

        observeLiveData()

        bindData()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    @PermissionNeed(Manifest.permission.READ_PHONE_STATE)
    fun initPermission() {
    }

    override fun initView() {

        setSwipeBackEnable(false)

        //如果启用MobileIM，初始化MobileIM
        if (AppPlugin.isEnabled(MobileIM)) {
            AppPlugin.invoke(MobileIM, "init", hashMapOf(Pair("context", this)))
        }

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

        viewModel.loading.observe(this, Observer {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        })

        viewModel.loginResult.observe(this, Observer {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            viewModel.loading.value = false
        })
    }

    override fun bindData() {
        //dataBinding
        activityLoginBinding = ActivityLoginBinding.bind(mainView)
//        activityLoginBinding.setVariable(BR.vm, viewModel)
//        activityLoginBinding.setVariable(BR.eventProxy, EventProxy(this))
        activityLoginBinding.vm = viewModel
        activityLoginBinding.eventProxy = EventProxy(this)

    }

    /**
     * （点击）事件代理
     */
    class EventProxy(private val loginActivity: LoginActivity) {

        val viewModel: LoginViewModel = loginActivity.viewModel
        val binding: ActivityLoginBinding = loginActivity.activityLoginBinding

        /**
         * 点击显示密码
         */
        fun clickShowPwd() {
            val value = viewModel.showPassword.value
            viewModel.showPassword.value = value?.not()
        }

        /**
         * 点击登录
         */
        fun clickLogin() {
            viewModel.login(
                binding.etUserAccount.text.toString(),
                binding.etUserPwd.text.toString()
            )
        }

    }

    companion object {
        fun go() {
            AppApplication.context.startActivity(
                Intent(AppApplication.context, LoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
        }
    }
}