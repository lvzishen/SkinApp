package com.ads.lib.loader;

import android.content.Context;

import com.ads.lib.AdUnitId;
import com.ads.lib.prop.AdPositionIdProp;
import com.baselib.cloud.CloudPropertyManager;


/**
 * Created by Eric Tjitra on 2/24/2017.
 */

public class AppLockAdsLoader extends BaseAdsLoader {

    public AppLockAdsLoader(Context cxt) {
        super(cxt);
    }

    @Override
    public String getNativeAdUnitId(Context cxt) {
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_APPLOCK_CLOUD,
                AdUnitId.UNIT_ID_KEY_NATIVE_APPLOCK_NATIVE_AD, "ES_App_Lock_Fill");
    }

    @Override
    public String getNativeAdPositionId(Context cxt) {
        return AdPositionIdProp.getInstance(cxt).getAdPositionIdByAdUnitId(AdUnitId.UNIT_ID_KEY_NATIVE_APPLOCK_NATIVE_AD);
    }

    @Override
    public String getPlacementId(Context cxt) {
        return CloudPropertyManager.getString(cxt, CloudPropertyManager.PATH_APPLOCK_CLOUD,
                "applock_fb_placement_id", "");
    }

    @Override
    public float getLoadPossibility(Context cxt) {
        return CloudPropertyManager.getFloat(cxt, CloudPropertyManager.PATH_APPLOCK_CLOUD,
                "applock_fb_ad_load_possibility", 1f);
    }

    @Override
    public boolean shouldPrepareIcon(Context cxt) {
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_APPLOCK_CLOUD,
                "applock_fb_ad_prepare_icon", 1) == 1;
    }

    @Override
    public boolean shouldPrepareBanner(Context cxt) {
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_APPLOCK_CLOUD,
                "applock_fb_ad_prepare_banner", 1) == 1;
    }

    @Override
    public boolean useParallelRequest(Context cxt) {
        return CloudPropertyManager.getInt(cxt, CloudPropertyManager.PATH_APPLOCK_CLOUD,
                "applock_fb_ad_parallel_request", 1) == 1;
    }

    @Override
    public long getBestWaitingTime(Context cxt) {
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_APPLOCK_CLOUD,
                "applock_fb_ad_best_waiting_time", 5000L);
    }

    @Override
    public long getAdsExpiryTime(Context cxt) {
        return CloudPropertyManager.getLong(cxt, CloudPropertyManager.PATH_APPLOCK_CLOUD,
                "applock_fb_ad_expiry_time", 60 * 60 * 1000L); // 60 minutes
    }

    private static final String[] CONFIGS = new String[]{CloudPropertyManager.PATH_APPLOCK_CLOUD};
    @Override
    public String[] getDependencyConfigs() {
        return  CONFIGS;
    }
}
