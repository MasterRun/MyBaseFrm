package com.jsongo.ajs.webloader

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.jsongo.ajs.AJs
import com.jsongo.ajs.helper.ConstValue

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
            webPath = "https://www.baidu.com";
        }
    }

    companion object {
        fun load(url: String) {
            val intent = Intent(AJs.context, DefaultWebLoader::class.java)
            intent.putExtra(ConstValue.webpath, url)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            AJs.context.startActivity(intent)
        }
    }
}
