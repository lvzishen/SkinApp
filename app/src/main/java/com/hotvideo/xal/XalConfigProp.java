package com.hotvideo.xal;

import android.content.Context;

import org.interlaken.common.env.BasicProp;


/**
 * Created by renyushuang on 2017/4/27.
 */

public class XalConfigProp extends BasicProp {

    public static final String PROP_FILE = "xal_config.prop";
    private volatile static XalConfigProp mInstance;

    //alex 服务接口
    private static final String XAL_ALEX_HOST = "xal.alex.host";
    private static final String XAL_ALEX_PATH = "xal.alex.server.path";


    private static final String XAL_LACHESIS_ENABLE = "xal.lachesis.enable";
    private static final String FUNC_USER_BEHAVIOR_FEATURE_INSTALLED_APP_LIST_REPORT = "user.behavior.install.app.list.report";
    private static final String FUNC_USER_BEHAVIOR_FEATURE_APP_OP_EVENT_REPORT = "user.behavior.app.op.event";
    private static final String FUNC_USER_BEHAVIOR_FEATURE_APP_USAGE_INFO_REPORT = "user.behavior.app.usage.info.report";
    private static final String FUNC_USER_BEHAVIOR_FEATURE_BASE_INFO_REPORT = "user.behavior.base.info.location.report";

    private static final String XAL_ALEX_ENABLE = "xal.alex.enable";
    private static final String XAL_ALEX_INSTALL_STRATEGY_ENABLE = "xal.alex.install.strategy.enable";
    private static final String XAL_ALEX_STARK_SDK_ENABLE = "xal.alex.starksdk.enable";


    private XalConfigProp(Context c) {
        super(c, PROP_FILE);
    }


    public static XalConfigProp getInstance(Context c) {
        if (null == mInstance) {
            synchronized (XalConfigProp.class) {
                if (null == mInstance) {
                    mInstance = new XalConfigProp(c.getApplicationContext());
                }
            }
        }
        return mInstance;
    }


    public static void reload(Context context) {

        synchronized (XalConfigProp.class) {
            mInstance = new XalConfigProp(context.getApplicationContext());
        }
    }


    public boolean isUserLocationEnable() {
        int defaultEnable = getInt(FUNC_USER_BEHAVIOR_FEATURE_BASE_INFO_REPORT, 0);
        if (defaultEnable == 1) {
            return true;
        }
        return false;
    }

    public boolean isUserAppListEnable() {
        int defaultEnable = getInt(FUNC_USER_BEHAVIOR_FEATURE_INSTALLED_APP_LIST_REPORT, 1);
        if (defaultEnable == 1) {
            return true;
        }
        return false;
    }

    public boolean isUserOpAppEnable() {
        int defaultEnable = getInt(FUNC_USER_BEHAVIOR_FEATURE_APP_OP_EVENT_REPORT, 1);
        if (defaultEnable == 1) {
            return true;
        }
        return false;
    }

    public boolean isUserOpUsageInfoEnable() {
        int defaultEnable = getInt(FUNC_USER_BEHAVIOR_FEATURE_APP_USAGE_INFO_REPORT, 1);
        if (defaultEnable == 1) {
            return true;
        }
        return false;
    }


    public boolean isEnableXalAlex() {
        int flag = getInt(XAL_ALEX_ENABLE, 1);
        return flag == 1;
    }

    public boolean isEnableXalAlexInstallStrategy() {
        int flag = getInt(XAL_ALEX_INSTALL_STRATEGY_ENABLE, 1);
        return flag == 1;
    }

    public boolean isEnableXalAlexStarkSDK() {
        int flag = getInt(XAL_ALEX_STARK_SDK_ENABLE, 1);
        return flag == 1;
    }

    public boolean isEnableXalLachesisSDK() {
        int flag = getInt(XAL_LACHESIS_ENABLE, 0);
        return flag == 1;
    }


    /**
     * 获取Alex实时打点的域名
     */
    public String getXalAlexUrl() {

        String host = get(XAL_ALEX_HOST);
        String path = get(XAL_ALEX_PATH);

        String dstServerUrl = host + path;
        return dstServerUrl;
    }
}