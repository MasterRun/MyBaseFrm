package com.jsongo.rawtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CompoundButton
import com.jsongo.rawtest.SettingListFragment.SettingItem
import com.jsongo.rawtest.SettingListFragment.SettingListFragment
import com.jsongo.rawtest.SettingListFragment.SettingSection
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.vondear.rxtool.view.RxToast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val settingSection = SettingSection(
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
        )

        val sectionList = arrayListOf(
            settingSection,
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
            ),
            settingSection,
            settingSection
        )

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, SettingListFragment.newInstance(sectionList))
        transaction.commit()

    }
}
