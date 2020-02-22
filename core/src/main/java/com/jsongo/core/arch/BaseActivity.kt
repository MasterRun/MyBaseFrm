package com.jsongo.core.arch

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.jsongo.core.R
import com.jsongo.core.common.ActivityCollector
import com.jsongo.core.widget.SlidingLayout
import com.jsongo.core.widget.TopbarLayout
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUIEmptyView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.layout_frm_base.*

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc activity父类
 */
abstract class BaseActivity : AppCompatActivity(), IPage {

    private lateinit var slidingLayout: SlidingLayout
    //loadingDialog
    lateinit var loadingDialog: QMUITipDialog
        protected set

    open val translucentWindow = true

    override lateinit var rlLayoutRoot: RelativeLayout
        protected set
    override lateinit var topbar: TopbarLayout
        protected set
    override lateinit var smartRefreshLayout: SmartRefreshLayout
        protected set
    override lateinit var nsv: NestedScrollView
        protected set
    override lateinit var flMainContainer: FrameLayout
        protected set
    override lateinit var flMainContainer2: FrameLayout
        protected set
    override lateinit var vsEmptyView: ViewStub
        protected set
    override var emptyView: QMUIEmptyView? = null
        protected set

    override lateinit var mainView: View

    override var mainLayoutId = 0

    override var containerIndex = 1

    var isForeground = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)

        setContentView(R.layout.layout_frm_base)

        initIPage(this)

        //loadingDialog
        loadingDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(getString(R.string.dialog_loading))
            .create()

        //侧滑返回
        slidingLayout = SlidingLayout(this)
        setSwipeBackEnable(true)

        if (translucentWindow) {
            //沉浸/透明状态栏
            QMUIStatusBarHelper.translucent(this)
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        }
        isForeground = true
    }

    /**
     * 获取widget
     */
    override fun getIPageView() {
        rlLayoutRoot = rl_layout_root
        topbar = findViewById(R.id.topbar)
        smartRefreshLayout = smart_refresh_layout
        nsv = findViewById(R.id.nsv)
        flMainContainer = fl_main_container
        flMainContainer2 = fl_main_container2
        vsEmptyView = vs_emptyview
    }

    override fun inflateEmptyView(): QMUIEmptyView? {
        if (emptyView == null) {
            emptyView = vsEmptyView.inflate().findViewById(R.id.empty_view)
        }
        return emptyView
    }

    /**
     * 侧滑返回
     */
    fun setSwipeBackEnable(isEnable: Boolean) {
        if (isEnable) {
            this.slidingLayout.bindActivity(this)
        } else {
            this.slidingLayout.setEnableSlidClose(false)
        }
    }

    override fun onResume() {
        super.onResume()
        isForeground = true;
    }

    override fun onPause() {
        super.onPause()
        isForeground = false
    }

    override fun onDestroy() {
        onIPageDestroy()
        loadingDialog.dismiss()
        clearFindViewByIdCache()
        ActivityCollector.removeActivity(this)
        super.onDestroy()
    }
}