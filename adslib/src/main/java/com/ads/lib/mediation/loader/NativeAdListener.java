package com.ads.lib.mediation;

import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.NativeAd;

public interface NativeAdListener {

    void onAdFail(AdErrorCode adErrorCode);

    void onAdLoaded(NativeAd nativeAd);
}
