package com.ads.lib.loader;

import android.content.Context;
import android.util.Log;

import com.ads.lib.AdUnitId;
import com.ads.lib.AdsLoader;
import com.ads.lib.prop.AdPositionIdProp;
import com.baselib.cloud.CloudPropertyManager;
import com.hotvideo.config.GlobalConfig;

/**
 * Created by Eric Tjitra on 2/24/2017.
 */

public class ResultBigAdsLoader extends BaseAdsLoader {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "ResultBigAdsLoader";

    // 结果页的类型 (加速、清理、CPU降温、专清、杀毒)
    private int mResultType;

    public ResultBigAdsLoader(Context cxt, int resultType) {
        super(cxt);
        mResultType = resultType;
    }

    @Override
    public String getNativeAdUnitId(Context cxt) {
        String keyUnitId = getUnitIdKey();
        String defaultUnitId = getDefaultUnitKey();

        if (DEBUG) {
            Log.d(TAG, "getNativeAdUnitId key:" + keyUnitId + ", def:" + defaultUnitId);
        }
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_RESULT_ADS_CONFIG, keyUnitId, defaultUnitId);
    }

    private String getDefaultUnitKey() {
            String keyUnitId = "";
            switch (mResultType) {
                case AdsLoader.TYPE_BOOST_RESULT:
                    keyUnitId ="SAM_RAMRes_Native";
                    break;
                case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                    keyUnitId ="FMG_CleanRes_320x50_V31";
                    break;
                case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                    keyUnitId = "FMG_NotiyClean_320x50_V31";
                    break;
                case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                    keyUnitId ="FMG_VirusRes_320x50_V31";
                    break;
                case AdsLoader.TYPE_RESULT_CPU_BANNER:
                    keyUnitId = "FMG-CPUcooler-Native-0327";
                    break;
                case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                    keyUnitId = "SAM_RAMRes_Native";
                    break;
                case AdsLoader.TYPE_CPU_COOL_RESULT:
                    keyUnitId = "SAM_CPU_Res_Native";
                    break;
                case AdsLoader.TYPE_RESULT_APP_LOCK:
                    keyUnitId = "FMG-CleanRes-FullScreen-Native-0309";
                    break;
                case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                    keyUnitId = "SAM_CleanRes_Native";
                    break;
                case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                    keyUnitId = "ES_Feeds_Cleaning_Results_One_Page_Full_Fill";
                    break;
                case AdsLoader.TYPE_ANTI_VIRUS:
                    keyUnitId = "SAM_VirusRes_Native";
                    break;
                case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                    keyUnitId ="SAM_RAMRes_Native";
                    break;
                case AdsLoader.TYPE_BATTERY_RESULT:
                    keyUnitId = "SAM_ChargeSave_Res_Native";
                    break;
                case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                    keyUnitId = "SAM_NotifyClean_Native";
                    break;
                case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                    keyUnitId ="ESL-MsgSecCleRes-FullScreen-0002";
                    break;
                case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                    keyUnitId ="SE-WifiSecRes-FullScreen-0021";
                    break;
                default:
                    break;
            }
            return keyUnitId;
    }

    @Override
    public String getNativeAdPositionId(Context cxt) {
        return AdPositionIdProp.getInstance(cxt).getAdPositionIdByAdUnitId(getUnitIdKey());
    }

    @Override
    public String getPlacementId(Context cxt) {
        String keyPlacementId = "";
        String defaultPlacementId = "";

        switch (mResultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyPlacementId = "boost_result_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_RESULT_APP_LOCK:
                keyPlacementId = "applock_result_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                keyPlacementId = "junk_smallresult_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                keyPlacementId = "noti_smallresult_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                keyPlacementId = "anti_smallresult_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_RESULT_CPU_BANNER:
                keyPlacementId = "cpu_smallresult_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                keyPlacementId = "boost_smallresult_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyPlacementId = "cpu_result_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyPlacementId = "junk_result_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyPlacementId = "special_clean_result_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyPlacementId = "av_result_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                keyPlacementId = "notify_boost_placement_id";
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyPlacementId = "battery_saver_big_placement_id";
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyPlacementId = "notification_cleaner_result_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyPlacementId = "notificationprotect_clean_result_big_ads_placement_id";
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyPlacementId = "wifi_scaner_result_big_ads_placement_id";
                break;
                case AdsLoader.TYPE_APPLOCK:
        }

        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_RESULT_ADS_CONFIG,
                keyPlacementId, defaultPlacementId);
        if (DEBUG) {
            Log.d(TAG, "getPlacementId key:" + keyPlacementId + " =:" + res);
        }
        return res;
    }

    public String getNewUserPlacementId(Context cxt) {
        String keyPlacementId = "";
        String defaultPlacementId = "";

        switch (mResultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyPlacementId = "boost_result_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyPlacementId = "cpu_result_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                keyPlacementId = "junk_smallresult_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_RESULT_APP_LOCK:
                keyPlacementId = "applock_result_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                keyPlacementId = "noti_smallresult_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                keyPlacementId = "anti_smallresult_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_RESULT_CPU_BANNER:
                keyPlacementId = "cpu_smallresult_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                keyPlacementId = "boost_smallresult_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyPlacementId = "junk_result_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyPlacementId = "special_clean_result_big_ads_placement_id_firstday";
                defaultPlacementId = "";
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyPlacementId = "av_result_big_ads_placement_id_firstday";
                defaultPlacementId = "";
                break;
            case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                keyPlacementId = "notify_boost_placement_id_firstday";
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyPlacementId = "battery_saver_big_placement_id_firstday";
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyPlacementId = "notification_cleaner_result_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyPlacementId = "notificationprotect_clean_result_big_ads_placement_id_firstday";
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyPlacementId = "wifi_scaner_result_big_ads_placement_id_firstday";
                break;
        }
        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_RESULT_ADS_CONFIG,
                keyPlacementId, defaultPlacementId);

        if (DEBUG) {
            Log.d(TAG, "[new]getPlacementId key:" + keyPlacementId + " =" + res);
        }
        return res;
    }

    @Override
    public float getLoadPossibility(Context cxt) {
        String keyPossibility = "";
        float defaultPossibility = 1.0f;
        switch (mResultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyPossibility = "boost_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_RESULT_APP_LOCK:
                keyPossibility = "applock_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                keyPossibility = "junk_smallresult_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                keyPossibility = "noti_smallresult_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                keyPossibility = "anti_smallresult_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_RESULT_CPU_BANNER:
                keyPossibility = "cpu_smallresult_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                keyPossibility = "boost_smallresult_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyPossibility = "cpu_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyPossibility = "junk_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyPossibility = "special_clean_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyPossibility = "av_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                keyPossibility = "notify_boost_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyPossibility = "battery_saver_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyPossibility = "notification_cleaner_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyPossibility = "notificationprotect_clean_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyPossibility = "wifi_scaner_result_big_ads_possibility";
                defaultPossibility = 1.0f;
                break;
        }
        float aFloat = CloudPropertyManager.getFloat(cxt, CloudPropertyManager.PATH_RESULT_ADS_CONFIG,
                keyPossibility, defaultPossibility);
        if (DEBUG) {
            Log.d(TAG, "resultAD getLoadPossibility key:" + keyPossibility + ", def:" + defaultPossibility +
                    ", cloudvalue:" + aFloat);
        }
        return aFloat;
    }

    @Override
    public boolean shouldPrepareIcon(Context cxt) {
        String keyShouldPrepareIcon = "";
        int defaultIconPreparation = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyShouldPrepareIcon = "boost_result_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                keyShouldPrepareIcon = "junk_smallresult_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_APP_LOCK:
                keyShouldPrepareIcon = "applock_result_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                keyShouldPrepareIcon = "noti_smallresult_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                keyShouldPrepareIcon = "anti_smallresult_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_CPU_BANNER:
                keyShouldPrepareIcon = "cpu_smallresult_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                keyShouldPrepareIcon = "boost_smallresult_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyShouldPrepareIcon = "cpu_result_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyShouldPrepareIcon = "junk_result_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyShouldPrepareIcon = "special_clean_result_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyShouldPrepareIcon = "av_result_big_ads_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                keyShouldPrepareIcon = "notify_boost_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyShouldPrepareIcon = "battery_saver_big_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyShouldPrepareIcon = "notification_cleaner_result_big_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyShouldPrepareIcon = "notificationprotect_clean_result_big_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyShouldPrepareIcon = "wifi_scaner_result_big_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareIcon key:" + keyShouldPrepareIcon + ", def:" + defaultIconPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_RESULT_ADS_CONFIG,
                keyShouldPrepareIcon, defaultIconPreparation) == 1;
    }

    @Override
    public boolean shouldPrepareBanner(Context cxt) {
        String keyShouldPrepareBanner = "";
        int defaultBannerPreparation = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyShouldPrepareBanner = "boost_result_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                keyShouldPrepareBanner = "junk_smallresult_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_APP_LOCK:
                keyShouldPrepareBanner = "applock_result_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                keyShouldPrepareBanner = "noti_smallresult_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                keyShouldPrepareBanner = "anti_smallresult_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_CPU_BANNER:
                keyShouldPrepareBanner = "cpu_smallresult_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                keyShouldPrepareBanner = "boost_smallresult_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyShouldPrepareBanner = "cpu_result_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyShouldPrepareBanner = "junk_result_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyShouldPrepareBanner = "special_clean_result_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyShouldPrepareBanner = "av_result_big_ads_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                keyShouldPrepareBanner = "notify_boost_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyShouldPrepareBanner = "battery_saver_big_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyShouldPrepareBanner = "notification_cleaner_result_big_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyShouldPrepareBanner = "notificationprotect_clean_result_big_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyShouldPrepareBanner = "wifi_scaner_result_big_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareBanner key:" + keyShouldPrepareBanner + ", def:" + defaultBannerPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_RESULT_ADS_CONFIG,
                keyShouldPrepareBanner, defaultBannerPreparation) == 1;
    }

    @Override
    public boolean useParallelRequest(Context cxt) {
        String keyUseParallel = "";
        int defaultParallel = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyUseParallel = "boost_result_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_RESULT_APP_LOCK:
                keyUseParallel = "applock_result_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                keyUseParallel = "junk_smallresult_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                keyUseParallel = "noti_smallresult_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                keyUseParallel = "anti_smallresult_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_RESULT_CPU_BANNER:
                keyUseParallel = "cpu_smallresult_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                keyUseParallel = "boost_smallresult_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyUseParallel = "cpu_result_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyUseParallel = "junk_result_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyUseParallel = "special_clean_result_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyUseParallel = "av_result_big_ads_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                keyUseParallel = "notify_boost_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyUseParallel = "battery_saver_big_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyUseParallel = "notification_cleaner_result_big_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyUseParallel = "notificationprotect_clean_result_big_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyUseParallel = "wifi_scaner_result_big_use_parallel_request";
                defaultParallel = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "useParallelRequest key:" + keyUseParallel + ", def:" + defaultParallel);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_RESULT_ADS_CONFIG,
                keyUseParallel, defaultParallel) == 1;
    }

    @Override
    public long getBestWaitingTime(Context cxt) {
        String keyBestWaitingTime = "";
        long defaultBestWaitingTime = 5000L;
        switch (mResultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyBestWaitingTime = "boost_result_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                keyBestWaitingTime = "junk_smallresult_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                keyBestWaitingTime = "noti_smallresult_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                keyBestWaitingTime = "anti_smallresult_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_RESULT_CPU_BANNER:
                keyBestWaitingTime = "cpu_smallresult_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_RESULT_APP_LOCK:
                keyBestWaitingTime = "applock_result_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                keyBestWaitingTime = "boost_smallresult_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyBestWaitingTime = "cpu_result_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyBestWaitingTime = "junk_result_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyBestWaitingTime = "special_clean_result_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyBestWaitingTime = "av_result_big_ads_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                keyBestWaitingTime = "notify_boost_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyBestWaitingTime = "battery_saver_big_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyBestWaitingTime = "notification_cleaner_result_big_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyBestWaitingTime = "notificationprotect_clean_result_big_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyBestWaitingTime = "wifi_scaner_result_big_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getBestWaitingTime key:" + keyBestWaitingTime + ", def:" + defaultBestWaitingTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_RESULT_ADS_CONFIG,
                keyBestWaitingTime, defaultBestWaitingTime);
    }

    @Override
    public long getAdsExpiryTime(Context cxt) {
        String keyAdsExpiryTime = "";
        long defaultAdsExpiryTime = 60 * 60 * 1000L;
        switch (mResultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyAdsExpiryTime = "boost_result_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_RESULT_APP_LOCK:
                keyAdsExpiryTime = "applock_result_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                keyAdsExpiryTime = "junk_smallresult_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                keyAdsExpiryTime = "noti_smallresult_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                keyAdsExpiryTime = "anti_smallresult_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_RESULT_CPU_BANNER:
                keyAdsExpiryTime = "cpu_smallresult_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                keyAdsExpiryTime = "boost_smallresult_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyAdsExpiryTime = "cpu_result_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyAdsExpiryTime = "junk_result_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyAdsExpiryTime = "special_clean_result_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyAdsExpiryTime = "av_result_big_ads_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                keyAdsExpiryTime = "notify_boost_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyAdsExpiryTime = "battery_saver_big_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyAdsExpiryTime = "notification_cleaner_result_big_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyAdsExpiryTime = "notificationprotect_clean_result_big_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyAdsExpiryTime = "wifi_scaner_result_big_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getAdsExpiryTime key:" + keyAdsExpiryTime + ", def:" + defaultAdsExpiryTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_RESULT_ADS_CONFIG,
                keyAdsExpiryTime, defaultAdsExpiryTime);
    }

    private static final String[] CONFIGS = new String[]{CloudPropertyManager.PATH_RESULT_ADS_CONFIG};

    @Override
    public String[] getDependencyConfigs() {
        return CONFIGS;
    }

    private String getUnitIdKey() {
        String keyUnitId = "";
        switch (mResultType) {
            case AdsLoader.TYPE_BOOST_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_BOOST_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_RESULT_JUNK_BANNER:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_RUBBISHBANNER_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_RESULT_NOTI_BANNER:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_NOTIBANNER_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_RESULT_ANTI_BANNER:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_ANTIBANNER_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_RESULT_CPU_BANNER:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_CPUBANNER_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_RESULT_BOOST_BANNER:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_BOOSTBANNER_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_CPU_COOL_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_CPU_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_RESULT_APP_LOCK:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_APPLOCK_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_RUBBISH_CLEAN_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_JUNK_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_SPECIAL_CLEAN_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_ANTI_VIRUS:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_AV_RESULT_BIG_ADS;
                break;
            case AdsLoader.TYPE_NOTIFICATION_BOOST_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFY_BOOST;
                break;
            case AdsLoader.TYPE_BATTERY_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_BATTERY_SAVER_BIG;
                break;
            case AdsLoader.TYPE_NOTIFICATION_CLEAN_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFICATION_CLEANER_RESULT_BIG;
                break;
            case AdsLoader.TYPE_NOTIFICATION_SECURITY_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFICATIONPROTECT_CLEAN_RESULT_BIG;
                break;
            case AdsLoader.TYPE_WIFI_SCAN_RESULT:
                keyUnitId = AdUnitId.UNIT_ID_KEY_NATIVE_WIFI_SCANER_RESULT_BIG;
                break;
            default:
                break;
        }
        return keyUnitId;
    }

}






