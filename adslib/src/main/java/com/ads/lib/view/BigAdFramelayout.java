package com.ads.lib.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.ads.lib.ModuleConfig;

/**
 * Created by jiangqingxin on 2017/7/21.
 */

public class BigAdFramelayout extends FrameLayout {

    private static final String TAG = "BigAdImageView";
    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private boolean isAnimStart = false;
    private final Path mPath = new Path();
    private int radius = 0;
    private ValueAnimator widhAnimator;
    private boolean isCanAnim;

    public BigAdFramelayout(Context context) {
        super(context);
    }

    public BigAdFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BigAdFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIsCanAnim(boolean isCanAnim) {
        this.isCanAnim = isCanAnim;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!isCanAnim) {
            super.dispatchDraw(canvas);
            return;
        }
        int minWidth = 0;
        final int height = getMeasuredHeight();
        final int width = getMeasuredWidth();
        int diagonal = (int) Math.sqrt(height * height + width * width) / 2 + 1;
        int maxWidth = diagonal;
//        canvas.drawColor(getResources().getColor(R.color.color_primary_blue));
//        canvas.save();
        mPath.addCircle(width / 2, height / 2, radius, Path.Direction.CW);
        // 有些机型(比如那个阿尔卡特的手机)，这个api不支持硬件加速，因此需要关闭硬件加速，在执行.
        try {
            canvas.clipPath(mPath);
        } catch (UnsupportedOperationException e) {
            // setLayoutType后回自动invalidate.
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        super.dispatchDraw(canvas);
        if (isAnimStart) {
            if (widhAnimator == null) {
                widhAnimator = ValueAnimator.ofInt(minWidth, diagonal);
                widhAnimator.setDuration(500);
                widhAnimator.setStartDelay(400);
                widhAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        radius = (int) animation.getAnimatedValue();
                    }
                });
                widhAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimStart = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        isAnimStart = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                widhAnimator.start();
            }
            invalidate();
        }
    }

    public void startShowAnim() {
        isAnimStart = true;
        invalidate();
    }

}
