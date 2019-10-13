package com.jsongo.ui.component.imagepreview;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jsongo.core.util.ActivityCollector;
import com.jsongo.ui.R;
import com.previewlibrary.GPreviewActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

/**
 * @author jsongo
 * @date 2018/12/9 22:24
 */
public class ImagePreviewActivity extends GPreviewActivity {
    boolean requestFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.INSTANCE.addActivity(this);
        QMUIStatusBarHelper.translucent(this);
    }

    /***
     * 重写该方法
     * 使用你的自定义布局
     **/
    @Override
    public int setContentLayout() {
        if (!requestFullScreen) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestFullScreen = true;
        }
        return R.layout.activity_image_preview_photo;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void transformOut() {
        finish();
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.INSTANCE.removeActivity(this);
        super.onDestroy();
    }
}
