package com.jsongo.mybasefrm.jsloader

import android.content.Intent
import android.os.Bundle
import com.jsongo.mybasefrm.ConstValue
import com.jsongo.mybasefrm.MyApplication
import kotlinx.android.synthetic.main.activity_base.*

/**
 * @author jsongo
 * @date 2019/5/9 19:45
 * @desc default wenloader   load baidu
 */
class DefaultWebLoader : AJsWebLoader() {

    override fun init() {
        super.init()

        if (intent.hasExtra(ConstValue.webpath)) {
            webPath = intent.getStringExtra(ConstValue.webpath)
        }
        if (webPath.isEmpty()) {
            //        webPath = "https://www.baidu.com";
            webPath = "file:///android_asset/web/index.html"
        }
    }

    companion object {
        fun load(url: String) {
            val intent = Intent(MyApplication.context, DefaultWebLoader::class.java)
            intent.putExtra(ConstValue.webpath, url)
            MyApplication.context.startActivity(intent)
        }
    }
}
