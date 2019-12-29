package com.jsongo.mybasefrm.ui.mypage.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.BaseFragment
import com.jsongo.core.arch.mvvm.IMvvmView
import com.jsongo.core.util.GlideUtil
import com.jsongo.core.widget.RxToast
import com.jsongo.mybasefrm.AppApplication
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.personalinfo.PersonalInfoActivity
import com.jsongo.ui.component.fragment.settinglist.SettingItem
import com.jsongo.ui.component.fragment.settinglist.SettingListFragment
import com.jsongo.ui.component.fragment.settinglist.SettingSection
import com.jsongo.ui.component.fragment.settinglist.correctDetailTextPosition
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.fragment_my_page.*
import java.util.*

@Page(R.layout.fragment_my_page, 1)
class MyPageFragment : BaseFragment(), IMvvmView {

    lateinit var myPageViewModel: MyPageViewModel

    lateinit var settingFragment: SettingListFragment

    lateinit var eventProxy: EventProxy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        initView()

        observeLiveData()
    }

    override fun initView() {

        smartRefreshLayout.isEnabled = false

        topbar.visibility = View.GONE

        val bgGray = ContextCompat.getColor(AppApplication.context, R.color.light_gray)

        rlLayoutRoot.setBackgroundColor(bgGray)

        eventProxy = EventProxy(this, myPageViewModel)

        qriv_header.setOnClickListener {
            eventProxy.goPersonalInfoPage()
        }
        tv_nickname.setOnClickListener {
            eventProxy.goPersonalInfoPage()
        }

        initSettingList()
    }

    override fun initViewModel() {
        myPageViewModel = ViewModelProviders.of(this).get(MyPageViewModel::class.java)
        lifecycle.addObserver(myPageViewModel)
    }

    override fun observeLiveData() {

        //监听用户信息
        myPageViewModel.userInfo.observe(this, androidx.lifecycle.Observer {
            tv_nickname.text = it.username

            val photoUrl = it.photo_url
            if (!photoUrl.isEmpty()) {
                GlideUtil.load(context, photoUrl, qriv_header)
            }

            when (it.gender) {
                "1" -> {
                    iv_gender.visibility = View.VISIBLE
                    Glide.with(this).load(R.mipmap.ic_gender_man).into(iv_gender)
                }
                "2" -> {
                    iv_gender.visibility = View.VISIBLE
                    Glide.with(this).load(R.mipmap.ic_gender_woman).into(iv_gender)
                }
                else -> {
                    iv_gender.visibility = View.GONE
                }
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
                    SettingItem("通用设置"),
                    SettingItem("分享软件")
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
                val listItemView = itemViewMap["0-0"]
                listItemView?.showRedDot(true)
                listItemView.correctDetailTextPosition()
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
        fun goPersonalInfoPage() {
            myPageFragment.startActivity(
                Intent(
                    myPageFragment.activity,
                    PersonalInfoActivity::class.java
                )
            )
        }
    }
}
