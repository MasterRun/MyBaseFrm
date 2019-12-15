package com.jsongo.mybasefrm.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jsongo.ajs.webloader.AJsApplet
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.annotation.anno.WhenMobileIMEnable
import com.jsongo.core.arch.BaseActivity
import com.jsongo.core.arch.mvvm.IMvvmView
import com.jsongo.core.constant.PRE_ANDROID_ASSET
import com.jsongo.core.constant.URL_REG
import com.jsongo.core.ui.splash.SplashActivity
import com.jsongo.core.util.*
import com.jsongo.mobileim.bean.Message
import com.jsongo.mobileim.core.MobileIMConfig
import com.jsongo.mobileim.operator.ChatMessageSender
import com.jsongo.mobileim.operator.SendCallback
import com.jsongo.mobileim.util.MobileIMMessageSign
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.login.LoginActivity
import com.jsongo.mybasefrm.ui.main.mainsample1.MainSample1Fragment
import com.jsongo.mybasefrm.ui.mypage.mypage.MyPageFragment
import com.jsongo.ui.component.zxing.Constant
import com.jsongo.ui.widget.FloatingView
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.tab.QMUITab
import com.safframework.log.L
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

@Page(R.layout.activity_main, 0)
class MainActivity : BaseActivity(), IMvvmView {

    val compositeDisposable = CompositeDisposable()

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
    lateinit var bottomTabs: Array<QMUITab>

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

        regIMReceiver()

        //消息数
        mainViewModel.mainTabTips.value = intArrayOf(0, 0, 66, 0)
    }

    /**
     * 初始化并登陆MobileIM
     */
    @WhenMobileIMEnable
    fun initMobileIM() {
        MobileIMConfig.init(this)
        MobileIMConfig.loginIM("testChatId", "testToken", object : SendCallback {
            override fun onSuccess() {
                super.onSuccess()
                L.e("login data send success")

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
                L.e("login data send failed")
            }
        })
    }

    /**
     * 注册MobileIM消息接收
     */
    @WhenMobileIMEnable
    fun regIMReceiver() {
        val disposable = RxBus.toFlowable().filter {
            MobileIMMessageSign.isMobileIMMessage(it.code)
        }.map {
            L.e("mobileim收到消息：${it}")
        }.subscribe()
        compositeDisposable.add(disposable)
    }

    override fun initView() {

        //禁用侧滑返回
        setSwipeBackEnable(false)
        //结束启动页登录页
        ActivityCollector.finish(SplashActivity::class.java)
        ActivityCollector.finish(LoginActivity::class.java)

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
        val normalColor = R.attr.qmui_config_color_gray_6
        val selectColor = R.attr.qmui_config_color_blue

        // 图标大小
        val iconShowSize = QMUIDisplayHelper.dp2px(this, 16)

        val tabBuilder = tab_seg.tabBuilder()
            //设置图标宽高
            .setNormalIconSizeInfo(iconShowSize, iconShowSize)
            //设置图标选中缩放比例
            .setSelectedIconScale(1.2f)
            //设置文本大小，可在布局文件中设置
//            .setTextSize(QMUIDisplayHelper.sp2px(this, 12), QMUIDisplayHelper.sp2px(this, 14))
            //设置tab图标的位置
//            .setIconPosition(QMUITab.ICON_POSITION_BOTTOM)
            //设置颜色
            .setColorAttr(normalColor, selectColor)
            .setDynamicChangeIconColor(false)
            .setAllowIconDrawOutside(false)

        //创建tab
        val tab1NormalDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab1)
        val tab2NormalDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab2)
        val tab3NormalDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab3)
        val tab4NormalDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_tab4)

        //tab的文本数组
        val mainSegTabTexts = resources.getStringArray(R.array.main_seg_tabs)

        val tab1 = tabBuilder
            .setNormalDrawable(tab1NormalDrawable)
//            .setSelectedDrawable(tab1SelectedDrawable)
            .setText(mainSegTabTexts[0])
            .build(this)

        val tab2 = tabBuilder
            .setNormalDrawable(tab2NormalDrawable)
            .setText(mainSegTabTexts[1])
            .build(this)

        val tab3 = tabBuilder
            .setNormalDrawable(tab3NormalDrawable)
            .setText(mainSegTabTexts[2])
            .build(this)

        val tab4 = tabBuilder
            .setNormalDrawable(tab4NormalDrawable)
            .setText(mainSegTabTexts[3])
            .build(this)

        bottomTabs = arrayOf(tab1, tab2, tab3, tab4)
        //添加tab
        tab_seg.addTab(tab1)
            .addTab(tab2)
            .addTab(tab3)
            .addTab(tab4)

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
        val tab = bottomTabs[tabIndex]
        if (count > 0) {
            tab.setRedPoint()
            tab.signCount = count
        } else {
            tab.clearSignCountOrRedPoint()
        }
        tab_seg.replaceTab(tabIndex, tab)
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
            //如果只剩MainActivity，安县返回键时启动桌面，不结束activity
            if (ActivityCollector.getActivities().size == 1 && ActivityCollector.topActivity == this) {
                val backHome = Intent(Intent.ACTION_MAIN)
                backHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                backHome.addCategory(Intent.CATEGORY_HOME)
                startActivity(backHome)
                return true
            }
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

        if (RegUtil.isMatch(URL_REG, str) || str.trim().startsWith(
                PRE_ANDROID_ASSET
            )) {
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
        compositeDisposable.dispose()
        //销毁悬浮窗
        floatingView?.destory()

        super.onIPageDestroy()
    }

}
