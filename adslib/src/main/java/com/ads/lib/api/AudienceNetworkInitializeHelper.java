package com.ads.lib.api;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;

import org.saturn.splash.config.AppConfig;

/**
 * Created by zhaozhiwen on 2019-06-01.
 */
public class AudienceNetworkInitializeHelper
        implements AudienceNetworkAds.InitListener {

    public static final String TAG = "AudienceNetworkInitializeHelper";
    public static final boolean DEBUG = AppConfig.DEBUG;

    /**
     * It's recommended to call this method from Application.onCreate().
     * Otherwise you can call it from all Activity.onCreate()
     * methods for Activities that contain ads.
     *
     * @param context Application or Activity.
     */
    static void initialize(Context context) {
        if (!AudienceNetworkAds.isInitialized(context)) {
//            if (DEBUG) {
//                AdSettings.turnOnSDKDebugger(context);
//            }

            AudienceNetworkAds
                    .buildInitSettings(context)
                    .withInitListener(new AudienceNetworkInitializeHelper())
                    .initialize();
        }
    }

    @Override
    public void onInitialized(AudienceNetworkAds.InitResult result) {
        if (DEBUG) {
            Log.d(TAG, "onInitialized: " + result.getMessage());
        }
    }
}
