package com.jsongo.core_mini.base_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jsongo.core_mini.widget.ILoadingDialog
import com.jsongo.core_mini.widget.IStatusView
import com.jsongo.core_mini.widget.ITopbar

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc fragment 父类
 */
abstract class BaseFragment : Fragment(), IPage {

    override var topbar: ITopbar? = null
    override var loadingDialog: ILoadingDialog? = null
    override var statusView: IStatusView? = null

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