package com.goodmorning.utils;

import android.content.Context;
import android.text.TextUtils;

import org.homeplanet.sharedpref.SharedPref;
import org.interlaken.common.utils.PackageInfoUtil;

public class SharedPrefUtil {
    private static String sPrefFileName;

    public static String getPrefFileName(Context context) {
        if (TextUtils.isEmpty(sPrefFileName)) {
            sPrefFileName = context.getPackageName() + "_" + PackageInfoUtil.getSelfFirstInstallTime(context) + "t";
        }
        return sPrefFileName;
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return SharedPref.getBoolean(context, getPrefFileName(context), key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPref.setBooleanVal(context, getPrefFileName(context), key, value);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return SharedPref.getInt(context, getPrefFileName(context), key, defaultValue);
    }

    public static void setInt(Context context, String key, int value) {
        SharedPref.setIntVal(context, getPrefFileName(context), key, value);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return SharedPref.getString(context, getPrefFileName(context), key, defaultValue);
    }

    static void setString(Context context, String key, String value) {
        SharedPref.setStringVal(context, getPrefFileName(context), key, value);
    }
}