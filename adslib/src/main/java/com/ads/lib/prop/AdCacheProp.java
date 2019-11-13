package com.ads.lib.prop;

import android.content.Context;
import android.util.Log;

import com.ads.lib.ModuleConfig;

import org.interlaken.common.env.AutoReloadPropXal;

public class AdCacheProp extends AutoReloadPropXal {
    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = DEBUG ? "AdCacheProp" : "";

    private static final String PROP_FILE = "turbo_c_v3.prop";
    private volatile static AdCacheProp mInstance;

    private static final String POSTFIX_PARALLEL_COUNT = "_pc";
    private static final String POSTFIX_INVENTORY = "_i";
    private static final String POSTFIX_IS_PREPARE_ICON = "_icon";
    private static final String POSTFIX_IS_PREPARE_BANNER = "_banner";
    private static final String POSTFIX_IS_MUTED = "_mute";
    private static final String POSTFIX_NEED_PRELOAD = "_pre";
    private static final String POSTFIX_PRIORITY_PRELOAD = "_priority";
    private static final String POSTFIX_PRIORITY_RETRY = "_retry";

    private AdCacheProp(Context c) {
        super(c, PROP_FILE);
    }

    public int getParallelCount(String propKey) {
        int value = getInt(propKey + POSTFIX_PARALLEL_COUNT, 1);
        if (DEBUG) {
            Log.i(TAG, "#getParallelCount propKey = " + propKey + "; value = " + value);
        }

        return value;
    }

    public int getInventory(String propKey) {
        int value = getInt(propKey + POSTFIX_INVENTORY, getDefaultInventory(propKey));
        if (DEBUG) {
            Log.i(TAG, "#getInventory propKey = " + propKey + "; value = " + value);
        }

        return value;
    }

    private int getDefaultInventory(String propKey){
        //应用外插屏共享缓存池
        if(propKey.equals("148013281")){
            return 2;
        }
        return 1;
    }

    public boolean isPrepareIcon(String propKey) {
        int value = getInt(propKey + POSTFIX_IS_PREPARE_ICON, 0);
        if (DEBUG) {
            Log.i(TAG, "#isPrepareIcon propKey = " + propKey + "; value = " + value);
        }

        return value == 1;
    }

    public boolean isPrepareBanner(String propKey) {
        int value = getInt(propKey + POSTFIX_IS_PREPARE_BANNER, 0);
        if (DEBUG) {
            Log.i(TAG, "#isPrepareIcon propKey = " + propKey + "; value = " + value);
        }

        return value == 1;
    }

    public boolean isMuted(String propKey) {
        int value = getInt(propKey + POSTFIX_IS_MUTED, 1);
        if (DEBUG) {
            Log.i(TAG, "#isPrepareIcon propKey = " + propKey + "; value = " + value);
        }

        return value == 1;
    }

    public boolean needPreload(String propKey) {
        int value = getInt(propKey + POSTFIX_NEED_PRELOAD, 0);
        if (DEBUG) {
            Log.i(TAG, "#isPrepareIcon propKey = " + propKey + "; value = " + value);
        }

        return value == 1;
    }

    public int getPriority(String propKey) {
        int value = getInt(propKey + POSTFIX_PRIORITY_PRELOAD, 1);
        if (DEBUG) {
            Log.i(TAG, "#getPriority propKey = " + propKey + "; value = " + value);
        }

        return value;
    }

    public int getRetry(String propKey) {
        int value = getInt(propKey + POSTFIX_PRIORITY_RETRY, getDefaultRetryCount(propKey));
        if (DEBUG) {
            Log.i(TAG, "#getRetry propKey = " + propKey + "; value = " + value);
        }

        return value;
    }

    private int getDefaultRetryCount(String propKey){
        //应用外插屏共享缓存池
        if(propKey.equals("148013281")){
            return 3;
        }
        return 1;
    }

    public static AdCacheProp getInstance(Context c) {
        if (null == mInstance) {
            synchronized (AdCacheProp.class) {
                if (null == mInstance) {
                    mInstance = new AdCacheProp(c.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public static void reload(Context context) {
        synchronized (AdCacheProp.class) {
            mInstance = new AdCacheProp(context.getApplicationContext());
        }
    }

}