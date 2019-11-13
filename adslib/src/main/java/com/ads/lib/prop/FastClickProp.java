package com.ads.lib.prop;

import android.content.Context;

import org.interlaken.common.env.BasicProp;

public class FastClickProp extends BasicProp {

    public static final String REMOTE_CONFIG_NAME = "fast_click.prop";

    private static FastClickProp INSTANCE;

    public static final int TYPE_CAN_CLICK_TOTAL = 0;
    public static final int TYPE_CAN_CLICK_TOP = 1;
    public static final int TYPE_CAN_CLICK_BOTTOM = 2;
    public static final int TYPE_CAN_CLICK_BUTTON = 3;

    private FastClickProp(Context context) {
        super(context, REMOTE_CONFIG_NAME);
    }

    public static FastClickProp getInstance(Context context) {
        if (null == INSTANCE) {
            synchronized (FastClickProp.class) {
                if (null == INSTANCE) {
                    INSTANCE = new FastClickProp(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public static void reload(Context context) {
        synchronized (FastClickProp.class) {
            INSTANCE = new FastClickProp(context.getApplicationContext());
        }
    }

    /**
     * Fast Return 优化开关
     *
     * @return
     */
    public boolean isEnableFROpt() {
        String key = "fr.opt.e";
        int isFROptEnable = getInt(key, 0);
        return isFROptEnable > 0;
    }

    public long getFROptTime() {
        String key = "fr.opt.ms";
        long optTime = getLong(key, 2000);
        if (optTime < 0) {
            optTime = 2000;
        }
        return optTime;
    }

    /**
     * 获取广告延迟点击的时长，单位毫秒
     *
     * @return
     */
    public long getFCDMS(String unitId) {

        String key = unitId + "_FC_D_MS";
        long delayTime = getLong(key, 0);
        if (delayTime < 0) {
            delayTime = 1000;
        }
        return delayTime;
    }

    /**
     * 是否开启FastClick
     *
     * @return
     */
    public boolean isEnabaleFastClickOpt(String unitId) {
        String key = unitId + "_fast_click_optimize_enable";
        int enable = getInt(key, 1);
        return enable == 1;
    }

    /**
     * 获取不可点击区域的广告源
     * @return
     */
    public String getUnClickableAreaSource() {
        String unclickableAreaSource = get("unclickable_area_source","ab,an");
        return unclickableAreaSource;
    }


    /**
     * 获取不可点击区域策略
     *
     * @param unitId
     * @return
     *  0 全部可点
     *  1 adchoices mediaView可点
     *  2 title summary icon call2Action可点
     *  3 call2Action可点
     */
    public int getClickAreaStrategy(String unitId){
        String key = unitId + "_click_area_strategy";
        return getInt(key,0);
    }


}
