package com.jsongo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsongo.ui.BaseUI;
import com.jsongo.ui.R;

/**
 * @author jsongo
 * @date 2019/3/11 22:24
 */
public class CornerImageLayout extends RelativeLayout {
    ImageView imageView;
    TextView cornerTextView;
    private RelativeLayout relativeLayout;

    public CornerImageLayout(Context context) {
        super(context);
    }

    public CornerImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CornerImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CornerImageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getCornerTextView() {
        return cornerTextView;
    }

    private void init() {
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
}
