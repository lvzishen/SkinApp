package com.ads.lib.splash.model;

import android.util.Log;
import android.view.View;

import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.mediation.bean.NativeViewBinder;
import com.ads.lib.mediation.loader.natives.NativeEventListener;
import com.goodmorning.config.GlobalConfig;

import java.util.List;


public class SplashNativeAd extends org.saturn.splash.loader.model.NativeAd <NativeAd> {


    public static final String TAG = "SplashNativeAd";
    public static final boolean DEBUG = GlobalConfig.DEBUG;

    public SplashNativeAd(NativeAd nativeAd){
        super(nativeAd);
        mTargetAd.setNativeEventListener(new NativeEventListener() {
            @Override
            public void onAdImpressed() {
                if(DEBUG){
                    Log.d(TAG,"onAdImpressed");
                }
                if(mEventListener != null){
                    mEventListener.onImpression();
                }
            }

            @Override
            public void onAdClicked() {
                if(DEBUG){
                    Log.d(TAG,"onAdClicked");
                }
                if(mEventListener != null){
                    mEventListener.onClicked();
                }

            }
        });
    }

    @Override
    public String getTitle() {
        return mTargetAd.getTitle();
    }

    @Override
    public void clear(View view) {

    }

    @Override
    public String getCallToAction() {
        return mTargetAd.getCallToAction();
    }

    @Override
    public String getSummary() {
        return mTargetAd.getText();
    }

    @Override
    public float getStarRating() {
        return 0;
    }


    public void prepare(NativeViewBinder viewBinder, List<View> viewList){
        mTargetAd.prepare(viewBinder, viewList);
    }

    @Override
    public boolean isDestoried() {
        return mTargetAd.isDestroyed();
    }

    @Override
    public boolean isExpired() {
        return mTargetAd.isExpired();
    }

    @Override
    public boolean isRecoredClicked() {
        return false;
    }

    @Override
    public boolean isRecoredImpression() {
        return mTargetAd.isImpress();
    }

    @Override
    public void destory() {

    }

    @Override
    public String getPlacementId() {
        return mTargetAd.getPlacementId();
    }

    @Override
    public String getPositionId() {
        return mTargetAd.getPositionId();
    }

    @Override
    public String getPoolPositionId() {
        return mTargetAd.getPoolPositionId();
    }
}
