package com.jsongo.mybasefrm.ui.main.mainsample1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.jsongo.core.common.RecyclerViewAdapter
import com.jsongo.core.util.GlideUtil
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.main.mainsample1.bean.QuickEntryItemBean
import kotlinx.android.synthetic.main.item_quick_entry.view.*

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午2:35
 * @desc : 快捷入口适配器
 */
class QuickEntryItemAdapter(
    context: Context?,
    dataList: MutableList<QuickEntryItemBean>
) : RecyclerViewAdapter<QuickEntryItemAdapter, QuickEntryItemAdapter.ViewHolder, QuickEntryItemBean>(
    context, dataList
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        type: Int,
        position: Int
    ): ViewHolder {
        val view = inflater.inflate(R.layout.item_quick_entry, parent, false)
        val holder = ViewHolder(view)
        setClickListener(this, holder, position, type)
        setLongClickListener(this, holder, position, type)
        return holder
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        dataItem: QuickEntryItemBean,
        type: Int,
        position: Int
    ) {
        holder.apply {
            itemView.tag = dataItem
            tvQuickTxt.text = dataItem.text
            tvQuickTxt.paint.isFakeBoldText = true
            GlideUtil.load(
                context,
                dataItem.iconUrl,
                RequestOptions.errorOf(dataItem.iconRes),
                ivQuickIcon
            )
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llQuickEntry: LinearLayoutCompat = itemView.ll_quick_entry
        val ivQuickIcon: AppCompatImageView = itemView.iv_quick_icon
        val tvQuickTxt: AppCompatTextView = itemView.tv_quick_txt
    }
}