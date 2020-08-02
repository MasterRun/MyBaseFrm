package com.jsongo.core_mini.widget

/**
 * @author ： jsongo
 * @date ： 2020/7/26 20:05
 * @desc :
 */
interface ILoadingDialog {
    var cancelable: Boolean
    fun show()
    fun show(msfg: String)
    fun dismiss()
}