package com.jsongo.mybasefrm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.tencent.smtt.sdk.QbSdk;
import com.vondear.rxtool.RxTool;

import org.jetbrains.annotations.Contract;

/**
 * @author jsongo
 * @date 2018/9/3 18:40
 */
public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (isDebug()) {
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
        QbSdk.initX5Environment(this, null);
        RxTool.init(this);
    }

    @Contract(pure = true)
    public static boolean isDebug() {
        return false;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(context);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //内存低是清理管理的缓存
        Glide.get(this).clearMemory();
    }
}
