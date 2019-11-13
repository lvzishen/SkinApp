package com.ads.lib.mediation.loader.natives;

import android.content.Context;

public class MopubRectangleBannerAdLoader extends AbsMopubBannerAdLoader {

    public MopubRectangleBannerAdLoader(Context context, String unitId, String placementID, String positionId) {
        super(context, unitId, placementID,positionId);
    }

    @Override
    public float getWidth() {
        return 300F;
    }

    @Override
    public float getHeight() {
        return 250F;
    }


    @Override
    public void destory() {
        mMopubBanner.destroy();
    }
}
