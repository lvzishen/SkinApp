package com.goodmorning.utils;

import android.app.Activity;

import com.baselib.sp.SharedPref;
import com.creativeindia.goodmorning.R;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class AppUtils {
    /**
     * 切换主题
     * @param activity
     */
    public static void changeTheme(Activity activity){
        boolean isSwitchTheme = SharedPref.getBoolean(getApplicationContext(),SharedPref.IS_SWITCH_THEME,false);
        if (isSwitchTheme){
            activity.setTheme(R.style.AppThemeNight);
        }else {
            activity.setTheme(R.style.AppTheme);
        }
    }

    /**
     * 切换主题
     * @param activity
     * @param isSwitchTheme
     */
    public static void switchTheme(Activity activity,boolean isSwitchTheme){
        SharedPref.setBoolean(getApplicationContext(),SharedPref.IS_SWITCH_THEME,isSwitchTheme);
        activity.recreate();
    }

    /**
     * 根据主题设置获取状态栏颜色
     * @return
     */
    public static int changeStatusColor(){
        boolean isSwitchTheme = SharedPref.getBoolean(getApplicationContext(),SharedPref.IS_SWITCH_THEME,false);
        if (isSwitchTheme){
            return ResUtils.getColor(R.color.color_1B0325);
        }else {
            return ResUtils.getColor(R.color.white);
        }
    }

    /**
     * 根据主题获取状态栏是否高亮
     * @return
     */
    public static boolean isStatusLight(){
        boolean isSwitchTheme = SharedPref.getBoolean(getApplicationContext(),SharedPref.IS_SWITCH_THEME,false);
        if (isSwitchTheme){
            return false;
        }else {
            return true;
        }
    }
}
