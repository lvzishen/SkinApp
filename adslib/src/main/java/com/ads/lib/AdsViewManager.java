package com.ads.lib;

import android.view.View;

import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.view.IAdsView;
import com.baselib.bitmap.BitmapLoader;
import com.baselib.bitmap.display.Displayer;


/**
 * Created by yeguolong on 17-6-20.
 */
public class AdsViewManager {

    public static void setupAdsData(IAdsView iAdsView, NativeAd nativeAd) {
        setupAdsData(iAdsView, nativeAd, true);
    }

    public static void setupAdsData(IAdsView iAdsView, NativeAd nativeAd, boolean loadDefaultImg) {
        setupAdsData(iAdsView, nativeAd, loadDefaultImg, null);
    }

    public static void setupAdsData(IAdsView iAdsView, NativeAd nativeAd, boolean loadDefaultImg, Displayer displayer) {
        if (nativeAd == null || iAdsView == null)
            return;
        nativeAd.prepare(iAdsView.getViewBinder(nativeAd));
        if(!nativeAd.isBanner) {
            iAdsView.getNativeAdsView().setVisibility(View.VISIBLE);
            View bannerView = iAdsView.getBannerView();
            if(bannerView!=null){
                bannerView.setVisibility(View.GONE);
            }
            BitmapLoader bitmapLoader = BitmapLoader.create(iAdsView.getContext());
            iAdsView.setTitle(nativeAd.getTitle());
            iAdsView.setDesc(nativeAd.getText());
            iAdsView.setBtnText(nativeAd.getCallToAction());
        }else{
            iAdsView.getNativeAdsView().setVisibility(View.GONE);
            iAdsView.getBannerView().setVisibility(View.VISIBLE);
        }

    }

}
