package com.jsongo.ajs.helper

import com.jsongo.ajs.AJs

/**
 * @author  jsongo
 * @date 2019/6/16 16:45
 * @desc
 */
object ConstValue {
    const val webpath = "webpath"
    const val LocalPicPrefix = "http://androidimg"

    const val jsBasePath = "web/js/lib"
    val jsList = ArrayList<String>()

    init {
        //加载js列表
        try {
            val assets = AJs.context.resources.assets
            val list = assets.list(jsBasePath)
            if (list != null) {
                for (s in list) {
                    if (s.endsWith(".js") && (s.contains("echarts") ||
                                s.contains("jquery") ||
                                s.contains("vconsole") ||
                                s.contains("zepto"))
                    ) {
                        jsList.add(s)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}