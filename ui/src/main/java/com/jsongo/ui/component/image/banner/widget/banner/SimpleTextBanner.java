package com.jsongo.ui.component.image.banner.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsongo.ui.R;
import com.jsongo.ui.component.image.banner.lib.widget.banner.BaseIndicatorBanner;

public class SimpleTextBanner extends BaseIndicatorBanner<String, SimpleTextBanner> {
    public SimpleTextBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleTextBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTextBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onTitleSelect(TextView tv, int position) {
    }

    @Override
    public Pair<View, ViewGroup.LayoutParams> onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.banner_simple_text, null);
        TextView tv = inflate.findViewById(R.id.tv);
        tv.setText(mDatas.get(position));
        return new Pair<>(inflate, null);
    }
}
