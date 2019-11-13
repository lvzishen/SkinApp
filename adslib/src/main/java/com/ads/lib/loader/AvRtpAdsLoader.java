package com.ads.lib.loader;

import android.content.Context;
import android.util.Log;

import com.ads.lib.AdUnitId;
import com.ads.lib.AdsLoader;
import com.ads.lib.prop.AdPositionIdProp;
import com.baselib.cloud.CloudPropertyManager;
import com.hotvideo.config.GlobalConfig;

/**
 * Created by yeguolong on 17-6-20.
 */
public class AvRtpAdsLoader extends BaseAdsLoader {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "AvRtpAdsLoader";

    private int mType = AdsLoader.TYPE_ANTI_VIRUS_RTP;

    public AvRtpAdsLoader(Context cxt, int type) {
        super(cxt);
        mType = type;
    }

    @Override
    public String getNativeAdUnitId(Context cxt) {
        String keyUnitId = getUnitIdKey();
        String defaultUnitId = "FMG-ProtectBott-Native-0309";
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                defaultUnitId = "FMG-VirusRes-FullScreen-Native-0309";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getNativeAdUnitId key:" + keyUnitId + ", def:" + defaultUnitId);
        }
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_AV_ADS_CONFIG,
                keyUnitId, defaultUnitId);
    }

    @Override
    public String getNativeAdPositionId(Context cxt) {
        return AdPositionIdProp.getInstance(cxt).getAdPositionIdByAdUnitId(getUnitIdKey());
    }

    @Override
    public String getPlacementId(Context cxt) {
        String keyPlacementId = "av_rtp_ads_placement_id";
        String defaultPlacementId = "";
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                keyPlacementId = "av_first_result_big_ads_placement_id";
                break;
        }

        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_AV_ADS_CONFIG,
                keyPlacementId, defaultPlacementId);
        if (DEBUG) {
            Log.d(TAG, "getPlacementId key:" + keyPlacementId + " = " + res);
        }
        return res;
    }

    public String getNewUserPlacementId(Context cxt) {
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                return null;
            case AdsLoader.TYPE_ANTI_VIRUS_RTP:
                String keyPlacementId = "av_rtp_ads_placement_id_firstday";
                String defaultPlacementId = "";
                String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_AV_ADS_CONFIG,
                        keyPlacementId, defaultPlacementId);
                if (DEBUG) {
                    Log.d(TAG, "[new]getPlacementId key:" + keyPlacementId + " = " + res);
                }
                return res;
        }
        return null;
    }

    @Override
    public float getLoadPossibility(Context cxt) {
        String keyPossibility = "av_rtp_ads_possibility";
        float defaultPossibility = 1.0f;
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                keyPossibility = "av_first_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
        }
        float aFloat = CloudPropertyManager.getFloat(cxt, CloudPropertyManager.PATH_AV_ADS_CONFIG,
                keyPossibility, defaultPossibility);
        if (DEBUG) {
            Log.d(TAG, "getLoadPossibility key:" + keyPossibility + ", def:" + defaultPossibility+
                    ", cloudvalue:"+aFloat);
        }
        return aFloat;
    }

    @Override
    public boolean shouldPrepareIcon(Context cxt) {
        String keyShouldPrepareIcon = "av_rtp_ads_should_prepare_icon";
        int defaultIconPreparation = 1;
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                keyShouldPrepareIcon = "av_first_result_big_ads_should_prepare_icon";
                defaultIconPreparation = 0;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareIcon key:" + keyShouldPrepareIcon + ", def:" + defaultIconPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_AV_ADS_CONFIG,
                keyShouldPrepareIcon, defaultIconPreparation) == 1;
    }

    @Override
    public boolean shouldPrepareBanner(Context cxt) {
        String keyShouldPrepareBanner = "av_rtp_ads_should_prepare_banner";
        int defaultBannerPreparation = 1;
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                keyShouldPrepareBanner = "av_first_result_big_ads_should_prepare_banner";
                defaultBannerPreparation = 0;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareBanner key:" + keyShouldPrepareBanner + ", def:" + defaultBannerPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_AV_ADS_CONFIG,
                keyShouldPrepareBanner, defaultBannerPreparation) == 1;
    }

    @Override
    public boolean useParallelRequest(Context cxt) {
        String keyUseParallel = "av_rtp_ads_use_parallel_request";
        int defaultParallel = 1;
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                keyUseParallel = "av_first_result_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "useParallelRequest key:" + keyUseParallel + ", def:" + defaultParallel);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_AV_ADS_CONFIG,
                keyUseParallel, defaultParallel) == 1;
    }

    @Override
    public long getBestWaitingTime(Context cxt) {
        String keyBestWaitingTime = "av_rtp_ads_best_waiting_time";
        long defaultBestWaitingTime = 5000L;
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                keyBestWaitingTime = "av_first_result_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getBestWaitingTime key:" + keyBestWaitingTime + ", def:" + defaultBestWaitingTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_AV_ADS_CONFIG,
                keyBestWaitingTime, defaultBestWaitingTime);
    }

    @Override
    public long getAdsExpiryTime(Context cxt) {
        String keyAdsExpiryTime = "av_rtp_ads_ads_expiry_time";
        long defaultAdsExpiryTime = 60 * 60 * 1000L;
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                keyAdsExpiryTime = "av_first_result_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getAdsExpiryTime key:" + keyAdsExpiryTime + ", def:" + defaultAdsExpiryTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_AV_ADS_CONFIG,
                keyAdsExpiryTime, defaultAdsExpiryTime);
    }

    private static final String[] CONFIGS = new String[]{CloudPropertyManager.PATH_AV_ADS_CONFIG};

    @Override
    public String[] getDependencyConfigs() {
        return CONFIGS;
    }

    private String getUnitIdKey() {
        String keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_AV_RTP_ADS;
        switch (mType) {
            case AdsLoader.TYPE_AV_FIRST_SCAN:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_AV_FIRST_RESULT_BIG_ADS;
                break;
        }
        return keyUnitId;
    }

}
