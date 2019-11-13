package com.jsongo.mybasefrm.ui.mypage

import android.os.Bundle
import com.jsongo.core.arch.BaseActivity
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.mypage.mypage.MyPageFragment
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * @author  jsongo
 * @date 2019/8/7 22:07
 * @desc 我的  页面  activity容器
 */
class MyPageActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*        rlLayoutRoot.setPadding(
            rlLayoutRoot.paddingLeft,
            rlLayoutRoot.paddingTop + QMUIStatusBarHelper.getStatusbarHeight(this),
            rlLayoutRoot.paddingRight,
            rlLayoutRoot.paddingBottom
        )*/
        QMUIStatusBarHelper.setStatusBarLightMode(this)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.rl_layout_root,
            MyPageFragment()
        )
        transaction.commit()
    }
}