package com.ads.lib.loader;

import android.content.Context;
import android.util.Log;

import com.ads.lib.AdUnitId;
import com.ads.lib.AdsLoader;
import com.ads.lib.prop.AdPositionIdProp;
import com.baselib.cloud.CloudPropertyManager;
import com.goodmorning.config.GlobalConfig;


public class MainNativeAdsLoader extends BaseAdsLoader {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "HomeTopAdsLoader";
    private int mResultType;

    public MainNativeAdsLoader(Context cxt, int type) {
        super(cxt);
        mResultType = type;
    }

    @Override
    public String getNativeAdUnitId(Context cxt) {
        String defaultUnitId = "FMG-Homepage-Ad-Native-0309";
        String keyUnitId = getUnitIdKey();
        if (DEBUG) {
            Log.d(TAG, "getNativeAdUnitId key:" + keyUnitId + ", def:" + defaultUnitId);
        }
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_HOME_AD_PROP,
                keyUnitId, defaultUnitId);
    }

    @Override
    public String getNativeAdPositionId(Context cxt) {
        return AdPositionIdProp.getInstance(cxt).getAdPositionIdByAdUnitId(getUnitIdKey());
    }

    @Override
    public String getPlacementId(Context cxt) {
        String keyPlacementId = "home_top_ad_placement_id";
        String defaultPlacementId = "mp:211d85a6cb4949ba9b25eeac9b89e096,mp:5aed135dbbde46a49551efa1098615ef,bat:12007_39740,mp:09ce0a58a4cd4965a35fb11d236e843d,al:EVER5_NATIVE,ath:EVER5_NATIVE";
        switch (mResultType) {
            case AdsLoader.TYPE_HOME_TOP_AD:
                keyPlacementId = "home_top_ad_placement_id";
                break;
        }
        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_HOME_AD_PROP,
                keyPlacementId, defaultPlacementId);
        if (DEBUG) {
            Log.d(TAG, "getPlacementId key:" + keyPlacementId + " =:" + res);
        }
        return res;
    }

    public String getNewUserPlacementId(Context cxt) {
        String keyPlacementId = "home_top_ad_placement_id_firstday";
        String defaultPlacementId = "mp:211d85a6cb4949ba9b25eeac9b89e096,mp5aed135dbbde46a49551efa1098615ef,bat:12007_39740,mp:09ce0a58a4cd4965a35fb11d236e843d,al:EVER5_NATIVE,ath:EVER5_NATIVE";
        switch (mResultType) {
            case AdsLoader.TYPE_HOME_TOP_AD:
                keyPlacementId = "home_top_ad_placement_id_firstday";
                break;
        }
        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_HOME_AD_PROP,
                keyPlacementId, defaultPlacementId);
        if (DEBUG) {
            Log.d(TAG, "[new]getPlacementId key:" + keyPlacementId + " =:" + res);
        }
        return res;
    }

    @Override
    public float getLoadPossibility(Context cxt) {
        String keyPossibility = "home_top_ad_possibility";
        float defaultPossibility = 1.0f;
        switch (mResultType) {
            case AdsLoader.TYPE_HOME_TOP_AD:
                keyPossibility = "home_top_ad_possibility";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getLoadPossibility key:" + keyPossibility + ", def:" + defaultPossibility);
        }
        return CloudPropertyManager.getFloat(cxt, CloudPropertyManager.PATH_HOME_AD_PROP,
                keyPossibility, defaultPossibility);
    }

    @Override
    public boolean shouldPrepareIcon(Context cxt) {
        String keyShouldPrepareIcon = "home_top_ad_should_prepare_icon";
        int defaultIconPreparation = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_HOME_TOP_AD:
                keyShouldPrepareIcon = "home_top_ad_should_prepare_icon";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareIcon key:" + keyShouldPrepareIcon + ", def:" + defaultIconPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_HOME_AD_PROP,
                keyShouldPrepareIcon, defaultIconPreparation) == 1;
    }

    @Override
    public boolean shouldPrepareBanner(Context cxt) {
        String keyShouldPrepareBanner = "home_top_ad_should_prepare_banner";
        int defaultBannerPreparation = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_HOME_TOP_AD:
                keyShouldPrepareBanner = "home_top_ad_should_prepare_banner";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareBanner key:" + keyShouldPrepareBanner + ", def:" + defaultBannerPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_HOME_AD_PROP,
                keyShouldPrepareBanner, defaultBannerPreparation) == 1;
    }

    @Override
    public boolean useParallelRequest(Context cxt) {
        String keyUseParallel = "home_top_ad_use_parallel_request";
        int defaultParallel = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_HOME_TOP_AD:
                keyUseParallel = "home_top_ad_use_parallel_request";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "useParallelRequest key:" + keyUseParallel + ", def:" + defaultParallel);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_HOME_AD_PROP,
                keyUseParallel, defaultParallel) == 1;
    }

    @Override
    public long getBestWaitingTime(Context cxt) {
        String keyBestWaitingTime = "home_top_ad_best_waiting_time";
        long defaultBestWaitingTime = 5000L;
        switch (mResultType) {
            case AdsLoader.TYPE_HOME_TOP_AD:
                keyBestWaitingTime = "home_top_ad_best_waiting_time";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getBestWaitingTime key:" + keyBestWaitingTime + ", def:" + defaultBestWaitingTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_HOME_AD_PROP,
                keyBestWaitingTime, defaultBestWaitingTime);
    }

    @Override
    public long getAdsExpiryTime(Context cxt) {
        String keyAdsExpiryTime = "home_top_ad_ads_expiry_time";
        long defaultAdsExpiryTime = 60 * 60 * 1000L;
        switch (mResultType) {
            case AdsLoader.TYPE_HOME_TOP_AD:
                keyAdsExpiryTime = "home_top_ad_ads_expiry_time";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getAdsExpiryTime key:" + keyAdsExpiryTime + ", def:" + defaultAdsExpiryTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_HOME_AD_PROP,
                keyAdsExpiryTime, defaultAdsExpiryTime);
    }

    private static final String[] CONFIGS = new String[]{CloudPropertyManager.PATH_HOME_AD_PROP};

    @Override
    public String[] getDependencyConfigs() {
        return CONFIGS;
    }

    private String getUnitIdKey() {
        String keyUnitId = AdUnitId.UNIT_ID_HOME_TOP;
        switch (mResultType) {
            case AdsLoader.TYPE_HOME_TOP_AD:
                keyUnitId = AdUnitId.UNIT_ID_HOME_TOP;
                break;
        }
        return keyUnitId;
    }
}
