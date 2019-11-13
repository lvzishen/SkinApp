package com.ads.lib.splash;

import android.content.Context;
import android.util.Log;

import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.mediation.loader.LoaderFactory;
import com.ads.lib.mediation.loader.natives.MopubNativeAdLoader;
import com.ads.lib.splash.model.SplashNativeAd;
import com.ads.lib.util.AdErrorCodeConvertUtil;
import com.goodmorning.config.GlobalConfig;

import org.saturn.splash.loader.interf.INativeAdLoader;
import org.saturn.splash.loader.callback.NativeAdListener;

public class SplashNativeAdLoader implements INativeAdLoader {

    public static final String TAG = "SplashNativeAdLoader";
    public static final boolean DEBUG = GlobalConfig.DEBUG;

    private static SplashNativeAdLoader mInstance;
    private NativeAdListener mAdListener;
    private final com.ads.lib.mediation.loader.natives.INativeAdLoader mNativeAdLoader;

    public static SplashNativeAdLoader getInstance(Context context, String unitId, String positionId) {
        if (mInstance == null) {
            synchronized (SplashNativeAdLoader.class) {
                if (mInstance == null) {
                    mInstance = new SplashNativeAdLoader(context, unitId, positionId);
                }
            }
        }
        return mInstance;
    }

    public SplashNativeAdLoader(Context context, String unitId, String placementId) {
        mNativeAdLoader = LoaderFactory.getNativeLoader(MopubNativeAdLoader.class.getSimpleName(), context, unitId, placementId,"148013297");
        mNativeAdLoader.setAdListener(new com.ads.lib.mediation.loader.natives.NativeAdListener() {
            @Override
            public void onAdFail(AdErrorCode adErrorCode) {
                if(DEBUG){
                    Log.d(TAG,adErrorCode.message);
                }
                if (mAdListener != null) {
                    mAdListener.onFail(AdErrorCodeConvertUtil.convert(adErrorCode));
                }

            }

            @Override
            public void onAdLoaded(NativeAd nativeAd) {
                if(DEBUG){
                    Log.d(TAG,"#onAdLoaded");
                }
                SplashNativeAd splashNativeAd = new SplashNativeAd(nativeAd);
                splashNativeAd.setLastLoadedTime(System.currentTimeMillis());
                if (mAdListener != null) {
                    mAdListener.onAdLoaded(splashNativeAd);
                }
            }
        });
    }

    @Override
    public void load(){
        if(DEBUG){
            Log.d(TAG,"#load");
        }
        mNativeAdLoader.load();
    }

    @Override
    public void destory() {

    }

    @Override
    public void setAdListener(NativeAdListener listener) {
        mAdListener = listener;
    }
}

