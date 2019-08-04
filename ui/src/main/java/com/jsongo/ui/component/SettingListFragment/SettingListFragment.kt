package com.jsongo.ui.component.SettingListFragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.jsongo.core.mvp.base.BaseFragment
import com.jsongo.ui.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.safframework.log.L
import kotlinx.android.synthetic.main.layout_setting_list.view.*
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView as QLv

//@ConfPage(R.layout.layout_setting_list, 3)
class SettingListFragment : BaseFragment() {

    override var mainLayoutId = R.layout.layout_setting_list
    override var containerIndex = 3

    private lateinit var sectionList: List<SettingSection>
    private var listener: OnFragmentInteractionListener? = null

    val sectionViewList = ArrayList<QMUIGroupListView.Section>()
    val itemViewMap = HashMap<String, QLv>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val glv = view.glv

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
            L.e(sectionViewList.add(section).toString())
        }
    }

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
        }
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(sections: List<SettingSection>) =
            SettingListFragment().apply {
                sectionList = sections
            }
    }
}
