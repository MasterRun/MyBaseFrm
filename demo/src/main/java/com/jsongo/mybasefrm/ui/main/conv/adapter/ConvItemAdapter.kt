package com.jsongo.mybasefrm.ui.main.conv.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jsongo.core.common.RecyclerViewAdapter
import com.jsongo.core.util.DateUtil
import com.jsongo.mybasefrm.databinding.ItemConvBinding
import java.util.*

/**
 * @author ： jsongo
 * @date ： 2020/1/2 11:52
 * @desc : 会话列表适配器
 */
class ConvItemAdapter(context: Context, dataList: MutableList<MutableMap<String, Any?>>) :
    RecyclerViewAdapter<ConvItemAdapter, ConvItemAdapter.ViewHolder, MutableMap<String, Any?>>(
        context, dataList
    ) {
    companion object {

        /**
         * 显示时间数据
         */
        @JvmStatic
        @BindingAdapter("tv_time")
        fun setTextViewTime(tv: TextView, any: Any?) {
            if (any is Date) {
                tv.text = DateUtil.relativeTime(any)
            } else if (any is String) {
                tv.text = any
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        type: Int,
        position: Int
    ): ViewHolder {
        val itemConvBinding = ItemConvBinding.inflate(inflater, parent, false)
        val viewHolder = ViewHolder(itemConvBinding)
        setClickListener(this, viewHolder, position, type)
        setLongClickListener(this, viewHolder, position, type)
        return viewHolder
    }

    /**
     * 字段
     * unreadCount
     * avatar
     * time
     * username
     * lastMessage
     */
    override fun onBindViewHolder(
        holder: ViewHolder,
        dataItem: MutableMap<String, Any?>,
        type: Int,
        position: Int
    ) {
        holder.itemConvBinding.setItem(dataItem)
    }

    class ViewHolder(val itemConvBinding: ItemConvBinding) :
        RecyclerView.ViewHolder(itemConvBinding.root)
}