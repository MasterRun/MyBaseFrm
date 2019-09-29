package com.jsongo.ajs.interaction.register

/**
 * @author  jsongo
 * @date 2019/6/18 21:38
 * @desc 已有的交互api
 */
object DefaultInteractionRegister : BaseInteractionRegister() {
    //api 映射
    override val interactionAPI = hashMapOf(
        Pair("cache.put", "com.jsongo.ajs.interaction.Cache.put"),
        Pair("cache.get", "com.jsongo.ajs.interaction.Cache.get"),

        Pair("common.back", "com.jsongo.ajs.interaction.Common.back"),
        Pair("common.messagedialog", "com.jsongo.ajs.interaction.Common.messagedialog"),
        Pair("common.localpic", "com.jsongo.ajs.interaction.Common.localpic"),
        Pair("common.showpic", "com.jsongo.ajs.interaction.Common.showpic"),
        Pair("common.go", "com.jsongo.ajs.interaction.Common.go"),
        Pair("common.load", "com.jsongo.ajs.interaction.Common.load"),
        Pair("common.scan", "com.jsongo.ajs.interaction.Common.scan"),

        Pair("file.selectImg", "com.jsongo.ajs.interaction.File.selectImg"),
        Pair("file.base64", "com.jsongo.ajs.interaction.File.base64"),
        Pair("file.delete", "com.jsongo.ajs.interaction.File.delete"),

        Pair("loading.show", "com.jsongo.ajs.interaction.Loading.show"),
        Pair("loading.hide", "com.jsongo.ajs.interaction.Loading.hide"),

        Pair("smartrefresh.enableRefresh", "com.jsongo.ajs.interaction.SmartRefresh.enableRefresh"),
        Pair(
            "smartrefresh.enableLoadmore",
            "com.jsongo.ajs.interaction.SmartRefresh.enableLoadmore"
        ),
        Pair("smartrefresh.color", "com.jsongo.ajs.interaction.SmartRefresh.color"),
        Pair("smartrefresh.header", "com.jsongo.ajs.interaction.SmartRefresh.header"),
        Pair("smartrefresh.footer", "com.jsongo.ajs.interaction.SmartRefresh.footer"),

        Pair("toast.error", "com.jsongo.ajs.interaction.Toast.error"),
        Pair("toast.warning", "com.jsongo.ajs.interaction.Toast.warning"),
        Pair("toast.info", "com.jsongo.ajs.interaction.Toast.info"),
        Pair("toast.normal", "com.jsongo.ajs.interaction.Toast.normal"),
        Pair("toast.success", "com.jsongo.ajs.interaction.Toast.success"),

        Pair("topbar.bgcolor", "com.jsongo.ajs.interaction.Topbar.bgcolor"),
        Pair("topbar.hide", "com.jsongo.ajs.interaction.Topbar.hide"),
        Pair("topbar.title", "com.jsongo.ajs.interaction.Topbar.title"),
        Pair("topbar.statusbar", "com.jsongo.ajs.interaction.Topbar.statusbar")
    )
}