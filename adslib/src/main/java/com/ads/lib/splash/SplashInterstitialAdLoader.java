package com.ads.lib.splash;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ads.lib.commen.AdLifecyclerManager;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.InterstitialWrapperAd;
import com.ads.lib.mediation.loader.LoaderFactory;
import com.ads.lib.mediation.loader.interstitial.IInterstitialWrapperAdAdLoader;
import com.ads.lib.mediation.loader.interstitial.InterstitialWrapperAdAdListener;
import com.ads.lib.mediation.loader.interstitial.MopubInterstitialWrapperAdLoader;
import com.ads.lib.splash.model.SplashInterstitialAd;
import com.ads.lib.util.AdErrorCodeConvertUtil;
import com.goodmorning.config.GlobalConfig;

import org.saturn.splash.loader.callback.InterstitialAdListener;
import org.saturn.splash.loader.interf.IInterstitialAdLoader;

public class SplashInterstitialAdLoader implements IInterstitialAdLoader{

    public static final String TAG = "SplashInterstitialAdLoader";
    public static final boolean DEBUG = GlobalConfig.DEBUG;

    private static SplashInterstitialAdLoader mInstance;
    private IInterstitialWrapperAdAdLoader mInterstitialLoader = null;
    private InterstitialAdListener mAdLister;


    public static SplashInterstitialAdLoader getInstance(Context context, String unitId, String positionId){
        if(mInstance == null){
            synchronized (SplashInterstitialAdLoader.class){
                if(mInstance == null){
                    mInstance = new SplashInterstitialAdLoader(context, unitId, positionId);
                }
            }
        }
        return mInstance;
    }


    public SplashInterstitialAdLoader(Context context, String unitId, String placementId) {
        if(DEBUG){
            Log.d(TAG,"#interstitial init");
        }
        Activity mainActivity = AdLifecyclerManager.getInstance(context).getMainActivity();
        if(mainActivity != null) {
            if(DEBUG){
                Log.d(TAG,"#interstitial real init");
            }
            mInterstitialLoader = LoaderFactory.getInterstitialWrapperLoader(MopubInterstitialWrapperAdLoader.class.getSimpleName(), mainActivity, unitId, placementId,"148013275");
            mInterstitialLoader.setAdListener(new InterstitialWrapperAdAdListener() {
                @Override
                public void onAdFail(AdErrorCode adErrorCode) {
                    if(DEBUG){
                        Log.d(TAG,adErrorCode.message);
                    }
                    if(mAdLister != null){
                        mAdLister.onFail(AdErrorCodeConvertUtil.convert(adErrorCode));
                    }
                }

                @Override
                public void onAdLoaded(InterstitialWrapperAd interstitialWrapperAd) {
                    if(DEBUG){
                        Log.d(TAG,"#onAdLoaded");
                    }
                    if(mAdLister != null){
                        SplashInterstitialAd interstitialAd = new SplashInterstitialAd(interstitialWrapperAd);
                        interstitialAd.setLastLoadedTime(System.currentTimeMillis());
                        mAdLister.onAdLoaded(interstitialAd);
                    }
                }
            });
        }
    }


    @Override
    public void load() {
        if(DEBUG){
            Log.d(TAG,"#load");
        }
        if(mInterstitialLoader != null){
            mInterstitialLoader.load();
        }
    }

    @Override
    public void destory() {
        if(mInterstitialLoader != null){
            mInterstitialLoader.destroy();
        }
    }

    @Override
    public void setAdListener(InterstitialAdListener listener) {
        mAdLister = listener;
    }
}

