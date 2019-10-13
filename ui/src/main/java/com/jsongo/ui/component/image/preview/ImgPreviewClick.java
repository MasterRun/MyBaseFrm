package com.jsongo.ui.component.image.preview;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;

import com.previewlibrary.GPreviewBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jsongo
 * @date 2018/12/9 22:39
 */ //预览图片
public class ImgPreviewClick implements View.OnClickListener {

    private Activity activity;
    private int index;
    private List<String> urlList;

    public ImgPreviewClick(Activity activity, int index, List<String> urlList) {
        this.activity = activity;
        this.index = index;
        this.urlList = urlList;
    }

    @Override
    public void onClick(View v) {
        start();
    }

    public void start() {
        //组织数据
        ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>(); // 这个最好定义成成员变量
        ThumbViewInfo item;
        mThumbViewInfoList.clear();
        for (int i = 0; i < urlList.size(); i++) {
            Rect bounds = new Rect();
            //new ThumbViewInfo(图片地址);
            item = new ThumbViewInfo(urlList.get(i));
            item.setBounds(bounds);
            mThumbViewInfoList.add(item);
        }

//打开预览界面
        GPreviewBuilder.from(activity)
                .to(ImagePreviewActivity.class)
                .setData(mThumbViewInfoList)
                .setCurrentIndex(index)
                .setSingleFling(true)
                .setType(GPreviewBuilder.IndicatorType.Number)
                .setDrag(false)
                // 小圆点
//  .setType(GPreviewBuilder.IndicatorType.Dot)
                .start();//启动
    }
}
