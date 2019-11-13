package com.ads.lib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.format.DateUtils;
import android.util.Log;

import com.baselib.cloud.CloudPropertyManager;
import com.goodmorning.config.GlobalConfig;

/**
 * Created by tangchun on 2017/7/24.
 */

public class InstallTimeHelper {

    private static boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "InstallTimeHelper";

    /**
     * 按照自然日判断是否为新用户
     */
    private static final int MODE_IS_TODAY = 1;

    /**
     * 按照云控判断小于指定天数为新用户
     */
    private static final int MODE_BETWEEN_DAYS = 2;
    //
    private static final String KEY_INSTALL_TIME = "key_install_time";
    private static long sInstallTime = -1L;

    public static long getInstallTime(Context cxt) {
        if (sInstallTime > -1L)
            return sInstallTime;
        try {
            long installTime = -1L;
            if (installTime <= 0L) {
                PackageInfo pi = cxt.getPackageManager().getPackageInfo(cxt.getPackageName(), 0);
                if (pi == null)
                    return -1;
                installTime = pi.firstInstallTime;
            }
            sInstallTime = installTime;
            //SharedPref.setLong(cxt,KEY_INSTALL_TIME,installTime);
            return installTime;
        } catch (Exception e) {
        }
        return -1;
    }

    private static final long ONEDAY = 24L * 3600L * 1000L;

    public static boolean isNewUser(Context cxt) {
        long installTime = getInstallTime(cxt);
        long now = System.currentTimeMillis();
        if (DEBUG) {
            Log.v(TAG, "installTime = " + installTime + " , now = " + now);
        }

        // 验证安装时间的合法性
        if (installTime <= 0 || now < installTime) {
            return false;
        }

        final int mode = CloudPropertyManager.getInt(cxt,
                CloudPropertyManager.PATH_NEW_USER_PROP, "mode", 1);

        if (mode == MODE_IS_TODAY && DateUtils.isToday(installTime)) {

            if (DEBUG) {
                Log.v(TAG, "#isNewUser, installation time is today.");
            }
            return true;
        } else if (mode == MODE_BETWEEN_DAYS) {

            final int day = CloudPropertyManager.getInt(cxt,
                    CloudPropertyManager.PATH_NEW_USER_PROP, "day", 1);
            final long millis = now - installTime;

            if (millis < day * ONEDAY) {
                if (DEBUG) {
                    Log.v(TAG, "#isNewUser, It's " +
                            CalendarUtils.millisToDuring(millis) + " from the installation time.");
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isInNewUserProtectDuration(Context cxt, int protectDay) {
        long time = protectDay * 24L * 3600L * 1000L;
        long installTime = getInstallTime(cxt);
        long now = System.currentTimeMillis();
        if (DEBUG) {
            Log.v(TAG, "installTime = " + installTime + " , now = " + now);
        }

        // 验证安装时间的合法性
        if (installTime <= 0 || now < installTime) {
            return false;
        }
        if (now - installTime < time) {
            return true;
        }
        return false;
    }

    public static boolean isInNewUserProtectDurationHour(Context cxt, float hour) {
        float time = hour * 3600L * 1000L;
        long installTime = getInstallTime(cxt);
        long now = System.currentTimeMillis();
        if (DEBUG) {
            Log.v(TAG, "installTime = " + installTime + " , now = " + now);
        }

        // 验证安装时间的合法性
        if (installTime <= 0 || now < installTime) {
            return false;
        }
        if (now - installTime < time) {
            return true;
        }
        return false;
    }

}
