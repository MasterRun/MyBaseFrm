package com.qmuiteam.qmui.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.collection.SimpleArrayMap;

import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.qqface.QMUIQQFaceCompiler;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.skin.defaultAttr.IQMUISkinDefaultAttrProvider;

/**
 * 这是一个对 {@link QMUITopBar} 的代理类，需要它的原因是：
 * 我们用 fitSystemWindows 实现沉浸式状态栏后，需要将 {@link QMUITopBar} 的背景衍生到状态栏后面，这个时候 fitSystemWindows 是通过
 * 更改 padding 实现的，而 {@link QMUITopBar} 是在高度固定的前提下做各种行为的，例如按钮的垂直居中，因此我们需要在外面包裹一层并消耗 padding
 * <p>
 * 这个类一般是配合 {@link QMUIWindowInsetLayout} 使用，并需要设置 fitSystemWindows 为 true
 * </p>
 *
 * @author ： jsongo
 * @date ： 19-10-19 下午8:33
 * @desc : copy from qmuitopbarlayout
 */

public class MyQMUITopBarLayout extends QMUIFrameLayout implements IQMUISkinDefaultAttrProvider {
    protected QMUITopBar mTopBar;
    private SimpleArrayMap<String, Integer> mDefaultSkinAttrs;

    public MyQMUITopBarLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public MyQMUITopBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.QMUITopBarStyle);
    }

    public MyQMUITopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDefaultSkinAttrs = new SimpleArrayMap<>(2);
        this.mDefaultSkinAttrs.put("bottomSeparator", R.attr.qmui_skin_support_topbar_separator_color);
        this.mDefaultSkinAttrs.put("background", R.attr.qmui_skin_support_topbar_bg);
        this.mTopBar = new QMUITopBar(context, attrs, defStyleAttr);
        this.mTopBar.setBackground((Drawable)null);
        this.mTopBar.updateBottomDivider(0, 0, 0, 0);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, this.mTopBar.getTopBarHeight());
        this.addView(this.mTopBar, lp);
    }

    public void setCenterView(View view) {
        this.mTopBar.setCenterView(view);
    }

    public QMUIQQFaceView setTitle(int resId) {
        return this.mTopBar.setTitle(resId);
    }

    public QMUIQQFaceView setTitle(String title) {
        return this.mTopBar.setTitle(title);
    }

    public void showTitlteView(boolean toShow) {
        this.mTopBar.showTitleView(toShow);
    }

    public void setSubTitle(int resId) {
        this.mTopBar.setSubTitle(resId);
    }

    public void setSubTitle(String subTitle) {
        this.mTopBar.setSubTitle(subTitle);
    }

    public void setTitleGravity(int gravity) {
        this.mTopBar.setTitleGravity(gravity);
    }

    public void addLeftView(View view, int viewId) {
        this.mTopBar.addLeftView(view, viewId);
    }

    public void addLeftView(View view, int viewId, android.widget.RelativeLayout.LayoutParams layoutParams) {
        this.mTopBar.addLeftView(view, viewId, layoutParams);
    }

    public void addRightView(View view, int viewId) {
        this.mTopBar.addRightView(view, viewId);
    }

    public void addRightView(View view, int viewId, android.widget.RelativeLayout.LayoutParams layoutParams) {
        this.mTopBar.addRightView(view, viewId, layoutParams);
    }

    public QMUIAlphaImageButton addRightImageButton(int drawableResId, int viewId) {
        return this.mTopBar.addRightImageButton(drawableResId, viewId);
    }

    public QMUIAlphaImageButton addLeftImageButton(int drawableResId, int viewId) {
        return this.mTopBar.addLeftImageButton(drawableResId, viewId);
    }

    public Button addLeftTextButton(int stringResId, int viewId) {
        return this.mTopBar.addLeftTextButton(stringResId, viewId);
    }

    public Button addLeftTextButton(String buttonText, int viewId) {
        return this.mTopBar.addLeftTextButton(buttonText, viewId);
    }

    public Button addRightTextButton(int stringResId, int viewId) {
        return this.mTopBar.addRightTextButton(stringResId, viewId);
    }

    public Button addRightTextButton(String buttonText, int viewId) {
        return this.mTopBar.addRightTextButton(buttonText, viewId);
    }

    public QMUIAlphaImageButton addLeftBackImageButton() {
        return this.mTopBar.addLeftBackImageButton();
    }

    public void removeAllLeftViews() {
        this.mTopBar.removeAllLeftViews();
    }

    public void removeAllRightViews() {
        this.mTopBar.removeAllRightViews();
    }

    public void removeCenterViewAndTitleView() {
        this.mTopBar.removeCenterViewAndTitleView();
    }

    public void setBackgroundAlpha(int alpha) {
        this.getBackground().setAlpha(alpha);
    }

    public int computeAndSetBackgroundAlpha(int currentOffset, int alphaBeginOffset, int alphaTargetOffset) {
        double alpha = (double)((float)(currentOffset - alphaBeginOffset) / (float)(alphaTargetOffset - alphaBeginOffset));
        alpha = Math.max(0.0D, Math.min(alpha, 1.0D));
        int alphaInt = (int)(alpha * 255.0D);
        this.setBackgroundAlpha(alphaInt);
        return alphaInt;
    }

    public void setDefaultSkinAttr(String name, int attr) {
        this.mDefaultSkinAttrs.put(name, attr);
    }

    @Override
    public SimpleArrayMap<String, Integer> getDefaultSkinAttrs() {
        return this.mDefaultSkinAttrs;
    }
}
