package com.jsongo.mybasefrm.bean

/**
 * author ： jsongo
 * createtime ： 2019/8/4 12:55
 * desc :设置列表的 section
 */
data class SettingSection(
    val title: String? = null,
    val desc: String? = null,
    val iconSize: Int? = null,
    val items: List<SettingItem>
)