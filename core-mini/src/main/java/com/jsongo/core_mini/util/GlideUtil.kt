package com.jsongo.core_mini.util

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.safframework.log.L
import java.io.File

/**
 * @author  jsongo
 * @date 2019/3/8 11:29
 */
object GlideUtil {
    const val TAG: String = "GlideUtil"

    fun load(context: Context?, url: String?, imageView: ImageView) {
        if (context != null) {
            Glide.with(context).load(url).into(imageView)
        } else {
            L.e(TAG, "Picture loading failed,context is null")
        }
    }

    fun load(context: Context?, url: String?, options: RequestOptions?, imageView: ImageView) {
        if (context != null) {
            var load = Glide.with(context).load(url)
            if (options != null) {
                load = load.apply(options)
            }
            load.into(imageView)
        } else {
            L.e(TAG, "Picture loading failed,activity is Destroyed")
        }
    }

    fun load(activity: Activity?, url: String, imageView: ImageView) {
        load(activity, url, null, imageView)
    }

    fun load(activity: Activity?, url: String?, options: RequestOptions?, imageView: ImageView) {
        if (activity != null && !activity.isDestroyed) {
            var load = Glide.with(activity).load(url)
            if (options != null) {
                load = load.apply(options)
            }
            load.into(imageView)
        } else {
            L.e(TAG, "Picture loading failed,activity is Destroyed")
        }
    }

    fun load(fragment: Fragment?, url: String?, imageView: ImageView) {
        val activity = fragment?.activity
        if (fragment != null && activity != null && !(activity.isDestroyed)) {
            Glide.with(fragment).load(url).into(imageView)
        } else {
            L.e(TAG, "Picture loading failed,activity is Destroyed")
        }
    }
    fun load(activity: Activity?, file: File?, options: RequestOptions?, imageView: ImageView) {
        if (file != null && file.exists() && file.isFile)
            if (activity != null && !activity.isDestroyed) {
                var load = Glide.with(activity).load(file)
                if (options != null) {
                    load = load.apply(options)
                }
                load.into(imageView)
            } else {
                L.e(TAG, "Picture loading failed,activity is Destroyed")
            }
    }
}