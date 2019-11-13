package com.ads.lib.mediation.helper;

import android.content.Context;
import android.util.Log;

import com.ads.lib.mediation.bean.InterstitialWrapperAd;
import com.ads.lib.mediation.cache.CacheManager;
import com.ads.lib.prop.AdCacheProp;
import com.ads.lib.prop.AdIDMappingProp;
import com.ads.lib.trigger.EventTrigger;
import com.ads.lib.trigger.config.EventTriggerConfig;
import com.ads.lib.trigger.listener.EventListener;
import com.ads.lib.trigger.listener.EventType;
import com.hotvideo.config.GlobalConfig;

public class SharedPoolHelper {

    private static final String TAG = "SharedPoolHelper";
    private static final boolean DEBUG = GlobalConfig.DEBUG;

    private volatile static SharedPoolHelper mInstance;
    private Context mContext;
    private static final String POOL_POSITION = "148013281";

    private long lastRequestTime;

    private SharedPoolHelper(Context context) {
        mContext = context;
    }

    public void start(){
        EventTrigger eventTrigger = EventTrigger.getInstance(mContext);
        eventTrigger.setEventListener(new EventListener() {
            @Override
            public void onEventComming(EventType eventType) {
                Context appCtx = mContext.getApplicationContext();
                if(!checkInterval(appCtx)){
                    if(DEBUG){
                        Log.d(TAG, "#onEventComming Not in the request period");
                    }
                    return;
                }
                lastRequestTime = System.currentTimeMillis();
                int inventory = AdCacheProp.getInstance(appCtx).getInventory(POOL_POSITION);
                new InterstitialAdHelper().preloadInterstitialAd(appCtx, "SAM_OutAdd_Interstitial_Group", POOL_POSITION, inventory);
            }
        });
        eventTrigger.openAllTrigger();
    }

    private boolean checkInterval(Context context) {
        long requestInterval = EventTriggerConfig.getInstance(context).getRequestInterval();
        long interval = lastRequestTime + requestInterval;
        long currentTime = System.currentTimeMillis();
        if(currentTime > interval){
            return true;
        }
        return false;
    }


    public static SharedPoolHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SharedPoolHelper.class) {
                if (mInstance == null) {
                    mInstance = new SharedPoolHelper(context);
                }
            }
        }
        return mInstance;
    }


    /**
     * 获取插屏广告
     *
     * @param unitId
     * @param positionId
     * @return 如果缓存池内有count个广告
     */
    public InterstitialWrapperAd getInterstitialAd(Context context, String unitId, String positionId) {
        if (!checkPermission(positionId)) {
            if(DEBUG){
                Log.d(TAG, String.format("#getInterstitialAd unitId = [%s], positionId = [%s] is null", unitId, positionId));
            }
            return null;
        }
        return CacheManager.getInstance(context).dequeueInterstitialWrapperAd(unitId, POOL_POSITION);
    }

    private String convert2PoolPositionId(String positionId){
        return AdIDMappingProp.getInstance(mContext).getCachePoolPositionId(positionId);
    }

    private boolean checkPermission(String positionId) {
        switch (convert2PoolPositionId(positionId)) {
                //应用外充电插屏
            case "148013293":
                //应用外MSN信息流
            case "148013301":
                return true;
            default:
                return false;
        }
    }


}
