package com.baselib.sp;

import android.content.Context;
import android.util.Log;

import com.goodmorning.config.GlobalConfig;

import java.util.List;


public class SharedPref {
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "SharedPref";
    public static final String NAME = "default_shared_prefs";
    public static final String FILE_OPERATION_NAME = "file_operation_shared_prefs";
    public static final String FILE_OPERATION_INDEXED = "file_operation_indexed";
    public static final String KEY_DIALOG_USAGE_OPEN = "key_dialog_usage_open";
    public static final String KEY_DIALOG_REMINDER_OPEN = "key_dialog_remionder_open";
    public static final String KEY_SCENE_INTERVAL_TIME = "key_scene_interval_time";
    public static final String KEY_SCENE_INSTALL_INTERVAL_TIME = "key_scene_install_interval_time";
    public static final String KEY_SCENE_LAST_RUBBISH_TIME = "key_scene_last_rubbish_time";
    public static final String NAME_NOTIFICATION = "notification_shared_prefs";
    public static final String KEY_SHAREDPREF = "shared_pref";
    //首页10s广告次数
    public static final String SP_KEY_HOME_RANGE_AD_TIMES = "sp_key_home_range_ad_times";
    //首页10s广告展示时间
    public static final String SP_KEY_HOME_RANGE_AD_SHOW_TIME = "sp_key_home_range_ad_show_time";
    public static final String LOW_IMAGE_TIPS = "low_image_tips";
    public static final String DUPLICATE_IMAGE_TIPS = "dupliacte_image_tips";
    public static final String DUPLICATE_ALL_SELECTED_DIALOG_TIPS = "duplicate_all_selected_dialog_tips";

    /**
     * 内存加速时间
     */
    public static final String KEY_BOOST_TIME = "key_boost_time";
    /**
     * 是否第一次打开悬浮窗
     */
    public static final String KEY_ISFIRST_OPEN_FLOAT = "key_isfirst_open_float";

    /**
     * 是否第一次打开桌面显示
     */
    public static final String KEY_ISFIRST_OPEN_DESKTOP = "key_isfirst_open_desktop";
    /**
     * 剪切板复制时间
     */
    public static final String KEY_CLIPBOARD_COPY_TIME = "key_clipboard_copy_time";
    /**
     * 下载总流量
     */
    public static final String KEY_TOTAL_RX = "key_total_rx";

    /**
     * 上传总流量
     */
    public static final String KEY_TOTAL_TX = "key_total_tx";

    /**
     * 剪切板显示的次数
     */
    public static final String KEY_CLIPBOARD_SHOW_TIME = "key_clipboard_show_time";
    //体外信息流展示次数
    public static final String SP_KEY_OUT_STREAM_TIMES = "sp_key_out_stream_times";
    //体外信息流展示时间
    public static final String SP_KEY_OUT_STREAM_SHOW_TIME = "sp_key_out_stream_show_time";

    //体外信息流展示设置开关
    public static final String SP_KEY_OUT_STREAM_SETTING_OPEN = "sp_key_out_stream_setting_open";
    /**
     * 是否加过1-Tap Boost快捷图标
     */
    public static final String SP_KEY_EVER_CREATED_BOOST_SHORTCUT = "sp_key_ever_created_boost_shortcut";
    // 是否创建过快捷图标
    public static final String SP_KEY_EVER_CREATED_SHORTCUT = "sp_key_ever_created_shortcut";

    //是否点击过清理垃圾按钮
    public static final String SP_FIRST_SHOW_RUBBISH_PERMISSION_CLEAN = "SP_FIRST_SHOW_RUBBISH_PERMISSION_CLEAN";
    //清理垃圾结果页权限view展示时间
    public static final String SP_RUBBISH_SHOW_PERMISSION_TIME = "SP_RUBBISH_SHOW_PERMISSION_TIME";

    // 是否从抽屉进入过AppLock
    public static final String SP_KEY_IS_USER_LEARNED_APPLOCK_DRAWER = "sp_key_is_user_learned_applock_drawer";
    // 是否进入过AppLock
    public static final String SP_KEY_IS_USER_LEARNED_APPLOCK = "sp_key_is_user_learned_applock_action_or_card";

    public static final String SP_AGREEMENT = "SP_AGREEMENT";
    public static final String SP_KEY_SHOW_STORAGE_SETTING_GUIDE = "SP_KEY_SHOW_STORAGE_SETTING_GUIDE";
    /**
     * 悬浮窗打开开关key
     */
    public static final String KEY_FLOAT_WINDOW_OPEN_SWITCH = "key_float_window_open_switch";
    /**
     * 悬浮窗只显示桌面key
     */
    public static final String KEY_FLOAT_DESKTOP_SWITCH = "key_float_desktop_switch";
    public static final String SP_KEY_LAST_PATH_UPLOAD_TIME = "key_last_path_upload_time";
    public static final String SP_KEY_LAST_FILE_INDEX_TIME = "key_last_file_index_time";
    /**
     * 应用锁开启次数
     */
    public static final String KEY_APP_LOCK_START_COUNT = "key_app_lock_start_count";
    /**
     * 应用锁开启弹出时间
     */
    public static final String KEY_APP_LOCK_START_TIME = "key_app_lock_start_time";
    // 最后一次展示 Interstitial 广告
    public static final String SP_KEY_LAST_INTERSTITIAL_SHOW_TIME = "SP_KEY_LAST_INTERSTITIAL_SHOW_TIME";
    // 当天显示Interstitial广告的次数
    public static final String SP_KEY_DAILY_INTERSTITIAL_CNT = "SP_KEY_DAILY_INTERSTITIAL_CNT";

    //预览广告最后一次展示时间
    public static final String SP_KEY_LAST_PREVIEW_AD_SHOW_TIME = "SP_KEY_LAST_PREVIEW_AD_SHOW_TIME";
    //预览广告展示次数（针对新用户）
    public static final String SP_KEY_PREVIEW_AD_SHOW_COUNT = "SP_KEY_PREVIEW_AD_SHOW_COUNT";

    //首页广告展示次数（针对新用户）
    public static final String KEY_MAIN_AD_MAX_DAY = "key_mian_ad_max_day";
    //首页广告最后一次展示时间
    public static final String KEY_MAIN_AD_LAST_SHOW_TIME = "key_mian_ad_last_show_time";
    //首页广告最后一次展示时间
    public static final String KEY_MAIN_INTERAD_LAST_SHOW_TIME = "key_mian_interad_last_show_time";
    public static final String KEY_FUNCTION_INTERAD_LAST_SHOW_TIME = "key_function_interad_last_show_time";
    public static final String KEY_DETAIL_AD_MAX_DAY = "key_detail_ad_max_day";
    public static final String KEY_DETAIL_AD_LAST_SHOW_TIME = "key_detail_last_show_time";

    public static final String KEY_SHOW_RUBBISH_TIPS = "key_show_rubbish_tips";
    //是否打开充电助手
    public static final String KEY_DISCHARGE_OPEN = "key_discharge_open";
    // 最后一次上传用户行为数据的时间
    public static final String SP_KEY_BEHAVIOR_LAST_UPLOAD_TIME = "sp_key_behavior_last_upload_time";

    public static final String SP_KEY_FULL_SCAN_IS_USED = "sp_key_full_scan_is_used";
    public static final String SP_KEY_FULL_SCAN_USED_TIME = "is_key_full_scan_used_time";

    //挽留弹窗最后展示时间
    public static final String SP_HOME_BACK_GUIDE_TIME = "sp_home_back_guide_time";
    public static final String SP_HOME_BACK_GUIDE_RUBBISH_TIME = "sp_home_back_guide_rubbish_time";
    public static final String SP_HOME_BACK_GUIDE_KEY_ISRUBBISHCLEANED = "sp_home_back_guide_key_isrubbishcleaned";
    public static final String SP_HOME_BACK_GUIDE_KEY_ISCLEANANTIVIRUS = "sp_home_back_guide_key_iscleanantivirus";
    public static final String SP_HOME_BACK_GUIDE_AV_TIME = "sp_home_back_guide_av_time";


    /**
     * 统计到的手机最小温度
     */
    public static final String SP_KEY_MIN_CPU_TEMP = "sp_key_min_cpu_temp";
    /**
     * 统计到手机的最高温度
     */
    public static final String SP_KEY_MAX_CPU_TEMP = "sp_key_max_cpu_temp";
    /**
     * CPU温度取样总数
     */
    public static final String SP_KEY_CPU_TEMP_SAMPLING_COUNT = "sp_key_cpu_temp_sampling_count";
    /**
     * 用户CPU温度的单位
     */
    public static final String SP_KEY_TEMPERATURE_UNIT_PREFERENCE = "sp_key_cpu_unit_preference";
    /**
     * 温度偏高临界值
     */
    public static final String SP_KEY_CPU_TEMP_HIGH_PIVOT = "sp_key_cpu_temp_high_pivot";
    /**
     * 温度过高的临界值
     */
    public static final String SP_KEY_CPU_TEMP_OVERHEAT_PIVOT = "sp_key_cpu_temp_overheat_pivot";
    public static final String SP_KEY_BOOST_CD_INTERVAL = "sp_key_boost_cd_interval";
    /**
     * 清理模式
     **/
    public static final String SP_KEY_CLEAN_MODE = "key_clean_mode";
    public static final int CLEANMODESMART = 1;
    /**
     * 活跃的接口
     */
    public static final String SP_KEY_LAST_ACTIVE_TIME = "sp_key_last_active";

    /**
     * 用于清理次数限制
     */
    public static final String SP_KEY_LAST_BOOST_TIME = "last_boost_time";
    public static final String SP_KEY_LAST_BOOST_SUCCESS_TIME = "last_boost_success_time";
    public static final String SP_KEY_BOOST_COUNT = "boost_count";

    public static final String SP_KEY_INSTALL_BUILD = "install_build";
    public static final String SP_KEY_AD_SHOWED = "ad_shown";

    public static final String KEY_NOTIFICATION_SCENE_TIME = "key_notification_scene_time";
    public static final String SP_KEY_IS_USED_NOTIFICATION_SWITCH = "sp_key_is_used_notification_switch";// 是否使用过通知栏通知开关
    public static final String SP_KEY_IS_OPEN_NOTIFICATION = "sp_key_is_open_notification";// 开启通知栏加速通知
    //最后一次提示清理弹窗时间
    public static final String SP_KEY_CLEAR_NOTIFY_VIEW_TIME = "sp_key_clear_notify_view_last_time";
    //最后一次清理弹窗中显示的通知条目数量
    public static final String SP_KEY_CLEAR_NOTIFY_VIEW_COUNT = "sp_key_clear_notify_view_count";
    public static final String SP_KEY_ISCLEANANTIVIRUS = "is_key_clean_antivirus";
    //原生广告过期时间存储
    public static final String SP_KEY_LAST_LOAD_SUCCESS_NATIVE_TIME = "la_lo_su_n_ti";
    //插屏广告过期时间存储
    public static final String SP_KEY_LAST_LOAD_SUCCESS_INTERSTITIAL_TIME = "la_lo_su_in_ti";
    //Banner广告过期时间存储
    public static final String SP_KEY_LAST_LOAD_SUCCESS_BANNER_TIME = "la_lo_su_bn_ti";
    //是否是第一次扫描
    public static final String SP_KEY_FIRST_SCAN_MAIN = "sp_key_first_scan_main";
    //用户清理垃圾总计
    public static final String SP_KEY_MY_CLEAN_TOTAL = "sp_key_my_clean_total";
    //用户清理天数总计
    public static final String SP_KEY_MY_DAY_TOTAL = "sp_key_my_day_total";

    public static final String SP_KEY_MAIN_UI_TIME = "sp_key_main_ui_time";
    public static final String SP_KEY_MAIN_IS_TODAY = "sp_key_main_is_today";

    /**
     * 内购弹窗显示时间
     */
    public static final String KEY_VIP_SHOW_DIALOG_TIME = "key_vip_show_dialog_time";

    /**
     * 内购否免费用过月订阅
     */
    public static final String KEY_VIP_IS_FREE_USED = "key_vip_is_free_used";

    /**
     * 广告开关
     */
    public static final String KEY_VIP_AD_SWITCH = "key_vip_ad_switch";
    /**
     * 语言切换
     */
    public static final String LANGUAGE = "language";

    /**
     * 是否首次启动
     */
    public static final String ISFIRSTSTART = "isfirststart";
    /**
     * 语言类型
     */
    public static final String LANGUAGE_TYPE = "language_type";
    /**
     * 频道
     */
    public static final String CHANNEL_CONTENT = "channel_content";
    /**
     * 切换主题
     */
    public static final String IS_SWITCH_THEME = "isSwitchTheme";
//    static SharedPrefStub getInstance(Context cxt) {
//        return SharedPrefStub.getInstance(cxt);
////        IBinder binder = BinderManager.getService(cxt, KEY_SHAREDPREF);
////        if (binder == null)
////            return null;
////
////        return ISharedPrefX.Stub.asInterface(binder);
//    }

    public static void setString(Context context, String key, String value) {
        setString(context, key, NAME, value);
    }

    public static String getString(Context context, String key, String defValue) {
        return getString(context, key, NAME, defValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        setBoolean(context, key, NAME, value);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getBoolean(context, key, NAME, defValue);
    }

    public static void setInt(Context context, String key, int value) {
        setInt(context, key, NAME, value);
    }

    public static int getInt(Context context, String key, int defValue) {
        return getInt(context, key, NAME, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        return getLong(context, key, NAME, defValue);
    }

    public static void setLong(Context context, String key, long value) {
        setLong(context, key, NAME, value);
    }

    public static void clear(Context context, String name) {
        try {
            org.homeplanet.sharedpref.SharedPref.clear(context, name);
//            getInstance(context).clear(name);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }

    public static void clearKey(Context context, String name, String key) {
        try {
            org.homeplanet.sharedpref.SharedPref.remove(context, name, key);
//            getInstance(context).clearKey(name, key);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }


    /**
     * 如果需要制定名字就用这个即可
     */
    public static void setString(Context context, String key, String name, String value) {
        try {
            org.homeplanet.sharedpref.SharedPref.setStringVal(context, name, key, value);
//            getInstance(context).putString(name, key, value);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }

    public static String getString(Context context, String key, String name, String defValue) {
        try {
//            return getInstance(context).getString(name, key, defValue);
            return org.homeplanet.sharedpref.SharedPref.getString(context, name, key, defValue);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
            return defValue;
        }
    }

    public static void setBoolean(Context context, String key, String name, boolean value) {
        try {
            org.homeplanet.sharedpref.SharedPref.setBooleanVal(context, name, key, value);
//            getInstance(context).putBoolean(name, key, value);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }

    public static boolean getBoolean(Context context, String key, String name, boolean defValue) {
        try {
//            return getInstance(context).getBoolean(name, key, defValue);
            return org.homeplanet.sharedpref.SharedPref.getBoolean(context, name, key, defValue);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
            return defValue;
        }
    }

    public static void setInt(Context context, String key, String name, int value) {
        try {
            org.homeplanet.sharedpref.SharedPref.setIntVal(context, name, key, value);
//            getInstance(context).putInteger(name, key, value);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }

    public static int getInt(Context context, String key, String name, int defValue) {
        try {
//            return getInstance(context).getInteger(name, key, defValue);
            return org.homeplanet.sharedpref.SharedPref.getInt(context, name, key, defValue);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
            return defValue;
        }
    }

    public static long getLong(Context context, String key, String name, long defValue) {
        try {
//            return getInstance(context).getLong(name, key, defValue);
            return org.homeplanet.sharedpref.SharedPref.getLong(context, name, key, defValue);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
            return defValue;
        }
    }

    public static void setLong(Context context, String key, String name, long value) {
        try {
            org.homeplanet.sharedpref.SharedPref.setLongVal(context, name, key, value);
//            getInstance(context).putLong(name, key, value);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }

    public static List<String> getStrings(Context context, String key, String name, List<String> def) {
        try {
//            return getInstance(context).getStrings(name, key, def);
            return org.homeplanet.sharedpref.SharedPref.getStringList(context, name, key);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
            return def;
        }
    }

}
