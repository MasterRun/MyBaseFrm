package com.jsongo.ajs.interaction.register

import com.jsongo.ajs.annotations.AjsApi

/**
 * @author  jsongo
 * @date 2019/6/18 21:38
 * @desc 已有的交互api
 */
@AjsApi
object DefaultInteractionRegister : BaseInteractionRegister() {
    //交互api的包路径
    const val packageName = "com.jsongo.ajs.interaction"
    //api与类名映射
    override val nameMapping = hashMapOf(
        Pair("cache", "$packageName.Cache"),
        Pair("common", "$packageName.Common"),
        Pair("loading", "$packageName.Loading"),
        Pair("smartrefresh", "$packageName.SmartRefresh"),
        Pair("toast", "$packageName.Toast"),
        Pair("topbar", "$packageName.Topbar")
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