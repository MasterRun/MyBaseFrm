package com.jsongo.ui.component.fragment.settinglist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import com.jsongo.core.arch.BaseFragment
import com.jsongo.core.widget.RxToast
import com.jsongo.ui.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView as QLv

//@Page(R.layout.layout_setting_list, 0)
class SettingListFragment : BaseFragment() {

    /**
     * 在lib中，资源id不是final变量，不能使用注解
     */
    override var mainLayoutId = R.layout.layout_setting_list
//    override var containerIndex = 0

    /**
     * 配置实体
     */
    private var sectionList: List<SettingSection>? = null

    /**
     * onViewCreate回调
     */
    var viewCreatedCallback: ViewCreatedCallback? = null

    /**
     * 生成的View，方便外部更改
     */
    val sectionViewList = ArrayList<QMUIGroupListView.Section>()
    val itemViewMap = HashMap<String, QLv>()

    lateinit var glv: QMUIGroupListView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        glv = view.findViewById(R.id.glv)
//        val glv = view as QMUIGroupListView

        //循环创建SectionView并保存
        sectionList?.forEachIndexed { index, it ->
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
        //添加ViewCreated回调给外部
        viewCreatedCallback?.onViewCreated(glv, sectionViewList, itemViewMap)
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
            setTipPosition(it.tipPosition)
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

    /**
     * onViewCreate回调
     */
    interface ViewCreatedCallback {
        fun onViewCreated(
            glv: QMUIGroupListView,
            sectionViewList: java.util.ArrayList<QMUIGroupListView.Section>,
            itemViewMap: java.util.HashMap<String, QLv>
        )

    }

    companion object {
        @JvmStatic
        fun newInstance(sections: List<SettingSection>) =
            SettingListFragment().apply {
                sectionList = sections
            }

        /**
         * 演示
         */
        val sectionListDemo = arrayListOf(
            SettingSection(
                "section1", /*"这是描述",*/ items = arrayListOf(
                    SettingItem(
                        "item1",
                        null,
                        "detail",
                        onClickListener = View.OnClickListener { RxToast.normal("click item1") }
                    ),
                    SettingItem(
                        "item2",
                        R.drawable.icon_menu,
                        accessoryType = com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                        onClickListener = View.OnClickListener { RxToast.normal("item 2") }
                    ),
                    SettingItem(
                        "item3",
                        R.drawable.next,
                        "hahah",
                        com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView.HORIZONTAL,
                        com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView.ACCESSORY_TYPE_SWITCH,
                        showNewTip = true,
                        checkChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, checked ->
                            RxToast.normal("checked:${checked}")
                        })
                )
            ),
            SettingSection("section2",
                items = arrayListOf(
                    SettingItem(
                        "item 2-1",
                        R.drawable.next,
                        "desc",
                        showRedDot = true,
                        onClickListener = View.OnClickListener { RxToast.normal("click item 2-1") }
                    ),
                    SettingItem(
                        "item 2-2",
                        R.drawable.next,
                        "desc",
                        showRedDot = true,
                        onClickListener = View.OnClickListener { RxToast.normal("click item 2-2") }
                    )
                )
            )
        )
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
        if (settingItem.tipPosition != QLv.TIP_POSITION_RIGHT) {
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