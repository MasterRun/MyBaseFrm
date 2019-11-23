package com.jsongo.core.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.core.content.ContextCompat;

import com.jsongo.core.R;

/**
 * @author jsongo
 * @date 2019/5/5 13:55
 * @desc
 */
public class SlidingLayout extends FrameLayout {
    private static final int SHADOW_WIDTH = 5;
    private Activity mActivity;
    private Scroller mScroller;
    private Drawable mLeftShadow;
    private int mShadowWidth;
    private int mInterceptDownX;
    private int mLastInterceptX;
    private int mLastInterceptY;
    private int mTouchDownX;
    private int mLastTouchX;
    private int mLastTouchY;
    private boolean isConsumed;
    private boolean enableSlidClose;

    public void setEnableSlidClose(boolean enableSlidClose) {
        this.enableSlidClose = enableSlidClose;
        if (!enableSlidClose) {
            this.scrollBack();
        }

    }

    public SlidingLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isConsumed = false;
        this.enableSlidClose = true;
        this.initView(context);
    }

    private void initView(Context context) {
        this.mScroller = new Scroller(context);
        this.mLeftShadow = ContextCompat.getDrawable(context, R.drawable.sliding_left_shadow);
        int density = (int) this.getResources().getDisplayMetrics().density;
        this.mShadowWidth = 5 * density;
    }

    public void bindActivity(Activity activity) {
        this.mActivity = activity;
        ViewGroup decorView = (ViewGroup) this.mActivity.getWindow().getDecorView();
        View child = decorView.getChildAt(0);
        decorView.removeView(child);
        this.addView(child);
        decorView.addView(this);
    }

    public void unBindActivity(Activity activity) {
        this.mActivity = activity;
        ViewGroup decorView = (ViewGroup) this.mActivity.getWindow().getDecorView();
        View child = decorView.getChildAt(0);
        if (child instanceof SlidingLayout) {
            View root = ((SlidingLayout) child).getChildAt(0);
            ((SlidingLayout) child).removeView(root);
            decorView.removeView(child);
            decorView.addView(root);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!this.enableSlidClose) {
            return super.onInterceptTouchEvent(ev);
        } else {
            boolean intercept = false;
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case 0:
                    intercept = false;
                    this.mInterceptDownX = x;
                    this.mLastInterceptX = x;
                    this.mLastInterceptY = y;
                    break;
                case 1:
                    intercept = false;
                    this.mInterceptDownX = this.mLastInterceptX = this.mLastInterceptY = 0;
                    break;
                case 2:
                    int deltaX = x - this.mLastInterceptX;
                    int deltaY = y - this.mLastInterceptY;
                    if (this.mInterceptDownX < this.getWidth() / 10 && Math.abs(deltaX) > Math.abs(deltaY)) {
                        intercept = true;
                    } else {
                        intercept = false;
                    }

                    this.mLastInterceptX = x;
                    this.mLastInterceptY = y;
                default:
            }

            return intercept;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.enableSlidClose) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case 0:
                    this.mTouchDownX = x;
                    this.mLastTouchX = x;
                    this.mLastTouchY = y;
                    break;
                case 1:
                    this.isConsumed = false;
                    this.mTouchDownX = this.mLastTouchX = this.mLastTouchY = 0;
                    if (-this.getScrollX() < this.getWidth() / 2) {
                        this.scrollBack();
                    } else {
                        this.scrollClose();
                    }
                    break;
                case 2:
                    int deltaX = x - this.mLastTouchX;
                    int deltaY = y - this.mLastTouchY;
                    if (!this.isConsumed && this.mTouchDownX < this.getWidth() / 10 && Math.abs(deltaX) > Math.abs(deltaY)) {
                        this.isConsumed = true;
                    }

                    if (this.isConsumed) {
                        int rightMovedX = this.mLastTouchX - (int) ev.getX();
                        if (this.getScrollX() + rightMovedX >= 0) {
                            this.scrollTo(0, 0);
                        } else {
                            this.scrollBy(rightMovedX, 0);
                        }
                    }

                    this.mLastTouchX = x;
                    this.mLastTouchY = y;
                default:
            }

            return true;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.scrollTo(this.mScroller.getCurrX(), 0);
            this.postInvalidate();
        } else if (-this.getScrollX() >= this.getWidth()) {
            this.mActivity.finish();
            this.mActivity.overridePendingTransition(0, 0);
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.drawShadow(canvas);
    }

    private void scrollBack() {
        int startX = this.getScrollX();
        int dx = -this.getScrollX();
        this.mScroller.startScroll(startX, 0, dx, 0, 300);
        this.invalidate();
    }

    private void scrollClose() {
        int startX = this.getScrollX();
        int dx = -this.getScrollX() - this.getWidth() - this.mShadowWidth;
        this.mScroller.startScroll(startX, 0, dx, 0, 300);
        this.invalidate();
    }

    private void drawShadow(Canvas canvas) {
        this.mLeftShadow.setBounds(0, 0, this.mShadowWidth, this.getHeight());
        canvas.save();
        canvas.translate((float) (-this.mShadowWidth), 0.0F);
        this.mLeftShadow.draw(canvas);
        canvas.restore();
    }
}
