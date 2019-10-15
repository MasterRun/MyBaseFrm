package com.jsongo.mybasefrm.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.ViewTreeObserver
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.annotation.anno.Page
import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.mybasefrm.R
import com.jsongo.ui.component.image.banner.lib.anim.select.ZoomInEnter
import com.jsongo.ui.component.image.banner.lib.transform.ZoomOutSlideTransformer
import com.jsongo.ui.component.image.banner.widget.bean.BannerItem
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.vondear.rxtool.view.RxToast
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
//        val webPath = "file:///android_asset/web/index.html"
        val webPath = "https://www.baidu.com"

        //使用 ajswebloader
        val ajswebloader = ajswebloader_flat_card
        if (ajswebloader is AJsWebLoader) {
            ajswebloader.webPath = webPath
            ajswebloader.topbar.visibility = View.GONE
        }
        //调整高度
        flat_card.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val _250dp2px = QMUIDisplayHelper.dp2px(this@Main2Activity, 250)
                if (flat_card.height > _250dp2px) {
                    flat_card.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val layoutParams = flat_card.layoutParams
                    layoutParams.height = _250dp2px
                    flat_card.layoutParams = layoutParams
                }
            }
        })

        //使用ajswebview
        card_webloader.url = "file:///android_asset/web/index.html"
        card_webloader.scrollable = true
        card_webloader.ajsWebViewHost = this
        card_webloader.initLoad()
        card_webloader2.ajsWebViewHost = this
        card_webloader2.initLoad()
    }

    private fun initImageBanner() {
        val items =
            arrayListOf(
                BannerItem(
//                        "http://pic26.nipic.com/20121221/9252150_142515375000_2.jpg",
                    "/storage/emulated/0/ADM/face1.jpg",
                    "这是图片1"
                ),
                BannerItem(
                    "http://img5.imgtn.bdimg.com/it/u=3300305952,1328708913&fm=26&gp=0.jpg",
//                        "http://pic39.nipic.com/20140307/13928177_195158772185_2.jpg",
                    "这是图片2"
                ),
                BannerItem(
                    "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=508387608,2848974022&fm=27&gp=0.jpg",
//                        "http://pic27.nipic.com/20130321/9678987_225139671149_2.jpg",
                    "这是图片3"
                )
            )
        simple_ib.setSelectAnimClass(ZoomInEnter::class.java)//set indicator select anim
            .setSource(items)//data source list
            .setTransformerClass(ZoomOutSlideTransformer::class.java)//set page transformer
            .startScroll()
        simple_ib.setOnItemClickListener {
            val item = items.get(it)
            RxToast.info(item.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onAjsLongCallBack(requestCode, resultCode, data)
    }

    override fun onIPageDestroy() {
        card_webloader.destroy()
        card_webloader2.destroy()
        super.onIPageDestroy()
    }

}
