package com.ads.lib.init;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import com.ads.lib.ModuleConfig;
import com.ads.lib.mediation.config.IAllowLoaderAdListener;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.FacebookAdapterConfiguration;
import com.mopub.mobileads.GooglePlayServicesAdapterConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by zhaozhiwen on 2018/12/13.
 */
public class MoPubStarkInit extends Observable {

    public static final String TAG = "Stark.MoPubStarkInit";
    public static final boolean DEBUG = ModuleConfig.DEBUG;
    private static MoPubStarkInit instance;

    /**
     * moPub广告源是否初始化完成1秒以上
     */

    public static boolean isInitAfterOneMinute = false;


    private Handler mHandler = new Handler(Looper.getMainLooper());

    private IAllowLoaderAdListener allowLoaderAdListener;

    private MoPubStarkInit() {

    }

    public static MoPubStarkInit getInstance() {
        if (instance == null) {
            synchronized (MoPubStarkInit.class) {
                if (instance == null) {
                    instance = new MoPubStarkInit();
                }
            }
        }
        return instance;
    }


    public void init(final Context context, String unitID) {

        if (TextUtils.isEmpty(unitID)) {

            if (DEBUG) {
                Log.d(TAG, "moPubInit: activity is null or unitID is empty");
            }
            return;
        }

        Map<String, String> flurryConfiguration = new HashMap<>();
//        flurryConfiguration.put(FlurryAgentWrapper.PARAM_API_KEY, "9MK75BWPKTP3Y948XQHH");
        Map<String, String> unityConfiguration = new HashMap<>();
        unityConfiguration.put("gameId", "500018875");
        Map<String, String> ironSourceConfiguration = new HashMap<>();
        ironSourceConfiguration.put("applicationKey", "8e9b2c85");

        Map<String, String> tapjoyConfiguration = new HashMap<>();
        tapjoyConfiguration.put("sdkKey", "kdgKHKFcRpC6PbtVIx1K0wECTHEu2UU7VOxCnkbmVc3qWkspxKm3YVshdfrn");

        Map<String, String> chartboostConfiguration = new HashMap<>();
        chartboostConfiguration.put("appId", "5ca473038a77330bcd69b303");
        chartboostConfiguration.put("appSignature", "1f80c147c27cfd1b0469628647e62cee43c25c60");

        Map<String, String> adColonyConfiguration = new HashMap<>();
        adColonyConfiguration.put("appId", "app92e3853893a04eb4a8");
        adColonyConfiguration.put("clientOptions", "filemagic");

        Map<String, String> vungleConfiguration = new HashMap<>();
        vungleConfiguration.put("appId", "5ca4a649a4ab3200182ce273");

        SdkConfiguration sdkConfiguration = new SdkConfiguration.
                Builder(unitID)
                .withAdditionalNetwork(FacebookAdapterConfiguration.class.getName())
                .withAdditionalNetwork(GooglePlayServicesAdapterConfiguration.class.getName())
//                .withAdditionalNetwork(AppLovinAdapterConfiguration.class.getName())
//                .withMediatedNetworkConfiguration(TapjoyAdapterConfiguration.class.getName(),tapjoyConfiguration)
//                .withMediatedNetworkConfiguration(ChartboostAdapterConfiguration.class.getName(),chartboostConfiguration)
//                .withMediatedNetworkConfiguration(AdColonyAdapterConfiguration.class.getName(),adColonyConfiguration)
//                .withMediatedNetworkConfiguration(VungleAdapterConfiguration.class.getName(),vungleConfiguration)
//                .withMediatedNetworkConfiguration(FlurryAdapterConfiguration.class.getName(),flurryConfiguration)
//                .withMediatedNetworkConfiguration(UnityAdsAdapterConfiguration.class.getName(),unityConfiguration)
//                .withMediatedNetworkConfiguration(IronSourceAdapterConfiguration.class.getName(),ironSourceConfiguration)
                .build();
        MoPub.initializeSdk(context, sdkConfiguration, new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (DEBUG) {
                            Log.d(TAG, "onInitializationFinished: onInitializationFinished and after one minute");
                        }
                        isInitAfterOneMinute = true;
                        setChanged();
                        notifyObservers();
                    }
                }, DateUtils.SECOND_IN_MILLIS);
            }
        });
    }

    public void addObservers(Observer observer) {
        addObserver(observer);
    }

    public IAllowLoaderAdListener getAllowLoaderAdListener() {
        return allowLoaderAdListener;
    }

    public void setAllowLoaderAdListener(IAllowLoaderAdListener allowLoaderAdListener) {
        this.allowLoaderAdListener = allowLoaderAdListener;
    }
}
