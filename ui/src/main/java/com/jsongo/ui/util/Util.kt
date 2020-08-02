package com.jsongo.ui.util

import android.view.View
import android.view.ViewGroup
import com.huantansheng.easyphotos.models.album.entity.Photo

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午9:03
 * @desc :
 */

/**
 * 根据类名查找View及子View中的类型，只返回一个
 *
 * @param view
 * @param clazz
 * @return
 */
fun View?.findViewByClass(clazz: Class<out View>): View? {
    if (this == null) {
        return null
    }
    if (clazz.isInstance(this)) {
        return this
    }
    if (this is ViewGroup) {
        val viewGroup = this
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val child = viewGroup.getChildAt(i)
            if (clazz.isInstance(child)) {
                return child
            } else if (child is ViewGroup) {
                return child.findViewByClass(clazz)
            }
        }
    }
    return null
}

object Util {
    fun getResultPhotosPaths(photos: List<Photo>): List<String> {
        val resultPaths = java.util.ArrayList<String>()
        for (photo in photos) {
            resultPaths.add(photo.path)
        }
        return resultPaths
    }
}