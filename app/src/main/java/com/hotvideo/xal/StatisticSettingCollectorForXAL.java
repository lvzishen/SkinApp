package com.hotvideo.xal;

import android.content.Context;
import android.os.Bundle;



/**
 * XAL 状态打点，和XAL_SET_STATE 不太一样
 * <p>
 * Created by tangchun on 2017/4/10.
 */

public class StatisticSettingCollectorForXAL {

    private static final String STATUS_PARAM_GOOGLE_SERVICE_STATUS = "google_service_status";
    private static final String STATUS_PARAM_GOOGLE_SERVICE_VERSION = "google_service_version";

    public static void onCollectData(Context c, Bundle parameters) {
//        if (UsageStatusHelper.isSupportUsageStatus()) {
//            if (UsageStatusHelper.checkUsageManagerPriviliage(c)) {
//                parameters.putString("android.permission.PACKAGE_USAGE_STATS_s", "1");
//            } else {
//                parameters.putString("android.permission.PACKAGE_USAGE_STATS_s", "0");
//            }
//        }
    }
}
