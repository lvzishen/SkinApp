package com.ads.lib.prop;

import android.content.Context;
import android.util.Log;

import com.ads.lib.ModuleConfig;

import org.interlaken.common.env.BasicProp;

public class TrackingProp extends BasicProp {

    public static final String REMOTE_CONFIG_NAME = "tracking.prop";
    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = "TrackingProp";

    private static TrackingProp INSTANCE;

    private TrackingProp(Context context) {
        super(context, REMOTE_CONFIG_NAME);
    }

    public static TrackingProp getInstance(Context context) {
        if (null == INSTANCE) {
            synchronized (TrackingProp.class) {
                if (null == INSTANCE) {
                    INSTANCE = new TrackingProp(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public static void reload(Context context) {
        synchronized (TrackingProp.class) {
            INSTANCE = new TrackingProp(context.getApplicationContext());
        }
    }


    /**
     * 是否开启打点
     *
     * @return true开启
     */
    public boolean isEnableTracking() {
        String key = "etrac";
        int isEnable = getInt(key, 1);
        boolean enable = isEnable > 0;
        if (DEBUG) {
            Log.d(TAG, "#isEnableTracking isEnable:" + enable);
        }
        return enable;
    }


}
