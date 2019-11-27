package com.baselib.cloud;

import android.content.Context;
import android.util.Log;

import com.goodmorning.config.GlobalConfig;


public class CloudPropertyManager {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "CloudPropertyManager";

    public static final String KEY_CLOUD_PROPERTY = CloudPropertyManagerBridge.KEY_CLOUD_PROPERTY;

    private static CloudPropertyImpl sCloudProperty = null;
    public static final String PATH_NETWORK_MONITOR_SPEED = "speed_swith_ad.prop";
    public static final String PATH_CONFIG = CloudPropertyManagerBridge.PATH_CONFIG;
    public static final String PATH_CONFIG2 = CloudPropertyManagerBridge.PATH_CONFIG2;
    public static final String PATH_BLOG = CloudPropertyManagerBridge.PATH_BLOG;
    public static final String PATH_CHARGER_LOCKER = CloudPropertyManagerBridge.PATH_CHARGER_LOCKER;
    public static final String PATH_EMERGENCY_ADS = CloudPropertyManagerBridge.PATH_EMERGENCY_ADS;
    //	public static final String PATH_MOBV_NATIVE_ADS="mobv_pr";
    public static final String PATH_RUBBISH = CloudPropertyManagerBridge.PATH_RUBBISH;
    public static final String PATH_BOOST_CLOUD = CloudPropertyManagerBridge.PATH_BOOST_CLOUD;
    public static final String PATH_ANTI_VIRUS = CloudPropertyManagerBridge.PATH_ANTI_VIRUS;
    public static final String PATH_APPLOCK_CLOUD = CloudPropertyManagerBridge.PATH_APPLOCK_CLOUD;
    public static final String PATH_GLOBAL_URL = CloudPropertyManagerBridge.PATH_GLOBAL_URL;
    public static final String PATH_RESULT_ADS_CONFIG = CloudPropertyManagerBridge.PATH_RESULT_ADS_CONFIG;
    public static final String PATH_LIST_ADS_CONFIG = CloudPropertyManagerBridge.PATH_LIST_ADS_CONFIG;
    public static final String PATH_AV_ADS_CONFIG = CloudPropertyManagerBridge.PATH_AV_ADS_CONFIG;
    public static final String PATH_ONE_TAP_BOOST_ADS_CONFIG = CloudPropertyManagerBridge.PATH_ONE_TAP_BOOST_ADS_CONFIG;
    public static final String PATH_FUNC_NOTIFICATION = CloudPropertyManagerBridge.PATH_FUNC_NOTIFICATION;
    public static final String PATH_BIG_ADS_STYLE = CloudPropertyManagerBridge.PATH_BIG_ADS_STYLE;
    public static final String PATH_SMART_LOCKER = CloudPropertyManagerBridge.PATH_SMART_LOCKER;
    public static final String PATH_INTERSTITIAL_ADS_PROP = CloudPropertyManagerBridge.PATH_INTERSTITIAL_ADS_PROP;
    public static final String PATH_NOTIFICATION_CLEAN_PROP = CloudPropertyManagerBridge.PATH_NOTIFICATION_CLEAN_PROP;
    public static final String PATH_SHARE_PROP = CloudPropertyManagerBridge.PATH_SHARE_PROP;
    public static final String PATH_NOTIFICATION_SECURITY_PROP = CloudPropertyManagerBridge.PATH_NOTIFICATION_SECURITY_PROP;
    public static final String PATH_NEW_USER_PROP = CloudPropertyManagerBridge.PATH_NEW_USER_PROP;
    public static final String PATH_NOTIFICATION_GUIDE_PROP = CloudPropertyManagerBridge.PATH_NOTIFICATION_GUIDE_PROP;
    public static final String PATH_R1_LOCKER_PROP = CloudPropertyManagerBridge.PATH_R1_LOCKER_PROP;
    public static final String PATH_SHOW_RATE_POSITION_PROP = CloudPropertyManagerBridge.PATH_SHOW_RATE_POSITION_PROP;
    public static final String PATH_WIFI_TRP_PROP = CloudPropertyManagerBridge.PATH_WIFI_RTP_PROP;
    public static final String PATH_URL_PROTECTOR_PROP = CloudPropertyManagerBridge.PATH_URL_PROTECTOR_PROP;
    public static final String PATH_SAFE_BROWSER_PROP = CloudPropertyManagerBridge.PATH_SAFE_BROWSER_PROP;
    public static final String PATH_CALL_BLOCK_AD_PROP = CloudPropertyManagerBridge.PATH_CALL_BLOCK_AD_PROP;
    public static final String PATH_SAFE_BROADCAST_PROP = CloudPropertyManagerBridge.PATH_SAFE_BROADCAST_PROP;
    public static final String PATH_LIKE_US_PROP = CloudPropertyManagerBridge.PATH_LIKE_US_PROP;
    public static final String PATH_APPLOCK_TIME = CloudPropertyManagerBridge.PATH_APPLOCK_TIME;
    public static final String PATH_PRIVACY_PROTECT = CloudPropertyManagerBridge.PATH_PRIVACY_PROTECT;
    public static final String PATH_OREO_GUIDE_RULE = CloudPropertyManagerBridge.PATH_OREO_GUIDE_RULE;
    public static final String PATH_LOCK_SCREEN_PROP = CloudPropertyManagerBridge.PATH_LOCK_SCREEN_PROP;
    public static final String PATH_FEED_GD_PROP = CloudPropertyManagerBridge.PATH_FEED_GD_PROP;
    public static final String PATH_DIS_CHARGE_PROP = CloudPropertyManagerBridge.PATH_DIS_CHARGE_PROP;
    public static final String PATH_DIS_CHARGE_PROP_SWITCH = CloudPropertyManagerBridge.PATH_DIS_CHARGE_PROP_SWITCH;
    public static final String PATH_SUPER_AD = CloudPropertyManagerBridge.PATH_SUPER_AD;
    public static final String PATH_TOP_PERMISSION_GUIDE = CloudPropertyManagerBridge.PATH_TOP_PERMISSION_GUIDE;
    public static final String PATH_APP_CACHE_PROP = CloudPropertyManagerBridge.PATH_APP_CACHE_PROP;
    public static final String PATH_RESULT_PRI = CloudPropertyManagerBridge.PATH_RESULT_PRI;
    public static final String PATH_HOME_AD_PROP = CloudPropertyManagerBridge.PATH_HOME_AD_PROP;
    public static final String FUNC_ADS_CONFIG_PROP = CloudPropertyManagerBridge.FUNC_ADS_CONFIG_PROP;
    public static final String SPLASH_AGE_PROP = CloudPropertyManagerBridge.SPLASH_AGE_PROP;
    public static final String PATH_HOME_GUIDE_PROP = CloudPropertyManagerBridge.PATH_HOME_GUIDE_PROP;
    public static final String PATH_INTERSTITIAL_STRATEGY = CloudPropertyManagerBridge.PATH_INTERSTITIAL_STRATEGY;
    public static final String PATH_WHATSAPP_CARD_AD = CloudPropertyManagerBridge.PATH_WHATSAPP_CARD_AD;
    public static final String PATH_DEEP_CLEAN_AD = CloudPropertyManagerBridge.PATH_DEEP_CLEAN_AD;
    public static final String PATH_PREVIEW_AD_PROP = CloudPropertyManagerBridge.PATH_PREVIEW_AD_PROP;
    public static final String PATH_LIST_DETAIL_AD_PROP = CloudPropertyManagerBridge.PATH_LIST_DETAIL_AD_PROP;
    public static final String PATH_NOTIFY_CARD_AD = CloudPropertyManagerBridge.PATH_NOTIFY_CARD_AD;
    public static final String PATH_OUT_APPLOCK_OP = CloudPropertyManagerBridge.PATH_OUT_APPLOCK_OP;
    public static final String PATH_FLOAT_WINDOW = CloudPropertyManagerBridge.PATH_FLOAT_WINDOW;
    public static final String PATH_HOME_AUTOU = CloudPropertyManagerBridge.PATH_HOME_AUTOU;
    public static final String PATH_CLIPBROAD = CloudPropertyManagerBridge.PATH_CLIPBROAD;
    public static final String PATH_SHARE = CloudPropertyManagerBridge.PATH_SHARE;

    public static final String PATH_HOME_GREETING = CloudPropertyManagerBridge.PATH_HOME_GREETING;

    public static final String PATH_HOME_BACK_GUIDE_PROP = CloudPropertyManagerBridge.PATH_HOME_BACK_GUIDE_PROP;
    public static final String PATH_UNINSTALL_POP_DIALOG_PROP = CloudPropertyManagerBridge.PATH_UNINSTALL_POP_DIALOG_PRO;
    public static final String PATH_CHECK_UPDATE = CloudPropertyManagerBridge.PATH_CHECK_UPDATE;
    public static final String PATH_EVERYDAY_PIC = CloudPropertyManagerBridge.PATH_EVERYDAY_PIC;

    public static CloudPropertyImpl getCloudPropertyServiceStub(Context cxt) {
        synchronized (CloudPropertyManager.class) {
            if (sCloudProperty == null) {
                sCloudProperty = new CloudPropertyImpl(cxt);
            }
        }
        return sCloudProperty;
    }


    public static float getFloat(Context cxt, String path, String key, float def) {
        return CloudPropertyManagerBridge.getFloat(cxt, path, key, def);
    }

    public static int getInt(Context cxt, String path, String key, int def) {
        return CloudPropertyManagerBridge.getInt(cxt, path, key, def);
    }

    public static String getString(Context cxt, String path, String key, String def) {
        return CloudPropertyManagerBridge.getString(cxt, path, key, def);
    }

    public static long getLong(Context cxt, String path, String key, long def) {
        return CloudPropertyManagerBridge.getLong(cxt, path, key, def);
    }

    public static void reload(Context cxt, String path) {
        if (sCloudProperty == null)
            return;
        try {
            sCloudProperty.reload(path);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }

    public static boolean isDirty(Context cxt, String[] pathes) {
        return CloudPropertyManagerBridge.idDirty(cxt, pathes);
    }


}
