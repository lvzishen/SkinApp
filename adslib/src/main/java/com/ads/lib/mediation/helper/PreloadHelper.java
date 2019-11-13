package com.ads.lib.mediation.helper;

import android.content.Context;
import android.util.Log;

import com.hotvideo.config.GlobalConfig;


public class PreloadHelper {

    public static final String TAG = "PreloadHelper";
    public static final boolean DEBUG = GlobalConfig.DEBUG;


    /**
     * 预览广告池
     * 时机：点击图片页加载
     */
    public static void preloadPreview(Context context) {
        if(DEBUG){
            Log.d(TAG,"#preloadPreview");
        }
//        new NativeAdHelper().preloadNativeAd(context,
//                AdUnitId.PUBLIC_INDEPENDENT_PREVIEW_NATIVE_UNIT_ID,
//                AdUnitId.AdPositionIdKey.KEY_PID_INDEPENDENT_PREVIEW_NATIVE,
//                1);
    }

    /**
     * 首页广告池
     * 时机：进入首页/动画过程中返回首页
     */
    public static void preloadHome(final Context context, long delay) {
        if(DEBUG){
            Log.d(TAG,"#preloadHome");
        }
//        new Handler(context.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new NativeAdHelper().preloadNativeAd(context, AdUnitId.PUBLIC_INDEPENDENT_HOME_NATIVE_UNIT_ID,
//                        AdUnitId.AdPositionIdKey.KEY_PID_INDEPENDENT_HOME_NATIVE,
//                        1);
//            }
//        },delay);
    }

    /**
     * 插屏高价广告池
     * 时机：点击功能按钮加载
     */
    public static void preloadHighInterstitial(Context context) {
        if(DEBUG){
            Log.d(TAG,"#preloadHighInterstitial");
        }
//        new InterstitialAdHelper().preloadInterstitialAd(context, AdUnitId.PUBLIC_HIGH_INTERSTITIAL_UNIT_ID,
//                AdUnitId.AdPositionIdKey.KEY_PID_HIGH_INTERSTITIAL,
//                1);
    }

    /**
     * 插屏中价广告池
     * 时机：点击功能按钮加载
     */
    public static void preloadMiddleInterstitial(Context context) {
        if(DEBUG){
            Log.d(TAG,"#preloadMiddleInterstitial");
        }
//        new InterstitialAdHelper().preloadInterstitialAd(context, AdUnitId.PUBLIC_MIDDLE_INTERSTITIAL_UNIT_ID,
//                AdUnitId.AdPositionIdKey.KEY_PID_MIDDLE_INTERSTITIAL,
//                1);
    }


    /**
     * 插屏低价广告池
     * 时机：点击功能按钮加载
     */
    public static void preloadLowInterstitial(Context context) {
        if(DEBUG){
            Log.d(TAG,"#preloadLowInterstitial");
        }
//        new InterstitialAdHelper().preloadInterstitialAd(context, AdUnitId.PUBLIC_LOW_INTERSTITIAL_UNIT_ID,
//                AdUnitId.AdPositionIdKey.KEY_PID_LOW_INTERSTITIAL,
//                1);
    }

    /**
     * 原生高价广告池
     * 时机：点击功能按钮加载
     */
    public static void preloadHighNative(Context context) {
        if(DEBUG){
            Log.d(TAG,"#preloadHighNative");
        }
//        new NativeAdHelper().preloadNativeAd(context, AdUnitId.PUBLIC_HIGH_NATIVE_UNIT_ID,
//                AdUnitId.AdPositionIdKey.KEY_PID_HIGH_NATIVE,
//                1);
    }

    /**
     * 原生高价广告池
     * 时机：点击功能按钮加载
     */
    public static void preloadMiddleNative(Context context) {
        if(DEBUG){
            Log.d(TAG,"#preloadMiddleNative");
        }
//        new NativeAdHelper().preloadNativeAd(context, AdUnitId.PUBLIC_MIDDLE_NATIVE_UNIT_ID,
//                AdUnitId.AdPositionIdKey.KEY_PID_MIDDLE_NATIVE,
//                1);
    }

    /**
     * 原生高价广告池
     * 时机：点击功能按钮加载
     */
    public static void preloadLowNative(Context context) {
        if(DEBUG){
            Log.d(TAG,"#preloadLowNative");
        }
//        new NativeAdHelper().preloadNativeAd(context, AdUnitId.PUBLIC_LOW_NATIVE_UNIT_ID,
//                AdUnitId.AdPositionIdKey.KEY_PID_LOW_NATIVE,
//                1);
    }

    /**
     * 列表原生池（native/320*50）
     * 时机：首页预加载
     */
    public static void preloadListNative(Context context) {
        if(DEBUG){
            Log.d(TAG,"#preloadListNative");
        }
//        new NativeAdHelper().preloadNativeAd(context, AdUnitId.PUBLIC_LIST_NATIVE_UNIT_ID,
//                AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_LIST_NATIVE,
//                1);
    }

}
