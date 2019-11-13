package com.ads.lib.prop;

import android.content.Context;
import android.util.Log;

import com.ads.lib.ModuleConfig;

import org.interlaken.common.env.AutoReloadPropXal;

public class AdStrategyProp extends AutoReloadPropXal {
    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = DEBUG ? "AdStrategyProp" : "";

    private static final String PROP_FILE = "turbo_s_v3.prop";
    private volatile static AdStrategyProp mInstance;

    private static final String AD_STRATEGY = "_ad_s";

    private AdStrategyProp(Context c) {
        super(c, PROP_FILE);
    }

    public String getDefaultStrategy(String propKey) {
        String value = get(propKey + AD_STRATEGY,"");
        if (DEBUG) {
            Log.i(TAG, "#getDefaultStrategy propKey = " + propKey + "; value = " + value);
        }

        return value;
    }

    public static AdStrategyProp getInstance(Context c) {
        if (null == mInstance) {
            synchronized (AdStrategyProp.class) {
                if (null == mInstance) {
                    mInstance = new AdStrategyProp(c.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public static void reload(Context context) {
        synchronized (AdStrategyProp.class) {
            mInstance = new AdStrategyProp(context.getApplicationContext());
        }
    }

}