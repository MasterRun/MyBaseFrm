package com.jsongo.ui.component.image.banner.lib.transform;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.jsongo.ui.component.image.banner.lib.util.ViewHelper;

public class FadeSlideTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {

        ViewHelper.setTranslationX(page, 0);

        if (position <= -1.0F || position >= 1.0F) {
            ViewHelper.setAlpha(page, 0.0F);
        } else if (position == 0.0F) {
            ViewHelper.setAlpha(page, 1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            ViewHelper.setAlpha(page, 1.0F - Math.abs(position));
        }
    }
}
