package com.ads.lib.mediation.helper;

import android.app.Activity;
import android.content.Context;

import com.ads.lib.commen.AdLifecyclerManager;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.InterstitialWrapperAd;
import com.ads.lib.mediation.cache.CacheManager;
import com.ads.lib.mediation.loader.interstitial.InterstitialWrapperAdAdListener;
import com.ads.lib.mediation.loader.interstitial.InterstitialWrapperAdLoader;
import com.ads.lib.prop.AdCacheProp;

public class InterstitialAdHelper {


    private int retryCount = -1;

    /**
     * 获取插屏广告
     *
     * @param unitId
     * @param positionId
     * @return 如果缓存池内有count个广告
     */
    public InterstitialWrapperAd getInterstitialAd(Context context, String unitId, String positionId) {
        return CacheManager.getInstance(context).dequeueInterstitialWrapperAd(unitId, positionId);
    }

    /**
     * 预加载插屏广告 直到填充满位置
     *
     * @param context
     * @param unitId
     * @param positionId
     * @param count
     */
    public void preloadInterstitialAd(final Context context, final String unitId, final String positionId, final int count) {
        if (context == null || context.getApplicationContext() == null) {
            return;
        }
        final Context ctx = context.getApplicationContext();
        if (retryCount == -1) {
            retryCount = AdCacheProp.getInstance(context).getRetry(positionId);
        }
        final Activity fixedActivity = AdLifecyclerManager.getInstance(ctx).getFixedActivity();
        if(fixedActivity == null){
            return;
        }
        InterstitialWrapperAdLoader loader = new InterstitialWrapperAdLoader(fixedActivity, unitId, positionId);
        loader.setAdListener(new InterstitialWrapperAdAdListener() {
            @Override
            public void onAdFail(AdErrorCode adErrorCode) {
                if (CacheManager.getInstance(ctx).getTempCacheAdCount(unitId, positionId) < count && retryCount > 0) {
                    retryCount--;
                    preloadInterstitialAd(fixedActivity, unitId, positionId, count);
                } else {
                    CacheManager.getInstance(ctx).flush(unitId, positionId);
                }
            }

            @Override
            public void onAdLoaded(InterstitialWrapperAd ad) {
                CacheManager.getInstance(ctx).enqueueAd2TempCache(positionId, unitId, ad);
                if (CacheManager.getInstance(ctx).getTempCacheAdCount(unitId, positionId) < count) {
                    preloadInterstitialAd(fixedActivity, unitId, positionId, count);
                } else {
                    CacheManager.getInstance(ctx).flush(positionId, unitId);
                }
            }
        });

        loader.load();

    }


}
