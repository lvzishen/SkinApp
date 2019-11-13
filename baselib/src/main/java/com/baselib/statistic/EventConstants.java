package com.baselib.statistic;

/**
 * Created by liyaguang on 2017/11/14.
 */

public class EventConstants {

    public static final int JUNK_ANALYZE = 67242869;

    public static final String JUNK_ANALYZE_TARGET_STRING = "target_s";
    /**  */
    public static final String JUNK_ANALYZE_STATUS_STRING = "status_s";
    /**  */
    public static final String JUNK_ANALYZE_TAKE_INT = "take_l";
    /**  */
    public static final String JUNK_ANALYZE_CACHE_STRING = "cache_s";
    /**  */
    public static final String JUNK_ANALYZE_PHOTO_STRING = "photo_s";
    /**  */
    public static final String JUNK_ANALYZE_VOICE_STRING = "voice_s";
    /**  */
    public static final String JUNK_ANALYZE_AUDIO_STRING = "audio_s";
    /**  */
    public static final String JUNK_ANALYZE_VIDEO_STRING = "video_s";
    /**  */
    public static final String JUNK_ANALYZE_DOC_STRING = "doc_s";

    public static final int MEMORY_BOOST = 67242357;
    /**  */
    public static final String MEMORY_BOOST_STATUS_STRING = "status_s";
    /**  */
    public static final String MEMORY_BOOST_HIBERNATE_STRING = "hibernate_s";
    /**  */
    public static final String MEMORY_BOOST_TAKE_INT = "take_l";
    /**  */
    public static final String MEMORY_BOOST_VALID_STRING = "valid_s";
    /**  */
    public static final String MEMORY_BOOST_TRIGGER_STRING = "trigger_s";

    public static final int CPU_COOLER = 67241589;
    /**  */
    public static final String CPU_COOLER_FROM_STATE_STRING = "from_state_s";
    /**  */
    public static final String CPU_COOLER_TO_STATE_STRING = "to_state_s";
    /**  */
    public static final String CPU_COOLER_DROPPED_STRING = "dropped_s";
    /**  */
    public static final String CPU_COOLER_TRIGGER_STRING = "trigger_s";

    public static final int JUNK_CLEAN = 67242101;

    public static final String JUNK_CLEAN_STATUS_STRING = "status_s";
    /**  */
    public static final String JUNK_CLEAN_TAKE_INT = "take_l";
    /**  */
    public static final String JUNK_CLEAN_VALID_STRING = "valid_s";
    /**  */
    public static final String JUNK_CLEAN_CACHE_TYPE_STRING = "cache_type_s";
    /**  */
    public static final String JUNK_CLEAN_CACHE_SIZE_INT = "cache_size_l";

    /**  */
    public static final int BATTERY_SAVER = 67303029;

    public static final String BATTERY_SAVER_STATUS_STRING = "status_s";
    /**  */
    public static final String BATTERY_SAVER_APP_STANDBY_STRING = "app_standby_s";
    /**  */
    public static final String BATTERY_SAVER_HIBERNATE_STRING = "hibernate_s";
    /**  */
    public static final String BATTERY_SAVER_TAKE_INT = "take_l";
    /**  */
    public static final String BATTERY_SAVER_VALID_STRING = "valid_s";
    /**  */
    public static final String BATTERY_SAVER_TRIGGER_STRING = "trigger_s";

    /**  */
    public static final int ANTIVIRUS_SCAN = 67303285;
    /**  */
    public static final String ANTIVIRUS_SCAN_TARGET_STRING = "target_s";
    /**  */
    public static final String ANTIVIRUS_SCAN_STATUS_STRING = "status_s";
    /**  */
    public static final String ANTIVIRUS_SCAN_TAKE_INT = "take_l";
    /**  */
    public static final String ANTIVIRUS_SCAN_VALID_STRING = "valid_s";
    /**  */
    public static final String ANTIVIRUS_SCAN_RESULT_INFO_STRING = "result_info_s";

    /**  */
    public static final int ANTIVIRUS_HANDLE = 67302517;
    /**  */
    public static final String ANTIVIRUS_HANDLE_FROM_STATE_STRING = "from_state_s";
    /**  */
    public static final String ANTIVIRUS_HANDLE_TO_STATE_STRING = "to_state_s";
    /**  */
    public static final String ANTIVIRUS_HANDLE_TRIGGER_STRING = "trigger_s";
    /**  */
    public static final String ANTIVIRUS_HANDLE_VIRUS_NUMBER_STRING = "virus_number_s";



    public static final int XCLN_BATTERY_CHARGE = 84042357;
    /** 充电结束时候的电量 */
    public static final String XCLN_BATTERY_CHARGE_BATTERY_LEVEL_INT = "battery_level_l";
    /** 充入的电量 */
    public static final String XCLN_BATTERY_CHARGE_CHARGED_LEVEL_INT = "charged_level_l";
    /** 充电时长 */
    public static final String XCLN_BATTERY_CHARGE_CHARGING_DURATION_INT = "charging_duration_l";
    /** 充电器类型 */
    public static final String XCLN_BATTERY_CHARGE_CHARGER_TYPE_INT = "charger_type_l";



    /** 用于记录界面展示结束时，一些结果信息 */
    public static final int XCLN_SHOW_RESULT = 84042101;
    /** 展示名称 */
    public static final String XCLN_SHOW_RESULT_NAME_STRING = "name_s";
    /** 展示容器 */
    public static final String XCLN_SHOW_RESULT_CONTAINER_STRING = "container_s";
    /** 展示来源 */
    public static final String XCLN_SHOW_RESULT_FROM_SOURCE_STRING = "from_source_s";
    /** 展示结果中第一步的结果 */
    public static final String XCLN_SHOW_RESULT_STEP1_INT = "step1_l";
    /** 展示结果中第二步的结果 */
    public static final String XCLN_SHOW_RESULT_STEP2_INT = "step2_l";
    /** 展示结果中第三步的结果 */
    public static final String XCLN_SHOW_RESULT_STEP3_INT = "step3_l";
    /** 展示结果中第四步的结果 */
    public static final String XCLN_SHOW_RESULT_STEP4_INT = "step4_l";
}
