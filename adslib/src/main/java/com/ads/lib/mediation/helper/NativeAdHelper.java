package com.ads.lib.mediation.helper;

import android.content.Context;

import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.mediation.cache.CacheManager;
import com.ads.lib.mediation.loader.natives.NativeAdListener;
import com.ads.lib.mediation.loader.natives.NativeAdLoader;
import com.ads.lib.prop.AdCacheProp;

public class NativeAdHelper {

    private int retryCount = -1;

    /**
     * 获取原生广告
     *
     * @param unitId
     * @param positionId
     * @return 如果缓存池内有count个广告
     */
    public NativeAd getNativeAd(Context context,String unitId, String positionId) {
        return CacheManager.getInstance(context).dequeueNativeAd(unitId, positionId);
    }

    /**
     * 预加载原生广告 直到填充满位置
     *
     * @param context
     * @param unitId 广告位unitId
     * @param positionId 广告位positionId
     * @param count 要预加载的缓存数 最多不能超过缓存池的预加载数
     */
    public void preloadNativeAd(final Context context, final String unitId, final String positionId, final int count) {
        if (retryCount == -1){
            retryCount = AdCacheProp.getInstance(context).getRetry(positionId);
        }
        NativeAdLoader nativeAdLoader = new NativeAdLoader(context, unitId, positionId);
        nativeAdLoader.setAdListener(new NativeAdListener() {
            @Override
            public void onAdFail(AdErrorCode adErrorCode) {
                if (CacheManager.getInstance(context).getAdCount(unitId, positionId) < count && retryCount > 0) {
                    retryCount --;
                    preloadNativeAd(context, unitId, positionId, count);
                }
            }

            @Override
            public void onAdLoaded(NativeAd ad) {
                CacheManager.getInstance(context).enqueueAd(positionId, unitId, ad);
                if (CacheManager.getInstance(context).getAdCount(unitId, positionId) < count) {
                    preloadNativeAd(context, unitId, positionId, count);
                }
            }
        });
        nativeAdLoader.load();
    }


}
