package com.ads.lib.prop;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ads.lib.AdUnitId;
import com.ads.lib.ModuleConfig;

import org.interlaken.common.env.BasicProp;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by {@author 32} on 2018/06/11.
 */

public class AdPositionIdProp extends BasicProp {
    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = DEBUG ? "AdPositionIdProp" : "";

    public static final String PROP_FILE = "app_apid_new.prop";

    private static AdPositionIdProp mInstance;

    private AdPositionIdProp(Context c) {
        super(c, PROP_FILE, "utf-8");

    }


    public static AdPositionIdProp getInstance(Context context) {
        if (null == mInstance) {
            synchronized (AdPositionIdProp.class) {
                if (null == mInstance) {
                    mInstance = new AdPositionIdProp(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public static void reload(Context context) {
        synchronized (AdPositionIdProp.class) {
            mInstance = new AdPositionIdProp(context.getApplicationContext());
        }
    }

    public String getAdPositionIdByAdUnitId(String adUnitID) {
        return getAdPositionIdByAdUnitId(adUnitID, "");
    }

    public String getPid(String pidKey) {
        return "";
    }

    public String getAdPositionIdByAdUnitId(String adUnitID, String postFix) {
        if (TextUtils.isEmpty(adUnitID)) {
            return "";
        }
        String adPositionIdKey;
        String defaultValue = "";
        switch (adUnitID) {
            case AdUnitId.UNIT_ID_KEY_NATIVE_ONE_TAP_BOOST_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_ONE_TAP_BOOST_ADS;
                defaultValue = "1669938";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_BOOST_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_BOOST_RESULT_BIG_ADS;
                defaultValue = "148013342";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_HOME_RANGE:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_HOME_RANGE_INTERSTITIAL;
                defaultValue = "148013346";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_OUT_POWER_CONNECT:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_POWER_CONNECT_INTERSTITIAL;
                defaultValue = "92513260";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_RUBBISHBANNER_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_RUBBISHBANNER_RESULT_BIG_ADS;
                defaultValue = "92510963";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_APPLOCK_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_APPLOCK_RESULT_BIG_ADS;
                defaultValue = "92511135";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_NOTIBANNER_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_NOTIBANNER_RESULT_BIG_ADS;
                defaultValue = "92511012";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_ANTIBANNER_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_ANTIBANNER_RESULT_BIG_ADS;
                defaultValue = "92510962";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_CPUBANNER_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_CPUBANNER_RESULT_BIG_ADS;
                defaultValue = "92511038";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_BOOSTBANNER_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_BOOSTBANNER_RESULT_BIG_ADS;
                defaultValue = "92511039";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_CPU_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_CPU_RESULT_BIG_ADS;
                defaultValue = "148013390";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_JUNK_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_JUNK_RESULT_BIG_ADS;
                defaultValue = "148013294";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_SPECIAL_CLEAN_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_SPECIAL_CLEAN_RESULT_BIG_ADS;
                defaultValue = "92510291";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_AV_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_AV_RESULT_BIG_ADS;
                defaultValue = "148013319";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFY_BOOST:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_NOTIFY_BOOST;
                defaultValue = "148013342";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_BATTERY_SAVER_BIG:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_BATTERY_SAVER_BIG;
                defaultValue = "148013392";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFICATION_CLEANER_RESULT_BIG:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_NOTIFICATION_CLEANER_RESULT_BIG;
                defaultValue = "148013295";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFICATIONPROTECT_CLEAN_RESULT_BIG:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_NOTIFICATIONPROTECT_CLEAN_RESULT_BIG;
                defaultValue = "1669938";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_BOOST_RESULT:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_BOOST_RESULT;
                defaultValue = "1661652";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_CPU_RESULT:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_CPU_RESULT;
                defaultValue = "1661651";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_JUNK_RESULT:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_JUNK_RESULT;
                defaultValue = "1661653";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_SPECIAL_CLEAN_RESULT:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_SPECIAL_CLEAN_RESULT;
                defaultValue = "1661653";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_AV_RESULT:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_AV_RESULT;
                defaultValue = "16610111";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_BATTERY_SAVER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_BATTERY_SAVER;
                defaultValue = "1661654";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFICATION_CLEANER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_NOTIFICATION_CLEANER;
                defaultValue = "148013322";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_WIFI_SCANER_RESULT_BIG:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_WIFI_SCANNER_RESULT_BIG;
                defaultValue = "1669938";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_NOTIFICATION_SECURITY:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_NOTIFICATION_SECURITY;
                defaultValue = "1661650";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_WIFI_SECURITY:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_WIFI_SECURITY;
                defaultValue = "1661656";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_CALL_BLOCK_FINISH:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_CALL_BLOCK_FINISH;
                defaultValue = "16610008";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_CALL_BLOCK_MARK:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_CALL_BLOCK_MARK;
                defaultValue = "16610008";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_AV_RTP_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_AV_RTP_ADS;
                defaultValue = "92510294";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_AV_FIRST_RESULT_BIG_ADS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_AV_FIRST_RESULT_BIG_ADS;
                defaultValue = "92510293";
                break;
            case AdUnitId.UNIT_ID_KEY_NATIVE_CHARGE_ASSISTANT:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NATIVE_CHARGE_ASSISTANT;
                defaultValue = "92510293";
                break;

            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_BOOST:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_BOOST;
                defaultValue = "148013321";
                break;
            case AdUnitId.UNIT_ID_KEY_HOMEINTER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_HOME;
                defaultValue = "148013344";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_OUT_STREAM:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_OUT_STREAM;
                defaultValue = "148013331";
                break;
            case AdUnitId.UNIT_ID_KEY_FUNCTIONINTER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_FUNCTION;
                defaultValue = "148013323";
                break;
            case AdUnitId.UNIT_ID_KEY_APPINSTALLINTER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_APPUNINSTALL;
                defaultValue = "92510986";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_CPU:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_CPU;
                defaultValue = "148013391";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_RUBBISH:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_RUBBISH;
                defaultValue = "148013296";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_BATTERYSAVER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_BATTERYSAVER;
                defaultValue = "148013414";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_ANTIVIRUS:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_ANTIVIRUS;
                defaultValue = "148013320";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_NOTIFYBOOST:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_NOTIFYBOOST;
                defaultValue = "148013342";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_NOTIFYCLEANER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_NOTIFYCLEANER;
                defaultValue = "148013322";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_NOTIFYPROTECTCLEANER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_NOTIFYPROTECTCLEANER;
                defaultValue = "pid_l_i";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_WIFI_SCANNER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_WIFI_SCANNER;
                defaultValue = "pid_l_i";
                break;

            case AdUnitId.UNIT_ID_SAFE_BROWSER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_SAFE_BROWSER;
                defaultValue = "1669938";
                break;
            case AdUnitId.UNIT_ID_HOME_TOP:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_HOME_TOP;
                defaultValue = "92510299";
                break;
            case AdUnitId.UNIT_ID_HOME_TOP2:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_HOME_TOP2;
                defaultValue = "92511036";
                break;
            case AdUnitId.UNIT_ID_HOME_TOP3:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_HOME_TOP3;
                defaultValue = "92511037";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_SUPPLEMENT:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_SUPPLEMENT;
                defaultValue = "pid_l_i";
                break;
            case AdUnitId.UNIT_ID_WHATSAPP_CARD_LIST:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_WHATSAPP_CARD_LIST;
                defaultValue = "92510300";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_HANGEDCALL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_HANGEDCALL;
                defaultValue = "16610136";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_DETACHCHARGER:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_INTERSTITIAL_DETACHCHARGER;
                defaultValue = "92510365";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_FLOATWINDOW_NETWORK_SPEED:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_FLOATWINDOW_NETWORK_SPEED;
                defaultValue = "148013418";
                break;
            //DETAIL LIST AD
            case AdUnitId.UNIT_ID_VIDEO_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_VIDEO_DETAIL_LIST;
                defaultValue = "92510302";
                break;
            case AdUnitId.UNIT_ID_DOC_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_DOC_DETAIL_LIST;
                defaultValue = "92510305";
                break;
            case AdUnitId.UNIT_ID_LARGEFILES_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_LARGEFILE_DETAIL_LIST;
                defaultValue = "92510307";
                break;
            case AdUnitId.UNIT_ID_DOWNLOAD_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_DOWNLOAD_DETAIL_LIST;
                defaultValue = "92510309";
                break;
            case AdUnitId.UNIT_ID_DUPLICATE_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_DUPLICATE_DETAIL_LIST;
                defaultValue = "92510361";
                break;
            case AdUnitId.UNIT_ID_MEMES_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_MEMES_DETAIL_LIST;
                defaultValue = "92510363";
                break;
            case AdUnitId.UNIT_ID_AUDIO_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_AUDIO_DETAIL_LIST;
                defaultValue = "92510303";
                break;
            case AdUnitId.UNIT_ID_IMAGE_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_IMAGE_DETAIL_LIST;
                defaultValue = "92510304";
                break;
            case AdUnitId.UNIT_ID_APPUNINSTALL_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_APPUNINSTALL_DETAIL_LIST;
                defaultValue = "92510301";
                break;
            case AdUnitId.UNIT_ID_KEY_INTERSTITIAL_OUT_POWER_DISCHARGE:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_POWER_DISCHARGE_INTERSTITIAL;
                defaultValue = "148013345";
                break;
            case AdUnitId.UNIT_ID_APPRESET_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_APPRESET_DETAIL_LIST;
                defaultValue = "92510414";
                break;
            case AdUnitId.UNIT_ID_APKDELETE_LIST_DETAIL:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_APKDELETE_DETAIL_LIST;
                defaultValue = "92510319";
                break;
            case AdUnitId.UNIT_ID_PREVIEW:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_PREVIEW;
                defaultValue = "92511010";
                break;
            case AdUnitId.UNIT_ID_NC_CARD:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_POSITION_ID_NC_CARD;
                defaultValue = "92510413";
                break;

            //new group
            case AdUnitId.PUBLIC_RESULT_NATIVE_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_RESULT_NATIVE;
                defaultValue = adPositionIdKey;
                break;
            case AdUnitId.PUBLIC_RESULT_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_RESULT_INTERSTITIAL;
                defaultValue = adPositionIdKey;
                break;
            case AdUnitId.PUBLIC_MAIN_BACK_NATIVE_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_MAIN_BACK_NATIVE;
                defaultValue = adPositionIdKey;
                break;
            case AdUnitId.PUBLIC_SEC_ENTRY_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_SEC_ENTRY_INTERSTITIAL;
                defaultValue = adPositionIdKey;
                break;
            case AdUnitId.PUBLIC_RES_BACK_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_RES_BACK_INTERSTITIAL;
                defaultValue = adPositionIdKey;
                break;
            case AdUnitId.PUBLIC_CHARGE_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_CHARGE_INTERSTITIAL;
                defaultValue = adPositionIdKey;
                break;
            case AdUnitId.PUBLIC_HOME_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_HOME_INTERSTITIAL;
                defaultValue = adPositionIdKey;
                break;
            case AdUnitId.PUBLIC_MSN_FEED_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_MSN_FEED_INTERSTITIAL;
                defaultValue = adPositionIdKey;
                break;
            case AdUnitId.PUBLIC_ADD_OUT_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_ADD_OUT_INTERSTITIAL;
                break;
            case AdUnitId.PUBLIC_CPU_RES_NATIVE_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_CPU_RES_NATIVE;
                break;
            case AdUnitId.PUBLIC_CPU_RES_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_CPU_RES_INTERSTITIAL;
                break;
            case AdUnitId.PUBLIC_CHARGE_SAVE_RES_NATIVE_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_CHARGE_SAVE_RES_NATIVE;
                break;
            case AdUnitId.PUBLIC_CHARGE_SAVE_RES_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_CHARGE_SAVE_RES_INTERSTITIAL;
                break;
            case AdUnitId.PUBLIC_SPEED_LIMIT_INTERSTITIAL_UNIT_ID:
                adPositionIdKey = AdUnitId.AdPositionIdKey.KEY_PID_PUBLIC_SPEED_LIMIT_INTERSTITIAL;
                break;
            default:
                adPositionIdKey = adUnitID;
        }

        if (TextUtils.isEmpty(adPositionIdKey)) {
            return "";
        }

        if (!TextUtils.isEmpty(postFix)) {
            adPositionIdKey += postFix;
        }

        return getAdPositionId(adPositionIdKey, defaultValue);
    }


    private String getAdPositionId(String v3Key, String defaultValue) {
        String adPidV3 = getString(v3Key, defaultValue);
        if (DEBUG) {
            Log.d(TAG, "getAdPositionId adPidV3 = " + adPidV3);
        }

        return adPidV3;
    }

    private String getString(String key, String defaultValue) {
        String value = get(key);

        if (TextUtils.isEmpty(value)) {
            value = defaultValue;
        }
        return value;
    }

}
