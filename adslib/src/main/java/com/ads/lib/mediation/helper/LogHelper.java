package com.ads.lib.mediation.helper;

import android.util.Log;

import com.ads.lib.ModuleConfig;

public class LogHelper {


    private static final boolean DEBUG = ModuleConfig.DEBUG;

    public static void logD(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }




}
