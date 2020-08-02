package com.jsongo.mybasefrm.ui.mypage.mypage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jsongo.ajs.webloader.AJsApplet
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.BaseFragmentWrapper
import com.jsongo.core.arch.mvvm.IMvvmView
import com.jsongo.core_mini.widget.RxToast
import com.jsongo.mybasefrm.AppApplication
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.databinding.FragmentMyPageBinding
import com.jsongo.mybasefrm.ui.main.MainActivity
import com.jsongo.mybasefrm.ui.personalinfo.PersonalInfoActivity
import com.jsongo.ui.component.fragment.settinglist.SettingItem
import com.jsongo.ui.component.fragment.settinglist.SettingListFragment
import com.jsongo.ui.component.fragment.settinglist.SettingSection
import com.jsongo.ui.component.fragment.settinglist.correctDetailTextPosition
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import java.util.*

@Page(R.layout.fragment_my_page, 1)
class MyPageFragment : BaseFragmentWrapper(), IMvvmView {

    companion object {
        /**
         * 加载性别图标
         */
        @JvmStatic
        @BindingAdapter("app:gender")
        fun loadGender(imageView: ImageView, gender: String?) {
            val context = imageView.context
            when (gender) {
                "1" -> {
                    imageView.visibility = View.VISIBLE
                    Glide.with(context).load(R.mipmap.ic_gender_man).into(imageView)
                }
                "2" -> {
                    imageView.visibility = View.VISIBLE
                    Glide.with(context).load(R.mipmap.ic_gender_woman).into(imageView)
                }
                else -> {
                    imageView.visibility = View.GONE
                }
            }
        }
    }

    lateinit var myPageViewModel: MyPageViewModel

    lateinit var myPageBinding: FragmentMyPageBinding

    lateinit var settingFragment: SettingListFragment

    lateinit var eventProxy: EventProxy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        initView()

        bindData()

        observeLiveData()
    }

    override fun initView() {
        myPageBinding = FragmentMyPageBinding.bind(mainView)

        smartRefreshLayout.isEnabled = false

        topbar.visibility = View.GONE

        val bgGray = ContextCompat.getColor(AppApplication.context, R.color.light_gray)

        rlLayoutRoot.setBackgroundColor(bgGray)

        eventProxy = EventProxy(this, myPageViewModel)

        initSettingList()
    }

    override fun initViewModel() {
        myPageViewModel = ViewModelProvider(this)[MyPageViewModel::class.java]
        lifecycle.addObserver(myPageViewModel)
    }

    override fun bindData() {
        myPageBinding.user = myPageViewModel.userInfo.value
        myPageBinding.eventProxy = eventProxy
    }

    override fun observeLiveData() {

        //监听用户信息
        myPageViewModel.userInfo.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                myPageBinding.user = it
            }
        })

        //监听错误信息
        myPageViewModel.errorMsg.observe(this, androidx.lifecycle.Observer {
            RxToast.error(it)
        })
    }

    /**
     * 初始化设置列表
     */
    fun initSettingList() {

        //初始化设置列表数据
        val sections = arrayListOf(
            SettingSection(
                items = arrayListOf(
                    SettingItem("精选新闻"),
                    SettingItem("糖豆任务", null, "215948糖豆"),
                    SettingItem("校区认证", null, "已认证")
                )
            ),
            SettingSection(
                items = arrayListOf(
                    SettingItem("通用设置", onClickListener = View.OnClickListener {
                        eventProxy.clickCommonSetting()
                    }),
                    SettingItem("分享软件")
                )
            ),
            SettingSection(
                items = arrayListOf(
                    SettingItem(
                        "退出登录",
                        onClickListener = View.OnClickListener {
                            eventProxy.clickLogout()
                        },
                        coverView = TextView(context).apply {
                            text = "退出登录"
                            setTextColor(Color.RED)
                            textSize = 16F
                            gravity = Gravity.CENTER
                        })
                )
            )
        )

        settingFragment = SettingListFragment.newInstance(sections)
        settingFragment.viewCreatedCallback = object : SettingListFragment.ViewCreatedCallback {
            override fun onViewCreated(
                glv: QMUIGroupListView,
                sectionViewList: ArrayList<QMUIGroupListView.Section>,
                itemViewMap: HashMap<String, QMUICommonListItemView>
            ) {
                //创建后二次设置View
                itemViewMap["0-0"]?.apply {
                    showRedDot(true)
                    correctDetailTextPosition()
                }

            }

        }
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, settingFragment)
        transaction.commit()
    }

    /**
     * 事件代理
     */
    class EventProxy(
        private val myPageFragment: MyPageFragment,
        private val viewModel: MyPageViewModel
    ) {
        /**
         * 跳转个人页面
         */
        fun goPersonalInfoPage() {
            myPageFragment.startActivity(
                Intent(
                    myPageFragment.activity,
                    PersonalInfoActivity::class.java
                )
            )
        }

        /**
         * 点击退出登录
         */
        fun clickLogout() {
            QMUIDialog.MessageDialogBuilder(myPageFragment.context)
                .setTitle("退出登录")
                .setMessage("确定退出登录？")
                .addAction(
                    0,
                    "退出",
                    QMUIDialogAction.ACTION_PROP_NEGATIVE
                ) { dialog, index ->
                    val activity = myPageFragment.activity
                    if (activity is MainActivity) {
                        activity.mainViewModel.logout()
                    }

                    dialog.dismiss()
                }
                .addAction("取消") { dialog, index ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        fun clickCommonSetting() {
            AJsApplet.load("file:///android_asset/web/index.html")
        }
    }
}
