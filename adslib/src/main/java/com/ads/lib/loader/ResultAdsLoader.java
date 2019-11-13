package com.ads.lib.loader;

import android.content.Context;
import android.util.Log;

import com.ads.lib.AdUnitId;
import com.ads.lib.AdsLoader;
import com.ads.lib.prop.AdPositionIdProp;
import com.baselib.cloud.CloudPropertyManager;
import com.goodmorning.config.GlobalConfig;

/**
 * Created by Eric Tjitra on 2/24/2017.
 */

public class ResultAdsLoader extends BaseAdsLoader {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "ResultAdsLoader";

    // 结果页的类型 (加速、清理、CPU降温、专清)
    private int resultType;

    public ResultAdsLoader(Context cxt, int type) {
        super(cxt);
        resultType = type;
    }

    @Override
    public String getNativeAdUnitId(Context cxt) {
        String keyUnitId = getUnitIdKey();
        String defaultUnitId = "";

        switch (resultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                defaultUnitId = "ES-BoostRes-TopOfFeeds-0003";
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                defaultUnitId = "ES-CpuRes-TopOfFeeds-0005";
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                defaultUnitId = "ES-CleanRes-TopOfFeeds-0001";
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                defaultUnitId = "ES_Feeds_Cleaning_Results_Page_01_Fill";
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                defaultUnitId = "ES-VirusRes-TopOfFeeds-0007";
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                defaultUnitId = "ES-BatteryRes-Feeds-0013";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getNativeAdUnitId key:" + keyUnitId + ", def:" + defaultUnitId);
        }
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_CONFIG,
                keyUnitId, defaultUnitId);
    }

    @Override
    public String getNativeAdPositionId(Context cxt) {
        return AdPositionIdProp.getInstance(cxt).getAdPositionIdByAdUnitId(getUnitIdKey());
    }

    @Override
    public String getPlacementId(Context cxt) {
        String keyPlacementId = "";
        String defaultPlacementId = "";

        switch (resultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyPlacementId = "boost_result_placement_id";
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyPlacementId = "cpu_result_placement_id";
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyPlacementId = "junk_result_placement_id";
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyPlacementId = "special_clean_result_placement_id";
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyPlacementId = "av_result_placement_id";
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyPlacementId = "battery_saver_placement_id";
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyPlacementId= "notification_cleaner_placement_id";
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyPlacementId = "notification_security_placement_id";
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyPlacementId = "wifi_security_placement_id";
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getPlacementId key:" + keyPlacementId + ", def:" + defaultPlacementId);
        }
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_CONFIG,
                keyPlacementId, defaultPlacementId);
    }

    @Override
    public float getLoadPossibility(Context cxt) {
        String keyPossibility = "";
        float defaultPossibility = 1.0f;
        switch (resultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyPossibility = "boost_result_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyPossibility = "cpu_result_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyPossibility = "junk_result_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyPossibility = "special_clean_result_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyPossibility = "av_result_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyPossibility = "battery_saver_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyPossibility = "notification_cleaner_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyPossibility = "notification_security_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyPossibility = "wifi_security_ads_possibility";
                defaultPossibility = 1.0f;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getLoadPossibility key:" + keyPossibility + ", def:" + defaultPossibility);
        }
        return CloudPropertyManager.getFloat(cxt, CloudPropertyManager.PATH_CONFIG,
                keyPossibility, defaultPossibility);
    }

    @Override
    public boolean shouldPrepareIcon(Context cxt) {
        String keyShouldPrepareIcon = "";
        int defaultIconPreparation = 1;
        switch (resultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyShouldPrepareIcon = "boost_result_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyShouldPrepareIcon = "cpu_result_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyShouldPrepareIcon = "junk_result_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyShouldPrepareIcon = "special_clean_result_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyShouldPrepareIcon = "av_result_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyShouldPrepareIcon = "battery_saver_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyShouldPrepareIcon = "notification_cleaner_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyShouldPrepareIcon = "notification_security_r_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyShouldPrepareIcon = "wifi_security_r_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareIcon key:" + keyShouldPrepareIcon + ", def:" + defaultIconPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_CONFIG,
                keyShouldPrepareIcon, defaultIconPreparation) == 1;
    }

    @Override
    public boolean shouldPrepareBanner(Context cxt) {
        String keyShouldPrepareBanner = "";
        int defaultBannerPreparation = 1;
        switch (resultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyShouldPrepareBanner = "boost_result_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyShouldPrepareBanner = "cpu_result_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyShouldPrepareBanner = "junk_result_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyShouldPrepareBanner = "special_clean_result_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyShouldPrepareBanner = "av_result_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyShouldPrepareBanner = "battery_saver_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyShouldPrepareBanner = "notification_cleaner_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyShouldPrepareBanner = "notification_security_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyShouldPrepareBanner = "wifi_security_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareBanner key:" + keyShouldPrepareBanner + ", def:" + defaultBannerPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_CONFIG,
                keyShouldPrepareBanner, defaultBannerPreparation) == 1;
    }

    @Override
    public boolean useParallelRequest(Context cxt) {
        String keyUseParallel = "";
        int defaultParallel = 1;
        switch (resultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyUseParallel = "boost_result_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyUseParallel = "cpu_result_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyUseParallel = "junk_result_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyUseParallel = "special_clean_result_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyUseParallel = "av_result_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyUseParallel = "battery_saver_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyUseParallel = "notification_cleaner_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyUseParallel = "notification_security_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyUseParallel = "wifi_security_use_parallel_request";
                defaultParallel = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "useParallelRequest key:" + keyUseParallel + ", def:" + defaultParallel);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_CONFIG,
                keyUseParallel, defaultParallel) == 1;
    }

    @Override
    public long getBestWaitingTime(Context cxt) {
        String keyBestWaitingTime = "";
        long defaultBestWaitingTime = 5000L;
        switch (resultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyBestWaitingTime = "boost_result_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyBestWaitingTime = "cpu_result_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyBestWaitingTime = "junk_result_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyBestWaitingTime = "special_clean_result_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyBestWaitingTime = "av_result_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyBestWaitingTime = "battery_saver_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyBestWaitingTime = "notification_cleaner_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyBestWaitingTime = "notification_security_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyBestWaitingTime = "wifi_security_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getBestWaitingTime key:" + keyBestWaitingTime + ", def:" + defaultBestWaitingTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_CONFIG,
                keyBestWaitingTime, defaultBestWaitingTime);
    }

    @Override
    public long getAdsExpiryTime(Context cxt) {
        String keyAdsExpiryTime = "";
        long defaultAdsExpiryTime = 60 * 60 * 1000L;
        switch (resultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyAdsExpiryTime = "boost_result_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyAdsExpiryTime = "cpu_result_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyAdsExpiryTime = "junk_result_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyAdsExpiryTime = "special_clean_result_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyAdsExpiryTime = "av_result_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyAdsExpiryTime = "battery_saver_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyAdsExpiryTime = "notification_cleaner_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyAdsExpiryTime = "notification_security_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyAdsExpiryTime = "wifi_security_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getAdsExpiryTime key:" + keyAdsExpiryTime + ", def:" + defaultAdsExpiryTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_CONFIG,
                keyAdsExpiryTime, defaultAdsExpiryTime);
    }

    private static final String[] CONFIGS = new String[]{CloudPropertyManager.PATH_CONFIG};
    @Override
    public String[] getDependencyConfigs() {
        return  CONFIGS;
    }

    private String getUnitIdKey() {
        String keyUnitId = "";
        switch (resultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_BOOST_RESULT;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_CPU_RESULT;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_JUNK_RESULT;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_SPECIAL_CLEAN_RESULT;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_AV_RESULT;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_BATTERY_SAVER;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFICATION_CLEANER;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFICATION_SECURITY;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_WIFI_SECURITY;
                break;
            default:
                break;
        }

        return keyUnitId;
    }
}
