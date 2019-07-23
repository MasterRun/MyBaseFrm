package com.jsongo.core.util

import android.content.Context
import android.support.annotation.ColorRes
import com.scwang.smartrefresh.header.*
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import com.scwang.smartrefresh.layout.header.ClassicsHeader

/**
 * author ： jsongo
 * createtime ： 2019/7/14 17:17
 * desc :SmartRefreshLayout 帮助
 */

/**
 * 刷新头部
 */
fun RefreshLayout.useHeader(context: Context, header: SmartRefreshHeader) =
    setRefreshHeader(
        when (header) {
            SmartRefreshHeader.DeliveryHeader -> DeliveryHeader(context)
            SmartRefreshHeader.DropBoxHeader -> DropBoxHeader(context)
            SmartRefreshHeader.BezierRadarHeader -> BezierRadarHeader(context)
            SmartRefreshHeader.BezierCircleHeader -> BezierCircleHeader(context)
            SmartRefreshHeader.FlyRefreshHeader -> FlyRefreshHeader(context)
            SmartRefreshHeader.ClassicsHeader -> ClassicsHeader(context)
            SmartRefreshHeader.PhoenixHeader -> PhoenixHeader(context)
            SmartRefreshHeader.TaurusHeader -> TaurusHeader(context)
            SmartRefreshHeader.FunGameBattleCityHeader -> FunGameBattleCityHeader(context)
            SmartRefreshHeader.FunGameHitBlockHeader -> FunGameHitBlockHeader(context)
            SmartRefreshHeader.WaveSwipeHeader -> WaveSwipeHeader(context)
            SmartRefreshHeader.MaterialHeader -> MaterialHeader(context)
            SmartRefreshHeader.StoreHouseHeader -> StoreHouseHeader(context).initWithString("loading...")
            SmartRefreshHeader.WaterDropHeader -> WaterDropHeader(context)
        }
    )

fun RefreshLayout.initWithStr(enlishString: String) = this.apply {
    val refreshHeader = this.refreshHeader
    if (refreshHeader is StoreHouseHeader) {
        refreshHeader.initWithString(enlishString)
    }
}

/**
 * 加载更多
 */
fun RefreshLayout.useFooter(context: Context, footer: SmartRefreshFooter) =
    setRefreshFooter(
        when (footer) {
            SmartRefreshFooter.BallPulseFooter -> BallPulseFooter(context)
            SmartRefreshFooter.ClassicsFooter -> ClassicsFooter(context)
            //SmartRefreshFooter.FalsifyFooter -> FalsifyFooter(context)
        }
    )

/**
 * 设置颜色
 */
fun RefreshLayout.userColors(
    primaryColor: Int,
    accentColor: Int
) = setPrimaryColors(primaryColor, accentColor)

/**
 * 设置颜色
 */
fun RefreshLayout.useColorIds(
    @ColorRes primaryColorId: Int, @ColorRes accentColorId: Int
) = setPrimaryColorsId(primaryColorId, accentColorId)

/**
 * 刷新监听
 */

/*
fun RefreshLayout.addRefreshListener(
    onRefresh: (refreshLayout: RefreshLayout) -> Unit
) = setOnRefreshListener(onRefresh)
*/

/**
 * 加载更多监听
 */

/*
fun RefreshLayout.addLoadMoreListener(
    onLoadMore: (refreshLayout: RefreshLayout) -> Unit
) = this.setOnLoadMoreListener(onLoadMore)
*/


/**
 * 刷新头
 */
enum class SmartRefreshHeader constructor(value: String) {
    DeliveryHeader("DeliveryHeader"),  //气球  smart_refresh_layout.setPrimaryColorsId(R.color.gray1, R.
    DropBoxHeader("DropBoxHeader"),   //盒子  默认颜色不建议更改
    BezierRadarHeader("BezierRadarHeader"),//贝塞尔雷达 颜色更换异常！ 不建议是使用
    BezierCircleHeader("BezierCircleHeader"),//贝塞尔圆圈 颜色可改
    FlyRefreshHeader("FlyRefreshHeader"),// 纸飞机 无效果！
    ClassicsHeader("ClassicsHeader"),// 经典刷新 颜色可改
    PhoenixHeader("PhoenixHeader"),// 金色校园 颜色不建议更改
    TaurusHeader("TaurusHeader"),// 飞机冲上云霄 颜色不建议更改
    FunGameBattleCityHeader("FunGameBattleCityHeader"),// 战争城市游戏 颜色不建议更改
    FunGameHitBlockHeader("FunGameHitBlockHeader"),// 打砖块游戏 颜色不建议更改
    WaveSwipeHeader("WaveSwipeHeader"),// 全屏水波 颜色可更改
    MaterialHeader("MaterialHeader"),// material 颜色不可更改
    StoreHouseHeader("StoreHouseHeader"),// .initWithString("loading...") // StoreHouse 颜色可更改 可设置英文
    WaterDropHeader("WaterDropHeader")// 水滴 颜色可更改
}

/**
 * 加载更多
 */
enum class SmartRefreshFooter constructor(value: String) {
    BallPulseFooter("BallPulseFooter"),//球脉冲  颜色可改
    ClassicsFooter("ClassicsFooter"),//经典加载更多  颜色不可改
    //FalsifyFooter//假的加载。。。
}