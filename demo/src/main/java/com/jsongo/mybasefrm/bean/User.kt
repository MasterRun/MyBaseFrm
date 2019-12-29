package com.jsongo.mybasefrm.bean

/**
 * @author ： jsongo
 * @date ： 2019/12/1 23:24
 * @desc :
 */
data class User(
    var account: String,
    var username: String,
    var gender: String,
    var photo_url: String
) {
    var user_guid: String? = null
    @Transient
    var id: Long? = null
    @Transient
    var password: String? = null
}