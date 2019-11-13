package com.ads.lib.log;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.ads.lib.mediation.helper.LogHelper;
import com.ads.lib.prop.TrackingProp;
import com.baselib.statistic.StatisticLogger;

import org.alex.analytics.AlexEventsConstant;


public class LogEventUtilsTracking {

    private static String TAG = "LogEventUtilsTracking";

    /**
     * 广告请求打点
     *
     * @param context
     * @param unitId
     * @param adpostionId
     */
    public static void logLoad(Context context, String unitId, String adpostionId) {
        logOperation(context,unitId,adpostionId,"ad_load");
    }

    /**
     * 广告展示打点
     *
     * @param context
     * @param unitId
     * @param adpostionId
     */
    public static void logImpression(Context context, String unitId, String adpostionId) {
        logOperation(context,unitId,adpostionId,"ad_impression");
    }


    /**
     * 广告请求成功打点
     *
     * @param context
     * @param unitId
     * @param adpostionId
     */
    public static void logLoadSuccess(Context context, String unitId, String adpostionId,long time) {
        logOperation(context,unitId,adpostionId,"ad_load_success",time,null);
    }

    /**
     * 广告请求失败打点
     *
     * @param unitId
     * @param adpostionId
     */
    public static void logLoadFail(Context context, String unitId, String adpostionId,long time,String resultCode) {
        logOperation(context, unitId, adpostionId, "ad_load_fail",time,resultCode);
    }

    /**
     * 广告点击打点
     *
     * @param unitId
     * @param adpostionId
     */
    public static void logClick(Context context, String unitId, String adpostionId) {
        logOperation(context, unitId, adpostionId, "ad_click");
    }

    /**
     * 广告关闭打点
     *
     * @param unitId
     * @param adpostionId
     */
    public static void logAdClose(Context context, String unitId, String adpostionId) {
        logOperation(context, unitId, adpostionId, "ad_close");
    }

    private static void logOperation(Context context, String unitId, String adpostionId, String logName) {
        logOperation(context, unitId, adpostionId, logName,-1,null);
    }


    private static void logOperation(Context context,String unitId, String adpostionId, String logName,long time,String resultCode){
        Context ctx = context.getApplicationContext();
        if (!isEnableTracking(ctx)) return;
        Bundle bundle = new Bundle();
        bundle.putString(AlexEventsConstant.XALEX_OPERATION_NAME_STRING, logName);
        bundle.putString(AlexEventsConstant.XALEX_OPERATION_TRIGGER_STRING, unitId);
        bundle.putString(AlexEventsConstant.XALEX_OPERATION_PACKAGE_STRING, adpostionId);
        if(time != -1){
            bundle.putLong(AlexEventsConstant.XALEX_OPERATION_TO_POSITION_Y_INT, time);
        }
        if(!TextUtils.isEmpty(resultCode)){
            bundle.putString(AlexEventsConstant.XALEX_PUSH_OPERATION_RESULT_CODE_STRING,resultCode);
        }
        LogHelper.logD(TAG,"#logName: " + bundle.toString());
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_OPERATION, bundle);
    }



    /**
     * 是否开启打点
     *
     * @return
     */
    private static boolean isEnableTracking(Context context) {
        return TrackingProp.getInstance(context.getApplicationContext())
                .isEnableTracking();
    }


}
