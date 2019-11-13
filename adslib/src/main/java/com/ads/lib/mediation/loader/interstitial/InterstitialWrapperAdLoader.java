package com.ads.lib.mediation.loader.interstitial;

import android.app.Activity;
import android.util.Log;

import com.ads.lib.concurrent.ThreadHelper;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.InterstitialWrapperAd;
import com.ads.lib.mediation.loader.dispatcher.AdLoaderDispatcher;
import com.goodmorning.config.GlobalConfig;

public class InterstitialWrapperAdLoader implements IInterstitialWrapperAdAdLoader, InterstitialWrapperAdAdListener {

    public static final String TAG = "InterstitialWrapperAdLoader";
    public static final boolean DEBUG = GlobalConfig.DEBUG;

    private InterstitialWrapperAdAdListener mAdListener;
    private final AdLoaderDispatcher mAdloaderDispatcher;

    public InterstitialWrapperAdLoader(Activity context, String unitId, String positionId) {
        if(DEBUG){
            Log.d(TAG,String.format("<init> placementID = [%s], unitId = [%s] ",positionId,unitId));
        }
        mAdloaderDispatcher = new AdLoaderDispatcher(context, unitId, positionId);
        mAdloaderDispatcher.setAdListener(this);
    }

    @Override
    public void load() {
        ThreadHelper.getInstance().postMainThread(new Runnable() {
            @Override
            public void run() {
                mAdloaderDispatcher.load();
            }
        });
    }



    @Override
    public boolean isLoading() {
        return mAdloaderDispatcher.isLoading();
    }

    @Override
    public void destroy() {
        mAdloaderDispatcher.destroy();
    }

    @Override
    public void setAdListener(InterstitialWrapperAdAdListener listener) {
        mAdListener = listener;
    }

    @Override
    public void destory() {
        mAdloaderDispatcher.destroy();
    }


    @Override
    public void onAdFail(final AdErrorCode adErrorCode) {
        if (mAdListener != null) {
            ThreadHelper.getInstance().postMainThread(new Runnable() {
                @Override
                public void run() {
                    mAdListener.onAdFail(adErrorCode);
                }
            });
        }
    }

    @Override
    public void onAdLoaded(final InterstitialWrapperAd interstitialWrapperAd) {
        if (mAdListener != null) {
            ThreadHelper.getInstance().postMainThread(new Runnable() {
                @Override
                public void run() {
                    mAdListener.onAdLoaded(interstitialWrapperAd);
                }
            });
        }
    }


}
