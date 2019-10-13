package com.jsongo.ui.component.image.banner.lib.transform;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.jsongo.ui.component.image.banner.lib.util.ViewHelper;

public class FlowTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        ViewHelper.setRotationY(page, position * -30f);
    }
}
