package com.ads.lib.splash.model;

import com.ads.lib.mediation.bean.InterstitialWrapperAd;
import com.ads.lib.mediation.bean.RewardTerm;
import com.ads.lib.mediation.loader.interstitial.InterstitialWrapperEventListener;

import org.saturn.splash.loader.model.InterstitialAd;

public class SplashInterstitialAd extends InterstitialAd<InterstitialWrapperAd> {


    public SplashInterstitialAd(InterstitialWrapperAd targetAd) {
        super(targetAd);
        targetAd.setEventListener(new InterstitialWrapperEventListener() {
            @Override
            public void onRewarded(RewardTerm rewardTerm) {

            }

            @Override
            public void onAdClosed() {
                if (mEventListener != null) {
                    mEventListener.onAdClosed();
                }
            }

            @Override
            public void onAdImpressed() {
                if (mEventListener != null) {
                    mEventListener.onImpression();
                }
            }

            @Override
            public void onAdClicked() {
                if (mEventListener != null) {
                    mEventListener.onClicked();
                }
            }
        });
    }

    @Override
    public void show() {
        mTargetAd.show();
    }

    @Override
    public boolean isDestoried() {
        return mTargetAd.isDestroyed();
    }

    @Override
    public boolean isExpired() {
        return mTargetAd.isExpired();
    }

    @Override
    public boolean isRecoredClicked() {
        return false;
    }

    @Override
    public boolean isRecoredImpression() {
        return false;
    }

    @Override
    public void destory() {
        mTargetAd.destroy();
    }

    @Override
    public String getPlacementId() {
        return mTargetAd.getPlacementId();
    }

    @Override
    public String getPositionId() {
        return mTargetAd.getPositionId();
    }

    @Override
    public String getPoolPositionId() {
        return mTargetAd.getPoolPositionId();
    }
}
