package com.jsongo.ui.component.image.banner.widget.banner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jsongo.ui.R;
import com.jsongo.ui.component.image.banner.lib.widget.banner.BaseIndicatorBanner;
import com.jsongo.ui.component.image.banner.widget.bean.BannerItem;

public class SimpleImageBanner extends BaseIndicatorBanner<BannerItem, SimpleImageBanner> {
    private ColorDrawable colorDrawable;

    public SimpleImageBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        colorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {
        final BannerItem item = mDatas.get(position);
        tv.setText(item.getTitle());
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.adapter_simple_image, null);
        ImageView iv = inflate.findViewById(R.id.iv);
        final BannerItem item = mDatas.get(position);
        int itemWidth = mDisplayMetrics.widthPixels;
        int itemHeight = (int) (itemWidth * 360 * 1.0f / 640);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

        String imgUrl = item.getImgUrl();

        if (!TextUtils.isEmpty(imgUrl)) {
            RequestOptions options = RequestOptions.centerCropTransform().placeholder(colorDrawable)
                    .override(itemWidth, itemHeight);
            Glide.with(mContext)
                    .load(imgUrl)
                    .apply(options)
                    .into(iv);
        } else {
            iv.setImageDrawable(colorDrawable);
        }

        return inflate;
    }
}
