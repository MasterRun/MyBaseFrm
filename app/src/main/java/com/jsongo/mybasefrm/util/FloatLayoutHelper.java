package com.jsongo.mybasefrm.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsongo.mybasefrm.MyApplication;
import com.jsongo.mybasefrm.R;
import com.jsongo.mybasefrm.widget.ImagePreview.ImgPreviewClick;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;

import java.util.List;

/**
 * @author jsongo
 * @date 2019/1/1 17:46
 */
public class FloatLayoutHelper {

    private int childSpace;
    private QMUIFloatLayout floatLayout;
    /**
     * 行最大显示个数
     */
    private int maxLineCount = 3;

    public int getChildSpace() {
        return childSpace;
    }

    public FloatLayoutHelper(QMUIFloatLayout floatLayout) {
        this(floatLayout, 4);
    }

    public FloatLayoutHelper(QMUIFloatLayout floatLayout, int childSpaceDp) {
        this(floatLayout, childSpaceDp, 3);
    }

    public FloatLayoutHelper(QMUIFloatLayout floatLayout, int childSpaceDp, int maxLineCount) {
        childSpace = QMUIDisplayHelper.dp2px(MyApplication.context, childSpaceDp);
        this.floatLayout = floatLayout;
        this.maxLineCount = maxLineCount;
    }

    public int getMaxLineCount() {
        return maxLineCount;
    }

    public void setMaxLineCount(int maxLineCount) {
        this.maxLineCount = maxLineCount;
    }

    public void computeSize(int floatLayoutWidth, int imgCount) {
        computeSize(floatLayoutWidth, imgCount, -1);
    }

    /**
     * 每行最大为3个，计算合适的size并设置到QMUIFloatLayout
     *
     * @param floatLayoutWidth 实际的QMUIFloatLayout宽度
     * @param imgCount         实际图片的个数
     * @param maxCount         设置允许显示的最大个数，-1为不限制，
     *                         maxCount==-1 计算全部显示的高度
     *                         maxCount==9 计算显示九宫格的高度
     *                         maxCount==3 计算一行显示的固定高度（最大显示3个,固定单行显示)
     *                         <p>
     *                         将imageViewsize设置到tag中
     */
    public void computeSize(int floatLayoutWidth, int imgCount, int maxCount) {
        ViewGroup.LayoutParams layoutParams = floatLayout.getLayoutParams();
        int containerWith = floatLayoutWidth - floatLayout.getPaddingLeft() - floatLayout.getPaddingRight();
        int imageViewSize = 0;
        if (maxLineCount == 3) {
            imageViewSize = computeSizeWhenMaxLineCountOf3(imgCount, maxCount, layoutParams, containerWith);
        } else if (maxLineCount == 1) {
            imageViewSize = containerWith;
            layoutParams.height = imageViewSize * maxCount + (maxCount - 1 * childSpace);
        }
        layoutParams.height = layoutParams.height + floatLayout.getPaddingTop() + floatLayout.getPaddingBottom();
        floatLayout.setTag(imageViewSize);
        floatLayout.setLayoutParams(layoutParams);
    }

    private int computeSizeWhenMaxLineCountOf3(int imgCount, int maxCount, ViewGroup.LayoutParams layoutParams, int containerWith) {
        int imageViewSize = 0;
        if (maxCount == 9) {
            switch (imgCount) {
                case 1:
                    imageViewSize = containerWith;
                    layoutParams.height = containerWith;//imageViewSize
                    break;
                case 2:
                    imageViewSize = (containerWith - childSpace) / 2;
                    layoutParams.height = imageViewSize;
                    break;
                case 3:
                    imageViewSize = (containerWith - childSpace * 2) / 3;
                    layoutParams.height = imageViewSize;
                    break;
                case 4:
                    imageViewSize = (containerWith - childSpace) / 2;
                    layoutParams.height = containerWith;//childSpace + imageViewSize * 2
                    break;
                case 5:
                case 6:
                    imageViewSize = (containerWith - childSpace * 2) / 3;
                    layoutParams.height = imageViewSize * 2 + childSpace;
                    break;
           /*     case 7:
                case 8:
                case 9:*/
                default:
                    imageViewSize = (containerWith - childSpace * 2) / 3;
                    layoutParams.height = containerWith;//imageViewSize * 3 + childSpace * 2
                    break;
            }
        } else if (maxCount == 3) {
            imageViewSize = (containerWith - 2 * childSpace) / 3;
            layoutParams.height = imageViewSize;
        } else if (maxCount == -1) {
            switch (imgCount) {
                case 1:
                    imageViewSize = containerWith;
                    layoutParams.height = containerWith;//imageViewSize
                    break;
                case 2:
                    imageViewSize = (containerWith - childSpace) / 2;
                    layoutParams.height = imageViewSize;
                    break;
                case 4:
                    imageViewSize = (containerWith - childSpace) / 2;
                    layoutParams.height = containerWith;//childSpace + imageViewSize * 2;
                    break;
                default:
                    imageViewSize = (containerWith - childSpace * 2) / 3;
                    int lineCount = imgCount / 3;
                    if (imgCount % 3 != 0) {
                        lineCount++;
                    }
                    layoutParams.height = imageViewSize * lineCount + childSpace * (lineCount - 1);
                    break;
            }
        }
        return imageViewSize;
    }

    public void loadImgs2FloatLayout(final Activity activity, final List<String> imageUrls) {
        loadImgs2FloatLayout(activity, imageUrls, 0, imageUrls.size());
    }

    /**
     * 将图片全部加载到FloatLayout
     *
     * @param activity
     * @param imageUrls
     * @param startIndex
     * @param count
     */
    public void loadImgs2FloatLayout(
            final Activity activity,
            final List<String> imageUrls, int startIndex, int count) {
        if (imageUrls.size() > 0) {
            floatLayout.post(() -> {
                int endIndex = startIndex + count;
                for (int i = startIndex; i < endIndex; i++) {
                    View view = floatLayout.getChildAt(i);
                    if (view instanceof ImageView) {
                        GlideUtil.INSTANCE.load(activity, imageUrls.get(i), (ImageView) view);
                        view.setOnClickListener(new ImgPreviewClick(activity, i, imageUrls));
                    } else {
                        addItemToFloatLayout(activity, imageUrls, i);
                    }
                }
            });
        }
    }

    /**
     * 将图片加载到FloatLayout，限制最大显示数量，
     *
     * @param activity
     * @param imageUrls
     * @param maxShowCount
     */
    public void loadImgs2FloatLayoutWithPlusnum(
            final Activity activity,
            final List<String> imageUrls, int maxShowCount) {
        if (imageUrls.size() > 0) {
            if (imageUrls.size() <= maxShowCount) {
                loadImgs2FloatLayout(activity, imageUrls);
            } else {
                floatLayout.post(() -> {
                    for (int i = 0; i < maxShowCount - 1; i++) {
                        View view = floatLayout.getChildAt(i);
                        if (view instanceof ImageView) {
                            GlideUtil.INSTANCE.load(activity, imageUrls.get(i), (ImageView) view);
                            view.setOnClickListener(new ImgPreviewClick(activity, i, imageUrls));
                        } else {
                            addItemToFloatLayout(activity, imageUrls, i);
                        }
                    }
                    addPlusNumToFloatLayout(
                            activity,
                            imageUrls.size() - maxShowCount + 1,
                            imageUrls.get(maxShowCount - 1));
                });
            }
        }
    }

    /**
     * 将ImageView添加到FloatLayout
     *
     * @param activity
     * @param urlList
     * @param index
     */
    private void addItemToFloatLayout(
            final Activity activity,
            final List<String> urlList,
            final int index) {
        ImageView img = new ImageView(activity);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideUtil.INSTANCE.load(activity, urlList.get(index), img);
        img.setOnClickListener(new ImgPreviewClick(activity, index, urlList));
        addViewToFloatLayout(img);
    }

    public void addViewToFloatLayout(View v) {
        floatLayout.setChildHorizontalSpacing(childSpace);
        floatLayout.setChildVerticalSpacing(childSpace);
        int childSize = (int) floatLayout.getTag();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(childSize, childSize);
        floatLayout.addView(v, lp);
    }

    /**
     * 将ImageView和数字添加到FloatLayout
     *
     * @param activity
     * @param num
     * @param imgUrl
     */
    private void addPlusNumToFloatLayout(
            final Activity activity,
            final int num,
            final String imgUrl) {
        ImageView img = new ImageView(activity);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideUtil.INSTANCE.load(activity, imgUrl, img);
        img.setColorFilter(activity.getResources().getColor(R.color.light_gray_transparent));
        FrameLayout.LayoutParams imgLayoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tv = new TextView(activity);
        tv.setText("+" + num);
        tv.setTextSize(60);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(activity.getResources().getColor(R.color.white));
        FrameLayout.LayoutParams tvLayoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        floatLayout.setChildHorizontalSpacing(childSpace);
        floatLayout.setChildVerticalSpacing(childSpace);
        FrameLayout frameLayout = new FrameLayout(activity);
        frameLayout.addView(img, imgLayoutParams);
        frameLayout.addView(tv, tvLayoutParams);
        int imageViewSize = (int) floatLayout.getTag();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(imageViewSize, imageViewSize);
        floatLayout.addView(frameLayout, lp);
    }

}
