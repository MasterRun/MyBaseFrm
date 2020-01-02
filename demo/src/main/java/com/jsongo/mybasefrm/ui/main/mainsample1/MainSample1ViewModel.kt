package com.jsongo.mybasefrm.ui.main.mainsample1

import androidx.lifecycle.MutableLiveData
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.mybasefrm.ui.main.mainsample1.bean.QuickEntryItemBean
import com.jsongo.mybasefrm.ui.main.mainsample1.bean.WebCardItemBean
import com.jsongo.ui.component.image.banner.widget.bean.BannerItem

/**
 * @author ： jsongo
 * @date ： 2019/11/8 17:23
 * @desc : 首页主模板1的ViewModel
 */
class MainSample1ViewModel : BaseViewModel() {
    /**
     * 轮播图的LiveData
     */
    val imgBannerList = MutableLiveData<MutableList<BannerItem>>()
    /**
     * 快捷入口的LiveDate
     */
    val quickEntryItemList = MutableLiveData<MutableList<QuickEntryItemBean>>()

    /**
     * 首页web卡片的LiveData
     */
    val webCardItemList = MutableLiveData<MutableList<WebCardItemBean>>()


    init {

        //轮播图数据
        val items = arrayListOf(
            BannerItem("http://www.jq22.com/demo/appyymoban201910161119/images/banner.png", "图片1"),
            BannerItem("http://www.jq22.com/demo/appyymoban201910161119/images/banner.png", "图片2"),
            BannerItem("http://www.jq22.com/demo/appyymoban201910161119/images/banner.png", "图片3")
        )
        imgBannerList.value = items

        //快捷入口列表项
        val quickEntryItem = mutableListOf(
            QuickEntryItemBean(
                "http://www.jq22.com/demo/appyymoban201910161119/images/nav-001.png",
                -1,
                "FM",
                "http://www.jq22.com/demo/jqueryrwdh201907110144/dist/"
            ),
            QuickEntryItemBean(
                "http://www.jq22.com/demo/appyymoban201910161119/images/nav-002.png",
                -1,
                "免费试听",
                "http://www.jq22.com/demo/planet201907121655/"
            ),
            QuickEntryItemBean(
                "http://www.jq22.com/demo/appyymoban201910161119/images/nav-003.png",
                -1,
                "等级课程",
                "http://www.jq22.com/demo/3dlft201904260032/"
            ),
            QuickEntryItemBean(
                "http://www.jq22.com/demo/appyymoban201910161119/images/nav-004.png",
                -1,
                "主题课程",
                "http://www.jq22.com/demo/threeph201903262259/"
            )
        )
        quickEntryItemList.value = quickEntryItem


        //卡片数据项
        val webCardItems = mutableListOf(
            WebCardItemBean(
//                    "http://www.jq22.com/demo/jquerykgd201908272252/",
                "file:///android_asset/web-cards/card_1/card_1.html",
                "新生必看",
                "",
                "查看全部"
            ),
            WebCardItemBean(
//                    "http://www.jq22.com/demo/clip-clop-clippity-clop201908030107/",
                "file:///android_asset/web-cards/card_2/card_2.html",
                "FM",
                "双语脱口秀，聊美国文化、学地道的英语"
            ),
            WebCardItemBean(
//                    "http://www.jq22.com/demo/colorfultouch201907302229/",
                "file:///android_asset/web-cards/card_3/card_3.html",
                "为你推荐",
                "根据你的兴趣为你精选优质课程"
            )
        )
        webCardItemList.value = webCardItems
    }

}