package com.baselib.ui;

/**
 * Created by Eric Tjitra on 6/16/2017.
 */

public class CommonConstants {


    public static final String USER_AGREEMENT_URL = "http://www.luckmorning.com/policy/user_privacy.html";
    public static final String PRIVACY_POLICY_URL = "http://www.luckmorning.com/policy/privacy.html";

    public static final String KEY_HAS_AGREEMENT_SPLASH = "key_has_agreement_splash";
    public static final String KEY_HAS_SHOWED_PRESS_AGREEMENT = "key_has_showed_press_agreement";

    public static final String EXTRA_NAVIGATOR_FROM = "extra_from_activity_stack_navigator";
    //表示是是否需要展示广告
    public static final String EXTRA_HAS_NAVIGATOR_ADS = "extra_has_navigator_ads";
    //表示是否能在主界面上展示引导
    public static final String EXTRA_CAN_SHOW_GUIDE_ON_HOME = "extra_can_show_guide_on_home";

    public static final int LAST_ACTION_APP_LOCK = 999;

    public static final int TYPE_NATIVE_CARD = 101;
    public static final int TYPE_NATIVE_FULL = 102;

    public static final int TYPE_INTERSTITIAL = 201;
    public static final int TYPE_NATIVE = 202;
    //省电插屏
    public static final int KEY_INTERSTITIAL_SAVE_POWER = 503;
    // Types of result page (only for Ads purpose)
    public static final int TYPE_RESULT_BOOST = 301;
    public static final int TYPE_RESULT_CPU = 302;
    public static final int TYPE_RESULT_SAVE_POWER_BANNER = 326;
    public static final int TYPE_HOME_AD = 350;
    public static final int TYPE_FUNCTION_AD = 360;
    public static final int TYPE_RESULT_JUNK = 303;
    public static final int TYPE_RESULT_BATTERY = 304;
    public static final int TYPE_RESULT_VIRUS = 305;
    public static final int TYPE_RESULT_SPECIAL_CLEAN = 306;
    public static final int TYPE_RESULT_NOTIFICATION_BOOST = 307;
    public static final int TYPE_RESULT_NOTIFICATION_CLEAN = 308;
    public static final int TYPE_RESULT_NOTIFICATION_SECURITY = 309;
    public static final int TYPE_RESULT_WIFI_SCAN_NROMAL = 310;
    public static final int TYPE_RESULT_WIFI_SCAN_ABNROMAL = 311;
    public static final int TYPE_RESULT_JUNK_BANNER = 320;
    public static final int TYPE_RESULT_NOTI_BANNER = 321;
    public static final int TYPE_RESULT_ANIT_BANNER = 322;
    public static final int TYPE_RESULT_CPU_BANNER = 323;
    public static final int TYPE_RESULT_BOOST_BANNER = 324;
    public static final int TYPE_RESULT_APP_LOCK = 325;
    public static final int TYPE_APPUNINSTALL_AD = 361;
    public static final int TYPE_FLOATWINDOW_NETWORK_SPEED = 373;
    //展示权限页
    public static final int TYPE_RUBBISH_PERMISSION_RESULT = 312;
    //当特定类型的广告没有加载到时，用这个广告进行填充，同时也用作首页广告
    public static final int TYPE_RESULT_SUPPLEMENT = 312;
    public static final int TYPE_RESULT_HANGEDCALL = 313;
    public static final int TYPE_RESULT_DETACHCHARGER = 314;
    public static final int TYPE_FULL_SCAN = 315;
    //插屏广告和全屏广告
    public static final int ADCATEGORY_INTERSTITIAL_AND_BIGADS = 500;
    public static final int ADCATEGORY_NATIVE_ADS = 501;
    //首页10s插屏
    public static final int HOME_INTERSTITIAL_RANGE_BIGADS = 502;
    //体外充电插屏
    public static final int OUT_POWER_CONNECT_INTERSTITIAL_OUT_BIGADS = 510;
    public static final String KEY_EXPECTED_ADS_CATEGORY = "key_ads_category";
    public static final String KEY_EXPECTED_ADS_TYPE = "key_ads_type";
    public static final String KEY_EXPECTED_ADS_COUNT = "key_ads_count";
    //体外信息流插屏
    public static final int OUT_STREAM_INTERSTITIAL_RANGE_OUT_BIGADS = 505;
    // Notifications
    public static final String NOTIFICATION_EXTRA = "NOTIFICATION_EXTRA";
    public static final int SOURCE_NOTIFICATION = 1;

    public static final int SOURCE_NOTIFICATION_PLAN_DES_APPCNT = 2;
    public static final int SOURCE_NOTIFICATION_PLAN_MEM_HIGH = 3;

    // 进入主界面的动画
    public static final String EXTRA_TYPE_ENTER_ANIM = "extra_type_enter_anim";
    public static final int TYPE_ENTER_ANIM_NONE = 0;
    public static final int TYPE_ENTER_ANIM_LEFT = 1;
    public static final int TYPE_ENTER_ANIM_RIGHT = 2;
    //体外拔电插屏
    public static final int OUT_POWER_DISCHARGE_INTERSTITIAL_OUT_BIGADS = 507;
    //垃圾清理相關
    public static final String EXTRA_KEY_IS_DEEPCLEAN = "key_extra_is_deep_clean";
    public static final String INTENT_EXTRA_DEEP_CLEAN_NOT_NEED_PERMISSION = "key_extra_deep_clean_not_need_permission";

    //结果页规则相关

    public static final String KEY_RESULT_LAST_USE_TIME = "key_result_last_use_time_";
    public static final String KEY_LAST_COMMON_RESULT_RESET_TIME = "key_last_common_result_reset_time";
    public static final String KEY_LAST_COMMON_RESULT_SAFE_INDEX = "key_last_common_result_safe_index";


    public static final String KEY_HAS_SHOW_RESULT_LIST_CARD = "key_has_show_result_list_card";
    public static final String KEY_SHOULD_SHOW_RESULT_LIST_CARD = "key_should_show_result_list_card";
    public static final String KEY_RESULT_CARD_LAST_SHOW_WHATSAPP = "key_result_card_last_show_whatsapp";
    public static final String KEY_RESULT_CARD_LAST_SHOW_FACEBOOK = "key_result_card_last_show_facebook";

    public static final String KEY_SCENE_MEM_SPEED = "key_scene_mem_speed";
    public static final String KEY_SCENE_RESIDUAL = "key_scene_residual";
    public static final String KEY_SCENE_SAVEPOWER = "key_scene_savepower";
    public static final String KEY_SCENE_WIFI = "key_scene_wifi";
    public static final String KEY_SCENE_REGULAR_CLEAN = "key_scene_regular_clean";
    public static final String KEY_SCENE_CLIPBOARD = "key_scene_clipboard";
}
