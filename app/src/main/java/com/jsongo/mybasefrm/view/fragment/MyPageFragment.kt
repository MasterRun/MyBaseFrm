package com.jsongo.mybasefrm.view.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.CompoundButton
import com.jsongo.core.annotations.ConfPage
import com.jsongo.core.mvp.base.BaseFragment
import com.jsongo.mybasefrm.AppApplication
import com.jsongo.mybasefrm.R
import com.jsongo.ui.component.SettingListFragment.SettingItem
import com.jsongo.ui.component.SettingListFragment.SettingListFragment
import com.jsongo.ui.component.SettingListFragment.SettingSection
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.vondear.rxtool.view.RxToast

@ConfPage(R.layout.fragment_my_page, 1)
class MyPageFragment : BaseFragment() {

    lateinit var settingFragment: SettingListFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        smartRefreshLayout.isEnabled = false

        topbar.visibility = View.GONE

        val bgGray = ContextCompat.getColor(AppApplication.context, R.color.gray1)
        smartRefreshLayout.setBackgroundColor(bgGray)

        val sectionList = arrayListOf(
            SettingSection(
                "section1", /*"这是描述",*/ items = arrayListOf(
                    SettingItem(
                        "item1",
                        null,
                        "detail",
                        onClickListener = View.OnClickListener { RxToast.normal("click item1") }
                    ),
                    SettingItem(
                        "item2",
                        R.drawable.icon_menu,
                        accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                        onClickListener = View.OnClickListener { RxToast.normal("item 2") }
                    ),
                    SettingItem(
                        "item3",
                        R.drawable.code_icon,
                        "hahah",
                        QMUICommonListItemView.HORIZONTAL,
                        QMUICommonListItemView.ACCESSORY_TYPE_SWITCH,
                        showNewTip = true,
                        checkChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, checked ->
                            RxToast.normal("checked:${checked}")
                        })
                )
            ),
            SettingSection("section2",
                items = arrayListOf(
                    SettingItem(
                        "item 2-1",
                        R.drawable.next,
                        "desc",
                        showRedDot = true,
                        onClickListener = View.OnClickListener { RxToast.normal("click item 2-1") }
                    ),
                    SettingItem(
                        "item 2-2",
                        R.drawable.next,
                        "desc",
                        showRedDot = true,
                        onClickListener = View.OnClickListener { RxToast.normal("click item 2-2") }
                    )
                )
            )
        )

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
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, settingFragment)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
/*        val listItemView = settingFragment.itemViewMap["0-0"]
        listItemView?.showRedDot(true)
        listItemView.correctDetailTextPosition()*/
        settingFragment.glv.setBackgroundColor(
            ContextCompat.getColor(
                AppApplication.context,
                R.color.gray1
            )
        )
    }
}
