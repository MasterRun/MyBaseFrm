package com.jsongo.ajs.interaction.register

import com.jsongo.ajs.interaction.*

/**
 * @author  jsongo
 * @date 2019/6/18 21:38
 * @desc 已有的交互api
 */
object DefaultInteractionRegister : BaseInteractionRegister() {

    //api与类名映射
    override val nameMapping = hashMapOf(
        Pair("cache", Cache::class.java.name),
        Pair("common", Common::class.java.name),
        Pair("loading", Loading::class.java.name),
        Pair("smartrefresh", SmartRefresh::class.java.name),
        Pair("toast", Toast::class.java.name),
        Pair("topbar", Topbar::class.java.name)
    )
    //已有的api
    override val interactionAPI = arrayListOf(
        "cache.put",
        "cache.get",

        "common.back",
        "common.messagedialog",
        "common.localpic",
        "common.showpic",
        "common.go",
        "common.load",

        "loading.show",
        "loading.hide",

        "smartrefresh.enableRefresh",
        "smartrefresh.enableLoadmore",
        "smartrefresh.color",
        "smartrefresh.header",
        "smartrefresh.footer",

        "toast.error",
        "toast.warning",
        "toast.info",
        "toast.normal",
        "toast.success",

        "topbar.bgcolor",
        "topbar.hide",
        "topbar.title",
        "topbar.statusbar"
    )
}