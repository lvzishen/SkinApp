package com.ads.lib;

/**
 * All ads-loading related callbacks are listed in this interface
 *
 * Created by yeguolong on 17-6-30.
 */
public interface AdsLoaderCallback {

    // 真正请求广告时给的回掉
    void onRealLoad();

    // 广告请求结果
    void onLoadResult(boolean isAdLoaded, String unitId, String positionId);

}
