package com.ads.lib.mediation.loader.interstitial;

import com.ads.lib.mediation.loader.base.IAdLoader;

public interface IInterstitialWrapperAdAdLoader extends IAdLoader<InterstitialWrapperAdAdListener> {

    void destroy();

    boolean isLoading();

}
