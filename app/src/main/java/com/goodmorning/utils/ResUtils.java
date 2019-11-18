
package com.goodmorning.utils;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class ResUtils {

    /**
     * 布局inflater
     */
    private static LayoutInflater inflater;

    public static LayoutInflater getInflater() {
        if (inflater == null) {
            inflater = LayoutInflater.from(getApplicationContext());
        }
        return inflater;
    }

    public static Resources getResources() {
        return getApplicationContext().getResources();
    }

    /**
     * 获取String资源
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String getString(int resId, Object... args) {
        return getResources().getString(resId, args);
    }

    public static String[] getArrayStringResource(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取Drawable资源
     *
     * @param resId
     * @return
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取color资源
     *
     * @param resId
     * @return
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取dimen资源
     *
     * @param resId
     * @return
     */
    public static float getDimension(int resId) {
        return getResources().getDimension(resId);
    }

    public static int getDimensionPixelSize(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    public static int getDimensionPixelOffset(int resId) {
        return getResources().getDimensionPixelOffset(resId);
    }

    /**
     * 获取assest管理器
     *
     * @return
     */
    public static AssetManager getAssets() {
        return getResources().getAssets();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

}
