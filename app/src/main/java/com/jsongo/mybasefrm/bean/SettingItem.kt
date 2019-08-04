package com.jsongo.mybasefrm.bean

import android.support.annotation.DrawableRes
import android.view.View
import android.widget.CompoundButton
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView as QLv

/**
 * author ： jsongo
 * createtime ： 2019/8/4 12:58
 * desc : 设置列表的item
 */
data class SettingItem(
    @DrawableRes val iconRes: Int? = null,
    val title: String,
    val detailText: String? = null,
    val orientation: Int = QLv.HORIZONTAL,
    val accessoryType: Int = QLv.ACCESSORY_TYPE_NONE,
    val showRedDot: Boolean = false,
    val redDotPosition: Int = QLv.REDDOT_POSITION_RIGHT,
    val showNewTip: Boolean = false,
    val onClickListener: View.OnClickListener = View.OnClickListener {},
    val checkChangeListener: CompoundButton.OnCheckedChangeListener? = null,
    val customAccessory: View? = null
)