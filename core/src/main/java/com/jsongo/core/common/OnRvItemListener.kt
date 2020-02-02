package com.jsongo.core.common

import androidx.recyclerview.widget.RecyclerView

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午2:59
 * @desc : item点击事件
 */
interface OnRvItemClickListener<A : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> {
    fun onRvItemClick(adapter: A, holder: VH, position: Int, type: Int)
}

/**
 * 长按事件
 */
interface OnRvItemLongClickListener<A : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> {
    fun onRvItemLongClick(adapter: A, holder: VH, position: Int, type: Int)
}