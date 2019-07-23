package com.jsongo.core.mvp.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.*

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc fragment 父类
 */
abstract class BaseFragment : Fragment() {
    /**
     * 布局资源id
     */
    abstract var contentLayoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val contentView = inflater.inflate(contentLayoutId, container, false)
        return contentView;
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }
}