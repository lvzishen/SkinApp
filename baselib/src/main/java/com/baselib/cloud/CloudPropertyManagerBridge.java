package com.baselib.cloud;

import android.content.Context;
import android.util.Log;

import com.goodmorning.config.GlobalConfig;


public class CloudPropertyManagerBridge {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "CloudPropertyManager";

    public static final String KEY_CLOUD_PROPERTY = "cloud_property";
    public static final String DIALOG_SCENE_CONFIG = "dialog_scene_global.prop";
    public static final String PATH_CONFIG = "config.prop";
    public static final String PATH_CONFIG2 = "config2.prop";
    public static final String PATH_BLOG = "blog_config.prop";
    public static final String PATH_CHARGER_LOCKER = "charger_locker_config.prop";
    public static final String PATH_EMERGENCY_ADS = "emergency_ads.prop";
    //	public static final String PATH_MOBV_NATIVE_ADS="mobv_pr";
    public static final String PATH_RUBBISH = "fm_ru.prop";
    public static final String PATH_BOOST_CLOUD = "common_prop.prop";
    public static final String PATH_FLOAT_WINDOW_SWITCH = "floatwindow.prop";
    public static final String PATH_HOME_AUTO_RAM = "homepage_auto_ram.prop";
    public static final String PATH_CLIPBOARD_PROP = "clipboard.prop";
    public static final String PATH_HOME_INTER_ADS_PROP = "home_inter_ad.prop";
    public static final String PATH_ANTI_VIRUS = "anti_virus_t.prop";
    public static final String PATH_FLOAT_WINDOW = CloudPropertyManagerBridge.PATH_FLOAT_WINDOW_SWITCH;
    public static final String PATH_HOME_AUTOU = CloudPropertyManagerBridge.PATH_HOME_AUTO_RAM;
    public static final String PATH_CLIPBROAD = CloudPropertyManagerBridge.PATH_CLIPBOARD_PROP;
    public static final String PATH_APPLOCK_CLOUD = "applock_prop.prop";
    public static final String PATH_SHARE = "share_path.prop";
    public static final String PATH_OUT_APP_POP_DIALOG_PROP = "out_app_pop_dialog.prop";
    public static final String PATH_GLOBAL_URL = "booster_profile.prop";
    public static final String PATH_RESULT_ADS_CONFIG = "reslut_native_ad_config.prop";
    public static final String PATH_LIST_ADS_CONFIG = "list_ads_loader.prop";
    public static final String PATH_AV_ADS_CONFIG = "av_rtp_ad_config.prop";
    public static final String PATH_ONE_TAP_BOOST_ADS_CONFIG = "one_tap_boost_ads_config.prop";
    public static final String PATH_FUNC_NOTIFICATION = "func_notification.prop";
    public static final String PATH_BIG_ADS_STYLE = "bigadsstyle.prop";
    public static final String PATH_SMART_LOCKER = "smart_locker_config.prop";
    public static final String PATH_INTERSTITIAL_ADS_PROP = "result_fullscreen_interstitial_ads_config.prop";
    public static final String PATH_NOTIFICATION_CLEAN_PROP = "notification_clean.prop";
    public static final String PATH_SHARE_PROP = "share.prop";
    public static final String PATH_NOTIFICATION_SECURITY_PROP = "notification_security.prop";
    public static final String PATH_NEW_USER_PROP = "new_user.prop";
    public static final String PATH_NOTIFICATION_GUIDE_PROP = "notification_guide.prop";
    public static final String PATH_R1_LOCKER_PROP = "r1.prop";
    public static final String PATH_OUT_APPLOCK_OP = "app_lock_start.prop";
    public static final String PATH_SHOW_RATE_POSITION_PROP = "show_rate_position.prop";
    public static final String PATH_WIFI_RTP_PROP = "wifi_rtp.prop";
    public static final String PATH_URL_PROTECTOR_PROP = "url_protector.prop";
    public static final String PATH_SAFE_BROWSER_PROP = "super_browser.prop";
    public static final String PATH_CALL_BLOCK_AD_PROP = "call_block_ads_config.prop";
    public static final String PATH_CALL_BLOCK_PROP = "call_block.prop";
    public static final String PATH_SAFE_BROADCAST_PROP = "safety_broadcast.prop";
    public static final String PATH_LIKE_US_PROP = "like_us.prop";
    public static final String PATH_APPLOCK_TIME = "applock_time.prop";
    public static final String PATH_PRIVACY_PROTECT = "privacy_protect.prop";
    public static final String PATH_OREO_GUIDE_RULE = "oreo_guide_rule.prop";
    public static final String PATH_LOCK_SCREEN_PROP = "lock_screen.prop";
    public static final String PATH_FEED_GD_PROP = "feed_gd_config.prop";
    public static final String PATH_DIS_CHARGE_PROP = "charge_helper_dialog_ad_config.prop";
    public static final String PATH_DIS_CHARGE_PROP_SWITCH = "charge_helper_dialog_config.prop";
    public static final String PATH_SUPER_AD = "super_ad.prop";
    public static final String PATH_TOP_PERMISSION_GUIDE = "top_permission_guide.prop";
    public static final String PATH_APP_CACHE_PROP = "app_cache.prop";
    public static final String PATH_ADVANCED_RUBBISH_CONTROLL = "rubbish_advanced.prop";
    public static final String PATH_RESULT_PRI = "common_result_priority.prop";
    public static final String PATH_HOME_AD_PROP = "main_ads_config.prop";
    public static final String FUNC_ADS_CONFIG_PROP = "func_ads_config.prop";
    public static final String PATH_HOME_GUIDE_PROP = "home_guide.prop";
    public static final String SPLASH_AGE_PROP = "splash_age.prop";
    public static final String PATH_INTERSTITIAL_STRATEGY = "result_back_inter_strategy_config.prop";
    public static final String PATH_NOTIFICATION_NS_NC_PROP = "notification_ns_nc.prop";
    public static final String PATH_WHATSAPP_CARD_AD = "whatsapp_card_ad.prop";
    public static final String PATH_DEEP_CLEAN_AD = "deep_clean_ad.prop";
    public static final String PATH_PREVIEW_AD_PROP = "preview_ads_config.prop";
    public static final String PATH_LIST_DETAIL_AD_PROP = "list_ad_shows.prop";
    public static final String PATH_NOTIFY_CARD_AD = "notify_card_ad.prop";
    public static final String PATH_NOTIFICATION_SCENE = "notification_scene_global.prop";
    public static final String PATH_HOME_BACK_GUIDE_PROP = "home_back_guid.prop";
    public static final String PATH_OUT_APP_JUMP_MAIN_PROP = "out_app_jump_main.prop";
    public static final String PATH_UNINSTALL_POP_DIALOG_PRO = "uninstall_pop_dialog.prop";
    public static final String PATH_HOME_GREETING = "home_greetings.prop";
    public static final String PATH_CHECK_UPDATE = "check_update.prop";
    public static final String PATH_EVERYDAY_PIC = "everyday_pic.prop";
    public static final String PATH_SHAREGUIDE_PROP = "share_login.prop";
    public static final String PATH_THANOS_CONFIG_PROP = "thanos_config.prop";
    private static CloudPropertyImpl sCloudProperty = null;

    public static CloudPropertyImpl getCloudPropertyServiceStub(Context cxt) {
        synchronized (CloudPropertyManager.class) {
            if (sCloudProperty == null) {
                sCloudProperty = new CloudPropertyImpl(cxt);
            }
        }
        return sCloudProperty;
    }


    public static float getFloat(Context cxt, String path, String key, float def) {
        CloudPropertyImpl cp = getCloudPropertyServiceStub(cxt);
        if (cp == null) {
            if (DEBUG) {
                Log.v(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + def);
            }
            return def;
        }

        float res;
        try {
            res = cp.getFloat(path, key, def);
            if (DEBUG) {
                Log.d(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + res);
            }
            return res;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
                Log.e(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + def);
            }
            return def;
        }
    }

    public static int getInt(Context cxt, String path, String key, int def) {
        CloudPropertyImpl cp = getCloudPropertyServiceStub(cxt);
        if (cp == null) {
            if (DEBUG) {
                Log.v(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + def);
            }
            return def;
        }

        int res;
        try {
            res = cp.getInt(path, key, def);
            if (DEBUG) {
                Log.d(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + res);
            }
            return res;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
                Log.e(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + def);
            }
            return def;
        }
    }

    public static String getString(Context cxt, String path, String key, String def) {
        CloudPropertyImpl cp = getCloudPropertyServiceStub(cxt);
        if (cp == null) {
            if (DEBUG) {
                Log.v(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + def);
            }
            return def;
        }

        String res;
        try {
            res = cp.getString(path, key, def);
            if (DEBUG) {
                Log.d(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + res);
            }
            return res;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
                Log.e(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + def);
            }
            return def;
        }

    }

    public static long getLong(Context cxt, String path, String key, long def) {
        CloudPropertyImpl cp = getCloudPropertyServiceStub(cxt);
        if (cp == null) {
            if (DEBUG) {
                Log.v(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + def);
            }
            return def;
        }

        long res;
        try {
            res = cp.getLong(path, key, def);
            if (DEBUG) {
                Log.d(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + res);
            }
            return res;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
                Log.e(TAG, "load cloud property file: " + path + ", key: " + key + ", value: " + def);
            }
            return def;
        }
    }

    public static void reload(Context cxt, String path) {
        CloudPropertyImpl cp = getCloudPropertyServiceStub(cxt);
        if (cp == null)
            return;
        try {
            cp.reload(path);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }

    public static boolean idDirty(Context cxt, String[] pathes) {
        CloudPropertyImpl cp = getCloudPropertyServiceStub(cxt);
        if (cp == null)
            return false;
        try {
            return cp.isDirty(pathes);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
        return false;
    }


}
