package com.ads.lib.loader;

import android.content.Context;
import android.util.Log;

import com.ads.lib.AdsLoader;
import com.ads.lib.prop.AdPositionIdProp;
import com.baselib.cloud.CloudPropertyManager;
import com.goodmorning.config.GlobalConfig;

/**
 * Created by Eric Tjitra on 2/24/2017.
 */

public class ListAdsLoader extends BaseAdsLoader {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "ListAdsLoader";

    private int mResultType;

    public ListAdsLoader(Context cxt, int resultType) {
        super(cxt);
        mResultType = resultType;
    }
    private String getDefaultKey() {
        String keyUnitId = "";
        switch (mResultType) {
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyUnitId = "FMG-Video-List-Native-0309";
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyUnitId = "FMG-Audio-List-Native-0309";
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyUnitId = "FMG-Picture-List-Native-0309";
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyUnitId = "FMG-Document-List-Row-Native-0309";
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyUnitId = "FMG-Large-File-List-Row-Native-0309";
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyUnitId = "FMG-Download-File-List-Row-Native-0309";
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyUnitId = "FMG-Duplicate-File-List-Row-Native-0309";
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyUnitId = "FMG-Low-Resolution-List-Native-0309";
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyUnitId = "FMG-Manager-Listpage-Native-0310";
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyUnitId = "FMG-AppCache-Native-0309";
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyUnitId = "FMG-Apkfiles-Native-0309";
                break;
            default:
                break;
        }
        return keyUnitId;
    }
    @Override
    public String getNativeAdUnitId(Context cxt) {
        String keyUnitId = getUnitIdKey();
        String defaultUnitId = getDefaultKey();

        if (DEBUG) {
            Log.d(TAG, "getNativeAdUnitId key:" + keyUnitId + ", def:" + defaultUnitId);
        }
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_LIST_ADS_CONFIG, keyUnitId, defaultUnitId);
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
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyPlacementId = "videos_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyPlacementId = "audios_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyPlacementId = "images_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyPlacementId = "documents_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyPlacementId = "large_files_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyPlacementId = "downloaded_files_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyPlacementId = "duplicate_files_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyPlacementId = "memes_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyPlacementId = "appuninstall_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyPlacementId = "appreset_list_ad_placement_id";
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyPlacementId = "appdelete_list_ad_placement_id";
                break;

        }
        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_LIST_ADS_CONFIG,
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
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyPlacementId = "videos_list_ad_placement_id_firstday";
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyPlacementId = "audios_list_ad_placement_id_firstday";
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyPlacementId = "images_list_ad_placement_id_firstday";
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyPlacementId = "documents_list_ad_placement_id_firstday";
                defaultPlacementId = "";
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyPlacementId = "large_files_list_ad_placement_id_firstday";
                defaultPlacementId = "";
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyPlacementId = "downloaded_files_list_ad_placement_id_firstday";
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyPlacementId = "duplicate_files_list_ad_placement_id_firstday";
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyPlacementId = "memes_list_ad_placement_id_firstday";
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyPlacementId = "appuninstall_list_ad_placement_id_firstday";
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyPlacementId = "appreset_list_ad_placement_id_firstday";
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyPlacementId = "appdelete_list_ad_placement_id_firstday";
                break;
        }
        String res = CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_LIST_ADS_CONFIG,
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
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyPossibility = "videos_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyPossibility = "audios_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyPossibility = "images_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyPossibility = "documents_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyPossibility = "large_files_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyPossibility = "downloaded_files_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyPossibility = "duplicate_files_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyPossibility = "memes_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyPossibility = "appuninstall_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyPossibility = "appreset_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyPossibility = "appdelete_list_ad_possibility";
                defaultPossibility = 1.0f;
                break;

        }
        if (DEBUG) {
            Log.d(TAG, "getLoadPossibility key:" + keyPossibility + ", def:" + defaultPossibility);
        }
        return CloudPropertyManager.getFloat(cxt, CloudPropertyManager.PATH_LIST_ADS_CONFIG,
                keyPossibility, defaultPossibility);
    }

    @Override
    public boolean shouldPrepareIcon(Context cxt) {
        String keyShouldPrepareIcon = "";
        int defaultIconPreparation = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyShouldPrepareIcon = "videos_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyShouldPrepareIcon = "audios_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyShouldPrepareIcon = "images_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyShouldPrepareIcon = "documents_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyShouldPrepareIcon = "large_files_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyShouldPrepareIcon = "downloaded_files_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyShouldPrepareIcon = "duplicate_files_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyShouldPrepareIcon = "memes_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyShouldPrepareIcon = "appuninstall_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyShouldPrepareIcon = "appreset_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyShouldPrepareIcon = "appdelete_list_ad_should_prepare_icon";
                defaultIconPreparation = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareIcon key:" + keyShouldPrepareIcon + ", def:" + defaultIconPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_LIST_ADS_CONFIG,
                keyShouldPrepareIcon, defaultIconPreparation) == 1;
    }

    @Override
    public boolean shouldPrepareBanner(Context cxt) {
        String keyShouldPrepareBanner = "";
        int defaultBannerPreparation = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyShouldPrepareBanner = "videos_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyShouldPrepareBanner = "audios_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyShouldPrepareBanner = "images_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyShouldPrepareBanner = "documents_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyShouldPrepareBanner = "large_files_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyShouldPrepareBanner = "downloaded_files_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyShouldPrepareBanner = "duplicate_files_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyShouldPrepareBanner = "memes_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyShouldPrepareBanner = "appuninstall_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyShouldPrepareBanner = "appreset_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyShouldPrepareBanner = "appdelete_list_ad_should_prepare_banner";
                defaultBannerPreparation = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "shouldPrepareBanner key:" + keyShouldPrepareBanner + ", def:" + defaultBannerPreparation);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_LIST_ADS_CONFIG,
                keyShouldPrepareBanner, defaultBannerPreparation) == 1;
    }

    @Override
    public boolean useParallelRequest(Context cxt) {
        String keyUseParallel = "";
        int defaultParallel = 1;
        switch (mResultType) {
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyUseParallel = "videos_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyUseParallel = "audios_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyUseParallel = "images_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyUseParallel = "documents_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyUseParallel = "large_files_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyUseParallel = "downloaded_files_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyUseParallel = "duplicate_files_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyUseParallel = "memes_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyUseParallel = "appuninstall_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyUseParallel = "appreset_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyUseParallel = "appdelete_list_ad_use_parallel_request";
                defaultParallel = 1;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "useParallelRequest key:" + keyUseParallel + ", def:" + defaultParallel);
        }
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_LIST_ADS_CONFIG,
                keyUseParallel, defaultParallel) == 1;
    }

    @Override
    public long getBestWaitingTime(Context cxt) {
        String keyBestWaitingTime = "";
        long defaultBestWaitingTime = 5000L;
        switch (mResultType) {
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyBestWaitingTime = "videos_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyBestWaitingTime = "audios_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyBestWaitingTime = "images_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyBestWaitingTime = "documents_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyBestWaitingTime = "large_files_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyBestWaitingTime = "downloaded_files_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyBestWaitingTime = "duplicate_files_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyBestWaitingTime = "memes_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyBestWaitingTime = "appuninstall_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyBestWaitingTime = "appreset_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyBestWaitingTime = "appdelete_list_ad_best_waiting_time";
                defaultBestWaitingTime = 5000L;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getBestWaitingTime key:" + keyBestWaitingTime + ", def:" + defaultBestWaitingTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_LIST_ADS_CONFIG,
                keyBestWaitingTime, defaultBestWaitingTime);
    }

    @Override
    public long getAdsExpiryTime(Context cxt) {
        String keyAdsExpiryTime = "";
        long defaultAdsExpiryTime = 60 * 60 * 1000L;
        switch (mResultType) {
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyAdsExpiryTime = "videos_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyAdsExpiryTime = "audios_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyAdsExpiryTime = "images_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyAdsExpiryTime = "documents_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyAdsExpiryTime = "large_files_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyAdsExpiryTime = "downloaded_files_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyAdsExpiryTime = "duplicate_files_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyAdsExpiryTime = "memes_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyAdsExpiryTime = "appuninstall_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyAdsExpiryTime = "appreset_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyAdsExpiryTime = "appdelete_list_ad_ads_expiry_time";
                defaultAdsExpiryTime = 60 * 60 * 1000L;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "getAdsExpiryTime key:" + keyAdsExpiryTime + ", def:" + defaultAdsExpiryTime);
        }
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_LIST_ADS_CONFIG,
                keyAdsExpiryTime, defaultAdsExpiryTime);
    }

    private static final String[] CONFIGS = new String[]{CloudPropertyManager.PATH_LIST_ADS_CONFIG};

    @Override
    public String[] getDependencyConfigs() {
        return CONFIGS;
    }

    private String getUnitIdKey() {
        String keyUnitId = "";
        switch (mResultType) {
            case AdsLoader.TYPE_VIDEOS_LIST:
                keyUnitId = "videos_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_AUDIOS_LIST:
                keyUnitId = "audios_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_IMAGE_LIST:
                keyUnitId = "images_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_DOC_LIST:
                keyUnitId = "documents_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_LARGE_FILE_LIST:
                keyUnitId = "large_files_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_DOWNLOAD_LIST:
                keyUnitId = "downloaded_files_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_DUPLICATES_LIST:
                keyUnitId = "duplicate_files_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_MEMES_LIST:
                keyUnitId = "memes_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_APPMANAGER_UNINSTALL:
                keyUnitId = "appuninstall_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_APPMANAGER_RESET:
                keyUnitId = "appreset_list_ad_unit_id";
                break;
            case AdsLoader.TYPE_APPMANAGER_APKDELETE:
                keyUnitId = "appdelete_list_ad_unit_id";
                break;
            default:
                break;
        }
        return keyUnitId;
    }

}






