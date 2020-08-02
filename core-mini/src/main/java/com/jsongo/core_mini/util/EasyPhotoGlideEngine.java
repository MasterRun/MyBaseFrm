package com.jsongo.core_mini.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.engine.ImageEngine;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Glide4.x的加载图片引擎实现,单例模式
 * Glide4.x的缓存机制更加智能，已经达到无需配置的境界。如果使用Glide3.x，需要考虑缓存机制。
 */

public class EasyPhotoGlideEngine implements ImageEngine {
    //单例
    private static EasyPhotoGlideEngine instance = null;

    //单例模式，私有构造方法
    private EasyPhotoGlideEngine() {
    }

    //获取单例
    public static EasyPhotoGlideEngine getInstance() {
        if (null == instance) {
            synchronized (EasyPhotoGlideEngine.class) {
                if (null == instance) {
                    instance = new EasyPhotoGlideEngine();
                }
            }
        }
        return instance;
    }

    /**
     * 加载图片到ImageView
     *
     * @param context   上下文
     * @param uri       图片uri
     * @param imageView 加载到的ImageView
     */

    @Override
    public void loadPhoto(@NonNull Context context, @NonNull Uri uri, @NonNull ImageView imageView) {
        Glide.with(context).load(uri).transition(withCrossFade()).into(imageView);
    }

    /**
     * 加载gif动图图片到ImageView，gif动图不动
     *
     * @param context   上下文
     * @param gifUri    gif动图uri
     * @param imageView 加载到的ImageView
     *                  <p>
     *                  备注：不支持动图显示的情况下可以不写
     */

    @Override
    public void loadGifAsBitmap(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {
        Glide.with(context).asBitmap().load(gifUri).into(imageView);
    }

    /**
     * 加载gif动图到ImageView，gif动图动
     *
     * @param context   上下文
     * @param gifUri    gif动图uri
     * @param imageView 加载动图的ImageView
     *                  <p>
     *                  备注：不支持动图显示的情况下可以不写
     */

    @Override
    public void loadGif(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {
        Glide.with(context).asGif().load(gifUri).transition(withCrossFade()).into(imageView);
    }


    /**
     * 获取图片加载框架中的缓存Bitmap
     *
     * @param context 上下文
     * @param uri     图片uri
     * @param width   图片宽度
     * @param height  图片高度
     * @return Bitmap
     * @throws Exception 异常直接抛出，EasyPhotos内部处理
     */

    @Override
    public Bitmap getCacheBitmap(@NonNull Context context, @NonNull Uri uri, int width, int height) throws Exception {
        return Glide.with(context).asBitmap().load(uri).submit(width, height).get();
    }
}