package com.ads.lib.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ads.lib.ModuleConfig;
import com.ads.lib.base.BaseCustomNativeEventNetwork;
import com.ads.lib.init.MoPubStarkInit;
import com.ads.lib.log.LogEventUtilsTracking;
import com.ads.lib.mediation.bean.AbsNativeAdWrapper;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.loader.natives.NativeAdListener;
import com.ads.lib.mediation.loader.natives.NativeEventListener;
import com.ads.lib.mediation.widget.AdIconView;
import com.ads.lib.mediation.widget.NativeMediaView;
import com.ads.lib.mediation.widget.NativeStaticViewHolder;
import com.ads.lib.prop.FastClickProp;
import com.ads.lib.util.FastClickUtils;
import com.baselib.sp.SharedPref;
import com.mopub.common.MoPub;
import com.mopub.nativeads.BaseNativeAd;
import com.mopub.nativeads.FacebookAdRenderer;
import com.mopub.nativeads.FacebookNative;
import com.mopub.nativeads.GooglePlayServicesAdRenderer;
import com.mopub.nativeads.GooglePlayServicesNative;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.StaticNativeAd;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;

/**
 * Created by zhaozhiwen on 2019/4/3.
 */
public class MopubNative extends AbsNativeAdWrapper implements BaseCustomNativeEventNetwork, Observer, MoPubNative.MoPubNativeNetworkListener {

    public static final String TAG = "MopubNative";
    public static final boolean DEBUG = ModuleConfig.DEBUG;

    private boolean retryLoad;
    private boolean isLoading;
    private MoPubNative moPubNative;
    private Context mContext;
    private String mPlacementID;
    private String mUnitId;
    private String mPositionId;
    private NativeAdListener nativeAdListener;
    private NativeAd mNativeAd;
    private NativeEventListener nativeEventListener;
    public boolean isDisplay;
    public boolean isDestroy;
    private long mRequestTime;
    private FastClickUtils mFastClickUtils;
    private NativeStaticViewHolder staticNativeViewHolder;


    public MopubNative(Context context, String unitId, String placementID, String positionId) {
        MoPubStarkInit.getInstance().addObservers(this);
        mContext = context;
        mPositionId = positionId;
        moPubNative = new MoPubNative(mContext, placementID, this);
        mPlacementID = placementID;
        mUnitId = unitId;
    }

    @Override
    public void destroy() {
        if (moPubNative != null) {
            moPubNative.destroy();
        }
        isDestroy = true;
        if (mFastClickUtils != null) {
            mFastClickUtils.detachCoverView();
        }
        mFastClickUtils = null;
    }

    public void setAdListener(NativeAdListener listener) {
        nativeAdListener = listener;
    }

    @Override
    public void setNativeEventListener(NativeEventListener listener) {
        nativeEventListener = listener;
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

    @Override
    public void update(Observable o, Object arg) {
        if (DEBUG) {
            Log.d(TAG, "update: mopub init end");
        }

        if (retryLoad) {
            startLoad();
        }
    }

    public void startLoad() {

        isDisplay = false;
        isDestroy = false;

        if (isSupportFaceBookRender()) {
            FacebookAdRenderer facebookAdRenderer = new FacebookAdRenderer(null);
            moPubNative.registerAdRenderer(facebookAdRenderer);
        }

        if (isSupportAdmobRender()) {
            GooglePlayServicesAdRenderer googlePlayServicesAdRenderer = new GooglePlayServicesAdRenderer(null);
            moPubNative.registerAdRenderer(googlePlayServicesAdRenderer);
        }

//        if (isSupportFlurryRender()) {
//            FlurryNativeAdRenderer flurryNativeAdRenderer = new FlurryNativeAdRenderer(null);
//            moPubNative.registerAdRenderer(flurryNativeAdRenderer);
//        }

        // TODO: 2019/4/3 OneByAOL待确认
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(null);
        moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);

        EnumSet<RequestParameters.NativeAdAsset> assetsSet = EnumSet.of(RequestParameters.NativeAdAsset.TITLE, RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT, RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.ICON_IMAGE, RequestParameters.NativeAdAsset.STAR_RATING);
        RequestParameters requestParameters = new RequestParameters.Builder()
                .desiredAssets(assetsSet)
                .build();
        moPubNative.makeRequest(requestParameters);
        isLoading = true;

        mRequestTime = SystemClock.elapsedRealtime();
        trackingLog();
    }

    private void trackingLog() {
        if (mContext != null && mUnitId != null && mPlacementID != null) {
            LogEventUtilsTracking.logLoad(mContext, mUnitId, mPlacementID);
        }
    }


    //是否支持Facebook sdk
    public boolean isSupportFaceBookRender() {
        try {
            Class fClass = Class.forName("com.mopub.nativeads.FacebookAdRenderer");
            if (fClass != null) {
                return true;
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "isSupportFaceBookRender: Exception", e);
            }
        }
        return false;
    }

    //是否支持admob sdk
    public boolean isSupportAdmobRender() {
        try {
            Class aClass = Class.forName("com.mopub.nativeads.GooglePlayServicesAdRenderer");
            if (aClass != null) {
                return true;
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "isSupportAdmobRender: Exception", e);
            }
        }
        return false;
    }

    //是否支持Flurry
    public boolean isSupportFlurryRender() {
        try {
            Class fClass = Class.forName("com.mopub.nativeads.FlurryNativeAdRenderer");
            if (fClass != null) {
                return true;
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "isSupportFlurryRender: Exception", e);
            }
        }
        return false;
    }

    @Override
    public void onNativeLoad(NativeAd nativeAd) {

        isLoading = false;
        logTrackingSuccess();
        if (nativeAdListener == null) {
            if (DEBUG) {
                Log.d(TAG, "onNativeLoad: ");
            }
            return;
        }

        if (nativeAd == null) {
            if (DEBUG) {
                Log.d(TAG, "onNativeLoad: no fill");
            }
            nativeAdListener.onAdFail(AdErrorCode.NETWORK_NO_FILL);
            return;
        }

        mNativeAd = nativeAd;
        SharedPref.setLong(mContext, SharedPref.SP_KEY_LAST_LOAD_SUCCESS_NATIVE_TIME, System.currentTimeMillis());
        nativeAdListener.onAdLoaded(setContentNative(nativeAd));
    }

    @Override
    public void onNativeFail(NativeErrorCode nativeErrorCode) {

        isLoading = false;

        logTrackingFail(nativeErrorCode.toString());

        if (nativeAdListener == null) {
            if (DEBUG) {
                Log.d(TAG, "onNativeLoad: nativeAdListener is empty");
            }
            return;
        }
        AdErrorCode errorCode;
        if (nativeErrorCode == null) {
            errorCode = AdErrorCode.UNSPECIFIED;
        } else if (nativeErrorCode == NativeErrorCode.EMPTY_AD_RESPONSE) {
            errorCode = AdErrorCode.NETWORK_RETURN_NULL_RESULT;
        } else if (nativeErrorCode == NativeErrorCode.IMAGE_DOWNLOAD_FAILURE) {
            errorCode = AdErrorCode.IMAGE_DOWNLOAD_FAILURE;
        } else if (nativeErrorCode == NativeErrorCode.CONNECTION_ERROR) {
            errorCode = AdErrorCode.CONNECTION_ERROR;
        } else if (nativeErrorCode == NativeErrorCode.INVALID_REQUEST_URL
                || nativeErrorCode == NativeErrorCode.NETWORK_INVALID_REQUEST) {
            errorCode = AdErrorCode.NETWORK_INVALID_REQUEST;
        } else if (nativeErrorCode == NativeErrorCode.NETWORK_TIMEOUT) {
            errorCode = AdErrorCode.NETWORK_TIMEOUT;
        } else if (nativeErrorCode == NativeErrorCode.NETWORK_NO_FILL) {
            errorCode = AdErrorCode.NETWORK_NO_FILL;
        } else if (nativeErrorCode == NativeErrorCode.NETWORK_INVALID_STATE ||
                nativeErrorCode == NativeErrorCode.INVALID_RESPONSE ||
                nativeErrorCode == NativeErrorCode.UNEXPECTED_RESPONSE_CODE ||
                nativeErrorCode == NativeErrorCode.SERVER_ERROR_RESPONSE_CODE) {
            errorCode = AdErrorCode.SERVER_ERROR;
        } else if (nativeErrorCode == NativeErrorCode.NATIVE_RENDERER_CONFIGURATION_ERROR) {
            errorCode = AdErrorCode.NATIVE_RENDERER_CONFIGURATION_ERROR;
        } else if (nativeErrorCode == NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR ||
                nativeErrorCode == NativeErrorCode.NATIVE_ADAPTER_NOT_FOUND) {
            errorCode = AdErrorCode.NATIVE_ADAPTER_NOT_FOUND;
        } else {
            errorCode = AdErrorCode.UNSPECIFIED;
        }
        if (DEBUG) {
            Log.d(TAG, "onError errorCode = " + errorCode);
        }

        nativeAdListener.onAdFail(errorCode);
    }

    private void logTrackingFail(String errorCode) {
        if (mContext != null && mUnitId != null && mPlacementID != null) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long useTime = elapsedRealtime - mRequestTime;
            LogEventUtilsTracking.logLoadFail(mContext, mUnitId, mPlacementID, useTime, errorCode);
        }
    }

    private void logTrackingSuccess() {
        if (mContext != null && mUnitId != null && mPlacementID != null) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long useTime = elapsedRealtime - mRequestTime;
            LogEventUtilsTracking.logLoadSuccess(mContext, mUnitId, mPlacementID, useTime);
        }
    }

    @Override
    public void onPrepare(@NonNull NativeStaticViewHolder staticNativeViewHolder, @NonNull List<? extends View> viewList) {
        if (mNativeAd == null || staticNativeViewHolder.mainView == null) {
            return;
        }
        this.staticNativeViewHolder = staticNativeViewHolder;
        if (DEBUG) {
            Log.d(TAG, "prepare 控件的展示和点击事件绑定");
        }
        try {

            AdChoicesBinder adChoicesBinder = new AdChoicesBinder(mContext, staticNativeViewHolder, mNativeAd);
            BaseNativeAd baseNativeAd = mNativeAd.getBaseNativeAd();

            List<View> clickableViews = FastClickUtils.getClickableViews(mContext,staticNativeViewHolder,baseNativeAd,mUnitId);

            if (baseNativeAd instanceof StaticNativeAd) {

                adChoicesBinder.mopubNativePrepare(((StaticNativeAd) baseNativeAd).getMainImageUrl(), ((StaticNativeAd) baseNativeAd).getIconImageUrl());
            } else if (baseNativeAd instanceof com.mopub.nativeads.FacebookNative.FacebookVideoEnabledNativeAd) {

                FacebookNative.FacebookVideoEnabledNativeAd facebookVideoEnabledNativeAd = (com.mopub.nativeads.FacebookNative.FacebookVideoEnabledNativeAd) baseNativeAd;
                adChoicesBinder.facebookNativePrepare(facebookVideoEnabledNativeAd,clickableViews);
            } else if (baseNativeAd instanceof com.mopub.nativeads.GooglePlayServicesNative.GooglePlayServicesNativeAd) {
                // TODO: 18-6-14  找亚洲
                adChoicesBinder.admobNativePrepare((GooglePlayServicesNative.GooglePlayServicesNativeAd) baseNativeAd, clickableViews,
                        ((GooglePlayServicesNative.GooglePlayServicesNativeAd) baseNativeAd).getMainImageUrl(),
                        ((GooglePlayServicesNative.GooglePlayServicesNativeAd) baseNativeAd).getIconImageUrl());
            }

//            else if (baseNativeAd instanceof FlurryCustomEventNative.FlurryVideoEnabledNativeAd) {
//
//                adChoicesBinder.normalNativePrepare(((FlurryCustomEventNative.FlurryVideoEnabledNativeAd) baseNativeAd).getMainImageUrl(), ((FlurryCustomEventNative.FlurryVideoEnabledNativeAd) baseNativeAd).getIconImageUrl());
//            }

            mNativeAd.prepare(staticNativeViewHolder.mainView);


        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "prepare: " + e);
            }
        }
    }

    private void fastClickOpt(@NonNull NativeStaticViewHolder staticNativeViewHolder, BaseNativeAd baseNativeAd) {
        if (baseNativeAd instanceof FacebookNative.FacebookVideoEnabledNativeAd) {
            FastClickProp fastClickProp = FastClickProp.getInstance(mContext.getApplicationContext());
            if (fastClickProp.isEnabaleFastClickOpt(mUnitId)) {
                if (mFastClickUtils == null) {
                    mFastClickUtils = new FastClickUtils();
                }
                long fastClickDelayTime = fastClickProp.getFCDMS(mUnitId);
                mFastClickUtils.addCoverViewIfNeed(staticNativeViewHolder, fastClickDelayTime);
            }
        }
    }

    @Override
    public boolean isLoaded() {
        // TODO: 2019/4/3 此接口不在Native中使用
        return false;
    }

    @Override
    public void show() {

    }

    public com.ads.lib.mediation.bean.NativeAd setContentNative(NativeAd nativeAd) {
        mNativeAd = nativeAd;
        BaseNativeAd baseNativeAd = nativeAd.getBaseNativeAd();
        com.ads.lib.mediation.bean.NativeAd.Builder builder = new com.ads.lib.mediation.bean.NativeAd.Builder(this);

        if (baseNativeAd instanceof FacebookNative.FacebookVideoEnabledNativeAd) {
            FacebookNative.FacebookVideoEnabledNativeAd facebookVideoEnabledNativeAd = (FacebookNative.FacebookVideoEnabledNativeAd) baseNativeAd;
            builder.setTitle(facebookVideoEnabledNativeAd.getTitle())
                    .setCallToAction(facebookVideoEnabledNativeAd.getCallToAction())
                    .setText(facebookVideoEnabledNativeAd.getText())
                    .setContext(mContext)
                    .setUnitId(mUnitId)
                    .setPlacementID(mPlacementID)
                    .setPositionId(mPositionId);
        } else if (baseNativeAd instanceof GooglePlayServicesNative.GooglePlayServicesNativeAd) {
            GooglePlayServicesNative.GooglePlayServicesNativeAd googlePlayServicesNativeAd = (GooglePlayServicesNative.GooglePlayServicesNativeAd) baseNativeAd;
            builder.setTitle(googlePlayServicesNativeAd.getTitle())
                    .setCallToAction(googlePlayServicesNativeAd.getCallToAction())
                    .setText(googlePlayServicesNativeAd.getText())
                    .setIconImageUrl(googlePlayServicesNativeAd.getIconImageUrl())
                    .setMainImageUrl(googlePlayServicesNativeAd.getMainImageUrl())
                    .setContext(mContext)
                    .setUnitId(mUnitId)
                    .setPlacementID(mPlacementID)
                    .setPositionId(mPositionId);
        } else if (baseNativeAd instanceof StaticNativeAd) {
            StaticNativeAd moPubStaticNativeAd = (StaticNativeAd) baseNativeAd;
            builder.setTitle(moPubStaticNativeAd.getTitle())
                    .setCallToAction(moPubStaticNativeAd.getCallToAction())
                    .setText(moPubStaticNativeAd.getText())
                    .setIconImageUrl(moPubStaticNativeAd.getIconImageUrl())
                    .setMainImageUrl(moPubStaticNativeAd.getMainImageUrl())
                    .setContext(mContext)
                    .setUnitId(mUnitId)
                    .setPlacementID(mPlacementID)
                    .setPositionId(mPositionId);
        }
//        else if (baseNativeAd instanceof FlurryCustomEventNative.FlurryVideoEnabledNativeAd) {
//            FlurryCustomEventNative.FlurryVideoEnabledNativeAd flurryVideoEnabledNativeAd = (FlurryCustomEventNative.FlurryVideoEnabledNativeAd) baseNativeAd;
//            builder.setTitle(flurryVideoEnabledNativeAd.getTitle())
//                    .setCallToAction(flurryVideoEnabledNativeAd.getCallToAction())
//                    .setText(flurryVideoEnabledNativeAd.getText())
//                    .setIconImageUrl(flurryVideoEnabledNativeAd.getIconImageUrl())
//                    .setMainImageUrl(flurryVideoEnabledNativeAd.getMainImageUrl())
//                    .setContext(mContext)
//                    .setUnitId(mUnitId)
//                    .setPlacementID(mPlacementID)
//                    .setPositionId(mPositionId);
//        }
        wrapNativeAd();

        builder.setmExpireTime(System.currentTimeMillis());
        return builder.isBanner(false).build();
    }

    private void wrapNativeAd() {
        mNativeAd.setMoPubNativeEventListener(new NativeAd.MoPubNativeEventListener() {
            @Override
            public void onImpression(View view) {
                BaseNativeAd baseNativeAd = mNativeAd.getBaseNativeAd();
                if(FastClickUtils.isNeedIntercepClick(mContext,baseNativeAd)){
                    fastClickOpt(staticNativeViewHolder, baseNativeAd);
                }
                if (nativeEventListener != null) {
                    if (DEBUG) {
                        Log.d(TAG, "onImpression: ");
                    }
                    nativeEventListener.onAdImpressed();
                    isDisplay = true;
                }
            }

            @Override
            public void onClick(View view) {
                if (nativeEventListener != null) {
                    if (DEBUG) {
                        Log.d(TAG, "onClick: ");
                    }
                    nativeEventListener.onAdClicked();
                }
            }
        });
    }

    @Override
    public boolean isExpired() {
        return ExpireTimeUtils.isExpired(mContext, SharedPref.SP_KEY_LAST_LOAD_SUCCESS_NATIVE_TIME);
    }

    @Override
    public boolean isDestroyed() {
        return isDestroy;
    }

    @Override
    public boolean isImpress() {
        return isDisplay;
    }

    @Override
    public void clear(View mainView) {
        destroy();
    }

}
