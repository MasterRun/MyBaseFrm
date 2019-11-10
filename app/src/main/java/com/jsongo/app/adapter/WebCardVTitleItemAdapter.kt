package com.jsongo.app.adapter

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.widget.WebLoaderCard
import com.jsongo.app.R
import com.jsongo.app.bean.WebCardItemBean
import com.jsongo.core.helper.RecyclerViewAdapter
import kotlinx.android.synthetic.main.item_webcard_v_title.view.*

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午2:36
 * @desc : web卡片适配器
 */
open class WebCardVTitleItemAdapter(
    context: Context?,
    val webViewHost: AjsWebViewHost,
    dataList: MutableList<WebCardItemBean>
) :
    RecyclerViewAdapter<WebCardVTitleItemAdapter, WebCardVTitleItemAdapter.ViewHolder, WebCardItemBean>(
        context, dataList
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        type: Int,
        position: Int
    ): ViewHolder {
        val view = inflater.inflate(R.layout.item_webcard_v_title, parent, false)
        val holder = ViewHolder(view)
        setClickListener(this, holder, position, type)
        setLongClickListener(this, holder, position, type)
        return holder
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        dataItem: WebCardItemBean,
        type: Int,
        position: Int
    ) {
        holder.apply {
            //加载页面
            webCard.url = dataItem.url
            webCard.ajsWebViewHost = webViewHost
            webCard.initLoad()
            //标题
            tvTitle.text = dataItem.title
            tvTitle.paint.isFakeBoldText = true
            //副标题
            if (TextUtils.isEmpty(dataItem.subTitle)) {
                tvSubTitle.visibility = View.GONE
            } else {
                tvSubTitle.text = dataItem.subTitle
                tvSubTitle.visibility = View.VISIBLE
            }
            //末尾标题
            if (TextUtils.isEmpty(dataItem.endTitle)) {
                tvEndTitle.visibility = View.GONE
            } else {
                tvEndTitle.text = dataItem.endTitle
                tvEndTitle.visibility = View.VISIBLE
            }
            //末尾副标题
            if (TextUtils.isEmpty(dataItem.endSubTitle)) {
                tvEndSubTitle.visibility = View.GONE
            } else {
                tvEndSubTitle.text = dataItem.endSubTitle
                tvEndSubTitle.visibility - View.VISIBLE
            }
        }
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llCardRoot: LinearLayoutCompat = itemView.ll_card_root
        val rlCardTitle: RelativeLayout = itemView.rl_card_title
        val tvTitle: AppCompatTextView = itemView.tv_title
        val tvSubTitle: AppCompatTextView = itemView.tv_subtitle
        val tvEndTitle: AppCompatTextView = itemView.tv_end_title
        val tvEndSubTitle: AppCompatTextView = itemView.tv_end_subtitle
        val webCard: WebLoaderCard = itemView.web_card
    }

}