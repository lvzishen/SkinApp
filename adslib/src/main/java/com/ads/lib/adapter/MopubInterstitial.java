package com.ads.lib.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.ads.lib.ModuleConfig;
import com.ads.lib.base.BaseCustomNativeEventNetwork;
import com.ads.lib.init.MoPubStarkInit;
import com.ads.lib.log.LogEventUtilsTracking;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.InterstitialWrapperAd;
import com.ads.lib.mediation.helper.LogHelper;
import com.ads.lib.mediation.loader.interstitial.InterstitialEventListener;
import com.ads.lib.mediation.loader.interstitial.InterstitialWrapperAdAdListener;
import com.ads.lib.mediation.widget.NativeStaticViewHolder;
import com.baselib.sp.SharedPref;
import com.mopub.common.MoPub;
import com.mopub.common.privacy.PersonalInfoManager;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;

/**
 * Created by zhaozhiwen on 2019/4/3.
 */
public class MopubInterstitial implements BaseCustomNativeEventNetwork, Observer {

    public static final String TAG = "MopubInterstitial";
    public static final boolean DEBUG = ModuleConfig.DEBUG;

    private boolean retryLoad;
    private boolean isLoading;
    private Context mContext;
    private String mPlacementID;
    private String mUnitId;
    private String mPositionId;
    private InterstitialWrapperAdAdListener interstitialWrapperAdAdListener;
    private MoPubInterstitial moPubInterstitial;
    private InterstitialEventListener interstitialEventListener;
    public boolean isDisplay;
    public boolean isDestroy;
    private long mRequestTime;

    public MopubInterstitial(Activity activity, String unitId, String placementID, String positionId) {
        MoPubStarkInit.getInstance().addObservers(this);
        mContext = activity.getApplicationContext();
        mPlacementID = placementID;
        mUnitId = unitId;
        mPositionId = positionId;
        moPubInterstitial = new MoPubInterstitial(activity, placementID);
    }

    public void setAdListener(InterstitialWrapperAdAdListener listener) {
        interstitialWrapperAdAdListener = listener;
    }

    public void setInterstitialEventListener(InterstitialEventListener listener) {
        interstitialEventListener = listener;
    }

    @Override
    public void destroy() {
        if (moPubInterstitial != null) {
            moPubInterstitial.destroy();
        }
        isDestroy = true;
        isLoading = false;
        LogHelper.logD(TAG,String.format("#destroy mUnitId = [%s]",mUnitId));
    }

    @Override
    public void loadAd() {
        //初始化结束前，尝试请求过广告
        retryLoad = true;

        if (!MoPub.isSdkInitialized()) {
            MoPubStarkInit.getInstance().init(mContext, mPlacementID);

        } else {
            if (isLoading || !MoPubStarkInit.isInitAfterOneMinute) {

                if (DEBUG) {
                    Log.d(TAG, "loadAd: 正在请求过程中，或者初始化完成未超过1s，不再重复请求");
                }
                return;
            }
            startLoad();
        }
    }

    public void startLoad() {

        isDisplay = false;
        isDestroy = false;

        PersonalInfoManager mPersonalInfoManager = MoPub.getPersonalInformationManager();
        final boolean canCollectPersonalInformation = mPersonalInfoManager.canCollectPersonalInformation();
        final boolean isPersionlizedAd = true;

        if (DEBUG) {
            Log.d(TAG, "canCollectPersonalInformation = " + canCollectPersonalInformation);
            Log.d(TAG, "isPersionlizedAd = " + isPersionlizedAd);
        }

        if (canCollectPersonalInformation != isPersionlizedAd) {
            if (isPersionlizedAd) {
                mPersonalInfoManager.grantConsent();

                if (DEBUG) {
                    Log.d(TAG, "grantConsent()");
                }

            } else {
                mPersonalInfoManager.revokeConsent();
                if (DEBUG) {
                    Log.d(TAG, "revokeConsent()");
                }
            }
        }

        moPubInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
                isLoading = false;
                logTrackingSuccess();
                if (interstitialWrapperAdAdListener != null) {
                    if (DEBUG) {
                        Log.d(TAG, "onInterstitialLoaded : ");
                    }
                    SharedPref.setLong(mContext,SharedPref.SP_KEY_LAST_LOAD_SUCCESS_INTERSTITIAL_TIME,System.currentTimeMillis());
                    InterstitialWrapperAd interstitialWrapperAd = new InterstitialWrapperAd();
                    interstitialWrapperAd.setInterstitialAd(MopubInterstitial.this);
                    interstitialWrapperAd.setContext(mContext);
                    interstitialWrapperAd.setUnitId(mUnitId);
                    interstitialWrapperAd.setPlacementID(mPlacementID);
                    interstitialWrapperAd.setPositionId(mPositionId);
                    interstitialWrapperAd.setExpiredTime(System.currentTimeMillis());
                    interstitialWrapperAdAdListener.onAdLoaded(interstitialWrapperAd);
                }
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
                isLoading = false;
                logTrackingFail(moPubErrorCode.toString());
                AdErrorCode nativeErrorCode;
                if (DEBUG) {
                    Log.i(TAG, "onInterstitialFailed: " + moPubErrorCode.toString());
                }
                if (MoPubErrorCode.NETWORK_INVALID_STATE.equals(moPubErrorCode)) {
                    nativeErrorCode = AdErrorCode.CONNECTION_ERROR;
                } else if (MoPubErrorCode.NO_FILL.equals(moPubErrorCode)) {
                    nativeErrorCode = AdErrorCode.NETWORK_NO_FILL;
                } else if (MoPubErrorCode.INTERNAL_ERROR.equals(moPubErrorCode)) {
                    nativeErrorCode = AdErrorCode.INTERNAL_ERROR;
                } else if (MoPubErrorCode.SERVER_ERROR.equals(moPubErrorCode)) {
                    nativeErrorCode = AdErrorCode.SERVER_ERROR;
                } else {
                    nativeErrorCode = AdErrorCode.UNSPECIFIED;
                }
                if (interstitialWrapperAdAdListener != null) {
                    if (DEBUG) {
                        Log.d(TAG, "onInterstitialFailed : ");
                    }
                    interstitialWrapperAdAdListener.onAdFail(nativeErrorCode);
                }
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

                if (interstitialEventListener != null) {
                    if (DEBUG) {
                        Log.d(TAG, "onInterstitialShown : ");
                    }
                    interstitialEventListener.onAdImpressed();
                    isDisplay = true;
                }
            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {
                if (interstitialEventListener != null) {
                    if (DEBUG) {
                        Log.d(TAG, "onInterstitialClicked : ");
                    }
                    interstitialEventListener.onAdClicked();
                }

            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
                if (interstitialEventListener != null) {
                    if (DEBUG) {
                        Log.d(TAG, "onInterstitialDismissed : ");
                    }
                    interstitialEventListener.onAdClosed();
                }
            }
        });
        moPubInterstitial.load();
        isLoading = true;
        mRequestTime = SystemClock.elapsedRealtime();
        logTracking();
    }


    private void logTrackingFail(String  errorCode) {
        if (mContext != null && mUnitId != null && mPlacementID != null) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long useTime = elapsedRealtime - mRequestTime;
            LogEventUtilsTracking.logLoadFail(mContext, mUnitId, mPlacementID,useTime,errorCode);
        }
    }

    private void logTrackingSuccess(){
        if (mContext != null && mUnitId != null && mPlacementID != null) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long useTime = elapsedRealtime - mRequestTime;
            LogEventUtilsTracking.logLoadSuccess(mContext, mUnitId, mPlacementID,useTime);
        }
    }

    private void logTracking() {
        if (mContext != null && mUnitId != null && mPlacementID != null) {
            LogEventUtilsTracking.logLoad(mContext, mUnitId, mPlacementID);
        }
    }

    @Override
    public void onPrepare(NativeStaticViewHolder staticNativeViewHolder, @NonNull List<? extends View> viewList) {

    }

    @Override
    public boolean isLoaded() {
        if (DEBUG) {
            Log.d(TAG, "isLoaded: =" + moPubInterstitial.isReady());
        }
        return moPubInterstitial != null && moPubInterstitial.isReady();
    }

    @Override
    public void show() {
        if (isLoaded()) {
            if (DEBUG) {
                Log.d(TAG, "show: ");
            }
            moPubInterstitial.show();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (DEBUG) {
            Log.d(TAG, "update: mopub init end");
        }

        if (retryLoad) {
            startLoad();
        }
    }

    public boolean isExpired(){
        return ExpireTimeUtils.isExpired(mContext,SharedPref.SP_KEY_LAST_LOAD_SUCCESS_INTERSTITIAL_TIME);
    }

    public boolean isLoading() {
        return isLoading;
    }
}
