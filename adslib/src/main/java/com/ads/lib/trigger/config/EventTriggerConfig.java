package com.ads.lib.trigger.config;

import android.content.Context;
import android.text.format.DateUtils;

import org.interlaken.common.env.BasicProp;

public class EventTriggerConfig extends BasicProp {


    private static final String TAG = "EventTriggerConfig";
    private static final boolean DEBUG = ModuleConfig.DEBUG;

    private static final String PROP_FILE = "fm_trigger.prop";
    private static EventTriggerConfig mInstance;

    /**
     * 模块开关
     * 默认开
     */
    private static final String MODULE_ENABLE = "m_e";

    /**
     * 计时器触发器开关
     */
    private static final String TRIGGER_TIMER_ENABLE = "t_t_e";
    /**
     * 网络变化触发器开关
     */
    private static final String TRIGGER_NET_CHANGE_ENABLE = "t_n_c_e";
    /**
     * 电量变化触发器开关
     */
    private static final String TRIGGER_BATTERY_ENABLE = "t_b_e";
    /**
     * 屏幕变化触发器开关
     */
    private static final String TRIGGER_SCREEN_ENABLE = "t_s_e";

    /**
     * 计时器触发间隔
     * 单位：分
     * 默认 45分
     */
    private static final String TRIGGER_TIMER_INTERVAL = "t_t_i";

    /**
     * 共享池广告请求间隔
     * 单位：分
     * 默认 45分
     *
     */
    private static final String TRIGGER_REQUEST_INTERVAL = "t_t_i";


    private EventTriggerConfig(Context context) {
        super(context, PROP_FILE);
    }

    public static EventTriggerConfig getInstance(Context c) {
        if (null == mInstance) {
            synchronized (EventTriggerConfig.class) {
                if (null == mInstance) {
                    mInstance = new EventTriggerConfig(c.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public static void reload(Context context) {
        synchronized (EventTriggerConfig.class) {
            mInstance = new EventTriggerConfig(context.getApplicationContext());
        }
    }

    /**
     * 模块开关
     * 默认关
     */
    public boolean isModuleEnable() {
        int enable = getInt(MODULE_ENABLE, 1);
        return enable == 1;
    }

    /**
     * 计时器触发器开关
     * 默认关
     */
    public boolean isTimerTriggerEnable() {
        int enable = getInt(TRIGGER_TIMER_ENABLE, 1);
        return enable == 1;
    }

    /**
     * 网络变化触发器开关
     * 默认关
     */
    public boolean isNetChangeTriggerEnable() {
        int enable = getInt(TRIGGER_NET_CHANGE_ENABLE, 1);
        return enable == 1;
    }

    /**
     * 电量触发器开关
     * 默认关
     */
    public boolean isBatteryTriggerEnable() {
        int enable = getInt(TRIGGER_BATTERY_ENABLE, 1);
        return enable == 1;
    }

    /**
     * 屏幕触发器开关
     * 默认关
     */
    public boolean isScreenTriggerEnable() {
        int enable = getInt(TRIGGER_SCREEN_ENABLE, 0);
        return enable == 1;
    }

    /**
     * 计时器触发间隔
     * 单位：分
     * 默认 45分
     */
    public long getTimerInterval(){
        int interval = getInt(TRIGGER_TIMER_INTERVAL, 45);
        return interval * DateUtils.MINUTE_IN_MILLIS;
    }

    /**
     * 计时器触发间隔
     * 单位：分
     * 默认 45分
     */
    public long getRequestInterval(){
        int interval = getInt(TRIGGER_REQUEST_INTERVAL, 45);
        return interval * DateUtils.MINUTE_IN_MILLIS;
    }

}
