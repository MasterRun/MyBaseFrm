package com.jsongo.mybasefrm.ui.main.conv

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.mvvm.stateful.StatefulFragment
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.main.conv.adapter.ConvItemAdapter
import com.jsongo.ui.util.addStatusBarHeightPadding
import com.qmuiteam.qmui.kotlin.wrapContent
import kotlinx.android.synthetic.main.fragment_conv_list.*

/**
 * @author ： jsongo
 * @date ： 2019/12/29 13:33
 * @desc : 消息模块 会话列表fragment
 */
@Page(R.layout.fragment_conv_list)
class ConvListFragment : StatefulFragment() {

    var convItemAdapter: ConvItemAdapter? = null

    var rvDatas: ObservableArrayList<Map<String, Any?>>? = null

    override fun initViewModel() {
    }

    override fun initView() {

        rlLayoutRoot.setBackgroundColor(Color.WHITE)

        topbar.addStatusBarHeightPadding()
        topbar.setBackgroundColor(Color.WHITE)
        topbar.setTitle("消息").setTextColor(Color.BLACK)
        topbar.backImageButton.visibility = View.GONE
//        onPageLoaded()
    }

    override fun observeLiveData() {
    }

    override fun bindData() {
        super.bindData()
        val context = context
        if (context != null) {
            rvDatas = ObservableArrayList<Map<String, Any?>>().apply {
                add(mapOf(Pair("name", "mockname1")))
                add(mapOf(Pair("name", "mockname2")))
                add(mapOf(Pair("name", "mockname3")))
            }
            convItemAdapter = ConvItemAdapter(context, rvDatas!!)
            rvDatas = convItemAdapter?.dataList as ObservableArrayList<Map<String, Any?>>?
            val rv_convs = rv_convs
            rv_convs?.layoutManager = LinearLayoutManager(context)
            rv_convs?.adapter = convItemAdapter
            onPageLoaded()
        } else {
            onPageError("context is null")
        }

        val tv = Button(context)
        tv.text = "更换数据"
        tv.setOnClickListener {
            rvDatas?.set(1, mapOf(Pair("name", "this is name2")))
        }
        flMainContainer.addView(tv, ViewGroup.LayoutParams(wrapContent, wrapContent))
    }
}