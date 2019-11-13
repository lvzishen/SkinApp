package com.ads.lib.mediation.loader.natives;

import android.content.Context;

public class MopubStripBannerAdLoader extends AbsMopubBannerAdLoader {


    public MopubStripBannerAdLoader(Context context, String unitId, String placementID, String positionId) {
        super(context, unitId, placementID,positionId);
    }

    @Override
    public float getWidth() {
        return 320F;
    }

    @Override
    public float getHeight() {
        return 50F;
    }


    @Override
    public void destory() {
        mMopubBanner.destroy();
    }
}
