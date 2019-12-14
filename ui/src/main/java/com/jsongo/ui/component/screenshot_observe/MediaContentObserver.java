package com.jsongo.ui.component.screenshot_observe;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * @author ： jsongo
 * @date ： 2019/12/9 10:19
 * @desc : 媒体内容观察者
 */
public class MediaContentObserver extends ContentObserver {

    /**
     * 读取媒体数据库时需要读取的列
     */
    private static final String[] MEDIA_PROJECTIONS = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATE_ADDED
    };

    /**
     * 判断是否是截图图片的文件名关键字
     */
    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap"
    };

    private final Uri contentUri;

    private final ContentResolver contentResolver;
    private final IScreenshotCallback screenshotCallback;

    public MediaContentObserver(Uri contentUri, Handler handler, ContentResolver contentResolver, IScreenshotCallback screenshotCallback) {
        super(handler);
        this.contentUri = contentUri;
        this.contentResolver = contentResolver;
        this.screenshotCallback = screenshotCallback;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        handleMediaContentChange(contentUri);
    }

    /**
     * 处理媒体内容更变
     *
     * @param contentUri
     */
    private void handleMediaContentChange(Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(contentUri, MEDIA_PROJECTIONS, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1");

            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }
            final int dateIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            final int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
            final int dateAddIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED);

            final String data = cursor.getString(dateIndex);
            final long dateTaken = cursor.getLong(dateTakenIndex);
            final long dateAdd = cursor.getLong(dateAddIndex);
            //处理获取到的第一行数据
            handleMediaRowData(data, dateTaken, dateAdd);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * 处理监听到的资源
     *
     * @param path
     * @param dateTaken
     * @param dateAdd
     */
    private void handleMediaRowData(String path, long dateTaken, long dateAdd) {
        long duration = 0;
        long step = 100;

        if (!isTimeValid(dateAdd)) {
            return;
        }

        //设置最大等待时间500ms （因为某些魅族手机保存有延迟）
        while (!checkScreenShot(path) && duration <= 500) {
            try {
                duration += step;
                Thread.sleep(step);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (checkScreenShot(path)) {
            if (screenshotCallback != null) {
                screenshotCallback.screenshotTaken(path);
            }
        } else {
            //没有截屏事件
        }
    }

    /**
     * 插入时间小于1s才有效
     * <p>
     * 个别手机会自己修改截图文件夹的文件， 截屏功能会误以为是用户在截屏操作，进行捕获。 加了一个时间判断
     *
     * @param dateAdd
     * @return
     */
    private boolean isTimeValid(long dateAdd) {
        return Math.abs(System.currentTimeMillis() / 1000 - dateAdd) < 1;
    }

    /**
     * 判断是否是截屏图片
     *
     * @param path
     * @return
     */
    private boolean checkScreenShot(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        path = path.toLowerCase();
        for (String keyword : KEYWORDS) {
            if (path.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

}
