package com.jsongo.mybasefrm.bean

import android.os.Parcel
import android.os.Parcelable
import com.jsongo.core.util.gson
import com.jsongo.core.util.strHashMapType
import java.io.Serializable

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午3:58
 * @desc : web 卡片实体类
 */
data class WebCardItemBean(
    //加载的url
    val url: String,
    //标题
    val title: String = "",
    //副标题
    val subTitle: String = "",
    //末标题
    val endTitle: String = "",
    //末尾副标题
    val endSubTitle: String = "",
    //所有参数
    val params: HashMap<String, String> = HashMap()
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        gson.fromJson<HashMap<String, String>>(parcel.readString(), strHashMapType)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(title)
        parcel.writeString(subTitle)
        parcel.writeString(endTitle)
        parcel.writeString(endSubTitle)
        parcel.writeString(gson.toJson(params))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebCardItemBean> {
        override fun createFromParcel(parcel: Parcel): WebCardItemBean {
            return WebCardItemBean(parcel)
        }

        override fun newArray(size: Int): Array<WebCardItemBean?> {
            return arrayOfNulls(size)
        }
    }
}