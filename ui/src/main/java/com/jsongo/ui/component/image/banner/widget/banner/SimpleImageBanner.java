package com.jsongo.ui.component.image.banner.widget.banner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.epoint.app.widget.banner.widget.bean.ImageBannerItem;
import com.jsongo.ui.R;
import com.jsongo.ui.component.image.banner.lib.widget.banner.BaseIndicatorBanner;

public class SimpleImageBanner<T extends ImageBannerItem> extends BaseIndicatorBanner<T, SimpleImageBanner<T>> {
    private ColorDrawable colorDrawable;

    public SimpleImageBanner(Context context) {
        this(context, Gravity.CENTER);
    }

    public SimpleImageBanner(Context context, int indicatorGravity) {
        super(context, indicatorGravity);
        initColorDrawable();
    }

    public SimpleImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initColorDrawable();
    }

    protected void initColorDrawable() {
        colorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    @Override
    public void onTitleSelect(TextView tv, int position) {
        final T item = mDatas.get(position);
        tv.setText(item.getTitle());
    }

    @Override
    public Pair<View, ViewGroup.LayoutParams> onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.banner_simple_image, null);
        ImageView iv = inflate.findViewById(R.id.iv);
        final T item = mDatas.get(position);
        int itemWidth = mDisplayMetrics.widthPixels;
        int itemHeight = (int) (itemWidth * 360 * 1.0f / 640);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

        String imgUrl = item.getImgUrl();

        if (!TextUtils.isEmpty(imgUrl)) {
            RequestOptions options = RequestOptions.placeholderOf(colorDrawable)
                    .override(itemWidth, itemHeight);
            Glide.with(mContext)
                    .load(imgUrl)
                    .apply(options)
                    .into(iv);
        } else {
            iv.setImageDrawable(colorDrawable);
        }
        return new Pair<>(inflate, null);
    }
}
