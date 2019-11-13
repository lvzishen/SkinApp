package com.ads.lib.loader;

import android.content.Context;
import android.util.Log;

import com.ads.lib.AdUnitId;
import com.ads.lib.AdsLoader;
import com.ads.lib.prop.AdPositionIdProp;
import com.baselib.cloud.CloudPropertyManager;
import com.hotvideo.config.GlobalConfig;

public class WhatsAppCardAdsLoader extends BaseAdsLoader {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "WhatsAppCardAdsLoader";
    private int mResultType;

    public WhatsAppCardAdsLoader(Context cxt, int type) {
        super(cxt);
        mResultType = type;
    }

    @Override
    public String getNativeAdUnitId(Context cxt) {
        String defaultUnitId = "FMG-WhatsApp-Listpage-Native-0309";
        String keyUnitId = getUnitIdKey();
        if (DEBUG) {
            Log.d(TAG, "getNativeAdUnitId key:" + keyUnitId + ", def:" + defaultUnitId);
        }
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_WHATSAPP_CARD_AD,
                keyUnitId, defaultUnitId);
    }

    @Override
    public String getNativeAdPositionId(Context cxt) {
        return AdPositionIdProp.getInstance(cxt).getAdPositionIdByAdUnitId(getUnitIdKey());
    }

    @Override
    public String getPlacementId(Context cxt) {
        String keyPlacementId = "whatsapp_card_list_ad_placement_id";
        String defaultPlacementId = "mp:8fc62e4c09cd49c9ae55357f3ff611c9,mp:1522d47f5f3f43fc95dd57ae901a7482";
        switch (mResultType) {
            case AdsLoader.TYPE_WHATSAPP_LIST_CARD_AD:
                keyPlacementId = "whatsapp_card_list_ad_placement_id";
                break;
        }
        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_WHATSAPP_CARD_AD,
                keyPlacementId, defaultPlacementId);
        if (DEBUG) {
            Log.d(TAG, "getPlacementId key:" + keyPlacementId + " =:" + res);
        }
        return res;
    }

    public String getNewUserPlacementId(Context cxt) {
        String keyPlacementId = "whatsapp_card_list_ad_placement_id_firstday";
        String defaultPlacementId = "mp:8fc62e4c09cd49c9ae55357f3ff611c9,mp:1522d47f5f3f43fc95dd57ae901a7482";
        switch (mResultType) {
            case AdsLoader.TYPE_WHATSAPP_LIST_CARD_AD:
                keyPlacementId = "whatsapp_card_list_ad_placement_id_firstday";
                break;
        }
        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_WHATSAPP_CARD_AD,
                keyPlacementId, defaultPlacementId);
        if (DEBUG) {
            Log.d(TAG, "[new]getPlacementId key:" + keyPlacementId + " =:" + res);
        }
        return res;
    }

    @Override
    public float getLoadPossibility(Context cxt) {
        String keyPossibility = "whatsapp_card_list_ad_possibility";
        float defaultPossibility = 1.0f;
        switch (mResultType) {
            case AdsLoader.TYPE_WHATSAPP_LIST_CARD_AD:
                keyPossibility = "whatsapp_card_list_ad_possibility";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getLoadPossibility key:" + keyPossibility + ", def:" + defaultPossibility);
        }
        return CloudPropertyManager.getFloat(cxt, CloudPropertyManager.PATH_WHATSAPP_CARD_AD,
                keyPossibility, defaultPossibility);
    }

    @Override
    public boolean shouldPrepareIcon(Context cxt) {
        String keyShouldPrepareIcon = "whatsapp_card_list_ad_should_prepare_icon";
        int defaultIconPreparation = 0;
        switch (mResultType) {
            case AdsLoader.TYPE_WHATSAPP_LIST_CARD_AD:
                keyShouldPrepareIcon = "whatsapp_card_list_ad_should_prepare_icon";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareIcon key:" + keyShouldPrepareIcon + ", def:" + defaultIconPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_WHATSAPP_CARD_AD,
                keyShouldPrepareIcon, defaultIconPreparation) == 1;
    }

    @Override
    public boolean shouldPrepareBanner(Context cxt) {
        String keyShouldPrepareBanner = "whatsapp_card_list_ad_should_prepare_banner";
        int defaultBannerPreparation = 0;
        switch (mResultType) {
            case AdsLoader.TYPE_WHATSAPP_LIST_CARD_AD:
                keyShouldPrepareBanner = "whatsapp_card_list_ad_should_prepare_banner";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareBanner key:" + keyShouldPrepareBanner + ", def:" + defaultBannerPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_WHATSAPP_CARD_AD,
                keyShouldPrepareBanner, defaultBannerPreparation) == 1;
    }

    @Override
    public boolean useParallelRequest(Context cxt) {
        String keyUseParallel = "whatsapp_card_list_ad_use_parallel_request";
        int defaultParallel = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_WHATSAPP_LIST_CARD_AD:
                keyUseParallel = "whatsapp_card_list_ad_use_parallel_request";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "useParallelRequest key:" + keyUseParallel + ", def:" + defaultParallel);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_WHATSAPP_CARD_AD,
                keyUseParallel, defaultParallel) == 1;
    }

    @Override
    public long getBestWaitingTime(Context cxt) {
        String keyBestWaitingTime = "whatsapp_card_list_ad_best_waiting_time";
        long defaultBestWaitingTime = 5000L;
        switch (mResultType) {
            case AdsLoader.TYPE_WHATSAPP_LIST_CARD_AD:
                keyBestWaitingTime = "whatsapp_card_list_ad_best_waiting_time";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getBestWaitingTime key:" + keyBestWaitingTime + ", def:" + defaultBestWaitingTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_WHATSAPP_CARD_AD,
                keyBestWaitingTime, defaultBestWaitingTime);
    }

    @Override
    public long getAdsExpiryTime(Context cxt) {
        String keyAdsExpiryTime = "whatsapp_card_list_ad_ads_expiry_time";
        long defaultAdsExpiryTime = 60 * 60 * 1000L;
        switch (mResultType) {
            case AdsLoader.TYPE_WHATSAPP_LIST_CARD_AD:
                keyAdsExpiryTime = "whatsapp_card_list_ad_ads_expiry_time";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getAdsExpiryTime key:" + keyAdsExpiryTime + ", def:" + defaultAdsExpiryTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_WHATSAPP_CARD_AD,
                keyAdsExpiryTime, defaultAdsExpiryTime);
    }

    private static final String[] CONFIGS = new String[]{CloudPropertyManager.PATH_WHATSAPP_CARD_AD};

    @Override
    public String[] getDependencyConfigs() {
        return CONFIGS;
    }

    private String getUnitIdKey() {
        String keyUnitId = AdUnitId.UNIT_ID_WHATSAPP_CARD_LIST;
        switch (mResultType) {
            case AdsLoader.TYPE_WHATSAPP_LIST_CARD_AD:
                keyUnitId = AdUnitId.UNIT_ID_WHATSAPP_CARD_LIST;
                break;
        }
        return keyUnitId;
    }
}
