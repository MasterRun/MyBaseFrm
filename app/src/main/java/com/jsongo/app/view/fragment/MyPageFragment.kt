package com.jsongo.app.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.jsongo.annotation.anno.ConfPage
import com.jsongo.app.AppApplication
import com.jsongo.app.R
import com.jsongo.app.view.activity.PersonalInfoActivity
import com.jsongo.core.mvp.base.BaseFragment
import com.jsongo.ui.component.SettingListFragment.SettingItem
import com.jsongo.ui.component.SettingListFragment.SettingListFragment
import com.jsongo.ui.component.SettingListFragment.SettingSection
import com.jsongo.ui.component.SettingListFragment.correctDetailTextPosition
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.fragment_my_page.*
import java.util.*

@ConfPage(R.layout.fragment_my_page, 1)
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
        qriv_header.setOnClickListener(onClickListener)
        tv_nickname.setOnClickListener(onClickListener)

        val sections = arrayListOf(
            SettingSection(
                items = arrayListOf(
                    SettingItem("item1"),
                    SettingItem("item2", null, "hello"),
                    SettingItem("item3", null, "hi")
                )
            ),
            SettingSection(
                items = arrayListOf(
                    SettingItem("settings"),
                    SettingItem("share")
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
