package org.thanos.core;

import org.interlaken.common.XalContext;

/**
 * Created by zhaobingfeng on 2019-08-16.
 */
public class ThanosCloudConstants {
    /**
     * 第一个频道的信息流列表请求缓存时间，单位为秒
     */
    public static final String DEFAULT_LIST_CACHE_TIME_IN_SEC = "9PYzy6a";
    /**
     * 竖屏视频播放界面显示广告图标
     */
    public static final String PORTRAIT_VIDEO_PLAYING_IS_SHOW_AD_ICON = "tGorbF1";
    /**
     * 列表项展示超过这个时间就算展示了，要打点上报。单位毫秒，默认1秒，即1000
     */
    public static final String LIST_ITEM_DISPLAY_DURATION_FACTOR = "Ucaw4g";
    /**
     * 频道列表的本地缓存时间，单位小时，默认10天，即240
     */
    public static final String CHANNEL_LIST_CACHE_TIME_IN_HOURS = "KaLOzFI";
    /** 浮窗通知过期时间，单位秒，默认24小时，即86400 */
    public static final String PUSH_OVERLAY_EXPIRE_TIME_IN_SEC = "bHusFSp";
    /** PUSH通知最多同时展现的个数，默认6条 */
    public static final String PUSH_NOTIFICATION_MAX_SHOWN_COUNT = "faAK8Hb";

    public static long getLong(String key, long defaultValue) {
        try {
            return Long.parseLong(XalContext.getCloudAttribute(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String getString(String key, String defaultValue) {
        return XalContext.getCloudAttribute(key, defaultValue);
    }
}
