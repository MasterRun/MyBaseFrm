package com.jsongo.mybasefrm.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.core.helper.RecyclerViewAdapter
import com.jsongo.core.util.GlideUtil
import com.jsongo.core.util.PRE_ANDROID_ASSET
import com.jsongo.core.util.URL_REG
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.bean.QuickEntryItemBean
import com.vondear.rxtool.RxRegTool
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
            itemView.setOnClickListener {
                val entryTag = dataItem.entryTag
                if (RxRegTool.isMatch(URL_REG, entryTag)
                    || entryTag.trim().startsWith(PRE_ANDROID_ASSET)
                ) {
                    AJsWebPage.load(entryTag)
                } else {
                    try {
                        context?.startActivity(Intent(context, Class.forName(entryTag)))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llQuickEntry: LinearLayoutCompat = itemView.ll_quick_entry
        val ivQuickIcon: AppCompatImageView = itemView.iv_quick_icon
        val tvQuickTxt: AppCompatTextView = itemView.tv_quick_txt
    }
}