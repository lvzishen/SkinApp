package com.ads.lib.mediation.loader.dispatcher;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.ads.lib.adapter.LocalStrategy;
import com.ads.lib.commen.AdLifecyclerManager;
import com.ads.lib.init.MoPubStarkInit;
import com.ads.lib.log.AdStatisticLoggerX;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.BaseAd;
import com.ads.lib.mediation.bean.InterstitialWrapperAd;
import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.mediation.cache.CacheManager;
import com.ads.lib.mediation.config.IAllowLoaderAdListener;
import com.ads.lib.mediation.constants.Constants;
import com.ads.lib.mediation.helper.LogHelper;
import com.ads.lib.mediation.helper.SharedPoolHelper;
import com.ads.lib.mediation.loader.LoaderFactory;
import com.ads.lib.mediation.loader.base.AdListener;
import com.ads.lib.mediation.loader.base.IAdLoader;
import com.ads.lib.mediation.loader.interstitial.IInterstitialWrapperAdAdLoader;
import com.ads.lib.mediation.loader.interstitial.InterstitialWrapperAdAdListener;
import com.ads.lib.mediation.loader.interstitial.MopubInterstitialWrapperAdLoader;
import com.ads.lib.mediation.loader.natives.INativeAdLoader;
import com.ads.lib.mediation.loader.natives.MopubNativeAdLoader;
import com.ads.lib.mediation.loader.natives.MopubRectangleBannerAdLoader;
import com.ads.lib.mediation.loader.natives.MopubStripBannerAdLoader;
import com.ads.lib.mediation.loader.natives.NativeAdListener;
import com.ads.lib.prop.AdCacheProp;
import com.ads.lib.prop.AdIDMappingProp;
import com.ads.lib.util.PageUtil;
import com.ads.lib.util.StrategyUtil;
import com.goodmorning.config.GlobalConfig;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdLoaderDispatcher {

    public static final String TAG = "AdLoaderDispatcher";
    public static final boolean DEBUG = GlobalConfig.DEBUG;


    private IAdLoader mAdLoader;
    private List<LocalStrategy> mStrategies;
    private AdListener mAdListener;
    private String mUnitId;
    private Context mContext;
    private String mPlacementID;
    private String mPoolPositionId;
    private String positionId;
    private boolean isLoading = false;
    private boolean isAlreadyCallback = false;
    private long mStartTime;
    private String mLogAdType;


    public AdLoaderDispatcher(@NotNull Context context, @NotNull String unitId, @NotNull String positionId) {
        this.mContext = context;
        this.mUnitId = unitId;
        this.positionId = positionId;
        this.mPoolPositionId = AdIDMappingProp.getInstance(context).getCachePoolPositionId(positionId);
        correctData();
        this.mPlacementID = AdIDMappingProp.getInstance(context).getPlacementId(mPoolPositionId);
        if (DEBUG) {
            Log.d(TAG, String.format("<init> positionId = [%s], mPlacementID = [%s], mPoolPositionId = [%s] unitId = [%s] ", positionId, mPlacementID, mPoolPositionId, unitId));
        }

    }

    private void correctData() {
        if(positionId.contains("_")){
            this.positionId = mPoolPositionId;
        }
        //如果是公共缓存池，那么他没有广告位pid，广告位pid就是广告池pid
        if(mPoolPositionId.contains("mp")){
            mPoolPositionId = positionId;
        }
    }

    public void setAdListener(AdListener listener) {
        this.mAdListener = listener;
    }

    public void load() {
        if (!isAllowLoadAd()) {
            if (mAdListener != null) {
                mAdListener.onAdFail(AdErrorCode.AD_POSITION_CLOSED);
            }
            LogHelper.logD(TAG, "#load ad position is closed");
            return;
        }
        if (isLoading) {
            LogHelper.logD(TAG, "#load isLoading break load");
            return;
        }
        isLoading = true;
        isAlreadyCallback = false;

        mStartTime = SystemClock.elapsedRealtime();

        mStrategies = StrategyUtil.getStrtegyByPlacementId(mPlacementID);

        logRequest(mStrategies);


        checkCache();

        if (needRequest(mUnitId, positionId)) {
            loadNextStrategy();
        } else {
            isLoading = false;
        }
    }

    private boolean isAllowLoadAd() {
        IAllowLoaderAdListener allowLoaderAdListener = MoPubStarkInit.getInstance().getAllowLoaderAdListener();
        if (allowLoaderAdListener != null) {
            return allowLoaderAdListener.isAllowLoaderAd(mUnitId, positionId);
        }
        return true;
    }


    /**
     * 请求打点
     *
     * @param strategies
     */
    private void logRequest(List<LocalStrategy> strategies) {
        if (strategies.size() <= 0) {
            return;
        }
        mLogAdType = AdStatisticLoggerX.getAdType(strategies);
        AdStatisticLoggerX.logLoadAdRequest(PageUtil.getPageName(positionId),
                positionId,
                AdStatisticLoggerX.ACTION_AD_REQUEST,
                AdStatisticLoggerX.AD_SOURCE_MOPUB,
                mLogAdType,
                AdStatisticLoggerX.AD_ACTION_VIEW,
                mPlacementID,
                mPoolPositionId);
    }


    private void logResult(boolean fromCache, AdErrorCode adErrorCode) {
        long diffTime = SystemClock.elapsedRealtime() - mStartTime;
        AdStatisticLoggerX.logonAdResult(PageUtil.getPageName(positionId),
                positionId,
                fromCache ? AdStatisticLoggerX.REQUEST_TYPE_CACHE : AdStatisticLoggerX.REQUEST_TYPE_REAL,
                AdStatisticLoggerX.ACTION_AD_REQUEST_RESULT,
                AdStatisticLoggerX.AD_SOURCE_MOPUB,
                mLogAdType,
                AdStatisticLoggerX.AD_ACTION_VIEW,
                mPlacementID, diffTime,
                adErrorCode == null ? "200" : adErrorCode.code,
                adErrorCode == null ? "result ok" : adErrorCode.message,
                mPoolPositionId);
    }

    private void checkCache() {
        CacheManager cacheManager = CacheManager.getInstance(mContext);
        boolean hasCache = cacheManager.hasCache(mUnitId, positionId);
        if (hasCache) {
            BaseAd baseAd = cacheManager.dequeueAd(mUnitId, positionId);
            callbackAd(baseAd, true);
        } else {
            checkSharedCache();
        }

    }

    private void checkSharedCache() {
        InterstitialWrapperAd interstitialAd = SharedPoolHelper.getInstance(mContext).getInterstitialAd(mContext, mUnitId, positionId);
        if(interstitialAd != null){
            if(DEBUG){
                if(DEBUG){
                    Log.d(TAG,"#checkSharedCache get ad from shared pool ");
                }
            }
            callbackAd(interstitialAd, true);
        }
    }

    private void callbackAd(BaseAd ad, boolean fromCache){
        if(ad == null){
            return;
        }
        if (!isAlreadyCallback && mAdListener != null) {
            logResult(fromCache, null);
            isAlreadyCallback = true;
            ad.setPlacementID(mPlacementID);
            ad.setFromCache(fromCache);
            mAdListener.onAdLoaded(ad);
        }
    }
    private boolean hasNextStrategy() {
        return mStrategies != null && mStrategies.size() > 0;
    }

    private void loadNextStrategy() {
        LocalStrategy strategy = mStrategies.remove(0);
        dispatch(strategy);
    }

    private void dispatch(LocalStrategy strategy) {
        if (!TextUtils.isEmpty(strategy.adType)) {
            switch (strategy.adType) {
                case Constants.AD_TYPE_FLAG_NATIVE:
                    mAdLoader = LoaderFactory.getNativeLoader(MopubNativeAdLoader.class.getSimpleName(),
                            mContext, mUnitId, strategy.placementId, positionId);
                    break;
                case Constants.AD_TYPE_FLAG_SIZE_MEDIUM_BANNER:
                    mAdLoader = LoaderFactory.getNativeLoader(MopubRectangleBannerAdLoader.class.getSimpleName(),
                            mContext, mUnitId, strategy.placementId, positionId);
                    break;
                case Constants.AD_TYPE_FLAG_SIZE_SMALL_BANNER:
                    mAdLoader = LoaderFactory.getNativeLoader(MopubStripBannerAdLoader.class.getSimpleName(),
                            mContext, mUnitId, strategy.placementId, positionId);
                    break;
                case Constants.AD_TYPE_FLAG_INTERSTITIAL:
                    Activity mainActivity = AdLifecyclerManager.getInstance(mContext).getMainActivity();
                    if (mainActivity != null) {
                        mAdLoader = LoaderFactory.getInterstitialWrapperLoader(MopubInterstitialWrapperAdLoader.class.getSimpleName(),
                                mainActivity, mUnitId, strategy.placementId, positionId);
                    }
                    break;
                default:
            }
            LogHelper.logD(TAG, String.format("#dispatch placementId = [%s]", strategy.placementId));
            if (mAdLoader != null) {
                if (mAdLoader instanceof INativeAdLoader) {
                    mAdLoader.setAdListener(new NativeAdListener() {
                        @Override
                        public void onAdFail(AdErrorCode adErrorCode) {
                            onAdFailProcess(adErrorCode);
                        }

                        @Override
                        public void onAdLoaded(NativeAd ad) {
                            onAdLoadedProcess(ad);

                        }
                    });
                }
                if (mAdLoader instanceof IInterstitialWrapperAdAdLoader) {
                    mAdLoader.setAdListener(new InterstitialWrapperAdAdListener() {
                        @Override
                        public void onAdFail(AdErrorCode adErrorCode) {
                            onAdFailProcess(adErrorCode);
                        }

                        @Override
                        public void onAdLoaded(InterstitialWrapperAd ad) {
                            onAdLoadedProcess(ad);
                        }
                    });

                }
                mAdLoader.load();
            } else {
                if (mAdListener != null) {
                    mAdListener.onAdFail(AdErrorCode.NATIVE_ADAPTER_NOT_FOUND);
                }
            }

        }
    }


    public void onAdFailProcess(AdErrorCode adErrorCode) {
        if (needRequest(mUnitId, positionId)) {
            loadNextStrategy();
        } else {
            if (!isAlreadyCallback && mAdListener != null) {
                isAlreadyCallback = true;
                logResult(false, adErrorCode);
                mAdListener.onAdFail(adErrorCode);
            }
            isLoading = false;
        }
    }


    public void onAdLoadedProcess(BaseAd ad) {
        if (needRequest(mUnitId, positionId)) {
            loadNextStrategy();
        }
        callbackAd(ad, false);
        isLoading = false;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void destroy() {
        if (mAdLoader != null) {
            mAdLoader.destory();
        }
        logResult(false, AdErrorCode.USER_CANCEL);
    }

    boolean needRequest(String unitId, String positionId) {
        return hasNextStrategy() && !isReachCacheLimit(unitId, positionId);
    }

    /**
     * 是否达到缓存数
     *
     * @param unitId
     * @param positionId
     * @return
     */
    boolean isReachCacheLimit(String unitId, String positionId) {
        boolean isUnitIdEmpty = TextUtils.isEmpty(unitId);
        int cacheCount = CacheManager.getInstance(mContext).getAdCount(unitId, positionId);
        int inventory = AdCacheProp.getInstance(mContext).getInventory(positionId);
        if (DEBUG) {
            Log.d(TAG, String.format("#isReachCacheLimit isUnitIdEmpty = [%b], cacheCount = [%d], inventory = [%d]", isUnitIdEmpty, cacheCount, inventory));
        }
        return isUnitIdEmpty || cacheCount >= inventory;
    }
}
