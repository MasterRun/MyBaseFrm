package com.jsongo.mybasefrm.ui.main.mainsample1

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.annotation.anno.permission.PermissionDeny
import com.jsongo.annotation.anno.permission.PermissionNeed
import com.jsongo.core.arch.BaseFragmentWrapper
import com.jsongo.core.arch.mvvm.IMvvmView
import com.jsongo.core.constant.PRE_ANDROID_ASSET
import com.jsongo.core.constant.URL_REG
import com.jsongo.core.util.RegUtil
import com.jsongo.core_mini.common.OnRvItemClickListener
import com.jsongo.core_mini.util.addStatusBarHeightPadding
import com.jsongo.core_mini.widget.RxToast
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.main.mainsample1.adapter.QuickEntryItemAdapter
import com.jsongo.mybasefrm.ui.main.mainsample1.adapter.WebCardVTitleItemAdapter
import com.jsongo.mybasefrm.ui.main.mainsample1.bean.QuickEntryItemBean
import com.jsongo.mybasefrm.ui.main.mainsample1.bean.WebCardItemBean
import com.jsongo.ui.component.image.banner.lib.anim.select.ZoomInEnter
import com.jsongo.ui.component.image.banner.lib.transform.ZoomOutSlideTransformer
import com.jsongo.ui.component.image.banner.widget.banner.SimpleImageBanner
import com.jsongo.ui.component.image.banner.widget.bean.BannerItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main_sample1.*

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午11:45
 * @desc : 首页主模板1
 */
@Page(R.layout.fragment_main_sample1, 0)
class MainSample1Fragment : BaseFragmentWrapper(), IMvvmView, AjsWebViewHost {

    override val hostFragment: Fragment?
        get() = this

    override val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

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

    var simpleIb: SimpleImageBanner<BannerItem>? = null

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
        mainSample1ViewModel = ViewModelProvider(this)[MainSample1ViewModel::class.java]
    }

    override fun initView() {
        simpleIb = view?.findViewById(R.id.simple_ib)

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
            simpleIb?.setSource(bannerList)?.startScroll()
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
        simpleIb?.apply {
            this.setSelectAnimClass(ZoomInEnter::class.java)//set indicator select anim
                .setSource(mainSample1ViewModel.imgBannerList.value)//data source list
                .setTransformerClass(ZoomOutSlideTransformer::class.java)//set page transformer
                .startScroll()
        }
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
        quickEntryItemAdapter =
            QuickEntryItemAdapter(
                context,
                mainSample1ViewModel.quickEntryItemList.value
                    ?: emptyList<QuickEntryItemBean>().toMutableList()
            )
        //点击事件
        quickEntryItemAdapter.itemClickListener = object :
            OnRvItemClickListener<QuickEntryItemAdapter, QuickEntryItemAdapter.ViewHolder> {
            //添加注解申请权限 demo
            @PermissionNeed(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
            override fun onRvItemClick(
                adapter: QuickEntryItemAdapter,
                holder: QuickEntryItemAdapter.ViewHolder,
                position: Int,
                type: Int
            ) {
                val entryTag = quickEntryItemAdapter.dataList[position].entryTag
                if (RegUtil.isMatch(URL_REG, entryTag)
                    || entryTag.trim().startsWith(PRE_ANDROID_ASSET)
                ) {
                    AJsWebPage.load(entryTag)
                } else {
                    try {
                        context?.startActivity(Intent(context, Class.forName(entryTag)))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            @PermissionDeny
            fun onPermissionDeny() {
                RxToast.error("权限拒绝")
            }
        }
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
        webCardVTitleItemAdapter =
            WebCardVTitleItemAdapter(
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

    override fun onDestroyIPage() {
        super.onDestroyIPage()
        compositeDisposable.dispose()
    }
}