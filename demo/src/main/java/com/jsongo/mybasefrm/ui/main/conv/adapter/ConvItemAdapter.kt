package com.jsongo.mybasefrm.ui.main.conv.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jsongo.core.helper.RecyclerViewAdapter
import com.jsongo.mybasefrm.databinding.ItemConvBinding

/**
 * @author ： jsongo
 * @date ： 2020/1/2 11:52
 * @desc : 会话列表适配器
 */
class ConvItemAdapter(context: Context, dataList: MutableList<MutableMap<String, Any?>>) :
    RecyclerViewAdapter<ConvItemAdapter, ConvItemAdapter.ViewHolder, MutableMap<String, Any?>>(
        context, dataList
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        type: Int,
        position: Int
    ): ViewHolder {
        val itemConvBinding = ItemConvBinding.inflate(inflater, parent, false)
        return ViewHolder(itemConvBinding)
    }

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