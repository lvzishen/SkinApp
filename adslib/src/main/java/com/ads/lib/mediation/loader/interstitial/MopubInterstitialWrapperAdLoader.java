package com.ads.lib.mediation.loader.interstitial;

import android.app.Activity;

import com.ads.lib.adapter.MopubInterstitial;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.helper.LogHelper;
import com.ads.lib.util.AllowLoadUtils;

public class MopubInterstitialWrapperAdLoader implements IInterstitialWrapperAdAdLoader {

    private static final String TAG = "MopubInterstitialWrapperAdLoader";
    private MopubInterstitial moPubInterstitial;

    private String unitId;
    private String positionId;
    private InterstitialWrapperAdAdListener listener;
    public MopubInterstitialWrapperAdLoader(Activity activity, String unitId, String placementID, String positionId) {
        this.unitId = unitId;
        this.positionId = positionId;
        moPubInterstitial = new MopubInterstitial(activity,unitId,placementID,positionId);
    }

    @Override
    public void destroy() {
        moPubInterstitial.destroy();
    }

    @Override
    public void setAdListener(InterstitialWrapperAdAdListener listener) {
        this.listener = listener;
        moPubInterstitial.setAdListener(listener);
    }

    @Override
    public void destory() {
        moPubInterstitial.destroy();
    }

    @Override
    public void load() {
        if(!AllowLoadUtils.isAllowLoadAd(unitId,positionId)){
            if(listener != null){
                listener.onAdFail(AdErrorCode.AD_POSITION_CLOSED);
            }
            LogHelper.logD(TAG,"#load ad position is closed");
            return;
        }
        moPubInterstitial.loadAd();
    }

    @Override
    public boolean isLoading() {
        return moPubInterstitial.isLoading();
    }


}
