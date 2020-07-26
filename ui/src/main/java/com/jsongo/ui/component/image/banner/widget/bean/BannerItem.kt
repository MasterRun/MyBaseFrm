package com.jsongo.ui.component.image.banner.widget.bean

import com.epoint.app.widget.banner.widget.bean.ImageBannerItem

data class BannerItem(
    override val imgUrl: String = "",
    override val title: String = ""
) : ImageBannerItem