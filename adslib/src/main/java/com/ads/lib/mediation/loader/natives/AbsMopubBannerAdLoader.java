package com.ads.lib.mediation.loader.natives;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ads.lib.adapter.MopubBanner;

public abstract class AbsMopubBannerAdLoader implements INativeAdLoader {

    protected MopubBanner mMopubBanner;
    protected Context mContext;

    public AbsMopubBannerAdLoader(Context context, String unitId, String placementID,String positionId) {
        mMopubBanner = new MopubBanner(context, unitId, placementID,positionId);
        this.mContext = context;
    }


    @Override
    public void setAdListener(NativeAdListener listener) {
        mMopubBanner.setAdListener(listener);
    }

    @Override
    public void load() {
        setSize();
        mMopubBanner.loadAd();
    }

    public abstract float getWidth();

    public abstract float getHeight();

    private void setSize() {
        float density = mContext.getResources().getDisplayMetrics().density;
        int w = (int) (getWidth() * density + 1.0F);
        int h = (int) (getHeight() * density + 1.0F);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(w, h);
        layoutParams.gravity = 1;
        mMopubBanner.setLayoutParams(layoutParams);
    }
}
