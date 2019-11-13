package com.baselib.statistic;


import android.content.Context;
import android.os.Bundle;

import org.alex.analytics.Alex;
import org.alex.analytics.biz.logger.general.AppEventsLogger;

/**
 * Created by tangchun on 2017/4/6.
 */

public class StatisticLogger {
    private static AppEventsLogger eventsLogger;

    static{
        eventsLogger = Alex.newLogger("security");
    }
    private StatisticLogger() { //no instance } static {
//        Alex.registerCallBack(new LauncherDesktopStatistics());
        //eventsLogger = Alex.newLogger("booster");
    }

    public static AppEventsLogger getLogger(){
        return eventsLogger;
    }

    public static void log(Context cxt, int functionCode) {
        eventsLogger.logEvent(functionCode);
    }

    public static void log(Context cxt,int eventName, String parameterKey, int parameterValue) {
        Bundle bundle = new Bundle();
        bundle.putInt(parameterKey, parameterValue);
        log(cxt,eventName, bundle);
    }

    public static void log(Context cxt,int functionCode, Bundle bundle) {
        eventsLogger.logEvent(functionCode, bundle);
    }

//    public static void logForCrashReport(int event, String mode, Bundle bundle) {
//        Alex.newLogger(mode).logEvent(event, bundle);
//    }

    public static void logTimeTrackStart(String id) {
        eventsLogger.config().startTimeTrack(id);
    }

    public static void logTimeTrackEnd(String id, int event, Bundle params) {
        eventsLogger.config().endTimeTrack(id).logEvent(event, params);
    }

    public static void logForCrashReport(int event, String mode, Bundle bundle) {
        Alex.newLogger(mode).logEvent(event, bundle);
    }
}
