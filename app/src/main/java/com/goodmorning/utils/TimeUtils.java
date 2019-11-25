package com.goodmorning.utils;

import java.util.Calendar;

public class TimeUtils {

    /**
     * 获取几时
     * @return
     */
    public static int getHourTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour;
    }
}
