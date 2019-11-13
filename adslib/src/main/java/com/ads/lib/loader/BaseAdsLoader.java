package com.ads.lib.loader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.ads.lib.AdsLoaderCallback;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.mediation.cache.CacheManager;
import com.ads.lib.mediation.loader.natives.NativeAdListener;
import com.ads.lib.mediation.loader.natives.NativeAdLoader;
import com.ads.lib.util.InstallTimeHelper;
import com.ads.lib.util.PossibilityCalculator;
import com.baselib.cloud.CloudPropertyManager;
import com.hotvideo.config.GlobalConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseAdsLoader {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "BaseAdsLoader";

    protected Context mContext;
    protected List<NativeAd> mAds = new ArrayList<>();

    private Handler mUiHandler = null;

    private static final Object sLock = new Object();
    private List<AdsLoaderCallback> mAdsLoaderCallbackList = new CopyOnWriteArrayList<AdsLoaderCallback>();

    public BaseAdsLoader(Context cxt) {
        mContext = cxt;
        mUiHandler = new Handler(Looper.getMainLooper());
    }

    private NativeAdLoader mStarkAdsManager;


    private boolean isInited = false;

    public abstract String getNativeAdUnitId(Context cxt);

    public abstract String getNativeAdPositionId(Context cxt);

    public abstract String getPlacementId(Context cxt);

    /**
     * 新用户可能会使用不同的广告ID
     *
     * @param cxt
     * @return
     */
    public String getNewUserPlacementId(Context cxt) {
        return null;
    }

    public abstract float getLoadPossibility(Context cxt);

    public abstract boolean shouldPrepareIcon(Context cxt);

    public abstract boolean shouldPrepareBanner(Context cxt);

    public abstract boolean useParallelRequest(Context cxt);

    public abstract long getBestWaitingTime(Context cxt);

    public abstract long getAdsExpiryTime(Context cxt);

    private synchronized void init() {
        if (isInited)
            return;

        String nativeAdUnitId = getNativeAdUnitId(mContext);
        String placementId = getPlacementId(mContext);
        if (InstallTimeHelper.isNewUser(mContext)) {
            if (DEBUG)
                Log.v(TAG, "#new user " + this);
            String newPlacementId = getNewUserPlacementId(mContext);
            if (!TextUtils.isEmpty(newPlacementId)) {
                placementId = newPlacementId;
                if (DEBUG)
                    Log.v(TAG, "#use new placementId " + placementId);
            }
        }

        if (DEBUG) {
            Log.i(TAG, this + " nativeAdUnitId = " + nativeAdUnitId);
            Log.i(TAG, this + " placement id = " + placementId);
        }

        // StarkSDK now works with only 1 AdsManager for different ad sources (FB, AdMob, AppLovin, etc)
        mStarkAdsManager = new NativeAdLoader(mContext, nativeAdUnitId, getNativeAdPositionId(mContext));


        isInited = true;
    }


    private NativeAdListener mListener = new NativeAdListener() {
        @Override
        public void onAdFail(AdErrorCode adErrorCode) {
//            if (DEBUG) {
//                Log.i(TAG, "error " + adErrorCode);
//                String toastStr = "(Debug Toast) onNativeFail: " + "\n" + adErrorCode.toString();
//                ToastUtils.showToast(Toast.makeText(mContext, toastStr, Toast.LENGTH_LONG));
//            }
            String unitId = getNativeAdUnitId(mContext);
            String adPositionId = getNativeAdPositionId(mContext);
            notifyLoadResult(false, unitId, adPositionId);
        }

        @Override
        public void onAdLoaded(NativeAd nativeAd) {
            if (nativeAd != null) {
                if (DEBUG) {
                    Log.d(TAG, "onAdLoaded success");
                }
                String unitId = getNativeAdUnitId(mContext);
                String adPositionId = getNativeAdPositionId(mContext);
                notifyLoadResult(true, unitId, adPositionId);
                CacheManager.getInstance(mContext).enqueueAd(adPositionId, unitId, nativeAd);
            } else {
                onAdFail(AdErrorCode.NETWORK_NO_FILL);
            }
        }
    };


    /**
     * Only load ads if the requested count is more than the available adsCount
     *
     * @param count 需要加载的广告条数
     */
    public void loadAds(int count) {
        loadAds(count, null, false);
    }

    public void loadAds(int count, final AdsLoaderCallback callback) {
        loadAds(count, callback, false);
    }

    /**
     * Only load ads if the requested count is more than the available adsCount.
     * Don't forget to remove callback when finished
     * @param count 需要加载的广告条数
     */


    /**
     * Only load ads if the requested count is more than the available adsCount.
     * Don't forget to remove callback when finished
     *
     * @param count                需要加载的广告条数
     * @param callback             广告毁掉
     * @param skipPossibilityCheck 是否需要跳过概率计算过程
     */
    public void loadAds(int count, final AdsLoaderCallback callback, boolean skipPossibilityCheck) {


        registerCallback(callback);

        int availableAdsCount = getAvailableAdsCount();
        final int adsToLoad = count - availableAdsCount;
        float possibility = getLoadPossibility(mContext);
        if (possibility < 0) {
            possibility = 1;
        }
        if (DEBUG) {
            Log.i(TAG, "load " + count + " ad(s)");
            Log.i(TAG, "availableAds = " + availableAdsCount);
            Log.i(TAG, "adsToLoad = " + adsToLoad);
            Log.i(TAG, "resultAD load possibility=" + possibility);
            Log.i(TAG, "skipPossibilityCheck=" + skipPossibilityCheck);
        }
        boolean possibilitySuccess = skipPossibilityCheck ||
                PossibilityCalculator.satisfyPossibility(possibility);

        if (DEBUG) {
            Log.i(TAG, "final check-->possibilitySuccess:" + possibilitySuccess);
        }

        if (possibilitySuccess && adsToLoad > 0 && availableAdsCount == 0) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                init();
                realLoadAds(adsToLoad);
            } else {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        realLoadAds(adsToLoad);
                    }
                });
            }
        }
    }

    /**
     * Method to really start loading ads
     *
     * @param adsToLoad number of ads to load
     */
    /*private void realLoadAds(int adsToLoad) {
        realLoadAds(adsToLoad, null);
    }*/

    /**
     * Method to really start loading ads
     *
     * @param adsToLoad number of ads to load
     */
    private void realLoadAds(int adsToLoad) {
        if (mStarkAdsManager != null) {
            if (DEBUG) {
                Log.i(TAG, "*****************************************");
                Log.i(TAG, "*** LOADING " + adsToLoad + " AD(S)");
                Log.i(TAG, "*** Object: " + this);
                Log.i(TAG, "*****************************************");
            }
            mStarkAdsManager.setAdListener(mListener);
            notifyRealLoad();
            mStarkAdsManager.load();
        }
    }

    /**
     * 获取可用的广告个数
     *
     * @return
     */
    public int getAvailableAdsCount() {
        if (mContext != null) {
            String unitId = getNativeAdUnitId(mContext);
            String positionId = getNativeAdPositionId(mContext);
            int count = CacheManager.getInstance(mContext).getAdCount(unitId, positionId);
            if (DEBUG) {
                Log.d(TAG, ":getAvailableAdsCount " + count);
            }
            return count;
        }
        return 0;
    }
    /*
    public boolean isContainFbAd() {
        synchronized (mAds) {
            for (int i = 0; i < mAds.size(); i++) {
                NativeAd ad = mAds.get(i);
                if (ad != null && "fb".equals(ad.getSourceTag())) {
                    return true;
                }
            }
        }
        return false;
    }
    */

    /**
     * Extract ads from a list of NativeAds stored in this class
     *
     * @param count
     * @param autoRemove
     * @return
     */
    public List<NativeAd> popAvailableAds(int count, boolean autoRemove) {
        String unitId = getNativeAdUnitId(mContext);
        String positionId = getNativeAdPositionId(mContext);
        if (DEBUG) {
            int startCount = CacheManager.getInstance(mContext).getAdCount(unitId, positionId);
            Log.d(TAG, ":getAvailableAdsCount startCount " + startCount);
        }
        List<NativeAd> ads = new ArrayList<>();
        if (mContext == null) {
            return ads;
        }
        NativeAd nativeAd = CacheManager.getInstance(mContext).dequeueNativeAd(getNativeAdUnitId(mContext), getNativeAdPositionId(mContext));
        if (DEBUG) {
            Log.d(TAG, ":getNativeAdUnitId " + getNativeAdUnitId(mContext) + "getNativeAdPositionId" + getNativeAdPositionId(mContext));
        }
        if (nativeAd == null) {
            return ads;
        } else {
            ads.add(nativeAd);
        }
        if (DEBUG) {
            int endCount = CacheManager.getInstance(mContext).getAdCount(unitId, positionId);
            Log.d(TAG, ":getAvailableAdsCount endCount " + endCount);
        }
        return ads;
    }

    public List<NativeAd> popAvailableAds(int count) {
        return popAvailableAds(count, true);
    }

    /**
     * 针对一个广告是否可以展示的判断方法
     *
     * @param nativeAd
     * @return {@code true} if ad is valid, and {@code false} otherwise
     */
//    protected boolean isAdReadyToBeShown(NativeAd nativeAd) {
//        return nativeAd != null
//                && !nativeAd.isExpired() // 是否过期了
//                && !nativeAd.isRecordedClicked() // 是否点击过
//                && !nativeAd.isDestroyed(); // 是否销毁了
//    }

    /**
     * Method to manually notify this class to remove an Ad from its storage
     *
     * @param nativeAd
     * @param isShown
     */
    public void setAdShowStatus(NativeAd nativeAd, boolean isShown) {
        if (nativeAd == null)
            return;

        if (DEBUG) {
            Log.i(TAG, "setAdShowStatus | mAds size before:" + mAds.size());
        }

        if (isShown && mAds != null) {
            if (DEBUG) {
                Log.i(TAG, "setAdShowStatus | mAds:" + mAds);
            }
            synchronized (mAds) {
                mAds.remove(nativeAd);
            }
        }

        if (DEBUG) {
            Log.i(TAG, "setAdShowStatus | mAds size after:" + mAds.size());
        }
    }

    private void addAds(NativeAd nad) {
        if (nad == null) {
            return;
        }

//        if (TextUtils.isEmpty(nad.getMainImageUrl())) {
//            if (DEBUG) {
//                Log.i(TAG, "image or url is null");
//            }
//            return;
//        }
        if (nad.isBanner) {
            if (DEBUG) {
                Log.i(TAG, "nad.isBanner():" + nad.isBanner);
            }
            return;
        }

        synchronized (mAds) {
            mAds.add(nad);
            if (DEBUG) {
                Log.i(TAG, "Storage has " + mAds.size() + " Ads");
            }
        }
    }

//    private void sortNativeAdsCacheByWeight(List<NativeAd> nativeAds) {
//        if (nativeAds != null && !nativeAds.isEmpty()) {
//            Collections.sort(nativeAds, new Comparator<NativeAd>() {
//
//                @Override
//                public int compare(NativeAd lhs, NativeAd rhs) {
//                    return Integer.compare(rhs.weight, lhs.weight);
//                }
//            });
//        }
//
//    }

    public void registerCallback(AdsLoaderCallback callback) {
        synchronized (sLock) {
            if (DEBUG) {
                Log.i(TAG, "registerCallback | alr contained: " + mAdsLoaderCallbackList.contains(callback));
            }
            if (mAdsLoaderCallbackList != null && !mAdsLoaderCallbackList.contains(callback)) {
                mAdsLoaderCallbackList.add(callback);
            }
        }
    }

    public void removeCallback(AdsLoaderCallback callback) {
        synchronized (sLock) {
            if (mAdsLoaderCallbackList != null && mAdsLoaderCallbackList.contains(callback)) {
                mAdsLoaderCallbackList.remove(callback);
            }
        }
    }

    private void notifyLoadResult(final boolean isAdLoaded, final String unitId, final String adPositionId) {
        if (mUiHandler == null)
            return;
        if (DEBUG) {
            if ("SAM_NotifyClean_Native".equals(unitId)) {
                Log.d(TAG, "loadAds: 通知栏清理结果页原生广告  请求结果：" + isAdLoaded);
            }
        }
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (sLock) {
                    if (mAdsLoaderCallbackList != null) {
                        for (AdsLoaderCallback callback : mAdsLoaderCallbackList) {
                            if (callback != null) {
                                callback.onLoadResult(isAdLoaded, unitId, adPositionId);
                            }
                        }
                    }
                }
            }
        });
    }

    private void notifyRealLoad() {
        if (mUiHandler == null)
            return;
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (sLock) {
                    if (mAdsLoaderCallbackList != null) {
                        for (AdsLoaderCallback callback : mAdsLoaderCallbackList) {
                            if (callback != null) {
                                callback.onRealLoad();
                            }

                        }
                    }
                }
            }
        });
    }

    public boolean isLoading() {
        return false;
    }

    public boolean shouldReload() {
        return CloudPropertyManager.isDirty(mContext, getDependencyConfigs());
    }

    public abstract String[] getDependencyConfigs();
}
