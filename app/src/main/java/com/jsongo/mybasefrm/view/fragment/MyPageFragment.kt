package com.jsongo.mybasefrm.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.jsongo.core.annotations.ConfPage
import com.jsongo.core.mvp.base.BaseFragment
import com.jsongo.mybasefrm.R
import com.jsongo.ui.component.SettingListFragment.SettingItem
import com.jsongo.ui.component.SettingListFragment.SettingListFragment
import com.jsongo.ui.component.SettingListFragment.SettingSection
import com.jsongo.ui.component.SettingListFragment.correctDetailTextPosition
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.vondear.rxtool.view.RxToast

@ConfPage(R.layout.fragment_my_page, 1)
class MyPageFragment : BaseFragment() {

    lateinit var settingFragment: SettingListFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        smartRefreshLayout.isEnabled = false

        topbar.tvTitle.text = "设置"

        val sectionList = arrayListOf(
            SettingSection(
                "section1", /*"这是描述",*/ items = arrayListOf(
                    SettingItem(
                        null,
                        "item1",
                        "detail",
                        onClickListener = View.OnClickListener { RxToast.normal("click item1") }
                    ),
                    SettingItem(
                        R.drawable.icon_menu,
                        "item2",
                        accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                        onClickListener = View.OnClickListener { RxToast.normal("item 2") }
                    ),
                    SettingItem(
                        R.drawable.code_icon,
                        "item3",
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
                        R.drawable.next,
                        "item 2-1",
                        "desc",
                        showRedDot = true,
                        onClickListener = View.OnClickListener { RxToast.normal("click item 2-1") }
                    ),
                    SettingItem(
                        R.drawable.next,
                        "item 2-2",
                        "desc",
                        showRedDot = true,
                        onClickListener = View.OnClickListener { RxToast.normal("click item 2-2") }
                    )
                )
            )
        )

        val layoutParams = nsv.layoutParams
        layoutParams.height = 2000
        nsv.layoutParams = layoutParams

        settingFragment = SettingListFragment.newInstance(sectionList)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, settingFragment)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        val listItemView = settingFragment.itemViewMap["0-0"]
        listItemView?.showRedDot(true)
        listItemView.correctDetailTextPosition()
    }
}
