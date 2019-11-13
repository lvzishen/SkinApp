package com.baselib.bitmap.display;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.baselib.bitmap.core.BitmapDisplayConfig;


public class SimpleDisplayer implements Displayer {

    @Override
    public void loadCompletedDisplay(View view, Bitmap bitmap,
                                     BitmapDisplayConfig config, boolean animationEnable) {
        switch (config.getAnimationType()) {
            case BitmapDisplayConfig.AnimationType.fadeIn:
                fadeInDisplay(view, bitmap, animationEnable);
                break;
            case BitmapDisplayConfig.AnimationType.userDefined:
                animationDisplay(view, bitmap, config.getAnimation(),
                        animationEnable);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadFailDisplay(View view, Bitmap bitmap) {
        setBitmap(view, bitmap);
    }

    @SuppressWarnings("deprecation")
    private void fadeInDisplay(View view, Bitmap bitmap, boolean animationEnable) {
        if (animationEnable) {
            final TransitionDrawable transitionDrawable = new TransitionDrawable(
                    new Drawable[]{
                            new ColorDrawable(Color.TRANSPARENT),
                            new BitmapDrawable(view.getResources(), bitmap)});
            if (view instanceof ImageView) {
                ((ImageView) view).setImageDrawable(transitionDrawable);
            } else {
                view.setBackgroundDrawable(transitionDrawable);
            }
            transitionDrawable.startTransition(300);
        } else {
            setBitmap(view, bitmap);
        }
    }

    @SuppressWarnings("deprecation")
    private void animationDisplay(View view, Bitmap bitmap,
                                  Animation animation, boolean animationEnable) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(bitmap);
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
        if (animationEnable) {
            animation.setStartTime(AnimationUtils.currentAnimationTimeMillis());
            view.startAnimation(animation);
        }
    }

    public static void setBitmap(View view, Bitmap bitmap) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(bitmap);
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
    }
}
