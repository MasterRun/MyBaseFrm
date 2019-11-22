package com.jsongo.mybasefrm.ui.main

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import com.jsongo.ajs.webloader.AJsApplet
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.annotation.anno.WhenMobileIMEnable
import com.jsongo.core.arch.BaseActivity
import com.jsongo.core.arch.mvvm.IMvvmView
import com.jsongo.core.ui.splash.SplashActivity
import com.jsongo.core.util.ActivityCollector
import com.jsongo.core.util.PRE_ANDROID_ASSET
import com.jsongo.core.util.URL_REG
import com.jsongo.mobileim.bean.Message
import com.jsongo.mobileim.core.MobileIMConfig
import com.jsongo.mobileim.operator.ChatMessageSender
import com.jsongo.mobileim.operator.SendCallback
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.main.mainsample1.MainSample1Fragment
import com.jsongo.mybasefrm.ui.mypage.mypage.MyPageFragment
import com.jsongo.ui.component.zxing.Constant
import com.jsongo.ui.widget.FloatingView
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.safframework.log.L
import com.vondear.rxtool.RxRegTool
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

@Page(R.layout.activity_main, 0)
class MainActivity : BaseActivity(), IMvvmView {

    /**
     * 悬浮按钮
     */
    var floatingView: FloatingView? = null

    /**
     * 首页的fragment
     */
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

    /**
     * Fragment ViewPager的page adapter
     */
    lateinit var pagerAdapter: FragmentStatePagerAdapter

    /**
     * 底部bar的数组
     */
    lateinit var bottomTabs: Array<QMUITabSegment.Tab>

    /**
     * mainViewModel
     */
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        initView()

        observeLiveData()

        initMobileIM()
    }

    @WhenMobileIMEnable
    fun initMobileIM() {
        MobileIMConfig.init(this)
        MobileIMConfig.loginIM("testChatId", "testToken", object : SendCallback {
            override fun onSuccess() {
                super.onSuccess()
                L.e("login im success")
                //发消息测试
                Observable.interval(5, TimeUnit.SECONDS, Schedulers.io())
                    .map {
                        ChatMessageSender.sendMessageAsync(
                            Message(
                                sender_id = "testChatId",
                                content = "这是消息内容"
                            ), "0", object : SendCallback {
                                override fun onSuccess() {
                                    super.onSuccess()
                                    L.e("send message success")
                                }

                                override fun onFailed() {
                                    super.onFailed()
                                    L.e("send message failed")
                                }
                            })
                    }.subscribe()
            }

            override fun onFailed() {
                super.onFailed()
                L.e("login im failed")
            }
        })
    }

    override fun initView() {

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

    /**
     * 初始化ViewModel
     */
    override fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    /**
     * 观察LiveData
     */
    override fun observeLiveData() {
        //监听角标数据
        mainViewModel.mainTabTips.observe(this, Observer { tipArray ->
            tipArray?.forEachIndexed { tabIndex, tipCount ->
                setTabTipCount(tabIndex, tipCount)
            }
        })
    }

    fun initTabBar() {
        //字体的选中和未选中颜色
        val normalColor = QMUIResHelper.getAttrColor(this, R.attr.qmui_config_color_gray_6)
//        val normalColor = QMUIResHelper.getAttrColor(this, R.color.seg_tab_unseleceted)
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

        bottomTabs = arrayOf(seg1, seg2, seg3, seg4)
        //添加tab
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
     * 设置未读消息数量
     *
     * @param count
     */
    open fun setTabTipCount(tabIndex: Int, count: Int) {
        //获取到tab
        val tabSeg = bottomTabs[tabIndex]
        if (count > 0) {
            tabSeg.setSignCountMargin(0, -QMUIDisplayHelper.dp2px(this, 4))//设置红点显示位置
            tabSeg.setmSignCountDigits(2)//设置红点中数字显示的最大位数
            tabSeg.showSignCountView(this, count)//第二个参数表示：显示的消息数
        } else {
            tabSeg.hideSignCountView()
        }
        tab_seg.replaceTab(tabIndex, tabSeg)
        tab_seg.notifyDataChanged()
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
                onScanResult(str)
            }
        }
    }

    /**
     * 扫码回调
     */
    fun onScanResult(str: String) {

        if (RxRegTool.isMatch(URL_REG, str) || str.trim().startsWith(PRE_ANDROID_ASSET)) {
            //加载页面
            AJsApplet.load(str)
        } else {
            try {
                //尝试打开原生页面
                startActivity(Intent(this, Class.forName(str)))
            } catch (e: Exception) {
                e.printStackTrace()
                //弹唱显示接口
                QMUIDialog.MessageDialogBuilder(this@MainActivity)
                    .setTitle("扫描结果")
                    .setMessage(str)
                    .addAction("打开") { dialog, index ->
                        //WebLoader加载字符串
                        dialog?.dismiss()
                        AJsWebPage.load(str)
                    }
                    .addAction("OK") { dialog, index ->
                        dialog?.dismiss()
                    }.show()
            }
        }
    }

    override fun onIPageDestroy() {
        //销毁悬浮窗
        floatingView?.destory()
        super.onIPageDestroy()
    }

}
