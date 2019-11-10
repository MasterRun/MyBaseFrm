package com.jsongo.app.bean

import android.os.Parcel
import android.os.Parcelable
import com.jsongo.core.util.gson
import com.jsongo.core.util.strHashMapType
import java.io.Serializable

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午6:45
 * @desc : 快捷入口实体类
 */
data class QuickEntryItemBean(
    //图标url
    val iconUrl: String = "",
    //图标资源id
    val iconRes: Int = -1,
    //文字
    val text: String = "",
    //入口标识   url/原生类名
    val entryTag: String = "",
    //所有参数
    val params: HashMap<String, String> = HashMap()
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        gson.fromJson<HashMap<String, String>>(parcel.readString(), strHashMapType)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(iconUrl)
        parcel.writeInt(iconRes)
        parcel.writeString(text)
        parcel.writeString(entryTag)
        parcel.writeString(gson.toJson(params))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuickEntryItemBean> {
        override fun createFromParcel(parcel: Parcel): QuickEntryItemBean {
            return QuickEntryItemBean(parcel)
        }

        override fun newArray(size: Int): Array<QuickEntryItemBean?> {
            return arrayOfNulls(size)
        }
    }

}