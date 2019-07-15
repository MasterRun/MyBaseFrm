package com.jsongo.mybasefrm

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.jsongo.mybasefrm.jsloader.AJsWebLoader
import com.jsongo.mybasefrm.util.SmartRefreshFooter
import com.jsongo.mybasefrm.util.SmartRefreshHeader
import com.jsongo.mybasefrm.util.useFooter
import com.jsongo.mybasefrm.util.useHeader
import com.jsongo.mybasefrm.widget.SlidingLayout
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import com.vondear.rxtool.view.RxToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_ajs_webloader.*
import kotlinx.android.synthetic.main.activity_base.*
import java.util.concurrent.TimeUnit

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
        smart_refresh_layout
            .useHeader(this, SmartRefreshHeader.BezierCircleHeader)
            .useFooter(this, SmartRefreshFooter.ClassicsFooter)
        empty_view.visibility = View.GONE

        val rxPermissions = RxPermissions(this)
        val disposable = rxPermissions
            .request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).subscribe { granted ->
                if (!granted) {
                    RxToast.warning("程序即将退出!")
                    val disposable = Observable.timer(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            if (rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE).not()) {
                                ActivityCollector.appExit()
                            }
                        }
                    addDisposable(disposable)
                } else {
                    if (this@BaseActivity is AJsWebLoader) {
                        this.bridgeWebView.reload()
                    }
                }
            }
        addDisposable(disposable)
    }

    abstract fun setLayout(): Int

    fun setSwipeBackEnable(isEnable: Boolean) {
        if (isEnable) {
            this.slidingLayout?.bindActivity(this)
        } else {
            this.slidingLayout?.setEnableSlidClose(false)
        }
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        topbar.clearFindViewByIdCache()
        clearFindViewByIdCache()
        compositeDisposable.dispose()
        ActivityCollector.removeActivity(this)
    }
}