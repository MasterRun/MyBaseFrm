package com.jsongo.mybasefrm.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.core.mvp.base.BaseFragment
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.adapter.QuickEntryItemAdapter
import com.jsongo.mybasefrm.adapter.WebCardVTitleItemAdapter
import com.jsongo.mybasefrm.bean.QuickEntryItemBean
import com.jsongo.mybasefrm.bean.WebCardItemBean
import com.jsongo.ui.component.image.banner.lib.anim.select.ZoomInEnter
import com.jsongo.ui.component.image.banner.lib.transform.ZoomOutSlideTransformer
import com.jsongo.ui.component.image.banner.widget.bean.BannerItem
import com.jsongo.ui.util.addStatusBarHeightPadding
import kotlinx.android.synthetic.main.fragment_main_sample1.*

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午11:45
 * @desc : 首页主模板1
 */
@Page(R.layout.fragment_main_sample1, 0)
class MainSample1Fragment : BaseFragment(), AjsWebViewHost {
    override val hostActivity: FragmentActivity?
        get() = activity

    override val hostFragment: Fragment?
        get() = this

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
     * 初始化快捷入口
     */
    private fun initQuickEntry() {

        //快捷入口列表项
        val quickEntryItem = mutableListOf(
            QuickEntryItemBean(
                "http://www.jq22.com/demo/appyymoban201910161119/images/nav-001.png",
                -1,
                "FM",
                "http://www.jq22.com/demo/jqueryrwdh201907110144/dist/"
            ),
            QuickEntryItemBean(
                "http://www.jq22.com/demo/appyymoban201910161119/images/nav-002.png",
                -1,
                "免费试听",
                "http://www.jq22.com/demo/planet201907121655/"
            ),
            QuickEntryItemBean(
                "http://www.jq22.com/demo/appyymoban201910161119/images/nav-003.png",
                -1,
                "等级课程",
                "http://www.jq22.com/demo/3dlft201904260032/"
            ),
            QuickEntryItemBean(
                "http://www.jq22.com/demo/appyymoban201910161119/images/nav-004.png",
                -1,
                "主题课程",
                "http://www.jq22.com/demo/threeph201903262259/"
            )
        )

        //解决滑动冲突问题
        rv_quick_entry.setHasFixedSize(true)
        rv_quick_entry.isNestedScrollingEnabled = false
        //设置适配器
        val quickEntryItemAdapter = QuickEntryItemAdapter(context, quickEntryItem)
        //设置adapter
        rv_quick_entry.adapter = quickEntryItemAdapter
        //设置列数
        rv_quick_entry.layoutManager = GridLayoutManager(context, 4)
    }

    /**
     * 初始化web卡片
     */
    private fun initWebCard() {

        //卡片数据项
        val webCardItems = mutableListOf(
            WebCardItemBean(
//                    "http://www.jq22.com/demo/jquerykgd201908272252/",
                "file:///android_asset/web-cards/card_1/card_1.html",
                "新生必看",
                "",
                "查看全部"
            ),
            WebCardItemBean(
//                    "http://www.jq22.com/demo/clip-clop-clippity-clop201908030107/",
                "file:///android_asset/web-cards/card_2/card_2.html",
                "FM",
                "双语脱口秀，聊美国文化、学地道的英语"
            ),
            WebCardItemBean(
//                    "http://www.jq22.com/demo/colorfultouch201907302229/",
                "file:///android_asset/web-cards/card_3/card_3.html",
                "为你推荐",
                "根据你的兴趣为你精选优质课程"
            )
        )

        //解决scrollview嵌套滑动冲突问题
        rv_web_cards.setHasFixedSize(true)
        rv_web_cards.isNestedScrollingEnabled = false

        //初始化适配器
        val webCardVTitleItemAdapter = WebCardVTitleItemAdapter(
            context, this, webCardItems
        )

        rv_web_cards.adapter = webCardVTitleItemAdapter
        rv_web_cards.layoutManager = LinearLayoutManager(context)
    }

    /**
     * 初始化轮播图
     */
    private fun initImageBanner() {
        //轮播图数据
        val items = arrayListOf(
            BannerItem("http://www.jq22.com/demo/appyymoban201910161119/images/banner.png", "图片1"),
            BannerItem("http://www.jq22.com/demo/appyymoban201910161119/images/banner.png", "图片2"),
            BannerItem("http://www.jq22.com/demo/appyymoban201910161119/images/banner.png", "图片3")
        )

        simple_ib.setSelectAnimClass(ZoomInEnter::class.java)//set indicator select anim
            .setSource(items)//data source list
            .setTransformerClass(ZoomOutSlideTransformer::class.java)//set page transformer
            .startScroll()
        simple_ib.setOnItemClickListener {
            val item = items.get(it)
            AJsWebPage.load(item.imgUrl)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //处理ajs回调
        onAjsLongCallBack(requestCode, resultCode, data)
    }
}