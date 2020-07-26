package com.epoint.app.widget.banner.widget.bean

/**
 * 作者： 朱旭
 * 创建时间： 2020/7/20 11:35
 * 版本： [1.0, 2020/7/20]
 * 版权： 国泰新点软件股份有限公司
 * 描述： 图片banner item
 */
interface ImageBannerItem {
    /**
     * 获取图片在线地址
     */
    val imgUrl: String

    /**
     * 获取banner文字
     */
    val title: String
}