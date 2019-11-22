package org.thanos.netcore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.homeplanet.sharedpref.SharedPref;
import org.interlaken.common.utils.PackageInfoUtil;
import org.thanos.netcore.internal.MorningDataCore;

/**
 * Created by zhaobingfeng on 2019-08-13.
 */
@SuppressLint("LongLogTag")
public class ThanosDataCorePrefUtils {
    private static final String SP_KEY_NEWS_COUNTRY_BY_SERVER = "n_c_b_s";
    private static final String SP_KEY_NEWS_LANG_BY_SERVER = "n_l_b_s";
    private static final String TAG = "Thanos.DataCorePrefUtils";
    private static String sPrefFileName;

    private static String getPrefFileName(Context context) {
        if (TextUtils.isEmpty(sPrefFileName)) {
            sPrefFileName = context.getPackageName() + "_" + PackageInfoUtil.getSelfFirstInstallTime(context) + "tdc";
        }
        return sPrefFileName;
    }

    public static void setNewsCountryAndLangByServer(Context context, String newsCountry, String lang) {
        if (MorningDataCore.DEBUG) {
            Log.d(TAG, "setNewsCountryAndLangByServer() called with: context = [" + context + "], newsCountry = [" + newsCountry + "], lang = [" + lang + "]");
        }
        SharedPref.setStringVal(context, getPrefFileName(context), SP_KEY_NEWS_COUNTRY_BY_SERVER, newsCountry);
        SharedPref.setStringVal(context, getPrefFileName(context), SP_KEY_NEWS_LANG_BY_SERVER, lang);
    }

    public static String getNewsCountryByServer(Context context) {
        return SharedPref.getString(context, getPrefFileName(context), SP_KEY_NEWS_COUNTRY_BY_SERVER, null);
    }

    public static String getLangByServer(Context context) {
        return SharedPref.getString(context, getPrefFileName(context), SP_KEY_NEWS_LANG_BY_SERVER, null);
    }
}
