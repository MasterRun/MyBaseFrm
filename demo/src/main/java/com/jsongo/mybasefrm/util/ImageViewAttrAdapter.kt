package com.jsongo.mybasefrm.util

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.jsongo.core.util.GlideUtil
import com.jsongo.ui.util.findViewByClass

/**
 * @author ： jsongo
 * @date ： 2020/1/3 10:57
 * @desc :
 */
object ImageViewAttrAdapter {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, bitmap: Bitmap?) {
        view.setImageBitmap(bitmap)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @JvmStatic
    @BindingAdapter("iv_imageUrl", "iv_placeHolder", "iv_error")
    fun loadImage(
        view: View,
        url: String?,
        holderDrawable: Int? = 0,
        errorDrawable: Int? = 0
    ) {
        val v = view.findViewByClass(ImageView::class.java)
        if (v is ImageView) {
            val options =
                RequestOptions.centerCropTransform()
                    .apply {
                        if (holderDrawable != null && holderDrawable != 0) {
                            placeholder(holderDrawable)
                        }
                        if (errorDrawable != null && errorDrawable != 0) {
                            error(errorDrawable)
                        }
                    }
            GlideUtil.load(view.context, url, options, v)
        }
    }

    @JvmStatic
    @BindingAdapter("iv_imageUrl")
    fun loadImage(
        view: View,
        url: String?
    ) {
        loadImage(
            view, url, null, null
        )
    }
}