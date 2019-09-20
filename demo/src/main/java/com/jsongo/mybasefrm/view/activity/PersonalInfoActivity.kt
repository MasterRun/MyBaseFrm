package com.jsongo.mybasefrm.view.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.jsongo.annotation.anno.ConfPage
import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.mybasefrm.AppApplication
import com.jsongo.mybasefrm.R
import com.jsongo.ui.component.SettingListFragment.SettingListFragment
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

@ConfPage(R.layout.activity_personal_info, 1)
class PersonalInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bgGray = ContextCompat.getColor(AppApplication.context, R.color.light_gray)
        rlLayoutRoot.setBackgroundColor(bgGray)

        smartRefreshLayout.isEnabled = false
        topbar.setBackgroundColor(Color.WHITE)
        topbar.setTitle("个人信息").setTextColor(Color.BLACK)
        topbar.backImageButton.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gray))

        QMUIStatusBarHelper.setStatusBarLightMode(this)

        val fragment = SettingListFragment.newInstance(SettingListFragment.sectionListDemo)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, fragment)
        transaction.commit()
    }
}
