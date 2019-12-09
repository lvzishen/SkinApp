package com.goodmorning.utils;

import android.util.Log;

import com.baselib.sp.SharedPref;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class CheckUtils {
//    public static String keyStartTime;
    /**
     * 检查是否显示每日一图
     * 1、当日早上6点到次日早上6点
     * 2、展示过就不展示了
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return true 显示，false 不显示
     */
    public static boolean isShowPic(long startTime,long endTime){
        long currentTime = System.currentTimeMillis();
        if (currentTime >= startTime && currentTime <= endTime){
            //是否显示过了
            boolean isShowed  = SharedPref.getBoolean(getApplicationContext(),String.valueOf(startTime),false);
            if (isShowed){
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否显示每日一图
     * @param startTime
     * @return
     */
    public static boolean isShowPic(String startTime){
        boolean isShowed  = SharedPref.getBoolean(getApplicationContext(),String.valueOf(startTime),false);
        if (isShowed){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 是否显示语言列表
     * @return true 显示，false 不显示
     */
    public static boolean isShowLanguage(){
        //是否首次启动
        boolean isShow = SharedPref.getBoolean(getApplicationContext(),SharedPref.ISFIRSTSTART,true);
        if (isShow){
            SharedPref.setBoolean(getApplicationContext(),SharedPref.ISFIRSTSTART,false);
            return true;
        }else {
            return false;
        }
    }
}
