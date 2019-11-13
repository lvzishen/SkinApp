package com.ads.lib.util;

import com.ads.lib.init.MoPubStarkInit;
import com.ads.lib.mediation.config.IAllowLoaderAdListener;

public class AllowLoadUtils {


    /**
     * 是否允许请求广告
     * @param unitId
     * @param positionId
     * @return true 允许
     */
    public static boolean isAllowLoadAd(String unitId,String positionId) {
        IAllowLoaderAdListener allowLoaderAdListener = MoPubStarkInit.getInstance().getAllowLoaderAdListener();
        if(allowLoaderAdListener != null){
            return allowLoaderAdListener.isAllowLoaderAd(unitId, positionId);
        }
        return true;
    }

}
