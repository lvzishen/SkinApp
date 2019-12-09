package com.baselib.statistic;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.goodmorning.config.GlobalConfig;

import org.alex.analytics.AlexEventsConstant;

import static com.baselib.statistic.EventConstants.ANTIVIRUS_HANDLE;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_HANDLE_FROM_STATE_STRING;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_HANDLE_TO_STATE_STRING;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_HANDLE_TRIGGER_STRING;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_HANDLE_VIRUS_NUMBER_STRING;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_SCAN;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_SCAN_RESULT_INFO_STRING;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_SCAN_STATUS_STRING;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_SCAN_TAKE_INT;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_SCAN_TARGET_STRING;
import static com.baselib.statistic.EventConstants.ANTIVIRUS_SCAN_VALID_STRING;
import static com.baselib.statistic.EventConstants.BATTERY_SAVER;
import static com.baselib.statistic.EventConstants.BATTERY_SAVER_APP_STANDBY_STRING;
import static com.baselib.statistic.EventConstants.BATTERY_SAVER_HIBERNATE_STRING;
import static com.baselib.statistic.EventConstants.BATTERY_SAVER_STATUS_STRING;
import static com.baselib.statistic.EventConstants.BATTERY_SAVER_TAKE_INT;
import static com.baselib.statistic.EventConstants.BATTERY_SAVER_TRIGGER_STRING;
import static com.baselib.statistic.EventConstants.BATTERY_SAVER_VALID_STRING;
import static com.baselib.statistic.EventConstants.CPU_COOLER;
import static com.baselib.statistic.EventConstants.CPU_COOLER_DROPPED_STRING;
import static com.baselib.statistic.EventConstants.CPU_COOLER_FROM_STATE_STRING;
import static com.baselib.statistic.EventConstants.CPU_COOLER_TO_STATE_STRING;
import static com.baselib.statistic.EventConstants.CPU_COOLER_TRIGGER_STRING;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE_AUDIO_STRING;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE_CACHE_STRING;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE_DOC_STRING;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE_PHOTO_STRING;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE_STATUS_STRING;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE_TAKE_INT;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE_TARGET_STRING;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE_VIDEO_STRING;
import static com.baselib.statistic.EventConstants.JUNK_ANALYZE_VOICE_STRING;
import static com.baselib.statistic.EventConstants.JUNK_CLEAN;
import static com.baselib.statistic.EventConstants.JUNK_CLEAN_CACHE_SIZE_INT;
import static com.baselib.statistic.EventConstants.JUNK_CLEAN_CACHE_TYPE_STRING;
import static com.baselib.statistic.EventConstants.JUNK_CLEAN_STATUS_STRING;
import static com.baselib.statistic.EventConstants.JUNK_CLEAN_TAKE_INT;
import static com.baselib.statistic.EventConstants.JUNK_CLEAN_VALID_STRING;
import static com.baselib.statistic.EventConstants.MEMORY_BOOST;
import static com.baselib.statistic.EventConstants.MEMORY_BOOST_HIBERNATE_STRING;
import static com.baselib.statistic.EventConstants.MEMORY_BOOST_STATUS_STRING;
import static com.baselib.statistic.EventConstants.MEMORY_BOOST_TAKE_INT;
import static com.baselib.statistic.EventConstants.MEMORY_BOOST_TRIGGER_STRING;
import static com.baselib.statistic.EventConstants.MEMORY_BOOST_VALID_STRING;
import static com.baselib.statistic.EventConstants.XCLN_BATTERY_CHARGE;
import static com.baselib.statistic.EventConstants.XCLN_BATTERY_CHARGE_BATTERY_LEVEL_INT;
import static com.baselib.statistic.EventConstants.XCLN_BATTERY_CHARGE_CHARGED_LEVEL_INT;
import static com.baselib.statistic.EventConstants.XCLN_BATTERY_CHARGE_CHARGER_TYPE_INT;
import static com.baselib.statistic.EventConstants.XCLN_BATTERY_CHARGE_CHARGING_DURATION_INT;
import static com.baselib.statistic.EventConstants.XCLN_SHOW_RESULT;
import static com.baselib.statistic.EventConstants.XCLN_SHOW_RESULT_CONTAINER_STRING;
import static com.baselib.statistic.EventConstants.XCLN_SHOW_RESULT_FROM_SOURCE_STRING;
import static com.baselib.statistic.EventConstants.XCLN_SHOW_RESULT_NAME_STRING;
import static com.baselib.statistic.EventConstants.XCLN_SHOW_RESULT_STEP1_INT;
import static com.baselib.statistic.EventConstants.XCLN_SHOW_RESULT_STEP2_INT;


/**
 * Created by tangchun on 2017/4/10.
 */

public class StatisticLoggerX {
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "StatisticLoggerX";

    public static final String STATUS_COMPLETE = "complete";
    public static final String STATUS_CANCEL = "cancel";


    public static final int POSITION_1 = 1;
    public static final int POSITION_2 = 2;
    public static final int POSITION_3 = 3;
    public static final int POSITION_4 = 4;
    public static final int POSITION_5 = 5;
    public static final int POSITION_6 = 6;
    public static final int POSITION_7 = 7;
    public static final int POSITION_8 = 8;
    public static final int POSITION_9 = 9;

    public static final String NAME_BIRD = "bird";
    public static final String NAME_BUBBLE = "bubble";

    public static final String RESULT_CODE_CANCEL = "cancel";
    public static final String RESULT_CODE_MISS = "miss";
    public static final String RESULT_CODE_HIT = "hit";

    /**
     * 悬浮窗点击事件打点
     *
     * @param category
     * @param container
     * @param name
     * @param fromSource
     */
    public static void logFloatClick(String category, String container, String name, String fromSource) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CATEGORY_STRING, category);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, fromSource);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
    }

    public static void logHomePullShow(String name) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, StatisticConstants.HOME_PULL_STATUS);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.v(TAG, "[show] logIntegralShow");
        }
    }

    /**
     * 悬浮窗显示打点
     *
     * @param category
     * @param container
     * @param name
     * @param fromSource
     */
    public static void logFloatShow(String category, String container, String name, String fromSource) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CONTENT_SHOW_CATEGORY_ID_STRING, category);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
    }

    /**
     * 显示类型打点
     *
     * @param category
     * @param container
     * @param name
     * @param fromSource
     */
    public static void logShowDot(String category, String container, String name, String fromSource) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CONTENT_SHOW_CATEGORY_ID_STRING, category);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
    }

    /**
     * 点击类型打点
     *
     * @param category
     * @param container
     * @param name
     * @param fromSource
     */
    public static void logClickDot(String category, String container, String name, String fromSource) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CATEGORY_STRING, category);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, fromSource);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
    }

    public static void logJunkAnalyze(Context cxt, String target, String status, long take, boolean hasCache, boolean hasPhoto, boolean hasVoice, boolean hasAudio, boolean hasVedio, boolean hasDoc) {
        Bundle bundle = new Bundle();
        bundle.putString(JUNK_ANALYZE_TARGET_STRING, target);
        bundle.putString(JUNK_ANALYZE_STATUS_STRING, status);
        bundle.putLong(JUNK_ANALYZE_TAKE_INT, take);
        bundle.putString(JUNK_ANALYZE_CACHE_STRING, hasCache ? "1" : "0");
        bundle.putString(JUNK_ANALYZE_PHOTO_STRING, hasPhoto ? "1" : "0");
        bundle.putString(JUNK_ANALYZE_VOICE_STRING, hasVoice ? "1" : "0");
        bundle.putString(JUNK_ANALYZE_AUDIO_STRING, hasAudio ? "1" : "0");
        bundle.putString(JUNK_ANALYZE_VIDEO_STRING, hasVedio ? "1" : "0");
        bundle.putString(JUNK_ANALYZE_DOC_STRING, hasDoc ? "1" : "0");
        StatisticLogger.log(cxt, JUNK_ANALYZE, bundle);
    }


    public static void logJunkClean(Context cxt, String status, long take, boolean valid, String cacheType, long cacheSize) {
        Bundle bundle = new Bundle();
        bundle.putString(JUNK_CLEAN_STATUS_STRING, status);
        bundle.putLong(JUNK_CLEAN_TAKE_INT, take);
        bundle.putString(JUNK_CLEAN_VALID_STRING, valid ? "1" : "0");
        bundle.putString(JUNK_CLEAN_CACHE_TYPE_STRING, cacheType);
        bundle.putLong(JUNK_CLEAN_CACHE_SIZE_INT, cacheSize);
        StatisticLogger.log(cxt, JUNK_CLEAN, bundle);
    }

    public static void logMemoryBoost(Context cxt, String status, boolean hibernate, long take, boolean valid, String trigger) {
        Bundle bundle = new Bundle();
        bundle.putString(MEMORY_BOOST_STATUS_STRING, status);
        bundle.putString(MEMORY_BOOST_HIBERNATE_STRING, hibernate ? "1" : "0");
        bundle.putLong(MEMORY_BOOST_TAKE_INT, take);
        bundle.putString(MEMORY_BOOST_VALID_STRING, valid ? "1" : "0");
        bundle.putString(MEMORY_BOOST_TRIGGER_STRING, trigger);
        StatisticLogger.log(cxt, MEMORY_BOOST, bundle);
    }

    public static void logCpuCooler(Context cxt, String fromState, String toState, String dropped, String trigger) {
        Bundle bundle = new Bundle();
        bundle.putString(CPU_COOLER_FROM_STATE_STRING, fromState);
        bundle.putString(CPU_COOLER_TO_STATE_STRING, toState);
        bundle.putString(CPU_COOLER_DROPPED_STRING, dropped);
        bundle.putString(CPU_COOLER_TRIGGER_STRING, trigger);
        StatisticLogger.log(cxt, CPU_COOLER, bundle);
    }

    public static void logTimeTrackStart(String id) {
        StatisticLogger.logTimeTrackStart(id);
    }

    public static void logTimeTrackEnd(String id, int event, Bundle params) {
        StatisticLogger.logTimeTrackEnd(id, event, params);
    }

    public static void logBatterySaver(Context cxt, String status, boolean isAppstandby, boolean isHibernate, long take, boolean valid, String trigger) {
        Bundle bundle = new Bundle();
        bundle.putString(BATTERY_SAVER_STATUS_STRING, status);
        bundle.putString(BATTERY_SAVER_APP_STANDBY_STRING, isAppstandby ? "1" : "0");
        bundle.putString(BATTERY_SAVER_HIBERNATE_STRING, isHibernate ? "1" : "0");
        bundle.putLong(BATTERY_SAVER_TAKE_INT, take);
        bundle.putString(BATTERY_SAVER_VALID_STRING, valid ? "1" : "0");
        bundle.putString(BATTERY_SAVER_TRIGGER_STRING, trigger);
        StatisticLogger.log(cxt, BATTERY_SAVER, bundle);
    }

    public static void logAntivirusScan(Context cxt, String target, String status, long take, boolean valid, String result_info) {
        Bundle bundle = new Bundle();
        bundle.putString(ANTIVIRUS_SCAN_TARGET_STRING, target);
        bundle.putString(ANTIVIRUS_SCAN_STATUS_STRING, status);
        bundle.putLong(ANTIVIRUS_SCAN_TAKE_INT, take);
        bundle.putString(ANTIVIRUS_SCAN_VALID_STRING, valid ? "1" : "0");
        bundle.putString(ANTIVIRUS_SCAN_RESULT_INFO_STRING, result_info);
        StatisticLogger.log(cxt, ANTIVIRUS_SCAN, bundle);
    }

    public static void logAntivirusHandle(Context cxt, String from_state, String to_state, String trigger, int virus_number) {
        Bundle bundle = new Bundle();
        bundle.putString(ANTIVIRUS_HANDLE_FROM_STATE_STRING, from_state);
        bundle.putString(ANTIVIRUS_HANDLE_TO_STATE_STRING, to_state);
        bundle.putString(ANTIVIRUS_HANDLE_TRIGGER_STRING, trigger);
        bundle.putString(ANTIVIRUS_HANDLE_VIRUS_NUMBER_STRING, String.valueOf(virus_number));
        StatisticLogger.log(cxt, ANTIVIRUS_HANDLE, bundle);
    }

    public static void setChecked(String name, String fromSource, boolean checked) {
        Bundle bundle = new Bundle();
        bundle.putString(AlexEventsConstant.XALEX_CHECK_NAME_STRING, name);
        bundle.putString(AlexEventsConstant.XALEX_CHECK_FROM_SOURCE_STRING, fromSource);
        bundle.putBoolean(AlexEventsConstant.XALEX_CHECK_CHECKED_BOOL, checked);
        if (DEBUG) {
            Log.v(TAG, fromSource + "->" + name + "->" + checked);
        }

        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CHECK, bundle);
        //FacebookLogger.logEvent(StaticStorage.sContext,"XALEX_CHECK",bundle);
    }

    public static void logEvent(int eventID, Bundle bundle) {
        StatisticLogger.getLogger().logEvent(eventID, bundle);
    }

    public static void setMove(Context cxt, String name, int fromPos, int toPos, String resultCode) {
        Bundle bundle = new Bundle();
        bundle.putString(AlexEventsConstant.XALEX_MOVE_NAME_STRING, name);
        if (fromPos > 0)
            bundle.putString(AlexEventsConstant.XALEX_MOVE_FROM_POSITION_STRING, String.valueOf(fromPos));
        if (toPos > 0)
            bundle.putString(AlexEventsConstant.XALEX_MOVE_TO_POSITION_STRING, String.valueOf(toPos));
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_MOVE_RESULT_CODE_STRING, resultCode);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_MOVE, bundle);
    }

    public static void logClick(Context cxt, String name, String container, String from_source, String to_destination, String url, int position, String text, String result_code, String flag) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TO_DESTINATION_STRING, to_destination);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_URL_STRING, url);
        if (position > 0) {
            bundle.putString(AlexEventsConstant.XALEX_CLICK_POSITION_STRING, position + "");
        }
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, text);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_RESULT_CODE_STRING, result_code);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FLAG_STRING, flag);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " => result = " + result_code);
        }
    }

    public static void logClick(String from_source, String name, String container) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container);
        }
    }

    /**
     * @param from_source
     * @param name
     * @param container
     * @param to_destination
     */
    public static void logClick(String from_source, String name, String container, String to_destination) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TO_DESTINATION_STRING, to_destination);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " to " + to_destination);
        }
    }

    public static void logClickType(String name, String container, String from_source, String type) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TYPE_STRING, type);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " to " + type);
        }
    }

    public static void logClick(String name, String container, String from_source, String type, int positionX, int positionY) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TYPE_STRING, type);
        bundle.putInt(AlexEventsConstant.XALEX_CLICK_POSITION_X_INT, positionX);
        bundle.putInt(AlexEventsConstant.XALEX_CLICK_POSITION_Y_INT, positionY);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " to " + type);
        }
    }

    /**
     * @param from_source
     * @param name
     * @param container
     * @param type
     */
    public static void logClickType1(String name, String container, String from_source, String type) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TYPE_STRING, type);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " to " + type);
        }
    }

    public static void logClickType2(String name, String container, String from_source, String type, String text) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TYPE_STRING, type);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, text);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " to " + type);
        }
    }

    public static void logClickType(String from_source, String name, String container, String flag, String style) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FLAG_STRING, flag);
        if (!TextUtils.isEmpty(style)) {
            addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_STYLE_STRING, style);
        }
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " to " + flag + " style " + style);
        }
    }

    public static void logShowType(String from_source, String name, String container, String flag, String style) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FLAG_STRING, flag);
        if (!TextUtils.isEmpty(style)) {
            addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_STYLE_STRING, style);
        }
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " to " + flag + " style " + style);
        }
    }

    public static void logClickWithTextValue2(String name, String container, String from_source, String textValue) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, textValue);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " value is " + textValue);
        }
    }

    public static void logClickWithTextValue(String from_source, String name, String container, String textValue) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, textValue);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " value is " + textValue);
        }
    }

    public static void logClickWithUrl(String from_source, String name, String container, String url) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_URL_STRING, url);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " URL is " + url);
        }
    }

    public static void logClickWithTextValueAndType(String from_source, String name, String container, String textValue, String type) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, textValue);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TYPE_STRING, type);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " value is " + textValue);
        }
    }


    public static void logClickWithTextValueAndTypeAndFlag(String from_source, String name, String container, String textValue, String type, String flag) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, textValue);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TYPE_STRING, type);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FLAG_STRING, flag);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " value is " + textValue);
        }
    }

    public static void logClickWithTextValueAndDestination(String from_source, String name, String container, String textValue, String type, String flag, String url, String style) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, textValue);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TYPE_STRING, type);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FLAG_STRING, flag);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_URL_STRING, url);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_STYLE_STRING, style);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click]" + from_source + "->" + name + " in " + container + " value is " + textValue);
        }
    }

    public static void logClickSimple(Context cxt, String name, String from_source, String result_code) {
        logClick(cxt, name, null, from_source, null, null, -1, null, result_code, null);
    }


    public static void logClickStyle(String name, String container, String fromSource, String style) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_STYLE_STRING, style);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.d(TAG, "logClick | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + fromSource);
        }
    }

    public static void logVipClick() {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, "VIP");
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, "Homepage");
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, "HomeActivty");
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CATEGORY_STRING, "window");
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click] logVipClick");
        }
    }

    public static void logVipClick(String name) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, StatisticConstants.VIP_CONTAINER);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CATEGORY_STRING, StatisticConstants.VIP_CATEGORY);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click] logVipClick");
        }
    }

    public static void logHomeVipClick(String from, String name) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, from);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.v(TAG, "[click] logHomeVipClick");
        }
    }


    public static void logVipShow() {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, "VIP");
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, "Homepage");
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, "HomeActivty");
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CONTENT_SHOW_CATEGORY_ID_STRING, "window");
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.v(TAG, "[show] logVipShow");
        }
    }

    public static void logVipShow(String name) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, StatisticConstants.VIP_CONTAINER);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CONTENT_SHOW_CATEGORY_ID_STRING, StatisticConstants.VIP_CATEGORY);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.v(TAG, "[show] logVipShow");
        }
    }

    private static void addIfNotEmpty(Bundle bundle, String key, String val) {
        if (!TextUtils.isEmpty(key))
            bundle.putString(key, val);
    }


    public static void logListEdit(Context cxt,
                                   String list_type,
                                   String action,
                                   String element,
                                   String from_source) {

        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_LIST_EDIT_LIST_TYPE_STRING, list_type);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_LIST_EDIT_ACTION_STRING, action);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_LIST_EDIT_ELEMENT_STRING, element);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_LIST_EDIT_FROM_SOURCE_STRING, from_source);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_LIST_EDIT, bundle);
        if (DEBUG) {
            Log.v(TAG, "[list_edit]" + from_source + "->" + list_type + " -> " + action + "->" + element);
        }
    }

    public static void logShow(String name, String container, String fromSource) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + fromSource);
        }
    }

    public static void logShow(String name, String container, String fromSource, String type) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_TYPE_STRING, type);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + fromSource);
        }
    }


    public static void logShowTypeAndStyle(String name, String container, String fromSource, String type, String style) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_TYPE_STRING, type);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_STYLE_STRING, style);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + fromSource);
        }
    }

    public static void logShowStyle(String name, String container, String fromSource, String style) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_STYLE_STRING, style);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + fromSource);
        }
    }


    public static void logShowDuration(String name, String container, String fromSource, long dur, String style) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_STYLE_STRING, style);
        bundle.putLong(AlexEventsConstant.XALEX_SHOW_DURATION_INT, dur);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + fromSource);
        }
    }

    public static void logShowD(String name, String container, String fromSource, String type, int dur, int inter) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_TYPE_STRING, type);
        bundle.putInt(AlexEventsConstant.XALEX_SHOW_DURATION_INT, dur);
        bundle.putInt(AlexEventsConstant.XALEX_SHOW_INTERVAL_INT, inter);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + fromSource);
        }
    }

    public static void logShowTypeStyle(String name, String container, String fromSource, String type, String style, String text) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_TYPE_STRING, type);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_STYLE_STRING, style);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_TEXT_STRING, text);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShowTypeStyle | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + fromSource
                    + ", type:" + type
                    + ", style:" + style
                    + ", text:" + text);
        }
    }

    public static void logShowTypeText(String name, String type, String text, String style) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_TYPE_STRING, type);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_TEXT_STRING, text);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_STYLE_STRING, style);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", type:" + type
                    + ", text:" + text);
        }
    }

    public static void logShow(String from_source, String name, String container, String textValue, String style) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, textValue);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_STYLE_STRING, style);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + from_source);
        }
    }

    public static void logShow(String from_source, String name, String container, String textValue, String style, String destination) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, textValue);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_STYLE_STRING, style);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TO_DESTINATION_STRING, destination);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + from_source);
        }
    }

    public static void logNotification(String from_source, String name, String container, String pkgName, String post_time, String title, String des, String key, String group_key, String id, String channel_id) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, from_source);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CATEGORY_STRING, pkgName);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_STYLE_STRING, post_time);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TO_DESTINATION_STRING, title);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, des);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TYPE_STRING, key);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_URL_STRING, group_key);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_RESULT_CODE_STRING, id);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FLAG_STRING, channel_id);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
        if (DEBUG) {
            Log.d(TAG, "logShow | name:" + name
                    + ", container:" + container
                    + ", fromSource:" + from_source);
        }
    }

    public static final void logCount(int functionCode) {
        StatisticLogger.getLogger().countEvent(functionCode);
    }

    /**
     * 与充电状态相关的打点
     *
     * @param batteryLevel
     * @param chargedLevel
     * @param chargedDuration
     * @param chargerType
     */
    public static void logBatteryCharge(int batteryLevel, int chargedLevel, long chargedDuration, int chargerType) {
        Bundle bundle = new Bundle();
        bundle.putInt(XCLN_BATTERY_CHARGE_BATTERY_LEVEL_INT, batteryLevel);
        bundle.putInt(XCLN_BATTERY_CHARGE_CHARGED_LEVEL_INT, chargedLevel);
        bundle.putLong(XCLN_BATTERY_CHARGE_CHARGING_DURATION_INT, chargedDuration);
        bundle.putLong(XCLN_BATTERY_CHARGE_CHARGER_TYPE_INT, chargerType);
        StatisticLogger.getLogger().logEvent(XCLN_BATTERY_CHARGE, bundle);
    }

    public static void logShowResult(String name, String container, String from_source, int step1, int step2) {
        Bundle bundle = new Bundle();
        bundle.putString(XCLN_SHOW_RESULT_FROM_SOURCE_STRING, from_source);
        bundle.putString(XCLN_SHOW_RESULT_NAME_STRING, name);
        bundle.putString(XCLN_SHOW_RESULT_CONTAINER_STRING, container);
        bundle.putInt(XCLN_SHOW_RESULT_STEP1_INT, step1);
        bundle.putInt(XCLN_SHOW_RESULT_STEP2_INT, step2);
        StatisticLogger.getLogger().logEvent(XCLN_SHOW_RESULT, bundle);

    }

    private static void addIfValNotEmpty(Bundle bundle, String key, String val) {
        if (!TextUtils.isEmpty(val))
            bundle.putString(key, val);
    }

    private static void addIfValNotEmpty(Bundle bundle, String key, long val) {
        if (val > 0)
            bundle.putLong(key, val);
    }


    public static void logNewAdOperation(String name, String positionId, String action,
                                         String adSource, String adType, String adAction,
                                         String adPlacementId, long duration, String adRequestType, String resultCode, String resultInfo, String adCachePool) {
        Bundle bundle = new Bundle();
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_NAME_STRING, name);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_POSITION_ID_STRING, positionId);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_ACTION_STRING, action);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_AD_SOURCE_STRING, adSource);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_AD_TYPE_STRING, adType);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_AD_ACTION_STRING, adAction);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_AD_PLACEMENT_ID_STRING, adPlacementId);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_DURATION_INT, duration);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_RESULT_CODE_STRING, resultCode);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_RESULT_INFO_STRING, resultInfo);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_AD_REQUESTTYPE_STRING, adRequestType);
        addIfValNotEmpty(bundle, AdOperationEvent.XCLN_AD_OPERATION_AD_CACHEPOOL_STRING, adCachePool);
        StatisticLogger.getLogger().logEvent(AdOperationEvent.XCLN_AD_OPERATION, bundle);
        if (DEBUG) {
            Log.d(TAG, "#logNewAdOperation: " + bundle.toString());
        }
    }

    public static void logPVShow(String pageName, String positionId, String action) {
        logNewAdOperation(pageName, positionId, action, null, null, null, null, 0, null, null, null, null);
    }

    public static void logADShow(String pageName, String positionId, String requestType, String action, String adType, String adPlacementId) {
        logNewAdOperation(pageName, positionId, action, null, adType, action, adPlacementId, 0, requestType, null, null, null);
    }

    public static void logAdPV(String pageName, String positionId, String action) {
        logNewAdOperation(pageName, positionId, action, null, null, null, null, 0, null, null, null, null);
    }

    public static void logAD(String pageName, String positionId, String requestType, String action, String adType, String adPlacementId) {
        logNewAdOperation(pageName, positionId, action, null, adType, action, adPlacementId, 0, requestType, null, null, null);
    }

    public static void logShowUpload(String fromSource, String name, String container, String text, String type) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_TYPE_STRING, type);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_SHOW_TEXT_STRING, text);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_SHOW, bundle);
    }

    public static void logClickUpload(String fromSource, String name, String container, String text, String type) {
        Bundle bundle = new Bundle();
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_NAME_STRING, name);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_CONTAINER_STRING, container);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_FROM_SOURCE_STRING, fromSource);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TYPE_STRING, type);
        addIfNotEmpty(bundle, AlexEventsConstant.XALEX_CLICK_TEXT_STRING, text);
        StatisticLogger.getLogger().logEvent(AlexEventsConstant.XALEX_CLICK, bundle);
    }

}
