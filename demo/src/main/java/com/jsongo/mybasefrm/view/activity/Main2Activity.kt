package com.jsongo.mybasefrm.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.adapter.QuickEntryItemAdapter
import com.jsongo.mybasefrm.adapter.WebCardVTitleItemAdapter
import com.jsongo.mybasefrm.bean.QuickEntryItemBean
import com.jsongo.mybasefrm.bean.WebCardItemBean
import com.jsongo.ui.component.image.banner.lib.anim.select.ZoomInEnter
import com.jsongo.ui.component.image.banner.lib.transform.ZoomOutSlideTransformer
import com.jsongo.ui.component.image.banner.widget.bean.BannerItem
import kotlinx.android.synthetic.main.activity_main2.*

@Page(R.layout.activity_main2, 0)
class Main2Activity : BaseActivity(), AjsWebViewHost {
    override val hostActivity: FragmentActivity?
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initImageBanner()

        initQuickEntry()

        initWebCard()
    }

    private fun initQuickEntry() {
        rv_quick_entry.setHasFixedSize(true)
        rv_quick_entry.isNestedScrollingEnabled = false
        val quickEntryItemAdapter = QuickEntryItemAdapter(
            this, mutableListOf(
                QuickEntryItemBean(
                    "http://www.jq22.com/demo/appyymoban201910161119/images/nav-001.png",
                    -1,
                    "FM",
                    "https://www.baidu.com"
                ),
                QuickEntryItemBean(
                    "http://www.jq22.com/demo/appyymoban201910161119/images/nav-002.png",
                    -1,
                    "免费试听",
                    "https://www.baidu.com"
                ),
                QuickEntryItemBean(
                    "http://www.jq22.com/demo/appyymoban201910161119/images/nav-003.png",
                    -1,
                    "等级课程",
                    "https://www.baidu.com"
                ),
                QuickEntryItemBean(
                    "http://www.jq22.com/demo/appyymoban201910161119/images/nav-004.png",
                    -1,
                    "主题课程",
                    "https://www.baidu.com"
                )
            )
        )
        rv_quick_entry.adapter = quickEntryItemAdapter
        rv_quick_entry.layoutManager = GridLayoutManager(this, 4)
    }

    private fun initWebCard() {
        //解决scrollview嵌套滑动冲突问题
        rv_web_cards.setHasFixedSize(true)
        rv_web_cards.isNestedScrollingEnabled = false


        val webCardVTitleItemAdapter = WebCardVTitleItemAdapter(
            this,
            this,
            mutableListOf(
                WebCardItemBean("https://www.baidu.com", "新生必看", "", "查看全部"),
                WebCardItemBean("file:///android_asset/web/index.html", "FM", "双语脱口秀，聊美国文化、学地道的英语"),
                WebCardItemBean("https://github.com", "为你推荐", "根据你的兴趣为你精选优质课程")
            )
        )
        rv_web_cards.adapter = webCardVTitleItemAdapter
        rv_web_cards.layoutManager = LinearLayoutManager(this)
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
