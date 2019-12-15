package com.jsongo.mybasefrm.ui.mypage.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.BaseFragment
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.util.toGsonBean
import com.jsongo.mybasefrm.AppApplication
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.bean.User
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
class MyPageFragment : BaseFragment() {

    lateinit var settingFragment: SettingListFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        smartRefreshLayout.isEnabled = false

        topbar.visibility = View.GONE

        val bgGray = ContextCompat.getColor(AppApplication.context, R.color.light_gray)
        rlLayoutRoot.setBackgroundColor(bgGray)
        val onClickListener = View.OnClickListener {
            startActivity(Intent(activity, PersonalInfoActivity::class.java))
        }
        val user: User? =
            CommonDbOpenHelper.getValue(CommonDbKeys.USER_INFO).toGsonBean(User::class.java)

        tv_nickname.text = user?.username ?: "未登录"

        qriv_header.setOnClickListener(onClickListener)
        tv_nickname.setOnClickListener(onClickListener)

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
                val listItemView = itemViewMap["0-0"]
                listItemView?.showRedDot(true)
                listItemView.correctDetailTextPosition()
            }

        }
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, settingFragment)
        transaction.commit()
    }

}
