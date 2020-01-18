package com.jsongo.mybasefrm.ui.main.conv

import android.graphics.Color
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.mvvm.stateful.StatefulFragment
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.main.conv.adapter.ConvItemAdapter
import com.jsongo.ui.util.addStatusBarHeightPadding
import kotlinx.android.synthetic.main.fragment_conv_list.*

/**
 * @author ： jsongo
 * @date ： 2019/12/29 13:33
 * @desc : 消息模块 会话列表fragment
 */
@Page(R.layout.fragment_conv_list)
class ConvListFragment : StatefulFragment() {

    var convItemAdapter: ConvItemAdapter? = null

    var rvDatas: ObservableArrayList<MutableMap<String, Any?>>? = null

    lateinit var convListViewModel: ConvListViewModel

    override fun initViewModel() {
        convListViewModel = ConvListViewModel()
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
            rvDatas = ObservableArrayList<MutableMap<String, Any?>>().apply {
                add(
                    mutableMapOf(
                        Pair("name", "mockname1"), Pair("time", "12:30"), Pair("messageCount", "12")/*,
                        Pair(
                            "avatar",
                            "http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20180806/9425645d47cd4f8e9c11fc6a9959340a.jpeg"
                        )*/
                    )
                )
                add(
                    mutableMapOf(
                        Pair("name", "mockname2"), Pair("time", "12:12"), Pair("messageCount", "3"),
                        Pair(
                            "avatar",
                            "http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20180806/9425645d47cd4f8e9c11fc6a9959340a.jpeg"
                        )
                    )
                )
                add(
                    mutableMapOf(
                        Pair("name", "mockname3"), Pair("time", "08:14"),
                        Pair(
                            "avatar",
                            "http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20180806/9425645d47cd4f8e9c11fc6a9959340a.jpeg"
                        )
                    )
                )
            }
            convItemAdapter = ConvItemAdapter(context, rvDatas!!)
            rvDatas = convItemAdapter?.dataList as ObservableArrayList<MutableMap<String, Any?>>?
            val rv_convs = rv_convs
            rv_convs?.layoutManager = LinearLayoutManager(context)
            rv_convs?.adapter = convItemAdapter
            onPageLoaded()
        } else {
            onPageError("context is null")
        }

    }
}