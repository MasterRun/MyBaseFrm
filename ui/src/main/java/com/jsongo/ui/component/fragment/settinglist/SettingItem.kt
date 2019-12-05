package com.jsongo.ui.component.fragment.settinglist

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.DrawableRes
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView as QLv

/**
 * author ： jsongo
 * createtime ： 2019/8/4 12:58
 * desc : 设置列表的item
 */
data class SettingItem(
    val title: String,
    @DrawableRes val iconRes: Int? = null,
    val detailText: String? = null,
    val orientation: Int = QLv.HORIZONTAL,
    val accessoryType: Int = QLv.ACCESSORY_TYPE_NONE,
    val showRedDot: Boolean = false,
    val tipPosition: Int = QLv.TIP_POSITION_RIGHT,
    val showNewTip: Boolean = false,
    val onClickListener: View.OnClickListener = View.OnClickListener {},
    val checkChangeListener: CompoundButton.OnCheckedChangeListener? = null,
    val customAccessory: View? = null
)