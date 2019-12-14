package com.jsongo.ui.component.screenshot_observe;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.FileObserver;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

/**
 * @author ： jsongo
 * @date ： 2019/12/9 14:38
 * @desc : 使用文件观察者监听
 */
public class ScreenshotFileObserver {

    /**
     * 适配各手机的截屏文件夹路径
     */
    private static String SNAP_SHOT_FOLDER_PATH;

    /**
     * 获取截图路径
     *
     * @return
     */
    public static synchronized String getSnapShotFolderPath() {
        if (TextUtils.isEmpty(SNAP_SHOT_FOLDER_PATH)) {
            //使用pictures下的screenshots,如果不存在，使用camera下的screenshots
            SNAP_SHOT_FOLDER_PATH = Environment.getExternalStorageDirectory()
                    + File.separator + Environment.DIRECTORY_PICTURES
                    + File.separator + "Screenshots" + File.separator;
            if (!new File(SNAP_SHOT_FOLDER_PATH).exists()) {
                SNAP_SHOT_FOLDER_PATH = Environment.getExternalStorageDirectory()
                        + File.separator + Environment.DIRECTORY_DCIM
                        + File.separator + "Screenshots" + File.separator;
            }
        }
        return SNAP_SHOT_FOLDER_PATH;
    }

    /**
     * 上次的截图文件路径，用于去重
     */
    private static String lastShotPath = "";

    /**
     * 延迟获取图片的尝试次数
     */
    private static int MAX_TRYS = 20;

    public static FileObserver initFileObserver(@NonNull IScreenshotCallback screenshotCallback) {
        return new FileObserver(getSnapShotFolderPath(), FileObserver.ALL_EVENTS) {
            @Override
            public void onEvent(int event, @Nullable String path) {
                if (!TextUtils.isEmpty(path) && event == FileObserver.CREATE && (!TextUtils.equals(path, lastShotPath))) {
                    lastShotPath = path;
                    String filePath = SNAP_SHOT_FOLDER_PATH + path;
                    int tryTime = 0;
                    while (true) {
                        try {
                            //收到事件后需要延迟获取
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            BitmapFactory.decodeFile(filePath);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            tryTime++;
                            if (tryTime >= MAX_TRYS) {
                                return;
                            }
                        }
                    }
                    screenshotCallback.screenshotTaken(filePath);
                }
            }
        };
    }
}
