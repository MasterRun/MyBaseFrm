package com.jsongo.ui.component.image.banner.lib.transform;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.jsongo.ui.component.image.banner.lib.util.ViewHelper;

public class RotateUpTransformer implements ViewPager.PageTransformer {

    private static final float ROT_MOD = -15f;

    @Override
    public void transformPage(View page, float position) {
        final float width = page.getWidth();
        final float rotation = ROT_MOD * position;

        ViewHelper.setPivotX(page, width * 0.5f);
        ViewHelper.setPivotY(page, 0f);
        ViewHelper.setTranslationX(page, 0f);
        ViewHelper.setRotation(page, rotation);
    }
}
