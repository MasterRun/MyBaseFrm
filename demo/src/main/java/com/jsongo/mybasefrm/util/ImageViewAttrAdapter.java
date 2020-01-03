package com.jsongo.mybasefrm.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.request.RequestOptions;
import com.jsongo.core.util.GlideUtil;
import com.jsongo.ui.util.UtilKt;

/**
 * @author ： jsongo
 * @date ： 2020/1/3 10:57
 * @desc :
 */
public class ImageViewAttrAdapter {
    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter({"iv_imageUrl", "iv_placeHolder", "iv_error"})
    public static void loadImage(View view, String url, int holderDrawable, int errorDrawable) {
        final View v = UtilKt.findViewByClass(view, ImageView.class);
        if (v instanceof ImageView) {
            final RequestOptions options = RequestOptions.centerCropTransform().placeholder(holderDrawable).error(errorDrawable);
            GlideUtil.INSTANCE.load(view.getContext(), url, options, ((ImageView) v));
        }
    }
}
