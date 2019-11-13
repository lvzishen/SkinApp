package com.ads.lib.mediation.loader.natives;

import android.content.Context;
import android.util.Log;

import com.ads.lib.concurrent.ThreadHelper;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.mediation.loader.dispatcher.AdLoaderDispatcher;
import com.goodmorning.config.GlobalConfig;


public class NativeAdLoader implements INativeAdLoader, NativeAdListener {

    public static final String TAG = "NativeAdLoader";
    public static final boolean DEBUG = GlobalConfig.DEBUG;

    private NativeAdListener mAdListener;
    private final AdLoaderDispatcher mAdloaderDispatcher;

    public NativeAdLoader(Context context, String unitId, String positionId) {
        if (DEBUG) {
            Log.d(TAG, String.format("<init> positionId = [%s], unitId = [%s] ", positionId, unitId));
        }
        mAdloaderDispatcher = new AdLoaderDispatcher(context, unitId, positionId);
        mAdloaderDispatcher.setAdListener(this);
    }


    @Override
    public void setAdListener(NativeAdListener listener) {
        mAdListener = listener;
    }

    @Override
    public void destory() {
        mAdloaderDispatcher.destroy();
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
    public void onAdLoaded(final NativeAd nativeAd) {
        if (mAdListener != null) {
            ThreadHelper.getInstance().postMainThread(new Runnable() {
                @Override
                public void run() {
                    mAdListener.onAdLoaded(nativeAd);
                }
            });
        }
    }
}
