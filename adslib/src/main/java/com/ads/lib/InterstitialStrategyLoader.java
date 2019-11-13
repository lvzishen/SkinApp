package com.ads.lib;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.baselib.cloud.CloudPropertyManagerBridge;
import com.baselib.sp.SharedPref;
import com.baselib.statistic.AdjustStatConstant;
import com.baselib.statistic.StatisticConstants;
import com.baselib.statistic.StatisticLoggerX;
import com.baselib.ui.CommonConstants;
import com.goodmorning.config.GlobalConfig;

/**
 * Created by apus on 2018/9/25.
 */

public class InterstitialStrategyLoader {
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "InterstitialStrategyLoader";
    private Context mContext = null;
    private int mType = CommonConstants.TYPE_RESULT_SUPPLEMENT;

    public InterstitialStrategyLoader(Context cxt, int type) {
        mContext = cxt;
        mType = type;
    }

    /**
     * 广告开关是否开启
     *
     * @param cxt
     * @return
     */
    private boolean isAdsEnabled(Context cxt) {
        String key = "";
        switch (mType) {
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
                key = "detachCharger_interstitial_switch";
                break;
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
                key = "hangedCall_interstitial_switch";
                break;
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
                key = "splash_interstitial_switch";
                break;
        }
        if (TextUtils.isEmpty(key))
            return false;
        int anInt = CloudPropertyManagerBridge.getInt(cxt, CloudPropertyManagerBridge.PATH_INTERSTITIAL_STRATEGY, key, 0);
        if(DEBUG){
            if(mType==CommonConstants.TYPE_RESULT_DETACHCHARGER){
                Log.d(TAG, "充电助手开关值: "+anInt);
            }

        }
        return  anInt == 1;
    }

    /**
     * 针对新用户做保护
     *
     * @param cxt
     * @return
     */
    private boolean isUnderNewUserProtection(Context cxt) {
        String key = "";
        switch (mType) {
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
                key = "detachCharger_interstitial_days_under_protection";
                break;
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
                key = "hangedCall_interstitial_days_under_protection";
                break;
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
                key = "splash_interstitial_days_under_protection";
                break;
        }
        if (TextUtils.isEmpty(key))
            return true;
        int days = CloudPropertyManagerBridge.getInt(cxt, CloudPropertyManagerBridge.PATH_INTERSTITIAL_STRATEGY, key, 2);
        if(DEBUG){
            if(mType==CommonConstants.TYPE_RESULT_DETACHCHARGER){
                Log.d(TAG, "充电助手新手保护天数: "+days);
            }

        }
        long installTime = getInstallTimeIfNeccessary(mContext);
        long now = System.currentTimeMillis();
        if ((now - installTime) < (days * DateUtils.DAY_IN_MILLIS)) {
            return true;
        }
        return false;
    }

    /**
     * 广告每天最大展示次数
     *
     * @return
     */
    private int getDailyMaxShowCnt() {
        String key = "";
        switch (mType) {
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
                key = "detachCharger_interstitial_daily_max";
                break;
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
                key = "hangedCall_interstitial_daily_max";
                break;
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
                key = "splash_interstitial_daily_max";
                break;
        }
        if (TextUtils.isEmpty(key))
            return 0;
        int anInt = CloudPropertyManagerBridge.getInt(mContext, CloudPropertyManagerBridge.PATH_INTERSTITIAL_STRATEGY, key, 0);
        if(DEBUG){
            if(mType==CommonConstants.TYPE_RESULT_DETACHCHARGER){
                Log.d(TAG, "充电助手单天最大展示次数: "+anInt);
            }

        }
        return anInt;

    }

    private long getShowAdsInterval() {
        String key = "";
        switch (mType) {
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
                key = "detachCharger_interstitial_interval";
                break;
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
                key = "hangedCall_interstitial_interval";
                break;
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
                key = "splash_interstitial_interval";
                break;
        }
        if (TextUtils.isEmpty(key))
            return 0L;

        long aLong = CloudPropertyManagerBridge.getLong(mContext, CloudPropertyManagerBridge.PATH_INTERSTITIAL_STRATEGY, key, 3600000L);
        if(DEBUG){
            if(mType==CommonConstants.TYPE_RESULT_DETACHCHARGER){
                Log.d(TAG, "充电助手广告时间间隔: "+aLong);
            }

        }
        return aLong;
    }

    private static long getInstallTimeIfNeccessary(Context cxt) {
        long installTime = SharedPref.getLong(cxt, "key_function_first_install_time", -1L);
        if (installTime > 0L)
            return installTime;
        else {
            long now = System.currentTimeMillis();
            SharedPref.setLong(cxt, "key_function_first_install_time", now);
            return now;
        }
    }


    public boolean shouldShowAds() {
//        Context cxt = mContext;
//        boolean shouldShowAds = isAdsEnabled(cxt);
//        if(DEBUG)
//            Log.v(TAG,"shouldShowAds = "+shouldShowAds);
//        if(!shouldShowAds) {
//            return false;
//        }
//
//        boolean underNewUserProtection = isUnderNewUserProtection(cxt);
//        if(DEBUG)
//            Log.v(TAG,"underNewUserProtection = "+underNewUserProtection);
//        if(underNewUserProtection)
//            return false;
//        long lastShowTime = SharedPref.getLong(mContext,"KEY_LAST_SHOW_SPLASH_ADS_TIME_" + mType,-1L);
//        long adsInterval = getShowAdsInterval();
//        long now = System.currentTimeMillis();
//        if((now - lastShowTime)<adsInterval){
//            if(DEBUG)
//                Log.v(TAG,"function interval is not ok ,"+adsInterval);
//            return false;
//        }else{
//            if(DEBUG)
//                Log.v(TAG,"function interval is  ok ,"+adsInterval);
//        }
//        if(DateUtils.isToday(lastShowTime)){
//            int cnt = SharedPref.getInt(mContext, "KEY_DAILY_SHOW_SPLASH_ADS_CNT_" + mType, 0);
//            int dailyMax = getDailyMaxShowCnt();
//            if(DEBUG){
//                Log.v(TAG,"showedCnt = "+cnt+", daily Max = "+dailyMax);
//            }
//            if(cnt >= dailyMax){
//                return false;
//            }
//        }else{
//
//        }
        return true;
    }

    public void onShowAds(String fromSource) {
        StatisticLoggerX.logShow(StatisticConstants.ADS_INTER_SHOW,
                StatisticConstants.ACTIVITY, fromSource);
        Adjust.trackEvent(new AdjustEvent(AdjustStatConstant.KEY_MEMORY_BOOST_AD));
        long now = System.currentTimeMillis();
        long firstShowTimeToady = SharedPref.getLong(mContext, "KEY_FIRST_SHOW_SPLASH_ADS_TIME_TODAY_" + mType, -1L);
        if (DateUtils.isToday(firstShowTimeToady)) {
            int cnt = SharedPref.getInt(mContext, "KEY_DAILY_SHOW_SPLASH_ADS_CNT_" + mType, 0);
            SharedPref.setInt(mContext, "KEY_DAILY_SHOW_SPLASH_ADS_CNT_" + mType, cnt + 1);
        } else {
            //每天只记录第一次展示的时间，如果发现当次的展示时间与这个时间不一致，那么就姜当天的展示从1开始累加
            SharedPref.setInt(mContext, "KEY_DAILY_SHOW_SPLASH_ADS_CNT_" + mType, 1);
            SharedPref.setLong(mContext, "KEY_FIRST_SHOW_SPLASH_ADS_TIME_TODAY_" + mType, now);
        }
        SharedPref.setLong(mContext, "KEY_LAST_SHOW_SPLASH_ADS_TIME_" + mType, now);
    }
}
