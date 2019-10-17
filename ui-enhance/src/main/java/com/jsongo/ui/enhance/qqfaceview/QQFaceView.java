package com.jsongo.ui.enhance.qqfaceview;

import android.content.Context;
import android.util.AttributeSet;

import com.qmuiteam.qmui.qqface.QMUIQQFaceCompiler;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

public class QQFaceView extends QMUIQQFaceView {
    public QQFaceView(Context context) {
        this(context, null);
    }

    public QQFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCompiler(QMUIQQFaceCompiler.getInstance(QQFaceManager.getInstance()));
    }
}
