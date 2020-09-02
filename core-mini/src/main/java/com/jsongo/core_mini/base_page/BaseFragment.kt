package com.jsongo.core_mini.base_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc fragment 父类
 */
@Keep
abstract class BaseFragment : Fragment(), IPage {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIPage(context!!)
    }

    override fun onDestroyIPage() {
    }

    override fun onDestroyView() {
        onDestroyIPage()
        super.onDestroyView()
    }

}