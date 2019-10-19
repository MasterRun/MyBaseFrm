package com.jsongo.core.helper

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午2:38
 * @desc :  简单封装的recyclerview  adapter
 *  约定:
 *  viewType = position
 *  position = type + typeQuota * type_position
 *  type = position%typeQuota 取余数
 *  type_position = position/typeQuota  整除
 */
abstract class RecyclerViewAdapter<A : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder, T>(
    val context: Context,
    dataList: MutableList<T>
) :
    RecyclerView.Adapter<VH>() {

    open var dataList: MutableList<T> = ArrayList()
        set(value) {
            field.clear()
            field.addAll(value)
        }

    var itemClickListener: OnRvItemClickListener<A, VH>? = null
    var itemLongClickListener: OnRvItemLongClickListener<A, VH>? = null

    init {
        this.dataList = dataList
    }

    companion object {
        /**
         * type 类型要小于此值
         */
        const val typeQuota = 100
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VH =
        onCreateViewHolder(p0, LayoutInflater.from(p0.context), getItemType(p1), p1)

    abstract fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        type: Int,
        position: Int
    ): VH

    override fun onBindViewHolder(p0: VH, p1: Int) {
        onBindViewHolder(p0, getItem(p1), getItemType(p1), p1)
    }

    abstract fun onBindViewHolder(holder: VH, dataItem: T, type: Int, position: Int)

    protected fun setClickListener(
        adapter: A, holder: VH, position: Int, type: Int
    ) {
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(adapter, holder, position, type)
        }
    }

    protected fun setLongClickListener(
        adapter: A, holder: VH, position: Int, type: Int
    ) {
        holder.itemView.setOnLongClickListener {
            if (itemLongClickListener != null) {
                itemLongClickListener?.onLongClick(adapter, holder, position, type)
                true
            } else {
                false
            }
        }
    }

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    open fun getItemType(position: Int): Int {
        return position % typeQuota
    }

    open fun getItem(position: Int): T {
        return dataList[position]
    }

}