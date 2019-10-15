package com.jsongo.mybasefrm.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.core.network.ServerAddr
import com.jsongo.mybasefrm.R
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

        initWebLoador()

    }

    private fun initWebLoador() {

        val serverAddress = ServerAddr.SERVER_ADDRESS

        //使用ajswebview
//        card_webloader.url = "file:///android_asset/web/index.html"
        card_webloader.url = "${serverAddress}image/others/banner/img_banner_4.jpg"
        card_webloader.ajsWebViewHost = this
        card_webloader.initLoad()
        card_webloader2.url = "${serverAddress}image/others/banner/img_banner_3.jpg"
        card_webloader2.ajsWebViewHost = this
        card_webloader2.initLoad()
        card_webloader3.url = "${serverAddress}image/others/banner/img_banner_5.jpg"
        card_webloader3.ajsWebViewHost = this
        card_webloader3.initLoad()
    }

    private fun initImageBanner() {
        val serverAddress = ServerAddr.SERVER_ADDRESS
        val items = arrayListOf(
            BannerItem("${serverAddress}image/others/banner/img_banner_1.jpg", "我是江小白"),
            BannerItem("${serverAddress}image/others/banner/img_banner_2.jpg", "画江湖之不良人"),
            BannerItem("${serverAddress}image/others/banner/img_banner_8.jpg", "罗小黑战记")
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

    override fun onIPageDestroy() {
        card_webloader.destroy()
        card_webloader2.destroy()
        card_webloader3.destroy()
        super.onIPageDestroy()
    }

}
