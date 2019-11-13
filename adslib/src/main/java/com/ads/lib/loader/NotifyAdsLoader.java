package com.ads.lib.loader;

import android.content.Context;
import android.util.Log;

import com.ads.lib.AdUnitId;
import com.ads.lib.AdsLoader;
import com.ads.lib.prop.AdPositionIdProp;
import com.baselib.cloud.CloudPropertyManager;
import com.hotvideo.config.GlobalConfig;

public class NotifyAdsLoader extends BaseAdsLoader {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "NotifyAdsLoader";
    private int cardType;

    public NotifyAdsLoader(Context cxt, int type) {
        super(cxt);
        cardType = type;
    }

    @Override
    public String getNativeAdUnitId(Context cxt) {
        String defaultUnitId = "SAM_NotifyClean_Native";
        String keyUnitId = getUnitIdKey();
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_NOTIFY_CARD_AD,
                keyUnitId, defaultUnitId);
    }

    @Override
    public String getNativeAdPositionId(Context cxt) {
        return AdPositionIdProp.getInstance(cxt).getAdPositionIdByAdUnitId(getUnitIdKey());
    }

    @Override
    public String getPlacementId(Context cxt) {
        String keyPlacementId = "nc_card_ad_placement_id";
        String defaultPlacementId = "mp:a2834bfe29734ee59ec33295625aa05f,mp:31d08a7653664f9da9ac99d9fc70287f";
        switch (cardType) {
            case AdsLoader.TYPE_NC_CARD_AD:
                keyPlacementId = "nc_card_ad_placement_id";
                break;
            case AdsLoader.TYPE_NS_CARD_AD:
                keyPlacementId = "ns_card_ad_placement_id";
                break;
        }
        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_NOTIFY_CARD_AD,
                keyPlacementId, defaultPlacementId);
        if (DEBUG) {
            Log.d(TAG, "getPlacementId key:" + keyPlacementId + " =:" + res);
        }
        return res;
    }

    public String getNewUserPlacementId(Context cxt) {
        String keyPlacementId = "nc_card_ad_placement_id_firstday";
        String defaultPlacementId = "mp:a2834bfe29734ee59ec33295625aa05f,mp:31d08a7653664f9da9ac99d9fc70287f";
        switch (cardType) {
            case AdsLoader.TYPE_NC_CARD_AD:
                keyPlacementId = "nc_card_ad_placement_id_firstday";
                break;
            case AdsLoader.TYPE_NS_CARD_AD:
                keyPlacementId = "ns_card_ad_placement_id_firstday";
                break;
        }
        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_NOTIFY_CARD_AD,
                keyPlacementId, defaultPlacementId);
        if (DEBUG) {
            Log.d(TAG, "[new]getPlacementId key:" + keyPlacementId + " =:" + res);
        }
        return res;
    }

    @Override
    public float getLoadPossibility(Context cxt) {
        String keyPossibility = "nc_card_ad_possibility";
        float defaultPossibility = 1.0f;
        switch (cardType) {
            case AdsLoader.TYPE_NC_CARD_AD:
                keyPossibility = "nc_card_ad_possibility";
                break;
            case AdsLoader.TYPE_NS_CARD_AD:
                keyPossibility = "ns_card_ad_possibility";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getLoadPossibility key:" + keyPossibility + ", def:" + defaultPossibility);
        }
        return CloudPropertyManager.getFloat(cxt, CloudPropertyManager.PATH_NOTIFY_CARD_AD,
                keyPossibility, defaultPossibility);
    }

    @Override
    public boolean shouldPrepareIcon(Context cxt) {
        String keyShouldPrepareIcon = "nc_card_ad_should_prepare_icon";
        int defaultIconPreparation = 1;
        switch (cardType) {
            case AdsLoader.TYPE_NC_CARD_AD:
                keyShouldPrepareIcon = "nc_card_ad_should_prepare_icon";
                break;
            case AdsLoader.TYPE_NS_CARD_AD:
                keyShouldPrepareIcon = "ns_card_ad_should_prepare_icon";
                break;

        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareIcon key:" + keyShouldPrepareIcon + ", def:" + defaultIconPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_NOTIFY_CARD_AD,
                keyShouldPrepareIcon, defaultIconPreparation) == 1;
    }

    @Override
    public boolean shouldPrepareBanner(Context cxt) {
        String keyShouldPrepareBanner = "nc_card_ad_should_prepare_banner";
        int defaultBannerPreparation = 1;
        switch (cardType) {
            case AdsLoader.TYPE_NC_CARD_AD:
                keyShouldPrepareBanner = "nc_card_ad_should_prepare_banner";
                break;
            case AdsLoader.TYPE_NS_CARD_AD:
                keyShouldPrepareBanner = "ns_card_ad_should_prepare_banner";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareBanner key:" + keyShouldPrepareBanner + ", def:" + defaultBannerPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_NOTIFY_CARD_AD,
                keyShouldPrepareBanner, defaultBannerPreparation) == 1;
    }

    @Override
    public boolean useParallelRequest(Context cxt) {
        String keyUseParallel = "nc_card_ad_use_parallel_request";
        int defaultParallel = 1;
        switch (cardType) {
            case AdsLoader.TYPE_NC_CARD_AD:
                keyUseParallel = "nc_card_ad_use_parallel_request";
                break;
            case AdsLoader.TYPE_NS_CARD_AD:
                keyUseParallel = "ns_card_ad_use_parallel_request";
                break;

        }
        if (DEBUG) {
            Log.d(TAG, "useParallelRequest key:" + keyUseParallel + ", def:" + defaultParallel);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_NOTIFY_CARD_AD,
                keyUseParallel, defaultParallel) == 1;
    }

    @Override
    public long getBestWaitingTime(Context cxt) {
        String keyBestWaitingTime = "nc_card_ad_best_waiting_time";
        long defaultBestWaitingTime = 5000L;
        switch (cardType) {
            case AdsLoader.TYPE_NC_CARD_AD:
                keyBestWaitingTime = "nc_card_ad_best_waiting_time";
                break;
            case AdsLoader.TYPE_NS_CARD_AD:
                keyBestWaitingTime = "ns_card_ad_best_waiting_time";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getBestWaitingTime key:" + keyBestWaitingTime + ", def:" + defaultBestWaitingTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_NOTIFY_CARD_AD,
                keyBestWaitingTime, defaultBestWaitingTime);
    }

    @Override
    public long getAdsExpiryTime(Context cxt) {
        String keyAdsExpiryTime = "nc_card_ad_ads_expiry_time";
        long defaultAdsExpiryTime = 60 * 60 * 1000L;
        switch (cardType) {
            case AdsLoader.TYPE_NC_CARD_AD:
                keyAdsExpiryTime = "nc_card_ad_ads_expiry_time";
                break;
            case AdsLoader.TYPE_NS_CARD_AD:
                keyAdsExpiryTime = "ns_card_ad_ads_expiry_time";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getAdsExpiryTime key:" + keyAdsExpiryTime + ", def:" + defaultAdsExpiryTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_NOTIFY_CARD_AD,
                keyAdsExpiryTime, defaultAdsExpiryTime);
    }

    private static final String[] CONFIGS = new String[]{CloudPropertyManager.PATH_NOTIFY_CARD_AD};

    @Override
    public String[] getDependencyConfigs() {
        return CONFIGS;
    }

    private String getUnitIdKey() {
        String keyUnitId = AdUnitId.UNIT_ID_NC_CARD;
        switch (cardType) {
            case AdsLoader.TYPE_NC_CARD_AD:
                keyUnitId = AdUnitId.UNIT_ID_NC_CARD;
                break;
        }
        return keyUnitId;
    }
}
