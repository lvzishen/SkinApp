package com.goodmorning.utils;

import com.baselib.sp.SharedPref;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class CheckUtils {
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
                SharedPref.setBoolean(getApplicationContext(),String.valueOf(startTime),true);
                return true;
            }
        }
        return false;
    }

    /**
     * 是否显示语言列表
     * @return true 显示，false 不显示
     */
    public static boolean isShowLanguage(){
        //是否首次启动
        boolean isShow = SharedPref.getBoolean(getApplicationContext(),SharedPref.ISFIRSTSTART,true);
        if (isShow){
            return true;
        }else {
            return false;
        }
    }
}
