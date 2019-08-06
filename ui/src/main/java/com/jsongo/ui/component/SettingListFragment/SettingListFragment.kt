package com.jsongo.ui.component.SettingListFragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.jsongo.core.mvp.base.BaseFragment
import com.jsongo.ui.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView as QLv

//@ConfPage(R.layout.layout_setting_list, 0)
class SettingListFragment : BaseFragment() {

    /**
     * 在lib中，资源id不是final变量，不能使用注解
     */
    override var mainLayoutId = R.layout.layout_setting_list
//    override var containerIndex = 0

    /**
     * 配置实体
     */
    private lateinit var sectionList: List<SettingSection>

    /**
     * 生成的View，方便外部更改
     */
    val sectionViewList = ArrayList<QMUIGroupListView.Section>()
    val itemViewMap = HashMap<String, QLv>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val glv = view.findViewById<QMUIGroupListView>(R.id.glv)
//        val glv = view as QMUIGroupListView

        //循环创建SectionView并保存
        sectionList.forEachIndexed { index, it ->
            val section = QMUIGroupListView.newSection(context)
                .setTitle(it.title)
            section.apply {
                if (it.desc != null) {
                    setDescription(it.desc)
                }
                val iconSize: Int
                if (it.iconSize != null && it.iconSize > 0) {
                    iconSize = it.iconSize
                } else {
                    iconSize = QMUIDisplayHelper.dp2px(context, 20)
                }
                it.items.forEachIndexed { itemIndex, it ->
                    val itemView = genItemView(it, glv)
                    addItemView(itemView, it.onClickListener)
                    itemViewMap["${index}-${itemIndex}"] = itemView
                }
                setLeftIconSize(iconSize, ViewGroup.LayoutParams.WRAP_CONTENT)
                addTo(glv)
            }
            sectionViewList.add(section)
        }
    }

    /**
     * 生成ListItemView
     */
    private fun genItemView(
        item: SettingItem,
        glv: QMUIGroupListView
    ) = glv.createItemView(item.title).apply {
        item.let {
            if (it.iconRes != null) {
                setImageDrawable(ContextCompat.getDrawable(context, it.iconRes))
            }
            if (it.detailText != null) {
                detailText = it.detailText
            }
            orientation = it.orientation
            accessoryType = it.accessoryType
            showRedDot(it.showRedDot)
            setRedDotPosition(it.redDotPosition)
            showNewTip(it.showNewTip)
            if (accessoryType == QLv.ACCESSORY_TYPE_SWITCH && it.checkChangeListener != null) {
                switch.setOnCheckedChangeListener(it.checkChangeListener)
            }
            if (it.customAccessory != null) {
                addAccessoryCustomView(it.customAccessory)
            }
            correctDetailTextPosition(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(sections: List<SettingSection>) =
            SettingListFragment().apply {
                sectionList = sections
            }
    }
}

/**
 * 修复在horizontal 布局下在右侧显示红点和detailtext时的覆盖问题
 */
fun QLv?.correctDetailTextPosition(settingItem: SettingItem? = null, offset: Int = 40) {
    if (this == null) {
        return
    }
    if (settingItem != null) {
        if (settingItem.detailText.isNullOrEmpty()) {
            return
        }
        if (settingItem.orientation != QLv.HORIZONTAL) {
            return
        }
        if (settingItem.showRedDot.not()) {
            return
        }
        if (settingItem.redDotPosition != QLv.REDDOT_POSITION_RIGHT) {
            return
        }
        if (settingItem.showNewTip) {
            return
        }
        if (settingItem.customAccessory != null && settingItem.accessoryType == QLv.ACCESSORY_TYPE_CUSTOM) {
            return
        }
    }
    val layoutParams = detailTextView.layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams.rightMargin = offset
        detailTextView.layoutParams = layoutParams
    }
}