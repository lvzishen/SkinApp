package com.ads.lib.mediation.loader.interstitial;

import com.ads.lib.mediation.loader.natives.NativeEventListener;

public interface InterstitialEventListener extends NativeEventListener {

    void onAdClosed();

}
