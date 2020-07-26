package com.jsongo.ui.component.image.banner.widget.banner;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.epoint.app.widget.banner.widget.bean.ImageBannerItem;
import com.jsongo.ui.R;
import com.jsongo.ui.component.image.banner.lib.widget.banner.BaseIndicatorBanner;

import java.util.List;

public class GridBanner<T extends ImageBannerItem> extends BaseIndicatorBanner<List<T>, GridBanner<T>> {

    protected int maxSpanCount = 4;

    /**
     * 控制修复外层嵌套scrollview导致滑动不流畅
     */
    protected boolean fixScroll = true;

    @Nullable
    protected onClickGridItem<T> onClickGridItem;

    //    @Nullable
//    protected QMUIFloatLayout flItemContainer;
    @Nullable
    private RecyclerView recyclerView;

    public GridBanner(Context context) {
        this(context, false);
    }

    public GridBanner(Context context, boolean isLoopEnable) {
        super(context, isLoopEnable);
    }

/*  不支持 xml 系列构造方法
    public GridBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }*/

//    @Nullable
//    public QMUIFloatLayout getFlItemContainer() {
//        return flItemContainer;
//    }

    public void fixScroll(boolean fixScroll) {
        this.fixScroll = fixScroll;
    }

    @Nullable
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public int getMaxSpanCount() {
        return maxSpanCount;
    }

    public void setMaxSpanCount(int maxSpanCount) {
        this.maxSpanCount = maxSpanCount;
    }

    public void setOnClickGridItem(GridBanner.onClickGridItem<T> onClickGridItem) {
        this.onClickGridItem = onClickGridItem;
    }

    @Override
    public void onTitleSelect(TextView tv, int position) {
    }

    @Override
    public Pair<View, ViewGroup.LayoutParams> onCreateItemView(int position) {
        List<T> items = mDatas.get(position);
        ViewGroup.LayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return new Pair<>(getRvPage(position, items), params);
    }

    /**
     * 使用recyclerview
     */
    public View getRvPage(int position, List<T> items) {
        recyclerView = new RecyclerView(mContext);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View gridItem = View.inflate(mContext, R.layout.banner_card_grid_item, null);
                return new ViewHolder(gridItem);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                if (holder instanceof GridBanner.ViewHolder) {
                    ViewHolder viewHolder = (ViewHolder) holder;
                    setItemData(viewHolder, items.get(position));
                    viewHolder.itemView.setOnClickListener(v -> {
                        //点击item
                        if (onClickGridItem != null) {
                            onClickGridItem.onClick(items.get(position));
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        });
        //计算spanCount
        int spanCount = maxSpanCount;
        if (position == 0 && items.size() < maxSpanCount) {
            spanCount = items.size();
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        //防止外层嵌套scrollview导致滑动不流畅
        if (fixScroll) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
        }
        return recyclerView;
    }

    /**
     * 使用qmuifloatview
     */
/*
    public View getQflPage(List<T> items) {
        View view = View.inflate(mContext, R.layout.wpl_banner_adapter_grid, null);
        flItemContainer = view.findViewById(R.id.fl_item_container);
        for (T item : items) {
            View gridItem = View.inflate(mContext, R.layout.wpl_card_grid_item, null);
            ViewHolder viewHolder = new ViewHolder(gridItem);
            setItemData(viewHolder, item);
            flItemContainer.addView(gridItem);
        }
        return view;
    }
*/

    /**
     * 设置文字和图标
     */
    public void setItemData(ViewHolder viewHolder, T t) {
        viewHolder.tvName.setText(t.getTitle());
        Glide.with(this).load(t.getImgUrl()).into(viewHolder.ivIcon);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }

    public interface onClickGridItem<T> {
        void onClick(T t);
    }

}
