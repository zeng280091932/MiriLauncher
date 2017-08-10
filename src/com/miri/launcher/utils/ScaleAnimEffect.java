package com.miri.launcher.utils;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * 缩放效果
 * @author penglin
 */
public class ScaleAnimEffect {

    //动画起始时 X坐标上的伸缩尺寸  
    private float fromXScale;

    //动画结束时 X坐标上的伸缩尺寸     
    private float toXScale;

    //动画起始时 Y坐标上的伸缩尺寸  
    private float fromYScale;

    //动画结束时Y坐标上的伸缩尺寸     
    private float toYScale;

    //动画时间间隔
    private long duration;

    public void setAttributs(float fromXScale, float toXScale, float fromYScale, float toYScale,
            long duration) {
        this.fromXScale = fromXScale;
        this.fromYScale = fromYScale;
        this.toXScale = toXScale;
        this.toYScale = toYScale;
        this.duration = duration;
    }

    public Animation scaleAnimation() {
        ScaleAnimation localScaleAnimation = new ScaleAnimation(this.fromXScale, this.toXScale,
                this.fromYScale, this.toYScale, Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5F);
        localScaleAnimation.setFillAfter(true);
        localScaleAnimation.setInterpolator(new AccelerateInterpolator());
        localScaleAnimation.setDuration(this.duration);
        return localScaleAnimation;
    }

}
