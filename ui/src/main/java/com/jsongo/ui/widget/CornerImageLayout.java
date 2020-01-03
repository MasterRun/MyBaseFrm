package com.jsongo.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.jsongo.ui.BaseUI;
import com.jsongo.ui.R;

import kotlin.text.StringsKt;

/**
 * @author jsongo
 * @date 2019/3/11 22:24
 */
public class CornerImageLayout extends RelativeLayout {
   protected ImageView imageView;
   protected TextView cornerTextView;
   protected RelativeLayout relativeLayout;

    public CornerImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CornerImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CornerImageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getCornerTextView() {
        return cornerTextView;
    }

    protected void init(Context context, AttributeSet attrs) {
        relativeLayout = (RelativeLayout) LayoutInflater.from(BaseUI.context)
                .inflate(R.layout.layout_corner_image, this, true);
        imageView = relativeLayout.findViewById(R.id.bar_iv);
        cornerTextView = relativeLayout.findViewById(R.id.bar_num);
    }

    public void setCornerTextVisiable(int visible) {
        if (visible == View.VISIBLE) {
            cornerTextView.setVisibility(VISIBLE);
        } else {
            cornerTextView.setVisibility(GONE);
        }
    }

    public void setCornerText(String text) {
        cornerTextView.setText(text);
        invalidate();
    }

    public void setMessageCount(@Nullable String count) {
        if (TextUtils.isEmpty(count)) {
            count = "0";
        }
        final Integer integer = StringsKt.toIntOrNull(count);
        if (integer != null) {
            setMessageCount(integer);
        }
    }

    public void setMessageCount(int count) {
        if (count == 0) {
            cornerTextView.setVisibility(View.GONE);
        } else {
            cornerTextView.setVisibility(View.VISIBLE);
            if (count < 100) {
                cornerTextView.setText(count + "");
            } else {
                cornerTextView.setText("99+");
            }
        }
        invalidate();
    }

    /**
     * 绑定消息数量
     *
     * @param cornerImageLayout
     * @param messageCount
     */
    @BindingAdapter("cil_messageCount")
    public static void setMessageCount(@NonNull CornerImageLayout cornerImageLayout, String messageCount) {
        cornerImageLayout.setMessageCount(messageCount);
    }

}
