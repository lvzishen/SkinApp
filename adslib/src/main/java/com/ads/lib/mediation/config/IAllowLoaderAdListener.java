package com.ads.lib.mediation.config;


/**
 * Created by {@author kai}on 18-1-16.
 */

public interface IAllowLoaderAdListener {

    /**
     * 是否允许请求广告
     *
     * @param unitId
     * @param adpositionId
     * @return
     */
    boolean isAllowLoaderAd(String unitId, String adpositionId);
}
