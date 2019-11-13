package com.ads.lib;


import android.content.Context;

import com.ads.lib.loader.AppLockAdsLoader;
import com.ads.lib.loader.AvRtpAdsLoader;
import com.ads.lib.loader.BaseAdsLoader;
import com.ads.lib.loader.ListAdsLoader;
import com.ads.lib.loader.MainNativeAdsLoader;
import com.ads.lib.loader.NotifyAdsLoader;
import com.ads.lib.loader.PreviewAdsLoader;
import com.ads.lib.loader.ResultBigAdsLoader;
import com.ads.lib.loader.WhatsAppCardAdsLoader;
import com.baselib.ui.CommonConstants;

import java.util.HashMap;
import java.util.Map;

import static com.baselib.ui.CommonConstants.TYPE_RESULT_JUNK_BANNER;
import static com.baselib.ui.CommonConstants.TYPE_RESULT_VIRUS;

public class AdsLoader {

    // 和结果页有关的
    public static final int TYPE_BOOST_RESULT = CommonConstants.TYPE_RESULT_BOOST;// 加速结果页
    public static final int TYPE_RESULT_JUNK_BANNER = CommonConstants.TYPE_RESULT_JUNK_BANNER;
    public static final int TYPE_RESULT_APP_LOCK = CommonConstants.TYPE_RESULT_APP_LOCK;
    public static final int TYPE_RESULT_NOTI_BANNER = CommonConstants.TYPE_RESULT_NOTI_BANNER;
    public static final int TYPE_RESULT_ANTI_BANNER = CommonConstants.TYPE_RESULT_ANIT_BANNER;
    public static final int TYPE_RESULT_CPU_BANNER = CommonConstants.TYPE_RESULT_CPU_BANNER;
    public static final int TYPE_RESULT_BOOST_BANNER = CommonConstants.TYPE_RESULT_BOOST_BANNER;

    public static final int TYPE_CPU_COOL_RESULT = CommonConstants.TYPE_RESULT_CPU;// cpu结果页
    public static final int TYPE_RUBBISH_CLEAN_RESULT = CommonConstants.TYPE_RESULT_JUNK;// 清理结果页
    public static final int TYPE_ANTI_VIRUS = TYPE_RESULT_VIRUS;// 杀毒结果页
    public static final int TYPE_BATTERY_RESULT = CommonConstants.TYPE_RESULT_BATTERY; // 省电结果页
    public static final int TYPE_SPECIAL_CLEAN_FOR_WHATSAPP = CommonConstants.TYPE_RESULT_SPECIAL_CLEAN;// 专清结果页
    public static final int TYPE_NOTIFICATION_BOOST_RESULT = CommonConstants.TYPE_RESULT_NOTIFICATION_BOOST;//从通知栏点击加速按钮
    public static final int TYPE_NOTIFICATION_CLEAN_RESULT = CommonConstants.TYPE_RESULT_NOTIFICATION_CLEAN;//通知栏清理结果页
    public static final int TYPE_NOTIFICATION_SECURITY_RESULT = CommonConstants.TYPE_RESULT_NOTIFICATION_SECURITY;//消息安全结果页
    public static final int TYPE_WIFI_SCAN_RESULT = CommonConstants.TYPE_RESULT_WIFI_SCAN_NROMAL; // Wifi扫描结果页
    public static final int TYPE_SUPPLMENT = CommonConstants.TYPE_RESULT_SUPPLEMENT;

    public static final int TYPE_ANTI_VIRUS_RTP = 1;// 实时防护安全弹窗
    public static final int TYPE_APPLOCK = 2;// 应用锁
    public static final int TYPE_ONE_TAP_BOOST = 3;// 一键加速结果
    public static final int TYPE_AV_FIRST_SCAN = 4;//首次进行扫描产生的结果页
    public static final int TYPE_CALL_BLOCK_MARK = 5;//电话拦截标记页面
    public static final int TYPE_CALL_BLOCK_FINISH = 6;//电话拦截完成页面
    public static final int TYPE_DIS_CHARGE = 9;//充电助手
    public static final int TYPE_HOME_TOP_AD = 10;//首页
    public static final int TYPE_WHATSAPP_LIST_CARD_AD = 11;//whatsapp列表卡片
    public static final int TYPE_NC_CARD_AD = 12;//通知栏清理
    public static final int TYPE_NS_CARD_AD = 13;//消息安全
    public static final int TYPE_PREVIEW_AD = 14;//预览页

    public static final int TYPE_VIDEOS_LIST = 15;// 加速结果页
    public static final int TYPE_AUDIOS_LIST = 16;// cpu结果页
    public static final int TYPE_IMAGE_LIST = 17;// 清理结果页
    public static final int TYPE_DOC_LIST = 18;// 杀毒结果页
    public static final int TYPE_LARGE_FILE_LIST = 19; // 省电结果页
    public static final int TYPE_DOWNLOAD_LIST = 20;// 专清结果页
    public static final int TYPE_DUPLICATES_LIST = 21;//从通知栏点击加速按钮
    public static final int TYPE_MEMES_LIST = 22;//通知栏清理结果页
    public static final int TYPE_NEW_DIS_CHARGE = 23;//充电助手
    public static final int TYPE_APPMANAGER_UNINSTALL = 23;// 应用卸载
    public static final int TYPE_APPMANAGER_RESET = 24;// 应用重置
    public static final int TYPE_APPMANAGER_APKDELETE = 25;// 安装包删除

//    private static AppLockAdsLoader sAppLockInstance = null;
//    private static BaseAdsLoader sTestInstance = null;
    //private static BaseAdsLoader sResultInstance = null;

    private static final Object sLock = new Object();

    private static Map<Integer, BaseAdsLoader> sAdsLoaderMap = new HashMap<>();
    private static Map<Integer, BaseAdsLoader> sBigResultAdsLoaders = new HashMap<>();

    public static BaseAdsLoader getAdsLoader(Context cxt, int type) {
        BaseAdsLoader instance;
        synchronized (sLock) {
            instance = sAdsLoaderMap.get(type);
            if (instance != null && instance.shouldReload())
                instance = null;
            if (instance == null) {
                switch (type) {
                    case TYPE_HOME_TOP_AD:
                        instance = new MainNativeAdsLoader(cxt, type);
                        break;
                    case TYPE_VIDEOS_LIST:
                    case TYPE_AUDIOS_LIST:
                    case TYPE_IMAGE_LIST:
                    case TYPE_DOC_LIST:
                    case TYPE_LARGE_FILE_LIST:
                    case TYPE_DOWNLOAD_LIST:
                    case TYPE_DUPLICATES_LIST:
                    case TYPE_MEMES_LIST:
                    case TYPE_APPMANAGER_UNINSTALL:
                    case TYPE_APPMANAGER_RESET:
                    case TYPE_APPMANAGER_APKDELETE:
                        instance = new ListAdsLoader(cxt, type);
                        break;
                    case TYPE_APPLOCK:
                        instance = new AppLockAdsLoader(cxt);
                        break;
                    case TYPE_ANTI_VIRUS_RTP:
                        instance = new AvRtpAdsLoader(cxt, type);
                        break;
                    case TYPE_PREVIEW_AD:
                        instance = new PreviewAdsLoader(cxt, type);
                        break;
                    case TYPE_NC_CARD_AD:
                        instance = new NotifyAdsLoader(cxt, type);
                        break;
                    case TYPE_WHATSAPP_LIST_CARD_AD:
                        instance = new WhatsAppCardAdsLoader(cxt, type);
                        break;
                    case TYPE_ANTI_VIRUS:
                    case TYPE_BOOST_RESULT:
                    case TYPE_CPU_COOL_RESULT:
                    case TYPE_RUBBISH_CLEAN_RESULT:
                    case TYPE_RESULT_JUNK_BANNER:
                    case TYPE_RESULT_APP_LOCK:
                    case TYPE_RESULT_NOTI_BANNER:
                    case TYPE_RESULT_ANTI_BANNER:
                    case TYPE_RESULT_CPU_BANNER:
                    case TYPE_RESULT_BOOST_BANNER:
                    case TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                    case TYPE_NOTIFICATION_BOOST_RESULT:
                    case TYPE_NOTIFICATION_CLEAN_RESULT:
                    case TYPE_NOTIFICATION_SECURITY_RESULT:
                    case TYPE_BATTERY_RESULT:
                    case TYPE_WIFI_SCAN_RESULT:
                        instance = new ResultBigAdsLoader(cxt, type);
                        break;
                }
                if (instance != null) {
                    sAdsLoaderMap.put(type, instance);
                }
            }
        }
        return instance;
    }

    public static BaseAdsLoader getBigAdsLoader(Context cxt, int type) {
        BaseAdsLoader instance;
        synchronized (sLock) {
            instance = sBigResultAdsLoaders.get(type);
            if (instance != null && instance.shouldReload())
                instance = null;
            if (instance == null) {
                switch (type) {
                    case TYPE_BOOST_RESULT:
                    case TYPE_CPU_COOL_RESULT:
                    case TYPE_RUBBISH_CLEAN_RESULT:
                    case TYPE_SPECIAL_CLEAN_FOR_WHATSAPP:
                    case TYPE_ANTI_VIRUS:
                    case TYPE_NOTIFICATION_BOOST_RESULT:
                    case TYPE_NOTIFICATION_CLEAN_RESULT:
                    case TYPE_NOTIFICATION_SECURITY_RESULT:
                    case TYPE_BATTERY_RESULT:
                    case TYPE_WIFI_SCAN_RESULT:
                        instance = new ResultBigAdsLoader(cxt, type);
                        break;
                    case TYPE_ANTI_VIRUS_RTP:
                        break;
                    case TYPE_APPLOCK:
                        break;
                    case TYPE_AV_FIRST_SCAN:
                        instance = new AvRtpAdsLoader(cxt, type);
                        break;
                    case TYPE_PREVIEW_AD:
                        instance = new PreviewAdsLoader(cxt, type);
                        break;

                }
                sBigResultAdsLoaders.put(type, instance);
            }
        }
        return instance;
    }

    public static BaseAdsLoader getResultInstance(Context cxt, int type) {
        return getAdsLoader(cxt, type);
    }

    public static BaseAdsLoader getAppLockInstance(Context cxt) {
        return getAdsLoader(cxt, TYPE_APPLOCK);
    }

//    public static BaseAdsLoader getTestInstance(Context cxt) {
//        synchronized (AdsLoader.class) {
//            if (sTestInstance == null)
//                sTestInstance = new BoostNoneAdsLoader(cxt);
//        }
//        return sTestInstance;
//    }


}
