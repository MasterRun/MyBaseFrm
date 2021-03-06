package com.jsongo.core.arch.mvvm

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
     * dataBinding
     */
    fun bindData() {}

    /**
     * 观察LiveData
     */
    fun observeLiveData()
}