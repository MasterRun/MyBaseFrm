package com.jsongo.ui.component.screenshot_observe;

import android.content.ContentResolver;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 * @author ： jsongo
 * @date ： 2019/12/9 11:03
 * @desc : 截屏监听工具,同时使用contentobserver和fileobserver
 */
public class ScreenshotObserveUtil {

    private static String strOfScreenshotObserveHost = null;

    public static void init(@NonNull Object obj, @NonNull ContentResolver resolver, @NonNull IScreenshotCallback callback) {
        strOfScreenshotObserveHost = obj.toString();
        FileObserverManager.init(callback);
        ContentObserverManager.init(resolver, callback);
    }

    /**
     * 监听
     */
    public static void register() {
        FileObserverManager.register();
        ContentObserverManager.register();
    }

    /**
     * 停止监听
     */
    public static void unregister(@NonNull Object obj) {
        if (TextUtils.equals(strOfScreenshotObserveHost, obj.toString())) {
            FileObserverManager.unregister();
            ContentObserverManager.unregister();
            strOfScreenshotObserveHost = null;
        }
    }


    static class FileObserverManager {

        private static FileObserver fileObserver;

        public static void init(@NonNull IScreenshotCallback callback) {
            fileObserver = ScreenshotFileObserver.initFileObserver(callback);
        }

        /**
         * 监听
         */
        public static void register() {
            fileObserver.startWatching();
        }

        /**
         * 停止监听
         */
        public static void unregister() {
            if (fileObserver != null) {
                fileObserver.stopWatching();
            }
        }

    }

    static class ContentObserverManager {

        private static ContentResolver contentResolver;
        private static IScreenshotCallback screenshotCallback;
        private static HandlerThread screenshotHandlerThread;
        private static Handler handler;
        private static MediaContentObserver internalObserver;
        private static MediaContentObserver externalObserver;

        /**
         * 初始化
         *
         * @param resolver
         * @param callback
         */
        public static void init(@NonNull ContentResolver resolver, @NonNull IScreenshotCallback callback) {
            contentResolver = resolver;
            screenshotCallback = callback;

            screenshotHandlerThread = new HandlerThread("Screenshot_Observer");
            screenshotHandlerThread.start();
            handler = new Handler(screenshotHandlerThread.getLooper());

            internalObserver = new MediaContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, handler, contentResolver, screenshotCallback);
            externalObserver = new MediaContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, handler, contentResolver, screenshotCallback);
        }


        /**
         * 注册
         */
        public static void register() {
            contentResolver.registerContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, false, internalObserver);
            contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, externalObserver);
        }

        /**
         * 解除注册
         */
        public static void unregister() {
            if (contentResolver != null) {
                if (internalObserver != null) {
                    contentResolver.unregisterContentObserver(internalObserver);
                }
                if (externalObserver != null) {
                    contentResolver.unregisterContentObserver(externalObserver);
                }
            }
            contentResolver = null;
            screenshotCallback = null;
            screenshotHandlerThread = null;
            handler = null;
            internalObserver = null;
            externalObserver = null;
        }
    }
}
