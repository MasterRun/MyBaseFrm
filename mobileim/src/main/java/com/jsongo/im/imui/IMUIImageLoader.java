package com.jsongo.im.imui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jsongo.core.util.GlideUtil;
import com.jsongo.core.util.LogcatUtil;
import com.jsongo.im.R;

import cn.jiguang.imui.commons.ImageLoader;

/**
 * @author jsongo
 * @date 2019/3/8 19:42
 */
public class IMUIImageLoader implements ImageLoader {

    private Context mContext;
    private RequestOptions options = RequestOptions.centerCropTransform();

    public IMUIImageLoader(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void loadAvatarImage(ImageView avatarImageView, String string) {
        if (string.contains("R.drawable")) {
            Integer resId = mContext.getResources().getIdentifier(string.replace("R.drawable.", ""),
                    "drawable", mContext.getPackageName());

            avatarImageView.setImageResource(resId);
        } else {
            GlideUtil.INSTANCE.load(mContext, string, options, avatarImageView);
        }
    }

    @Override
    public void loadImage(final ImageView imageView, String string) {

        final float density = mContext.getResources().getDisplayMetrics().density;
        final float MIN_WIDTH = 60 * density;
        final float MAX_WIDTH = 200 * density;
        final float MIN_HEIGHT = 60 * density;
        final float MAX_HEIGHT = 200 * density;
        // You can use other image load libraries.
        Glide.with(mContext)
                .asBitmap()
                .load(string)
                .apply(new RequestOptions().fitCenter().placeholder(R.drawable.aurora_picture_not_found))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int imageWidth = resource.getWidth();
                        int imageHeight = resource.getHeight();
                        LogcatUtil.i("Image width " + imageWidth + " height: " + imageHeight);

                        // 裁剪 bitmap
                        float width, height;
                        if (imageWidth > imageHeight) {
                            if (imageWidth > MAX_WIDTH) {
                                float temp = MAX_WIDTH / imageWidth * imageHeight;
                                height = temp > MIN_HEIGHT ? temp : MIN_HEIGHT;
                                width = MAX_WIDTH;
                            } else if (imageWidth < MIN_WIDTH) {
                                float temp = MIN_WIDTH / imageWidth * imageHeight;
                                height = temp < MAX_HEIGHT ? temp : MAX_HEIGHT;
                                width = MIN_WIDTH;
                            } else {
                                float ratio = imageWidth / imageHeight;
                                if (ratio > 3) {
                                    ratio = 3;
                                }
                                height = imageHeight * ratio;
                                width = imageWidth;
                            }
                        } else {
                            if (imageHeight > MAX_HEIGHT) {
                                float temp = MAX_HEIGHT / imageHeight * imageWidth;
                                width = temp > MIN_WIDTH ? temp : MIN_WIDTH;
                                height = MAX_HEIGHT;
                            } else if (imageHeight < MIN_HEIGHT) {
                                float temp = MIN_HEIGHT / imageHeight * imageWidth;
                                width = temp < MAX_WIDTH ? temp : MAX_WIDTH;
                                height = MIN_HEIGHT;
                            } else {
                                float ratio = imageHeight / imageWidth;
                                if (ratio > 3) {
                                    ratio = 3;
                                }
                                width = imageWidth * ratio;
                                height = imageHeight;
                            }
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        params.width = (int) width;
                        params.height = (int) height;
                        imageView.setLayoutParams(params);
                        Matrix matrix = new Matrix();
                        float scaleWidth = width / imageWidth;
                        float scaleHeight = height / imageHeight;
                        matrix.postScale(scaleWidth, scaleHeight);
                        imageView.setImageBitmap(Bitmap.createBitmap(resource, 0, 0, imageWidth, imageHeight, matrix, true));
                    }
                });
    }

    @Override
    public void loadVideo(ImageView imageCover, String uri) {
        long interval = 5000 * 1000;
        Glide.with(mContext)
                .asBitmap()
                .load(uri)
                // Resize image view by change override size.
                .apply(new RequestOptions().frame(interval).override(200, 400))
                .into(imageCover);

    }

}
