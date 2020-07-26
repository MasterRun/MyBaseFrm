package com.jsongo.ui.component.image.banner.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jsongo.ui.R;
import com.jsongo.ui.component.image.banner.lib.widget.banner.BaseIndicatorBanner;

public class SimpleGuideBanner extends BaseIndicatorBanner<Integer, SimpleGuideBanner> {
    public SimpleGuideBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleGuideBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBarShowWhenLast(false);
    }

    @Override
    public Pair<View, ViewGroup.LayoutParams> onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.banner_simple_guide, null);
        ImageView iv = inflate.findViewById(R.id.iv);
        TextView tv_jump = inflate.findViewById(R.id.tv_jump);

        final Integer resId = mDatas.get(position);
        tv_jump.setVisibility(position == mDatas.size() - 1 ? VISIBLE : GONE);

        Glide.with(mContext)
                .load(resId)
                .into(iv);

        tv_jump.setOnClickListener(v -> {
            if (onJumpClickL != null) {
                onJumpClickL.onJumpClick();
            }
        });
        return new Pair<>(inflate, null);
    }

    private OnJumpClickL onJumpClickL;

    public interface OnJumpClickL {
        void onJumpClick();
    }

    public void setOnJumpClickL(OnJumpClickL onJumpClickL) {
        this.onJumpClickL = onJumpClickL;
    }
}
