package com.ads.lib.util;

import android.content.Context;
import android.util.Log;

import com.baselib.sp.SharedPref;
import com.goodmorning.config.GlobalConfig;


/**
 * 广告开关工具
 *
 * @author yangweining
 * 2019/8/7
 */
public class AdSwitchUtil {
    private static final String TAG = "AdSwitchUtil";

    /**
     * 判断广告是否打开
     *
     * @param context
     * @return
     */
    public static boolean isOpen(Context context) {
        boolean isVipOpen = getSwitch(context);
        if (GlobalConfig.DEBUG) {
            Log.i(TAG, "isVipOpen:" + isVipOpen);
        }
        return isVipOpen;
    }

    /**
     * 设置广告开关
     *
     * @param context
     * @param isOpen
     */
    public static void setSwitch(Context context, boolean isOpen) {
        SharedPref.setBoolean(context, SharedPref.KEY_VIP_AD_SWITCH, isOpen);
    }

    /**
     * 获取内购广告开关
     *
     * @param context
     * @return
     */
    public static boolean getSwitch(Context context) {
        return SharedPref.getBoolean(context, SharedPref.KEY_VIP_AD_SWITCH, true);
    }


}
