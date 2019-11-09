package com.jsongo.core.base.mvvm

/**
 * @author ： jsongo
 * @date ： 2019/11/9 23:55
 * @desc : MVVM的view接口
 */
interface IMvvmView {

    /**
     * 初始化ViewModel
     */
    fun initViewModel()

    /**
     * 初始化视图
     */
    fun initView()

    /**
     * 观察LiveData
     */
    fun observeLiveData()
}