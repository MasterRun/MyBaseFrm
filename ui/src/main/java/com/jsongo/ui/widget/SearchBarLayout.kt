package com.jsongo.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.jsongo.core.util.KeyboardUtil
import com.jsongo.ui.R
import kotlinx.android.synthetic.main.layout_search_bar.view.*

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午8:20
 * @desc : 标准搜索框
 */
class SearchBarLayout(
    mContext: Context,
    attrs: AttributeSet? = null
) : LinearLayoutCompat(mContext, attrs) {

    constructor(mContext: Context) : this(mContext, null)

    val onSearchListener: OnSearchListener? = null

    val etSearch: AppCompatEditText
    val ivSearch: AppCompatImageView
    val ivClear: AppCompatImageView

    init {
        var showClear = true
        var hintText = ""
        if (attrs != null) {
            val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.SearchBarLayout)
            showClear =
                styledAttrs.getBoolean(R.styleable.SearchBarLayout_sb_showClear, true)
            hintText = styledAttrs.getString(R.styleable.SearchBarLayout_sb_hintText) ?: ""
            styledAttrs.recycle()
        }

        val view = LayoutInflater.from(context).inflate(R.layout.layout_search_bar, this, true)
        etSearch = view.et_search
        ivSearch = view.iv_search
        ivClear = view.iv_clear

        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtil.hideSoftInput(context, etSearch)
                onSearchListener?.onSearch(etSearch.text.toString())
            }
            true
        }
        if (showClear) {
            ivClear.setOnClickListener {
                etSearch.setText("")
            }
        } else {
            ivClear.visibility = View.GONE
        }

        etSearch.hint = hintText
    }

    interface OnSearchListener {
        fun onSearch(keyword: String)
    }

}