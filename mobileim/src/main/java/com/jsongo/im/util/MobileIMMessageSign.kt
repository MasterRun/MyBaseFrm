package com.jsongo.im.util

/**
 * @author ： jsongo
 * @date ： 2019/11/13 16:35
 * @desc : 常量
 * 约定：即时通讯的rxbus消息code标识占用范围为 15001-15999
 */
object MobileIMMessageSign {

    /**
     * 是否是即时通讯消息
     */
    fun isMobileIMMessage(code: Int): Boolean =
        code == IM_LOGIN_EVENT || code == IM_RECEIVE_MESSAGE || code == IM_RECEIVE_SERVER_DATA

    //登录事件标识
    const val IM_LOGIN_EVENT = 15001
    //登录成功
    const val LOGIN_EVENT_SUCCESS = 1
    //登录失败
    const val LOGIN_EVENT_FAIL = 2
    //掉线
    const val LOGIN_EVENT_LINK_FAIL = 3

    //消息标识
    const val IM_RECEIVE_MESSAGE = 15002

    //服务端数据标识
    const val IM_RECEIVE_SERVER_DATA = 15003

}