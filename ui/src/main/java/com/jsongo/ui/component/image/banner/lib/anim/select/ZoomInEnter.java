package com.jsongo.ui.component.image.banner.lib.anim.select;

import android.animation.ObjectAnimator;
import android.view.View;

import com.jsongo.ui.component.image.banner.lib.anim.BaseAnimator;

public class ZoomInEnter extends BaseAnimator {
    public ZoomInEnter() {
        this.mDuration = 200;
    }

    @Override
    public void setAnimation(View view) {
        this.mAnimatorSet.playTogether(ObjectAnimator.ofFloat(view, "scaleX", new float[]{1.0F, 1.5F}),
                ObjectAnimator.ofFloat(view, "scaleY", new float[]{1.0F, 1.5F}));
    }
}
