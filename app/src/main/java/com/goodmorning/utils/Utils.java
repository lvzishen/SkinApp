package com.goodmorning.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by hejunge on 2017/11/17.
 */

public class Utils {

    public static String getNewsCountry(Context context) {
        //必须使用XAL提供的跨进程保存数据 否则在详情页读不到数据
        String country = SharedPrefUtil.getString(context, SharedPrefConstant.NEWS_COUNTRY, "");
        if (TextUtils.isEmpty(country)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String oldCountry = preferences.getString(SharedPrefConstant.NEWS_COUNTRY, "");
            country = oldCountry;
            if (!TextUtils.isEmpty(oldCountry)) {
                //只有第一次升级需要同步旧值
                setNewsCountry(context, oldCountry);
            }
        }

        return country;
    }

    public static void setNewsCountry(Context context, String country) {
        SharedPrefUtil.setString(context, SharedPrefConstant.NEWS_COUNTRY, country);
    }

    public static void setLang(Context context, String lang) {
        SharedPrefUtil.setString(context, SharedPrefConstant.LANGUAGE, lang);
    }

    public static String getLang(Context context) {
        //必须使用XAL提供的跨进程保存数据 否则在详情页读不到数据
        String lang = SharedPrefUtil.getString(context, SharedPrefConstant.LANGUAGE, "");
        if (TextUtils.isEmpty(lang)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String oldLang = preferences.getString(SharedPrefConstant.LANGUAGE, "");
            lang = oldLang;
            if (!TextUtils.isEmpty(oldLang)) {
                //只有第一次升级需要同步旧值
                setLang(context, oldLang);
            }
        }
        return lang;
    }

    /**
     * 整数相除 保留一位小数
     * (若小于1000则返回原数,大于1000则除以1000)
     */
    public static String division1k(int a) {
        if (a < 1000) {
            return a + " ";
        }
        String result;
        float num = (float) a / 1000;
        DecimalFormat df = new DecimalFormat("0.0");
        result = df.format(num) + "k ";
        return result;
    }

    /**
     * 判断当前APP语言是否为从右到左的国家
     *
     * @param context
     * @return
     */
    public static boolean isLanguageRTL(Context context) {
        String localeLanguage = Utils.getLang(context);
        if ((localeLanguage.equals("fa")) || (localeLanguage.equals("ar")) || (localeLanguage.equals("iw"))) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
        } else {
            return false;
        }
    }

    public static String divisionViewNum(int a) {
        if (a < 1000) {
            return a + " ";
        } else if (a >= 1000 && a < 1000000) {
            float var2;
            var2 = (float) a / 1000.0F;
            DecimalFormat var3 = new DecimalFormat("0.0");
            return var3.format((double) var2) + "k ";
        } else {
            float var2 = (float) a / 1000000.0F;
            DecimalFormat var3 = new DecimalFormat("0.0");
            return var3.format((double) var2) + "m ";
        }
    }

    public static int[] getFirstAndLastCompletelyPosition(RecyclerView recyclerView) {
        int[] array = new int[2];
        if (recyclerView == null) {
            return array;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //判断是当前layoutManager是否为LinearLayoutManager
        // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            try {
                //获取最后一个可见view的位置
                int last = linearManager.findLastCompletelyVisibleItemPosition();
                //获取第一个可见view的位置
                int first = linearManager.findFirstCompletelyVisibleItemPosition();
                array[0] = first;
                array[1] = last;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public static int[] getFirstAndLastPosition(RecyclerView recyclerView) {
        int[] array = new int[2];
        if (recyclerView == null) {
            return array;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //判断是当前layoutManager是否为LinearLayoutManager
        // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            try {
                //获取最后一个可见view的位置
                int last = linearManager.findLastVisibleItemPosition();
                //获取第一个可见view的位置
                int first = linearManager.findFirstVisibleItemPosition();
                array[0] = first;
                array[1] = last;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    /**
     * todo 接入方需要提供给SDK
     * @param context
     * @return
     */
    public static String getSelfGooglePlayPageUrl(Context context) {
        return "https://play.google.com/store/apps/details?id=" + context.getPackageName();
    }



    /**
     * SDCARD是否存
     */
    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

}