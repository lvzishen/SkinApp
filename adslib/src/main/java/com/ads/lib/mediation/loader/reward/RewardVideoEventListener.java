package com.ads.lib.mediation.loader.reward;

import com.ads.lib.mediation.bean.RewardTerm;
import com.ads.lib.mediation.loader.interstitial.InterstitialEventListener;

public interface RewardVideoEventListener extends InterstitialEventListener {

    void onRewarded(RewardTerm rewardTerm);

}
