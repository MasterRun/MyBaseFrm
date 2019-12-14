package com.jsongo.ui.component.screenshot_observe;

import android.text.TextUtils;

/**
 * @author ： jsongo
 * @date ： 2019/12/9 10:16
 * @desc : 截屏监听回调
 */
public interface IScreenshotCallback {
    void screenshotTaken(String path);

    /**
     * 代理类
     */
    public abstract class ScreenshotCallbackProxy implements IScreenshotCallback {

        private static String lastShotPath = "";

        @Override
        public void screenshotTaken(String path) {
            if (TextUtils.equals(path, lastShotPath)) {
                return;
            }
            lastShotPath = path;
            onGetScreenshot(path);
        }

        public abstract void onGetScreenshot(String path);
    }
}
