package com.ads.lib.log;

import com.ads.lib.adapter.LocalStrategy;
import com.ads.lib.mediation.constants.Constants;
import com.ads.lib.util.PageUtil;
import com.baselib.statistic.StatisticLoggerX;

import java.util.List;

public class AdStatisticLoggerX {

    public static final String AD_TYPE_NATIVE = "native";
    public static final String AD_TYPE_INTERSTITIAL = "interstitial";
    public static final String AD_TYPE_RECTANGLE_BANNERL = "rectangle_banner";
    public static final String AD_TYPE_RSTRIP_BANNER = "strip_banner";

    public static final String ACTION_AD_IMPRESSION = "ad_impression";
    public static final String ACTION_AD_CLICK = "ad_click";
    public static final String ACTION_AD_REQUEST = "ad_request";
    public static final String ACTION_AD_REQUEST_RESULT = "ad_request_result";

    public static final String AD_SOURCE_MOPUB = "mopub";

    public static final String AD_ACTION_VIEW = "view";
    public static final String AD_ACTION_PV_SHOW  = "pv_show";
    public static final String AD_ACTION_AD_SHOW  = "ad_show";
    public static final String AD_ACTION_AD_PV  = "ad_pv";
    public static final String AD_ACTION_AD_CLICK  = "ad_click";
    public static final String REQUEST_TYPE_REAL = "real";
    public static final String REQUEST_TYPE_CACHE = "cache";

    public static void logLoadAdRequest(String name, String positionId, String action,
                                        String adSource, String adType, String adAction,
                                        String adPlacementId, String adCachePool) {
        StatisticLoggerX.logNewAdOperation(name, positionId, action, adSource, adType, adAction, adPlacementId, 0, null, null, null, adCachePool);
    }


    public static void logonAdResult(String name, String positionId, String adRequestType, String action,
                                     String adSource, String adType, String adAction,
                                     String adPlacementId, long duration, String resultCode, String resultInfo, String adCachePool) {
        StatisticLoggerX.logNewAdOperation(name, positionId, action, adSource, adType, adAction, adPlacementId, duration, adRequestType, resultCode, resultInfo, adCachePool);

    }

    public static void logImpression(String name, String positionId, String action,
                                     String adSource, String adType, String adAction,
                                     String adPlacementId,String adCachePool) {
        StatisticLoggerX.logNewAdOperation(name, positionId, action, adSource, adType, adAction, adPlacementId, 0, null, null, null, adCachePool);

    }

    public static void logClick(String name, String positionId, String action,
                                String adSource, String adType, String adAction,
                                String adPlacementId,String adCachePool) {
        StatisticLoggerX.logNewAdOperation(name, positionId, action, adSource, adType, adAction, adPlacementId, 0, null, null, null, adCachePool);
    }


    public static String getAdType(List<LocalStrategy> strategies){
        if(strategies.size() <= 0){
            return null;
        }
        LocalStrategy strategy = strategies.get(0);
        switch (strategy.adType) {
            case Constants.AD_TYPE_FLAG_NATIVE:
                return AdStatisticLoggerX.AD_TYPE_NATIVE;
            case Constants.AD_TYPE_FLAG_SIZE_MEDIUM_BANNER:
                return AdStatisticLoggerX.AD_TYPE_RECTANGLE_BANNERL;
            case Constants.AD_TYPE_FLAG_SIZE_SMALL_BANNER:
                return AdStatisticLoggerX.AD_TYPE_RSTRIP_BANNER;
            case Constants.AD_TYPE_FLAG_INTERSTITIAL:
                return AdStatisticLoggerX.AD_TYPE_INTERSTITIAL;
            default:
                return null;
        }
    }

}
