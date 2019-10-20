package com.jsongo.mybasefrm.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.core.util.ActivityCollector
import com.jsongo.core.view.activity.SplashActivity
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.view.fragment.MainSample1Fragment
import com.jsongo.mybasefrm.view.fragment.MyPageFragment
import com.jsongo.ui.component.zxing.Constant
import com.jsongo.ui.widget.FloatingView
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.safframework.log.L
import com.vondear.rxtool.RxRegTool
import kotlinx.android.synthetic.main.activity_main.*

@Page(R.layout.activity_main, 0)
class MainActivity : BaseActivity() {

    lateinit var pagerAdapter: FragmentStatePagerAdapter

    var floatingView: FloatingView? = null

    val fragments = arrayOf(
        AJsWebLoader.newInstance(
            "http://www.jq22.com/demo/appsjqg201910152359/",
            false,
            bgColor = Color.WHITE
        ),
        MainSample1Fragment(),
        AJsWebLoader.newInstance(
            "http://www.jq22.com/demo/appjymoban201908222316/",
            false,
            bgColor = Color.WHITE
        ),
        MyPageFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //禁用侧滑返回
        setSwipeBackEnable(false)
        //结束启动页
        ActivityCollector.finish(SplashActivity::class.java)

        //悬浮扫码按钮
        floatingView = FloatingView(this)
        floatingView?.show()

        initTabBar()

        QMUIStatusBarHelper.setStatusBarLightMode(this)
    }

    private fun initTabBar() {
        //字体的选中和未选中颜色
        val normalColor = QMUIResHelper.getAttrColor(this, R.attr.qmui_config_color_gray_6)
        val selectColor = ContextCompat.getColor(this, R.color.seg_tab_selected)

        tab_seg.setDefaultNormalColor(normalColor)
        tab_seg.setDefaultSelectedColor(selectColor)
        // tab_seg.setDefaultTabIconPosition(QMUITabSegment.ICON_POSITION_BOTTOM);

        // 如果 icon 显示大小和实际大小不吻合:
        // 1. 设置icon 的 bounds
        // 2. Tab 使用拥有5个参数的构造器
        // 3. 最后一个参数（setIntrinsicSize）设置为false
        val tab1NormalDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab1)
        val tab1SelectedDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab1_selected)
        val tab2NormalDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab2)
        val tab2SelectedDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab2_selected)
        val tab3NormalDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab3)
        val tab3SelectedDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab3_selected)
        val tab4NormalDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab4)
        val tab4SelectedDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab4_selected)

        // 设置大小
        val iconShowSize = QMUIDisplayHelper.dp2px(this, 20)
        tab1NormalDrawable?.setBounds(0, 0, iconShowSize, iconShowSize)
        tab1SelectedDrawable?.setBounds(0, 0, iconShowSize, iconShowSize)
        tab2NormalDrawable?.setBounds(0, 0, iconShowSize, iconShowSize)
        tab2SelectedDrawable?.setBounds(0, 0, iconShowSize, iconShowSize)
        tab3NormalDrawable?.setBounds(0, 0, iconShowSize, iconShowSize)
        tab3SelectedDrawable?.setBounds(0, 0, iconShowSize, iconShowSize)
        tab4NormalDrawable?.setBounds(0, 0, iconShowSize, iconShowSize)
        tab4SelectedDrawable?.setBounds(0, 0, iconShowSize, iconShowSize)

        val mainSegTabTexts = resources.getStringArray(R.array.main_seg_tabs)

        val seg1 = QMUITabSegment.Tab(
            tab1NormalDrawable,
            tab1SelectedDrawable,
            mainSegTabTexts[0], false, false
        )

        val seg2 = QMUITabSegment.Tab(
            tab2NormalDrawable,
            tab2SelectedDrawable,
            mainSegTabTexts[1], false, false
        )
        val seg3 = QMUITabSegment.Tab(
            tab3NormalDrawable,
            tab3SelectedDrawable,
            mainSegTabTexts[2], false, false
        )

        val seg4 = QMUITabSegment.Tab(
            tab4NormalDrawable,
            tab4SelectedDrawable,
            mainSegTabTexts[3], false, false
        )
        tab_seg.addTab(seg1)
            .addTab(seg2)
            .addTab(seg3)
            .addTab(seg4)

        //viewpager的adapter
        pagerAdapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = fragments[position]

            override fun getCount() = fragments.size

        }
        pager.adapter = pagerAdapter
        pager.offscreenPageLimit = 3
        tab_seg.setupWithViewPager(pager, false)

        //默认选中的tab
        tab_seg.selectTab(resources.getInteger(R.integer.main_selected_tab))
    }

    /**
     * 设置返回键不关闭应用,回到桌面
     *
     * @param keyCode
     * @param event
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) && event.action == KeyEvent.ACTION_DOWN) {
            val backHome = Intent(Intent.ACTION_MAIN)
            backHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            backHome.addCategory(Intent.CATEGORY_HOME)
            startActivity(backHome)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        L.json(data)
        // 扫描二维码/条码回传
        if (requestCode == FloatingView.SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val str = data.getStringExtra(Constant.CODED_CONTENT)
                if (RxRegTool.isURL(str)) {
                    AJsWebPage.load(str)
                } else {
                    try {
                        startActivity(Intent(this, Class.forName(str)))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        QMUIDialog.MessageDialogBuilder(this@MainActivity)
                            .setTitle("扫描结果")
                            .setMessage(str)
                            .addAction("OK") { dialog, index ->
                                dialog?.dismiss()
                            }.show()
                    }
                }
            }
        }
    }

    override fun onIPageDestroy() {
        floatingView?.destory()
        super.onIPageDestroy()
    }

}
