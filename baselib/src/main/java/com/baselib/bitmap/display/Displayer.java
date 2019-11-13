package com.baselib.bitmap.display;

import android.graphics.Bitmap;
import android.view.View;

import com.baselib.bitmap.core.BitmapDisplayConfig;


public interface Displayer {

    /**
     * 图片加载完成 回调的函数
     *
     * @param bitmap
     * @param config
     */
    void loadCompletedDisplay(View view, Bitmap bitmap, BitmapDisplayConfig config, boolean animationEnable);

    /**
     * 图片加载失败回调的函数
     *
     * @param imageView
     * @param bitmap
     */
    void loadFailDisplay(View imageView, Bitmap bitmap);

}
