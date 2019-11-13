package com.ads.lib.mediation.bean;

import android.content.Context;

import com.ads.lib.adapter.ExpireTimeUtils;
import com.ads.lib.adapter.MopubInterstitial;
import com.ads.lib.log.AdStatisticLoggerX;
import com.ads.lib.log.LogEventUtilsTracking;
import com.ads.lib.mediation.loader.interstitial.InterstitialEventListener;
import com.ads.lib.mediation.loader.interstitial.InterstitialWrapperEventListener;
import com.ads.lib.util.PageUtil;

public class InterstitialWrapperAd extends BaseAd implements InterstitialEventListener {

    private MopubInterstitial interstitialAd;
    private InterstitialEventListener mEventListener;
    private long expiredTime;

    public boolean isAdLoaded() {
        if (isInterstitialEmpty()) {
            return false;
        }
        return interstitialAd.isLoaded();
    }


    public boolean isDisplayed() {
        if (isInterstitialEmpty()) {
            return true;
        }
        return interstitialAd.isDisplay;
    }

    @Override
    public boolean isExpired() {
        if (isInterstitialEmpty()) {
            return true;
        }
        return ExpireTimeUtils.isExpired(expiredTime);
    }

    public boolean isDestroyed() {
        if (isInterstitialEmpty()) {
            return true;
        }
        return interstitialAd.isDestroy;
    }

    public void setEventListener(InterstitialWrapperEventListener interstitialListener) {
        this.mEventListener = interstitialListener;
    }

    public void show() {
        if (isInterstitialEmpty()) {
            return;
        }
        interstitialAd.show();
    }

    public void destroy() {
        if (isInterstitialEmpty()) {
            return;
        }

        interstitialAd.destroy();
    }

    public MopubInterstitial getInterstitialAd() {
        return interstitialAd;
    }

    public void setInterstitialAd(MopubInterstitial interstitialAd) {
        this.interstitialAd = interstitialAd;
        if (!isInterstitialEmpty()) {
            interstitialAd.setInterstitialEventListener(this);
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setUnitId(String unitId) {
        this.mUnitId = unitId;
    }


    public void setPositionId(String positionId) {
        this.mPositionId = positionId;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public boolean isInterstitialEmpty() {
        if (interstitialAd == null) {
            return true;
        }
        return false;
    }

    @Override
    public void onAdClosed() {
        if (mEventListener != null) {
            mEventListener.onAdClosed();
            if (mContext != null && mUnitId != null && mPlacementID != null) {
                LogEventUtilsTracking.logAdClose(mContext, mUnitId, mPlacementID);
            }
        }
    }

    @Override
    public void onAdImpressed() {
        AdStatisticLoggerX.logImpression(PageUtil.getPageName(mPositionId),
                mPositionId,
                AdStatisticLoggerX.ACTION_AD_IMPRESSION,
                AdStatisticLoggerX.AD_SOURCE_MOPUB,
                AdStatisticLoggerX.AD_TYPE_INTERSTITIAL,
                AdStatisticLoggerX.AD_ACTION_VIEW,
                mPlacementID,
                getPoolPositionId());
        if (mEventListener != null) {
            mEventListener.onAdImpressed();
            if (mContext != null && mUnitId != null && mPlacementID != null) {
                LogEventUtilsTracking.logImpression(mContext, mUnitId, mPlacementID);
            }
        }
    }

    @Override
    public void onAdClicked() {
        AdStatisticLoggerX.logClick(PageUtil.getPageName(mPositionId),
                mPositionId,
                AdStatisticLoggerX.ACTION_AD_CLICK,
                AdStatisticLoggerX.AD_SOURCE_MOPUB,
                AdStatisticLoggerX.AD_TYPE_INTERSTITIAL,
                AdStatisticLoggerX.AD_ACTION_VIEW,
                mPlacementID,
                getPoolPositionId());
        if (mEventListener != null) {
            mEventListener.onAdClicked();
            if (mContext != null && mUnitId != null && mPlacementID != null) {
                LogEventUtilsTracking.logClick(mContext, mUnitId, mPlacementID);
            }
        }
    }
}
