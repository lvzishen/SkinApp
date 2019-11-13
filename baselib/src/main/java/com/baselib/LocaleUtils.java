package com.baselib;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.Nullable;

import com.baselib.sp.SharedPref;
import com.goodmorning.config.GlobalConfig;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleUtils {
    private static final String TAG = "LocaleUtils";
    private static List<LocaleBean> localeBeanList = new ArrayList<>();

    /**
     * 保存Locale的key
     */
    private static final String LOCALE_KEY = "LOCALE_KEY";

    /**
     * 获取用户设置的Locale
     *
     * @param pContext Context
     * @return Locale
     */
    public static LocaleBean getUserLocale(Context pContext) {
        String _LocaleJson = SharedPref.getString(pContext, LOCALE_KEY, "");
        if (GlobalConfig.DEBUG) {
            Log.i(TAG, _LocaleJson);
        }
        if (TextUtils.isEmpty(_LocaleJson)) {
            return null;
        }
        return jsonToLocale(_LocaleJson);
    }

    /**
     * 获取当前的Locale
     *
     * @param pContext Context
     * @return Locale
     */
    public static Locale getCurrentLocale(Context pContext) {
        Locale _Locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
            _Locale = pContext.getResources().getConfiguration().getLocales().get(0);
        } else {
            _Locale = pContext.getResources().getConfiguration().locale;
        }
        return _Locale;
    }

    /**
     * 保存用户设置的Locale
     *
     * @param pContext    Context
     * @param localeBean Locale
     */
    public static void saveUserLocale(Context pContext, LocaleBean localeBean) {
        String _LocaleJson = localeToJson(localeBean);
        SharedPref.setString(pContext, LOCALE_KEY, _LocaleJson);
    }

    /**
     * Locale转成json
     *
     * @param pUserLocale UserLocale
     * @return json String
     */
    private static String localeToJson(LocaleBean pUserLocale) {
        Gson _Gson = new Gson();
        return _Gson.toJson(pUserLocale);
    }

    /**
     * json转成Locale
     *
     * @param pLocaleJson LocaleJson
     * @return Locale
     */
    private static LocaleBean jsonToLocale(String pLocaleJson) {
        Gson _Gson = new Gson();
        return _Gson.fromJson(pLocaleJson, LocaleBean.class);
    }

    /**
     * 更新Locale
     *
     * @param pContext       Context
     * @param localeBean New User Locale
     */
    public static boolean updateLocale(Context pContext, LocaleBean localeBean) {
        if (localeBean != null && needUpdateLocale(pContext, localeBean.locale)) {
            Configuration _Configuration = pContext.getResources().getConfiguration();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(localeBean.locale);
            } else {
                _Configuration.locale = localeBean.locale;
            }
            DisplayMetrics _DisplayMetrics = pContext.getResources().getDisplayMetrics();
            pContext.getResources().updateConfiguration(_Configuration, _DisplayMetrics);
            saveUserLocale(pContext, localeBean);
            return true;
        }
        return false;
    }

    /**
     * 判断需不需要更新
     *
     * @param pContext       Context
     * @param pNewUserLocale New User Locale
     * @return true / false
     */
    public static boolean needUpdateLocale(Context pContext, Locale pNewUserLocale) {
        return pNewUserLocale != null && !getCurrentLocal(pContext).locale.equals(pNewUserLocale);
    }

    /**
     * 获取当前系统语言
     *
     * @param pContext
     * @return
     */
    public static LocaleBean getCurrentLocal(Context pContext) {
        LocaleBean localeBean = getUserLocale(pContext);
        if (localeBean == null) {
            Locale locale = getCurrentLocale(pContext);
            localeBean = new LocaleBean("", locale, "");
            List<LocaleBean> list = getLocaleBeanList();
            if (list.contains(localeBean)) {
                localeBean = list.get(list.indexOf(localeBean));
            } else {
                localeBean = list.get(0);
            }

        }
        return localeBean;

    }

    public static Context attachBaseContext(Context context) {
        Resources resources = context.getResources();
        LocaleBean locale = getUserLocale(context);// getSetLocale方法是获取新设置的语言
        if (locale == null) {
            return context;
        }
        if (GlobalConfig.DEBUG) {
            Log.i(TAG, "updateResources:" + locale);
        }
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale.locale);
            configuration.setLocales(new LocaleList(locale.locale));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale.locale);
        } else {
            configuration.locale = locale.locale;
        }
        return context.createConfigurationContext(configuration);
    }


    public static List<LocaleBean> getLocaleBeanList() {
        if (localeBeanList.isEmpty()) {
            LocaleBean en = new LocaleBean("en", Locale.ENGLISH, "English");
            LocaleBean es = new LocaleBean("es", new Locale("es"), "Espanol");
            LocaleBean pt = new LocaleBean("pt", new Locale("pt"), "Português");
            LocaleBean zh_cn = new LocaleBean("zh-rCN", Locale.SIMPLIFIED_CHINESE, "中文(简体)");
            LocaleBean zh_tw = new LocaleBean("zh-rTW", Locale.TRADITIONAL_CHINESE, "中文(繁體)");
            LocaleBean de = new LocaleBean("de", Locale.GERMAN, "Deutsch");
            LocaleBean fr = new LocaleBean("fr", Locale.FRENCH, "Français");
            LocaleBean hi = new LocaleBean("hi", new Locale("hi"), "हिन्दी");
            LocaleBean it = new LocaleBean("it", Locale.ITALIAN, "Italiano");
            LocaleBean ja = new LocaleBean("ja", Locale.JAPANESE, "日本語");
            LocaleBean ko = new LocaleBean("ko", Locale.KOREAN, "한국어");
            LocaleBean ru = new LocaleBean("ru", new Locale("ru"), "русский");
            LocaleBean th = new LocaleBean("th", new Locale("th"), "ไทย");
            LocaleBean tr = new LocaleBean("tr", new Locale("tr"), "Türk dili");
            LocaleBean vi = new LocaleBean("vi", new Locale("vi"), "Tiếng việt");
            LocaleBean in = new LocaleBean("in", new Locale("in"), "Indonesia");
            LocaleBean fa = new LocaleBean("fa", new Locale("fa"), "پارس");
            LocaleBean ar = new LocaleBean("ar", new Locale("ar"), "العربية");
            localeBeanList.add(en);
            localeBeanList.add(es);
            localeBeanList.add(pt);
            localeBeanList.add(zh_cn);
            localeBeanList.add(zh_tw);
            localeBeanList.add(de);
            localeBeanList.add(fr);
            localeBeanList.add(hi);
            localeBeanList.add(it);
            localeBeanList.add(ja);
            localeBeanList.add(ko);
            localeBeanList.add(ru);
            localeBeanList.add(th);
            localeBeanList.add(tr);
            localeBeanList.add(vi);
            localeBeanList.add(in);
            localeBeanList.add(fa);
            localeBeanList.add(ar);
        }
        return localeBeanList;
    }


    public static class LocaleBean {
        public String tag;
        public Locale locale;
        public String text;

        public LocaleBean(String tag, Locale locale, String text) {
            this.tag = tag;
            this.locale = locale;
            this.text = text;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            Locale other = ((LocaleBean) obj).locale;
            if ("zh".equals(other.getLanguage()) && "zh".equals(locale.getLanguage())) {
                if (!"CN".equals(other.getCountry()) && !"CN".equals(locale.getCountry())) {
                    return true;
                }
                return locale.getCountry().equals(other.getCountry());
            }
            return locale.getLanguage().equals(other.getLanguage());
        }

        @Override
        public String toString() {
            return "LocaleBean{" +
                    "tag='" + tag + '\'' +
                    ", locale=" + locale +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

}

