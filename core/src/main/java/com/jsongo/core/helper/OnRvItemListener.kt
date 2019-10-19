package com.jsongo.core.helper

import android.support.v7.widget.RecyclerView

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午2:59
 * @desc : item点击事件
 */
interface OnRvItemClickListener<A : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> {
    fun onClick(adapter: A, holder: VH, position: Int, type: Int)
}

/**
 * 长按事件
 */
interface OnRvItemLongClickListener<A : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> {
    fun onLongClick(adapter: A, holder: VH, position: Int, type: Int)
}