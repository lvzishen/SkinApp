package com.ads.lib.loader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.ads.lib.AdUnitId;
import com.ads.lib.AdsLoaderCallback;
import com.ads.lib.commen.AdLifecyclerManager;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.InterstitialWrapperAd;
import com.ads.lib.mediation.cache.CacheManager;
import com.ads.lib.mediation.helper.LogHelper;
import com.ads.lib.mediation.loader.interstitial.InterstitialWrapperAdAdListener;
import com.ads.lib.mediation.loader.interstitial.InterstitialWrapperAdLoader;
import com.ads.lib.prop.AdPositionIdProp;
import com.baselib.cloud.CloudPropertyManager;
import com.baselib.ui.CommonConstants;
import com.hotvideo.config.GlobalConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

/**
 * A singleton class to load interstitial ads
 * <p>
 * Created by Eric Tjitra on 7/12/2017.
 */

public class InterstitialAdsLoader {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "InterstitialAdsLoader";

    private static final long DEFAULT_LOAD_TIMEOUT = 20000L; // 20 seconds
    private static final long TIMEOUT_INTERVAL = 60 * 60 * 1000L; // 1 hour

    /**
     * 加载广告
     */
    public static final int MSG_LOAD = 1;

    private static final Object sLock = new Object();
    private static HashMap<Integer, InterstitialAdsLoader> sLoaders = new HashMap<Integer, InterstitialAdsLoader>();

    private Handler mUiHandler;
    private Handler mTaskHandler;
    private InterstitialWrapperAdLoader mLoader;
    private List<InterstitialWrapperAd> mAdWrapperList = new ArrayList<>();
    private List<AdsLoaderCallback> mCallbacksList = new ArrayList<>();

    private long mLastLoadTime;

    private static final int RETRY_COUNT = 2;
    private int retryCount;
//    private final HandlerThread mHandlerThread;


    @IntDef({CommonConstants.TYPE_RESULT_BOOST, CommonConstants.TYPE_RESULT_CPU,
            CommonConstants.TYPE_RESULT_JUNK, CommonConstants.TYPE_RESULT_BATTERY,
            CommonConstants.TYPE_RESULT_VIRUS, CommonConstants.TYPE_RESULT_NOTIFICATION_BOOST,
            CommonConstants.TYPE_FUNCTION_AD, CommonConstants.TYPE_APPUNINSTALL_AD,
            CommonConstants.HOME_INTERSTITIAL_RANGE_BIGADS, CommonConstants.TYPE_FLOATWINDOW_NETWORK_SPEED,
            CommonConstants.TYPE_RESULT_NOTIFICATION_CLEAN, CommonConstants.TYPE_RESULT_SUPPLEMENT, CommonConstants.TYPE_HOME_AD})

    private @interface InterstitialTypes {
    }

    public static InterstitialAdsLoader getInstance(Context context, /*@InterstitialTypes */int type) {
        if (type == CommonConstants.TYPE_FULL_SCAN) {
            type = CommonConstants.TYPE_RESULT_VIRUS;
        }
        switch (type) {
            case CommonConstants.TYPE_RESULT_BOOST:
            case CommonConstants.TYPE_FLOATWINDOW_NETWORK_SPEED:
            case CommonConstants.TYPE_RESULT_JUNK:
            case CommonConstants.OUT_POWER_DISCHARGE_INTERSTITIAL_OUT_BIGADS:
            case CommonConstants.TYPE_RESULT_BATTERY:
            case CommonConstants.TYPE_RESULT_VIRUS:
            case CommonConstants.TYPE_RESULT_CPU:
            case CommonConstants.TYPE_RESULT_NOTIFICATION_BOOST:
            case CommonConstants.TYPE_RESULT_NOTIFICATION_CLEAN:
            case CommonConstants.TYPE_RESULT_NOTIFICATION_SECURITY:
            case CommonConstants.TYPE_RESULT_WIFI_SCAN_NROMAL:
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
            case CommonConstants.OUT_STREAM_INTERSTITIAL_RANGE_OUT_BIGADS:
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
            case CommonConstants.OUT_POWER_CONNECT_INTERSTITIAL_OUT_BIGADS:
            case CommonConstants.TYPE_HOME_AD:
            case CommonConstants.TYPE_FUNCTION_AD:
            case CommonConstants.HOME_INTERSTITIAL_RANGE_BIGADS:
            case CommonConstants.TYPE_APPUNINSTALL_AD:
                synchronized (sLoaders) {
                    InterstitialAdsLoader loader = sLoaders.get(type);
                    if (loader != null && loader.shouldReload()) {
                        loader = null;
                    }
                    if (loader == null) {
                        loader = new InterstitialAdsLoader(context, type);
                        sLoaders.put(type, loader);
                    }
                    return loader;
                }
        }
        return null;
    }

    private int mType = -1;
    private Context mContext = null;

    private InterstitialAdsLoader(Context context, int type) {
        // No instantiation allowed to guarantee data integrity
        mType = type;
        init(context);
        mUiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_LOAD:
                        doRealLoad();
                        break;

                    default:
                }
            }
        };
//        mHandlerThread = new HandlerThread("task");
//        mHandlerThread.run();
//        mTaskHandler = new Handler(mHandlerThread.getLooper()){
//
//        };
    }

    private void init(Context context) {
        mContext = context;
        String unitId = getUnitId(context);

        if (DEBUG) {
            Log.d(TAG, "init | unitId: " + unitId);
            Log.d(TAG, "init | AdPositionId: " + getAdPositionId(context));
        }
        if (AdLifecyclerManager.getInstance(mContext).getFixedActivity() != null) {
            mLoader = new InterstitialWrapperAdLoader(AdLifecyclerManager.getInstance(mContext).getFixedActivity(), unitId, getAdPositionId(context));
        } else {
//            if (DEBUG) {
//                throw new NullPointerException("activity instance is empty!");
//            }
        }
    }

    private String getUnitId(Context context) {
        if (context == null)
            return null;
        String value = getDefaultValue();
        String key = getUnitIdKey();
        String a = CloudPropertyManager.getString(context,
                CloudPropertyManager.PATH_INTERSTITIAL_ADS_PROP,
                key,
                value);
        return CloudPropertyManager.getString(context,
                CloudPropertyManager.PATH_INTERSTITIAL_ADS_PROP,
                key,
                value);
    }


    private String getAdPositionId(Context context) {
        if (context == null)
            return null;
        String key = getUnitIdKey();

        return AdPositionIdProp.getInstance(context).getAdPositionIdByAdUnitId(key);
    }

    private String getPlacementId(Context context) {
        if (context == null)
            return null;

        String key = "interstitial_placement_id";
        String value = "ab1:ca-app-pub-8286695259495135/9059235327,an1:299692303841609_299713507172822,ab1:ca-app-pub-8286695259495135/8692403819";
        switch (mType) {
            case CommonConstants.TYPE_RESULT_BOOST:
                key = "interstitial_boost_placement_id";
                break;
            case CommonConstants.TYPE_HOME_AD:
                key = "interstitial_home_inter_placement_id";
                break;
            case CommonConstants.TYPE_FUNCTION_AD:
                key = "interstitial_function_inter_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_CPU:
                key = "interstitial_placement_id";
                break;
            case CommonConstants.TYPE_FLOATWINDOW_NETWORK_SPEED:
                key = "interstitial_net_placement_id";
                break;
            case CommonConstants.TYPE_APPUNINSTALL_AD:
                key = "interstitial_appuninstall_inter_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_JUNK:
                key = "interstitial_rubbish_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_BATTERY:
                key = "interstitial_batterysaver_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_VIRUS:
                key = "interstitial_antivirus_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_BOOST:
                key = "interstitial_notifyboost_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_CLEAN:
                key = "interstitial_notifycleaner_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_SECURITY:
                key = "interstitial_notifyprotectcleaner_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_WIFI_SCAN_NROMAL:
                key = "interstitial_wifi_scaner_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
                key = "interstitial_supplement_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
                key = "interstitial_hangedcall_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
                key = "interstitial_detachcharger_placement_id";
                break;
        }

        String res = CloudPropertyManager.getString(context,
                CloudPropertyManager.PATH_INTERSTITIAL_ADS_PROP,
                key,
                value);
        if (DEBUG) {
            Log.v(TAG, key + "  = " + res);
        }
        return res;
    }

    private String getNewPlacementId(Context context) {
        if (context == null)
            return null;

        String key = "interstitial_placement_id_firstday";
        String value = "ab1:ca-app-pub-8286695259495135/8940294755,an1:299692303841609_299704290507077,ab1:ca-app-pub-8286695259495135/5156962414";
        switch (mType) {
            case CommonConstants.TYPE_RESULT_BOOST:
                key = "interstitial_boost_placement_id_firstday";
                break;
            case CommonConstants.TYPE_HOME_AD:
                key = "interstitial_home_inter_placement_id_firstday";
                break;
            case CommonConstants.TYPE_FUNCTION_AD:
                key = "interstitial_function_inter_placement_id_firstday";
                break;
            case CommonConstants.TYPE_APPUNINSTALL_AD:
                key = "interstitial_appuninstall_inter_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_CPU:
                key = "interstitial_placement_id_firstday";
                break;
            case CommonConstants.TYPE_FLOATWINDOW_NETWORK_SPEED:
                key = "interstitial_net_placement_id";
                break;
            case CommonConstants.TYPE_RESULT_JUNK:
                key = "interstitial_rubbish_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_BATTERY:
                key = "interstitial_batterysaver_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_VIRUS:
                key = "interstitial_antivirus_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_BOOST:
                key = "interstitial_notifyboost_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_CLEAN:
                key = "interstitial_notifycleaner_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_SECURITY:
                key = "interstitial_notifyprotectcleaner_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_WIFI_SCAN_NROMAL:
                key = "interstitial_wifi_scaner_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
                key = "interstitial_supplement_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
                key = "interstitial_hangedcall_placement_id_firstday";
                break;
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
                key = "interstitial_detachcharger_placement_id_firstday";
                break;
        }

        String res = CloudPropertyManager.getString(context,
                CloudPropertyManager.PATH_INTERSTITIAL_ADS_PROP,
                key,
                value);
        if (DEBUG) {
            Log.v(TAG, "[new]" + key + "  = " + res);
        }
        return res;
    }

    private String getExpiryStrategy(Context context) {
        if (context == null)
            return null;

        String key = "interstitial_expiry_strategy";
        String value = "ab:120,an:120";
        switch (mType) {
            case CommonConstants.TYPE_RESULT_BOOST:
                key = "interstitial_boost_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_HOME_AD:
                key = "interstitial_home_inter_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_APPUNINSTALL_AD:
                key = "interstitial_appuninstall_inter_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_FUNCTION_AD:
                key = "interstitial_function_inter_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_RESULT_CPU:
                break;
            case CommonConstants.TYPE_FLOATWINDOW_NETWORK_SPEED:
                break;
            case CommonConstants.TYPE_RESULT_JUNK:
                key = "interstitial_rubbish_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_RESULT_BATTERY:
                key = "interstitial_batterysaver_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_RESULT_VIRUS:
                key = "interstitial_antivirus_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_BOOST:
                key = "interstitial_notifyboost_expiry_strategy";
                value = "ab:120,an:120";
            case CommonConstants.TYPE_RESULT_NOTIFICATION_CLEAN:
                key = "interstitial_notifycleaner_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_SECURITY:
                key = "interstitial_notifyprotectcleaner_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_RESULT_WIFI_SCAN_NROMAL:
                key = "interstitial_wifi_scaner_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
                key = "interstitial_supplement_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
                key = "interstitial_hangedcall_expiry_strategy";
                value = "ab:120,an:120";
                break;
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
                key = "interstitial_detachcharger_expiry_strategy";
                value = "ab:120,an:120";
                break;
        }

        return CloudPropertyManager.getString(context,
                CloudPropertyManager.PATH_INTERSTITIAL_ADS_PROP,
                key,
                value // Digits are in minutes, i.e. 120 minutes
        );
    }

    private static final String[] CONFIGS = new String[]{CloudPropertyManager.PATH_INTERSTITIAL_ADS_PROP};

    public String[] getDependencyConfigs() {
        return CONFIGS;
    }

    public boolean shouldReload() {
        return CloudPropertyManager.isDirty(mContext, getDependencyConfigs());
    }

    /**
     * The only load method publicly available for callers
     *
     * @param forceLoad {@code true} if this method should load a new Ad regardless of
     *                  how many ads are already available, or {@code false} otherwise
     */
    public void startLoad(final boolean forceLoad) {
        if (mLoader == null)
            return;

        // Check logic goes here
        int availableAds = getAvailableAdsCount();
        if (DEBUG) {
            Log.d(TAG, "startLoad | force:" + forceLoad + ", available:" + availableAds);
        }
        if (availableAds > 0) {
            LogHelper.logD(TAG, String.format("#startLoad stop , availableAds = [%d]", availableAds));
            return;
        }

        doRealLoad();
    }

    private void doRealLoad() {
        try {
            if (mLoader == null)
                return;

            mLastLoadTime = System.currentTimeMillis();

            notifyOnRealLoad();
            if (DEBUG) {
                Log.i(TAG, "*****************************************");
                Log.i(TAG, "*** LOADING INTERSTITIAL");
                Log.i(TAG, "*** Object: " + this);
                Log.i(TAG, "*****************************************");
            }
            final String unitId = getUnitId(mContext);
            final String adPositionId = getAdPositionId(mContext);
            mLoader.setAdListener(new InterstitialWrapperAdAdListener() {
                @Override
                public void onAdFail(AdErrorCode adErrorCode) {
                    if (DEBUG) {
                        Log.i(TAG, "onAdFailed: " + adErrorCode);
                    }
                    if (retryCount < RETRY_COUNT) {
                        ++retryCount;
                        switch (unitId) {
                            case "ESL-VirusRes-Inter-0010":
                                if (DEBUG) {
                                    Log.d(TAG, "retry ESL-VirusRes-Inter-0010");
                                }
                                mUiHandler.sendEmptyMessageDelayed(MSG_LOAD, 5000);
                                break;

                            default:
                                notifyLoadResult(false, unitId, adPositionId);
                        }
                    } else {
                        notifyLoadResult(false, unitId, adPositionId);
                    }
                }

                @Override
                public void onAdLoaded(InterstitialWrapperAd interstitialWrapperAd) {
                    if (DEBUG) {
                        Log.i(TAG, "onAdLoaded | adWrapper: " + interstitialWrapperAd);
                    }
                    if (interstitialWrapperAd == null)
                        return;

                    notifyLoadResult(true, unitId, adPositionId);
                    CacheManager.getInstance(mContext).enqueueAd(adPositionId, unitId, interstitialWrapperAd);
                }

            });
            mLoader.load();
        } catch (Exception e) {
            if (DEBUG) {
                Log.i(TAG, "load e " + e.toString());
            }
        }
    }

    public InterstitialWrapperAd popAds() {

        if (mContext == null) {
            return null;
        }
        return CacheManager.getInstance(mContext).dequeueInterstitialWrapperAd(getUnitId(mContext), getAdPositionId(mContext));
    }

    /**
     * 是否准备好广告
     *
     * @return
     */
    public boolean isReady() {
        return mAdWrapperList != null && mAdWrapperList.size() > 0;
    }

   /* private String getInterstitialType(InterstitialWrapperAd adWrapper) {
        if (adWrapper != null && adWrapper.isAdLoaded()) {
            return "ab1".equals(adWrapper.getBaseStaticaAdsWrapper().sourceTag) ?
                    "AdMob" : "an1".equals(adWrapper.getBaseStaticaAdsWrapper().sourceTag) ?
                    "Facebook" : "Unknown";
        }
        return null;
    }

    private void storeAdWrapper(InterstitialWrapperAd adWrapper) {
        if (!isAdReadyToBeShown(adWrapper))
            return;

        synchronized (sLock) {
            if (mAdWrapperList != null) {
                mAdWrapperList.add(adWrapper);
                if (DEBUG) {
                    Log.d(TAG, "Ad is stored | source: " + getInterstitialType(adWrapper));
                    Log.d(TAG, "Ad is stored | size now: " + mAdWrapperList.size());
                }
            }
        }
    }*/

    private boolean isAdReadyToBeShown(InterstitialWrapperAd interstitialWrapperAd) {
        if (interstitialWrapperAd == null || interstitialWrapperAd.isDisplayed() || interstitialWrapperAd.isExpired() || interstitialWrapperAd.isDestroyed())
            return false;

        return true;
    }

    public int getAvailableAdsCount() {
        if (mContext != null) {
            return CacheManager.getInstance(mContext).getAdCount(getUnitId(mContext), getAdPositionId(mContext));
        }
        return 0;
    }

    public void registerCallback(AdsLoaderCallback callback) {
        if (callback == null)
            return;

        synchronized (sLock) {
            if (mCallbacksList != null && !mCallbacksList.contains(callback)) {
                mCallbacksList.add(callback);
            }
        }
    }

    public void removeCallback(AdsLoaderCallback callback) {
        if (callback == null)
            return;

        synchronized (sLock) {
            if (mCallbacksList != null && mCallbacksList.contains(callback)) {
                mCallbacksList.remove(callback);
            }
        }
    }

    private void notifyOnRealLoad() {
        if (mUiHandler == null)
            return;

        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (sLock) {
                    if (mCallbacksList != null) {
                        for (AdsLoaderCallback callback : mCallbacksList) {
                            if (callback != null) {
                                callback.onRealLoad();
                            }
                        }
                    }
                }
            }
        });
    }

    private void notifyLoadResult(final boolean isAdLoaded, final String unitId, final String positionId) {
        if (mUiHandler == null)
            return;
        retryCount = 0;
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (sLock) {
                    if (mCallbacksList != null) {
                        for (AdsLoaderCallback callback : mCallbacksList) {
                            if (callback != null) {
                                callback.onLoadResult(isAdLoaded, unitId, positionId);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Call this in Activity's onDestroy
     *
     * @param adWrapper
     */
    public void removeAd(InterstitialWrapperAd adWrapper) {
        if (adWrapper != null) {
            if (DEBUG) {
                Log.d(TAG, "removing ad....");
                if (mAdWrapperList != null && !mAdWrapperList.isEmpty()) {
                    Log.d(TAG, "Removed Ad number: " + mAdWrapperList.indexOf(adWrapper));
                }
            }
        }
    }

    public void destroy() {
        if (mLoader == null || mAdWrapperList == null)
            return;

        mLoader.destroy();
        synchronized (sLock) {
            Iterator<InterstitialWrapperAd> itr = mAdWrapperList.iterator();
            while (itr.hasNext()) {
                InterstitialWrapperAd ad = itr.next();
                if (!isAdReadyToBeShown(ad)) {
                    mLoader.setAdListener(null);
                    removeAd(ad);
                    itr.remove();
                } else {

                }
            }
        }
    }

    private String getDefaultValue() {
        String value = "";
        switch (mType) {
            case CommonConstants.TYPE_RESULT_BOOST:
                value = "SAM_RAMRes_Interstitial";
                break;
            case CommonConstants.OUT_STREAM_INTERSTITIAL_RANGE_OUT_BIGADS:
                value = "SAM_MSNFeed_Interstitial";
                break;
            case CommonConstants.OUT_POWER_DISCHARGE_INTERSTITIAL_OUT_BIGADS:
                value = "SAM_Charge_Interstitial";
                break;
            case CommonConstants.HOME_INTERSTITIAL_RANGE_BIGADS:
                value = "SAM_Homepage_Interstitial";
                break;
            case CommonConstants.TYPE_HOME_AD:
                value = "SAM_ResBack_Interstitial";
                break;
            case CommonConstants.TYPE_FUNCTION_AD:
                value = "SAM_SecEntry_Interstitial";
                break;
            case CommonConstants.TYPE_APPUNINSTALL_AD:
                value = "ESL-BoostRes-Inter-0006";
                break;
            case CommonConstants.TYPE_RESULT_CPU:
                value = "SAM_CPU_Res_Interstitial";
                break;
            case CommonConstants.TYPE_FLOATWINDOW_NETWORK_SPEED:
                value = "SAM_SpeedLimit_Interstitial_V15";
                break;
            case CommonConstants.TYPE_RESULT_JUNK:
                value = "SAM_CleanRes_Interstitial";
                break;
            case CommonConstants.TYPE_RESULT_BATTERY:
                value = "SAM_ChargeSave_Res_Interstitial";
                break;
            case CommonConstants.TYPE_RESULT_VIRUS:
                value = "SAM_VirusRes_Interstitial";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_BOOST:
                value = "SAM_RAMRes_Interstitial";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_CLEAN:
                value = "SAM_NotifyClean_Interstitial";
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_SECURITY:
                value = "ESL-MsgSecCleRes-Inter-0001";
                break;
            case CommonConstants.TYPE_RESULT_WIFI_SCAN_NROMAL:
                value = "SE-WifiSecRes-Inter-0020";
                break;
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
                value = "SE-Respage-Nofill-Inter-0036";
                break;
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
                value = "SE-HangedCall-Backhome-Inter-0039";
                break;
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
                value = "FMG-Charging-Assistant-InterStitial-0309";
                break;
            default:
                break;
        }

        return value;
    }

    @NonNull
    private String getUnitIdKey() {
        String key = "";
        switch (mType) {
            case CommonConstants.TYPE_RESULT_BOOST:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_BOOST;
                break;
            case CommonConstants.OUT_STREAM_INTERSTITIAL_RANGE_OUT_BIGADS:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_OUT_STREAM;
                break;
            case CommonConstants.TYPE_HOME_AD:
                key = AdUnitId.UNIT_ID_KEY_HOMEINTER;
                break;
            case CommonConstants.HOME_INTERSTITIAL_RANGE_BIGADS:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_HOME_RANGE;
                break;
            case CommonConstants.TYPE_FUNCTION_AD:
                key = AdUnitId.UNIT_ID_KEY_FUNCTIONINTER;
                break;
            case CommonConstants.TYPE_APPUNINSTALL_AD:
                key = AdUnitId.UNIT_ID_KEY_APPINSTALLINTER;
                break;
            case CommonConstants.TYPE_RESULT_CPU:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_CPU;
                break;
            case CommonConstants.TYPE_FLOATWINDOW_NETWORK_SPEED:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_FLOATWINDOW_NETWORK_SPEED;
                break;
            case CommonConstants.TYPE_RESULT_JUNK:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_RUBBISH;
                break;
            case CommonConstants.TYPE_RESULT_BATTERY:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_BATTERYSAVER;
                break;
            case CommonConstants.TYPE_RESULT_VIRUS:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_ANTIVIRUS;
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_BOOST:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_NOTIFYBOOST;
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_CLEAN:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_NOTIFYCLEANER;
                break;
            case CommonConstants.TYPE_RESULT_NOTIFICATION_SECURITY:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_NOTIFYPROTECTCLEANER;
                break;
            case CommonConstants.TYPE_RESULT_WIFI_SCAN_NROMAL:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_WIFI_SCANNER;
                break;
            case CommonConstants.TYPE_RESULT_SUPPLEMENT:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_SUPPLEMENT;
                break;
            case CommonConstants.OUT_POWER_CONNECT_INTERSTITIAL_OUT_BIGADS:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_OUT_POWER_CONNECT;
                break;
            case CommonConstants.OUT_POWER_DISCHARGE_INTERSTITIAL_OUT_BIGADS:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_OUT_POWER_DISCHARGE;
                break;
            case CommonConstants.TYPE_RESULT_HANGEDCALL:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_HANGEDCALL;
                break;
            case CommonConstants.TYPE_RESULT_DETACHCHARGER:
                key = AdUnitId.UNIT_ID_KEY_INTERSTITIAL_DETACHCHARGER;
                break;
            default:
                break;
        }

        return key;
    }
}
