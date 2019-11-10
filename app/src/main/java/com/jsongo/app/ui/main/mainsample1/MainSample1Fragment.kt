package com.jsongo.app.ui.main.mainsample1

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.app.R
import com.jsongo.app.adapter.QuickEntryItemAdapter
import com.jsongo.app.adapter.WebCardVTitleItemAdapter
import com.jsongo.app.bean.QuickEntryItemBean
import com.jsongo.app.bean.WebCardItemBean
import com.jsongo.core.base.BaseFragment
import com.jsongo.core.base.mvvm.IMvvmView
import com.jsongo.ui.component.image.banner.lib.anim.select.ZoomInEnter
import com.jsongo.ui.component.image.banner.lib.transform.ZoomOutSlideTransformer
import com.jsongo.ui.util.addStatusBarHeightPadding
import kotlinx.android.synthetic.main.fragment_main_sample1.*

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午11:45
 * @desc : 首页主模板1
 */
@Page(R.layout.fragment_main_sample1, 0)
class MainSample1Fragment : BaseFragment(), IMvvmView, AjsWebViewHost {
    override val hostActivity: FragmentActivity?
        get() = activity

    override val hostFragment: Fragment?
        get() = this

    /**
     * ViewModel
     */
    lateinit var mainSample1ViewModel: MainSample1ViewModel

    /**
     * 快捷入口适配器
     */
    lateinit var quickEntryItemAdapter: QuickEntryItemAdapter

    /**
     * web卡片适配器
     */
    lateinit var webCardVTitleItemAdapter: WebCardVTitleItemAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()

        initView()

        observeLiveData()
    }

    /**
     * 初始化ViewModel
     */
    override fun initViewModel() {
        mainSample1ViewModel = ViewModelProviders.of(this).get(MainSample1ViewModel::class.java)
    }

    override fun initView() {

        //修复状态栏高度
        ll_sb_container.addStatusBarHeightPadding()

        //初始化轮播图
        initImageBanner()

        //初始化快捷入口
        initQuickEntry()

        //初始化web卡片
        initWebCard()

    }

    /**
     * 观察LiveData
     */
    override fun observeLiveData() {
        //监听轮播图
        mainSample1ViewModel.imgBannerList.observe(this, Observer { bannerList ->
            simple_ib.setSource(bannerList).startScroll()
        })

        //监听快捷入口数据
        mainSample1ViewModel.quickEntryItemList.observe(this, Observer {
            if (it != null) {
                quickEntryItemAdapter.dataList = it
                quickEntryItemAdapter.notifyDataSetChanged()
            }
        })

        //监听web卡片数据
        mainSample1ViewModel.webCardItemList.observe(this, Observer {
            if (it != null) {
                webCardVTitleItemAdapter.dataList = it
                webCardVTitleItemAdapter.notifyDataSetChanged()
            }
        })
    }

    /**
     * 初始化轮播图
     */
    private fun initImageBanner() {
        simple_ib.setSelectAnimClass(ZoomInEnter::class.java)//set indicator select anim
            .setSource(mainSample1ViewModel.imgBannerList.value)//data source list
            .setTransformerClass(ZoomOutSlideTransformer::class.java)//set page transformer
            .startScroll()
        simple_ib.setOnItemClickListener { index ->
            mainSample1ViewModel.imgBannerList.value?.get(index)?.let { bannerItem ->
                AJsWebPage.load(bannerItem.imgUrl)
            }
        }
    }

    /**
     * 初始化快捷入口
     */
    private fun initQuickEntry() {

        //解决滑动冲突问题
        rv_quick_entry.setHasFixedSize(true)
        rv_quick_entry.isNestedScrollingEnabled = false
        //设置适配器
        quickEntryItemAdapter = QuickEntryItemAdapter(
            context,
            mainSample1ViewModel.quickEntryItemList.value
                ?: emptyList<QuickEntryItemBean>().toMutableList()
        )
        //设置adapter
        rv_quick_entry.adapter = quickEntryItemAdapter
        //设置列数
        rv_quick_entry.layoutManager = GridLayoutManager(context, 4)
    }

    /**
     * 初始化web卡片
     */
    private fun initWebCard() {

        //解决scrollview嵌套滑动冲突问题
        rv_web_cards.setHasFixedSize(true)
        rv_web_cards.isNestedScrollingEnabled = false

        //初始化适配器
        webCardVTitleItemAdapter = WebCardVTitleItemAdapter(
            context,
            this,
            mainSample1ViewModel.webCardItemList.value
                ?: emptyList<WebCardItemBean>().toMutableList()
        )

        rv_web_cards.adapter = webCardVTitleItemAdapter
        rv_web_cards.layoutManager = LinearLayoutManager(context)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //处理ajs回调
        onAjsLongCallBack(requestCode, resultCode, data)
    }
}