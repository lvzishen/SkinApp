package com.baselib;

import android.content.Context;
import android.util.Log;

import com.hotvideo.config.GlobalConfig;

import org.adoto.xut.AdotoUserTagKeys;
import org.adoto.xut.AdotoUserTagSDK;
import org.interlaken.common.utils.SimcardUtils;

import java.util.Locale;

public class NewsSDKUtils {
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "NewsSDKUtils";

    public static boolean isAllowShowNews(Context context) {
        String simMccCountryCode = SimcardUtils.getSimMccCountryCode(context);
        String location = AdotoUserTagSDK.getUserTagKeyWordInfo(AdotoUserTagKeys.USER_TAG_KEY_CCS, "");
        boolean isChina = "CN".equalsIgnoreCase(location) || "CN".equalsIgnoreCase(simMccCountryCode) || Locale.getDefault().getCountry().equalsIgnoreCase("CN");
        if (DEBUG) {
            Log.i(TAG, "isAllowShowNews: location=" + location + ", mccCountryCode=" + simMccCountryCode + ", " + Locale.getDefault().getCountry() + ", isChina=" + isChina);
        }
        return !isChina;
    }
}
