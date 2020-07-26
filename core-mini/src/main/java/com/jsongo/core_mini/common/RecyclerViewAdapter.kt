package com.jsongo.core_mini.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback
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

    val listChangedCallback = ListChangedCallback()

    /**
     * 数据集
     */
    open var dataList: MutableList<T> = ObservableArrayList()
        set(value) {
            val fieldIterator = field.iterator()
            val valueIterator = value.iterator()
            var i = 0
            //赋值替换
            while (fieldIterator.hasNext() && valueIterator.hasNext()) {
                fieldIterator.next()
                field[i] = valueIterator.next()
                i++
            }
            //如果有多余的，直接替换
            while (fieldIterator.hasNext()) {
                fieldIterator.next()
                fieldIterator.remove()
            }
            //如果还有数据，放入集合
            while (valueIterator.hasNext()) {
                field.add(valueIterator.next())
            }
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
     * 如果是ObservableArrayList，添加监听
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (dataList is ObservableList<T>) {
            (dataList as ObservableList<T>).addOnListChangedCallback(
                listChangedCallback
            )
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        if (dataList is ObservableList<T>) {
            (dataList as ObservableList<T>).removeOnListChangedCallback(
                listChangedCallback
            )
        }
    }

    //region 处理数据集变化
    protected open fun onChanged(newItems: ObservableList<T>) {
        resetItems(newItems)
        notifyDataSetChanged()
    }

    protected open fun onItemRangeChanged(
        newItems: ObservableList<T>,
        positionStart: Int,
        itemCount: Int
    ) {
        resetItems(newItems)
//        notifyDataSetChanged()
        notifyItemRangeChanged(positionStart, itemCount)
    }

    protected open fun onItemRangeInserted(
        newItems: ObservableList<T>,
        positionStart: Int,
        itemCount: Int
    ) {
        resetItems(newItems)
//        notifyDataSetChanged()
        notifyItemRangeInserted(positionStart, itemCount)
    }

    protected open fun onItemRangeMoved(newItems: ObservableList<T>) {
        resetItems(newItems)
        notifyDataSetChanged()
    }

    protected open fun onItemRangeRemoved(
        newItems: ObservableList<T>,
        positionStart: Int,
        itemCount: Int
    ) {
        resetItems(newItems)
//        notifyDataSetChanged()
        notifyItemRangeRemoved(positionStart, itemCount)
    }

    protected open fun resetItems(newItems: ObservableList<T>) {
//        this.dataList = newItems
    }
    //endregion

    /**
     * 设置点击事件
     */
    protected fun setClickListener(
        adapter: A, holder: VH, position: Int, type: Int
    ) {
        holder.itemView.setOnClickListener {
            itemClickListener?.onRvItemClick(adapter, holder, position, type)
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
                itemLongClickListener?.onRvItemLongClick(adapter, holder, position, type)
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

    inner class ListChangedCallback : OnListChangedCallback<ObservableList<T>>() {
        override fun onChanged(newItems: ObservableList<T>) {
            this@RecyclerViewAdapter.onChanged(newItems)
        }

        override fun onItemRangeChanged(
            newItems: ObservableList<T>,
            i: Int,
            i1: Int
        ) {
            this@RecyclerViewAdapter.onItemRangeChanged(newItems, i, i1)
        }

        override fun onItemRangeInserted(
            newItems: ObservableList<T>,
            i: Int,
            i1: Int
        ) {
            this@RecyclerViewAdapter.onItemRangeInserted(newItems, i, i1)
        }

        override fun onItemRangeMoved(
            newItems: ObservableList<T>,
            i: Int,
            i1: Int,
            i2: Int
        ) {
            this@RecyclerViewAdapter.onItemRangeMoved(newItems)
        }

        override fun onItemRangeRemoved(
            sender: ObservableList<T>,
            positionStart: Int,
            itemCount: Int
        ) {
            this@RecyclerViewAdapter.onItemRangeRemoved(sender, positionStart, itemCount)
        }
    }
}