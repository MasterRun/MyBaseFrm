package com.jsongo.mybasefrm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.scwang.smartrefresh.header.BezierCircleHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_base.*

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc activity父类
 */
abstract class BaseActivity : AppCompatActivity() {

    val compositeDisposable = CompositeDisposable()

    private var slidingLayout: SlidingLayout? = null
    //loadngDialog
    var loadingDialog: QMUITipDialog? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)

        setContentView(R.layout.activity_base)

        //添加主内容到界面
        val layoutId = setLayout()
        val mainView = LayoutInflater.from(this).inflate(layoutId, null)
        fl_main_container.addView(mainView)

        //loadingDialog
        loadingDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("加载中...")
            .create()

        //侧滑返回
        slidingLayout = SlidingLayout(this)
        setSwipeBackEnable(true)

        //沉浸/透明状态栏
        QMUIStatusBarHelper.translucent(this)
        QMUIStatusBarHelper.setStatusBarDarkMode(this)

        //初始化下拉刷新
        smart_refresh_layout.setRefreshHeader(BezierCircleHeader(this))
            .setRefreshFooter(ClassicsFooter(this))

        empty_view.visibility = View.GONE

    }

    abstract fun setLayout(): Int

    fun setSwipeBackEnable(isEnable: Boolean) {
        if (isEnable) {
            this.slidingLayout?.bindActivity(this)
        } else {
            this.slidingLayout?.setEnableSlidClose(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        topbar.clearFindViewByIdCache()
        clearFindViewByIdCache()
        compositeDisposable.dispose()
        ActivityCollector.removeActivity(this)
    }
}