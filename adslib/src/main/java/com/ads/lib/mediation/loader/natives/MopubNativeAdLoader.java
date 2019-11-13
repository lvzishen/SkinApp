package com.ads.lib.mediation.loader.natives;

import android.content.Context;

import com.ads.lib.adapter.MopubNative;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.helper.LogHelper;
import com.ads.lib.util.AllowLoadUtils;

public class MopubNativeAdLoader implements INativeAdLoader {

    private static final String TAG = "MopubNativeAdLoader";
    private MopubNative mopubNative;
    private String unitId;
    private String positionId;
    private NativeAdListener listener;

    public MopubNativeAdLoader(Context context, String unitId, String placementID, String positionId) {
        mopubNative = new MopubNative(context, unitId, placementID,positionId);
        this.unitId = unitId;
        this.positionId = positionId;
    }

    @Override
    public void setAdListener(NativeAdListener listener) {
        this.listener = listener;
        mopubNative.setAdListener(listener);
    }

    @Override
    public void destory() {
        mopubNative.destroy();
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
        mopubNative.loadAd();
    }
}
