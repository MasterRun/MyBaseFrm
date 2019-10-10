package com.jsongo.mybasefrm.view.activity

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import com.jsongo.ajs.webloader.BaseWebLoader
import com.jsongo.annotation.anno.ConfPage
import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.mybasefrm.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import kotlinx.android.synthetic.main.activity_main2.*

@ConfPage(R.layout.activity_main2, 2)
class Main2Activity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val webPath = "file:///android_asset/web/index.html"
        val webPath = "https://www.baidu.com"

        val fragWebLoader = ajswebloader_flat_card
        if (fragWebLoader is BaseWebLoader) {
            fragWebLoader.webPath = webPath
            fragWebLoader.topbar.visibility = View.GONE
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

    }
}
