package com.jsongo.im.bean

import android.os.Parcel
import android.os.Parcelable
import cn.jiguang.imui.commons.models.IUser

/**
 * @author jsongo
 * @date 2019/3/8 19:52
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class ChatUser
constructor(
    private var id: String,
    private var displayName: String,
    private var avatarFilePath: String
) : IUser, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun getId(): String {
        return id
    }

    override fun getDisplayName(): String {
        return displayName
    }

    override fun getAvatarFilePath(): String {
        return avatarFilePath
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(displayName)
        parcel.writeString(avatarFilePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatUser> {
        override fun createFromParcel(parcel: Parcel): ChatUser {
            return ChatUser(parcel)
        }

        override fun newArray(size: Int): Array<ChatUser?> {
            return arrayOfNulls(size)
        }
    }

}
