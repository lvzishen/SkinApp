package com.baselib.bitmap.core;

import android.graphics.Bitmap;
import android.view.animation.Animation;

public class BitmapDisplayConfig {

    private int bitmapWidth;
    private int bitmapHeight;

    private Animation animation;

    private int animationType;

    private String loadingUrl;
    private Bitmap loadingBitmap;
    private Bitmap loadFailBitmap;
    private int loadingBitmapRes;
    private int loadingFailBitmapRes;

    public int getBitmapWidth() {
        return this.bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return this.bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public int getAnimationType() {
        return this.animationType;
    }

    public void setAnimationType(int animationType) {
        this.animationType = animationType;
    }

    public Bitmap getLoadingBitmap() {
        return this.loadingBitmap;
    }

    public void setLoadingBitmap(Bitmap loadingBitmap) {
        this.loadingBitmap = loadingBitmap;
    }

    public void setLoadingBitmapRes(int loadingBitmapRes) {
        this.loadingBitmapRes = loadingBitmapRes;
    }

    public int getLoadingBitmapRes() {
        return loadingBitmapRes;
    }

    public void setLoadingFailBitmapRes(int loadingFailBitmapRes) {
        this.loadingFailBitmapRes = loadingFailBitmapRes;
    }

    public int getLoadingFailBitmapRes() {
        return loadingFailBitmapRes;
    }

    public Bitmap getLoadFailBitmap() {
        return this.loadFailBitmap;
    }

    public void setLoadFailBitmap(Bitmap loadFailBitmap) {
        this.loadFailBitmap = loadFailBitmap;
    }

    public String getLoadingUrl() {
        return loadingUrl;
    }

    public void setLoadingUrl(String loadingUrl) {
        this.loadingUrl = loadingUrl;
    }

    public class AnimationType {
        public static final int userDefined = 0;
        public static final int fadeIn = 1;
    }
}
