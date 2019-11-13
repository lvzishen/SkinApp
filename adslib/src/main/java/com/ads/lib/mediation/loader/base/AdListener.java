package com.ads.lib.mediation.loader.base;

import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.BaseAd;
import com.ads.lib.mediation.bean.NativeAd;

public interface AdListener<AD extends BaseAd> {

    void onAdFail(AdErrorCode adErrorCode);

    void onAdLoaded(AD ad);

}
