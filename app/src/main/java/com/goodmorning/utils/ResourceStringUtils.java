package com.goodmorning.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;

import androidx.annotation.ArrayRes;
import androidx.annotation.PluralsRes;

import java.util.Locale;

/**
 * Created by zhangqubo on 2016/9/9.
 */
public class ResourceStringUtils {

    private static final String TAG = "ResourceStringUtils";

    private static Resources mResources;

    private static boolean mNeedReset = false;

    public static Resources getResources(Context mContext) {
        return mContext.getResources();
    }

    public static String[] getStringArray(@ArrayRes int id, Context context) {
        return context.getResources().getStringArray(id);
    }
    public static String getString(Resources resources, int resId) {
        return resources.getString(resId);
    }

    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public static String getString(int resId, Context context) {
        return context.getResources().getString(resId);
    }

    public static String getQuantityString(Context mContext, @PluralsRes int id, int quantity, Object... formatArgs) {
        return mContext.getResources().getQuantityString(id, quantity, formatArgs);
    }

    public static String getString(Context mContext, int resId, Object... formatArgs) {

        String str;
        Resources res_lang = null;
        Resources currentResources = mContext.getResources();
        try {
            str = res_lang.getString(resId, formatArgs);
        } catch (Exception e) {
            str = null;
        } finally {
            resetLocale(currentResources);
        }

        if (TextUtils.isEmpty(str)) {
            return mContext.getResources().getString(resId, formatArgs);
        }
        return str;
    }

    private static Resources getResourcesByLocale(Resources res, String language, String country) {
        /**
         * Modified by Potter on 2016/10/26
         */
        if (TextUtils.isEmpty(language) || TextUtils.isEmpty(country)) {
            mNeedReset = false;
            return res;
        }

        language = adjustLanguageCode(language);
        country = country.toUpperCase(Locale.US);

        Configuration systemConfiguration = res.getConfiguration();
        // Same as the system locale
        if (language.equals(systemConfiguration.locale.getLanguage()) && country.equals(systemConfiguration.locale.getCountry())) {
            mNeedReset = false;
            return res;
        }

        if (mResources == null) {
            Configuration configuration = new Configuration(res.getConfiguration());
            configuration.locale = new Locale(language, country);
            long startTime = System.currentTimeMillis();
            mResources = new Resources(res.getAssets(), res.getDisplayMetrics(), configuration);
            long endTime = System.currentTimeMillis();
        } else {
            // Check if it is the same as system locale
            if (!language.equals(systemConfiguration.locale.getLanguage()) || !country.equals(systemConfiguration.locale.getCountry())) {
                Configuration configuration = mResources.getConfiguration();
                configuration.locale = new Locale(language, country);
                long startTime = System.currentTimeMillis();
                mResources.updateConfiguration(configuration, res.getDisplayMetrics());
                long endTime = System.currentTimeMillis();
            }
        }

        mNeedReset = true;

        return mResources;
    }

    private static String adjustLanguageCode(String languageCode) {
        String adjusted = languageCode.toLowerCase(Locale.US);
        // Map new language codes to the obsolete language
        // codes so the correct resource bundles will be used.
        if (languageCode.equals("he")) {
            adjusted = "iw";
        } else if (languageCode.equals("id")) {
            adjusted = "in";
        } else if (languageCode.equals("yi")) {
            adjusted = "ji";
        }

        return adjusted;
    }

    private static void resetLocale(Resources res) {
        if (!mNeedReset) {
            return;
        }
        long startTime = System.currentTimeMillis();
        res.updateConfiguration(res.getConfiguration(), res.getDisplayMetrics());
        long endTime = System.currentTimeMillis();
        return;
    }

}
