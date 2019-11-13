package com.ads.lib.adapter;

import android.content.Context;
import android.text.format.DateUtils;

import com.baselib.sp.SharedPref;

/**
 * Created by zhaozhiwen on 2019/4/4.
 */
public class ExpireTimeUtils {

    //默认广告源的过期时间为45分钟
    public static final long AD_SOURCE_EXPIRE_TIME = DateUtils.MINUTE_IN_MILLIS * 45;

    public static boolean isExpired(Context context,String key){
        long currentTime = System.currentTimeMillis();
        long loadedTime = SharedPref.getLong(context,key,0);
        long diff = currentTime - loadedTime;
        if(diff > 0 && diff > AD_SOURCE_EXPIRE_TIME){
            return true;
        }

        return false;
    }

    public static boolean isExpired(long expiredTime){
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - expiredTime;
        if(diff > AD_SOURCE_EXPIRE_TIME){
            return true;
        }

        return false;
    }


    public static long calcExpiredTime(){
        long currentTime = System.currentTimeMillis();
        long expiredTime = currentTime + AD_SOURCE_EXPIRE_TIME;
        return expiredTime;
    }
}
