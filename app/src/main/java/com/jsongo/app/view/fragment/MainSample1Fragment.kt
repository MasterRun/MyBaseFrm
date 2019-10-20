package com.jsongo.app.view.fragment

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
import com.jsongo.core.mvp.base.BaseFragment
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

        ll_sb_container.addStatusBarHeightPadding()

        initImageBanner()

        initQuickEntry()

        initWebCard()
    }

    private fun initQuickEntry() {
        rv_quick_entry.setHasFixedSize(true)
        rv_quick_entry.isNestedScrollingEnabled = false
        val quickEntryItemAdapter = QuickEntryItemAdapter(
            context, mutableListOf(
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
        )
        rv_quick_entry.adapter = quickEntryItemAdapter
        rv_quick_entry.layoutManager = GridLayoutManager(context, 4)
    }

    private fun initWebCard() {
        //解决scrollview嵌套滑动冲突问题
        rv_web_cards.setHasFixedSize(true)
        rv_web_cards.isNestedScrollingEnabled = false

        val webCardVTitleItemAdapter = WebCardVTitleItemAdapter(
            context,
            this,
            mutableListOf(
                WebCardItemBean(
                    "http://www.jq22.com/demo/jquerykgd201908272252/",
                    "新生必看",
                    "",
                    "查看全部"
                ),
                WebCardItemBean(
                    "http://www.jq22.com/demo/clip-clop-clippity-clop201908030107/",
                    "FM",
                    "双语脱口秀，聊美国文化、学地道的英语"
                ),
                WebCardItemBean(
                    "http://www.jq22.com/demo/colorfultouch201907302229/",
                    "为你推荐",
                    "根据你的兴趣为你精选优质课程"
                )
            )
        )
        rv_web_cards.adapter = webCardVTitleItemAdapter
        rv_web_cards.layoutManager = LinearLayoutManager(context)
    }

    private fun initImageBanner() {
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
        onAjsLongCallBack(requestCode, resultCode, data)
    }
}