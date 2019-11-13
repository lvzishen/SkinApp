package com.ads.lib.mediation.loader;

import android.app.Activity;
import android.content.Context;

import com.ads.lib.mediation.loader.interstitial.IInterstitialWrapperAdAdLoader;
import com.ads.lib.mediation.loader.interstitial.MopubInterstitialWrapperAdLoader;
import com.ads.lib.mediation.loader.natives.INativeAdLoader;
import com.ads.lib.mediation.loader.natives.MopubNativeAdLoader;
import com.ads.lib.mediation.loader.natives.MopubRectangleBannerAdLoader;
import com.ads.lib.mediation.loader.natives.MopubStripBannerAdLoader;

import androidx.annotation.NonNull;

public class LoaderFactory {


    public static INativeAdLoader getNativeLoader(@NonNull String className, Context context, String unitId, String placementID, String positionId) {
        if (className.equals(MopubNativeAdLoader.class.getSimpleName())) {
            return new MopubNativeAdLoader(context, unitId, placementID,positionId);
        } else if (className.equals(MopubRectangleBannerAdLoader.class.getSimpleName())) {
            return new MopubRectangleBannerAdLoader(context, unitId, placementID,positionId);
        } else if (className.equals(MopubStripBannerAdLoader.class.getSimpleName())) {
            return new MopubStripBannerAdLoader(context, unitId, placementID,positionId);
        }
        return null;
    }


    public static IInterstitialWrapperAdAdLoader getInterstitialWrapperLoader(@NonNull String className, Activity context, String unitId, String placementID, String positionId) {
        if (className.equals(MopubInterstitialWrapperAdLoader.class.getSimpleName())) {
            return new MopubInterstitialWrapperAdLoader(context, unitId, placementID,positionId);
        }
        return null;
    }


}
