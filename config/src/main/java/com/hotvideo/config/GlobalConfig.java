package com.hotvideo.config;

import android.app.Application;
import android.content.Context;
import android.os.Build;

/**
 * 全局配置
 */
public class GlobalConfig {
    public static final boolean DEBUG = false;

    /**
     * 先将特殊应用的过滤逻辑去掉
     */
    public static final boolean FILTER_SPECIAL_APP_CACHE = false;

    /**
     * 是否使用新版的权限引导
     */
    public static final boolean USE_PERMISSION_GUIDE_NEWER = true;



    private static int targetSDKVersion = -1;

    /**
     * ApplicationContext对象， 用于获取全局Context信息z
     */
    public static Context sContext = null;

    public static void init(Context context) {
        if (context instanceof Application) {
            sContext = context;
            return;
        }

        sContext = context.getApplicationContext();
    }

    public static boolean isOreoAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 当前的TargetSDK版本号
     */
    public static int getTargetSDKVersion() {
        if (sContext == null) {
            throw new RuntimeException("context has not inited.");
        }
        if (targetSDKVersion < 0) {
            targetSDKVersion = sContext.getApplicationInfo().targetSdkVersion;
        }
        return targetSDKVersion;
    }


    public static boolean needCompatPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getTargetSDKVersion() >= Build.VERSION_CODES.M;
    }

}
