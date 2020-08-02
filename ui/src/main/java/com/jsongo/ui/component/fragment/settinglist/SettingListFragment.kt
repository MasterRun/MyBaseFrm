package com.jsongo.ui.component.fragment.settinglist

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jsongo.core_mini.base_page.BaseFragment
import com.jsongo.core_mini.widget.ILoadingDialog
import com.jsongo.core_mini.widget.IStatusView
import com.jsongo.core_mini.widget.ITopbar
import com.jsongo.core_mini.widget.RxToast
import com.jsongo.ui.R
import com.qmuiteam.qmui.kotlin.matchParent
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView as QLv

class SettingListFragment : BaseFragment() {

    override
    val loadingDialog: ILoadingDialog? = null

    override val topbar: ITopbar?
        get() = null
    override val statusView: IStatusView?
        get() = null
    override val layoutId: Int = R.layout.layout_setting_list

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

    override fun initIPage(context: Context) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        glv = view.findViewById(R.id.glv)
//        val glv = view as QMUIGroupListView

        createSection()

        //添加ViewCreated回调给外部
        viewCreatedCallback?.onViewCreated(glv, sectionViewList, itemViewMap)
    }

    /**
     * 循环创建SectionView并保存
     */
    protected fun createSection() {
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
                    addItemView(itemView, null)
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
    protected fun genItemView(
        item: SettingItem,
        glv: QMUIGroupListView
    ) = glv.createItemView(item.title).apply {
        item.let {
            //自定义添加覆盖View
            if (it.coverView != null) {
                setOnClickListener(it.onClickListener)
                addCoverView(it.coverView)
                return@let
            }
            if (it.iconRes != null) {
                setImageDrawable(ContextCompat.getDrawable(context, it.iconRes))
            }
            if (it.detailText != null) {
                detailText = it.detailText
            }
            orientation = it.orientation
            accessoryType = it.accessoryType
            //设置显示位置
            setTipPosition(it.tipPosition)
            if (it.showRedDot) {
                //显示红点
                showRedDot(it.showRedDot)
            }
            if (it.showNewTip) {
                //显示新提示
                showNewTip(it.showNewTip)
            }
            //设置点击事件
            setOnClickListener { v ->
                //如果显示switch，切换状态
                if (accessoryType == QLv.ACCESSORY_TYPE_SWITCH) {
                    switch.isChecked = !switch.isChecked
                }
                it.onClickListener.onClick(v)
            }
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
        val sectionListDemo: ArrayList<SettingSection> by lazy {
            arrayListOf(
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
                            accessoryType = QLv.ACCESSORY_TYPE_CHEVRON,
                            onClickListener = View.OnClickListener { RxToast.normal("item 2") }
                        ),
                        SettingItem(
                            "item3",
                            R.drawable.next,
                            "hahah",
                            QLv.HORIZONTAL,
                            QLv.ACCESSORY_TYPE_SWITCH,
                            showNewTip = true,
                            checkChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, checked ->
                                RxToast.normal("checked:${checked}")
                            }
                        )
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
                            showNewTip = true,
                            onClickListener = View.OnClickListener { RxToast.normal("click item 2-2") }
                        )
                    )
                )
            )
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

/**
 * 添加遮盖的View
 */
fun QLv?.addCoverView(view: View) {
    if (this == null) {
        return
    }
    //隐藏文本
    textView?.visibility = View.GONE
    //设置id
    if (this.id == View.NO_ID) {
        this.id = R.id.cl_root
    }
    //添加View，填充居中
    addView(view, ConstraintLayout.LayoutParams(matchParent, matchParent).let {
        it.startToStart = this.id
        it.endToEnd = this.id
        it.rightToRight = this.id
        it.leftToLeft = this.id
        it
    })
}