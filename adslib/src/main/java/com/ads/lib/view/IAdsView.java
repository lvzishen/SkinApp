package com.ads.lib.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.mediation.bean.NativeViewBinder;
import com.ads.lib.mediation.widget.AdIconView;
import com.ads.lib.mediation.widget.NativeMediaView;

/**
 * 广告View的抽象接口
 * Created by yeguolong on 17-6-20.
 */
public interface IAdsView {

    Context getContext();

    View getRootAdsView();

    View getNativeAdsView();

    NativeViewBinder getViewBinder(NativeAd nativeAd);

    int getLogoId();

    int getIconId();

    int getTitleId();

    int getDescId();

    int getBtnId();

    int getChoiceId();

    int getImageId();

    AdsLogoView getLogo();

    NativeMediaView getImage();

    AdIconView getIcon();

    FrameLayout getChoice();

    void setTitle(CharSequence text);

    void setDesc(CharSequence text);

    void setBtnText(CharSequence text);

    void setLogoVisible(boolean visible);

    void setVisibility(int visibility);

    boolean isShown();

    void reset();

    View getBannerView();

}
