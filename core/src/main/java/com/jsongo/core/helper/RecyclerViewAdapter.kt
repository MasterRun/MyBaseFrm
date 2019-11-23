package com.jsongo.core.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午2:38
 * @desc :  简单封装的RecyclerView  adapter
 *  约定:
 *  viewType = position
 *  position = type + typeQuota * type_position
 *  type = position%typeQuota 取余数
 *  type_position = position/typeQuota  整除
 */
abstract class RecyclerViewAdapter<A : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder, T>(
    var context: Context?,
    dataList: MutableList<T>
) : RecyclerView.Adapter<VH>() {

    /**
     * 数据,默认是清空后重新添加
     */
    open var dataList: MutableList<T> = ArrayList()
        set(value) {
            //清空数据重新添加
            field.clear()
            field.addAll(value)
        }

    /**
     * 点击事件
     */
    var itemClickListener: OnRvItemClickListener<A, VH>? = null
    /**
     * 长按事件
     */
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        return onCreateViewHolder(
            parent,
            LayoutInflater.from(parent.context),
            getItemType(viewType),
            viewType
        )
    }

    abstract fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        type: Int,
        position: Int
    ): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, getItem(position), getItemType(position), position)
    }

    abstract fun onBindViewHolder(holder: VH, dataItem: T, type: Int, position: Int)

    /**
     * 设置点击事件
     */
    protected fun setClickListener(
        adapter: A, holder: VH, position: Int, type: Int
    ) {
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(adapter, holder, position, type)
        }
    }

    /**
     * 设置长按事件
     */
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
        //type使用position
        return position
    }

    /**
     * 获取item的类型
     */
    open fun getItemType(position: Int): Int {
        return position % typeQuota
    }

    //获取数据项
    open fun getItem(position: Int): T {
        return dataList[position]
    }

}