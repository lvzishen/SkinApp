package com.ads.lib.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ads.lib.ModuleConfig;
import com.ads.lib.mediation.widget.NativeMediaView;
import com.ads.lib.mediation.widget.NativeStaticViewHolder;
import com.bumptech.glide.Glide;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.mopub.common.util.Drawables;
import com.mopub.common.util.ImageUtils;
import com.mopub.nativeads.FacebookNative;
import com.mopub.nativeads.GooglePlayServicesNative;
import com.mopub.nativeads.NativeAd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;

class AdChoicesBinder {

    public static final String TAG = "AdChoicesBinder";
    public static final boolean DEBUG = ModuleConfig.DEBUG;
    private Context context;
    private NativeStaticViewHolder staticNativeViewHolder;
    private NativeAd mMoupubNativeAd;
    private boolean isCheckView = false;
    private HashSet<AdElementType> validViewMap;

    AdChoicesBinder(Context context,
                    NativeStaticViewHolder staticNativeViewHolder,
                    NativeAd nativeAd) {
        this.context = context;
        this.staticNativeViewHolder = staticNativeViewHolder;
        this.mMoupubNativeAd = nativeAd;
    }

    void mopubNativePrepare(String urlBanner, String iconUrl) {

        if (staticNativeViewHolder.adChoiceViewGroup != null
                && staticNativeViewHolder.adChoiceViewGroup instanceof FrameLayout) {
            View adChoiceView = new ImageView(staticNativeViewHolder.adChoiceViewGroup.getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(35, 35);
            layoutParams.rightMargin = 10;
            layoutParams.topMargin = 10;
            ((ImageView) adChoiceView).setImageDrawable(Drawables.NATIVE_PRIVACY_INFORMATION_ICON.createDrawable(context));
            addAdChoiceView(adChoiceView, layoutParams);
        }
        if (staticNativeViewHolder.mediaView != null) {
            staticNativeViewHolder.mediaView.addMediaView(staticNativeViewHolder, urlBanner);
        }

        if (staticNativeViewHolder.iconImageView != null) {
            staticNativeViewHolder.iconImageView.addAIconView(staticNativeViewHolder, iconUrl);
        }

    }

    void normalNativePrepare(String urlBanner, String iconUrl) {

        if (staticNativeViewHolder.mediaView != null) {
            staticNativeViewHolder.mediaView.addMediaView(staticNativeViewHolder, urlBanner);
        }

        if (staticNativeViewHolder.iconImageView != null) {
            staticNativeViewHolder.iconImageView.addAIconView(staticNativeViewHolder, iconUrl);
        }

    }


    void facebookNativePrepare(FacebookNative.FacebookVideoEnabledNativeAd baseNativeAd, List<View> clickableViews) {

        MediaView mediaView = null;
        com.facebook.ads.AdIconView adIconView = null;
        if (baseNativeAd.getFacebookNativeAd() != null) {

            if (staticNativeViewHolder.adChoiceViewGroup != null
                    && staticNativeViewHolder.adChoiceViewGroup instanceof FrameLayout) {

                View adChoiceView = new AdChoicesView(staticNativeViewHolder.adChoiceViewGroup.getContext()
                        , baseNativeAd.getFacebookNativeAd(), true);
                addAdChoiceView(adChoiceView, null);
            }
        }

        if (staticNativeViewHolder.mediaView != null) {
            mediaView = new MediaView(context);
            staticNativeViewHolder.mediaView.addMediaView(mediaView, staticNativeViewHolder, null);
//            mediaView.setNativeAd(baseNativeAd.dequeueNativeAd());
        }

        if (staticNativeViewHolder.iconImageView != null) {
            adIconView = new com.facebook.ads.AdIconView(context);
            staticNativeViewHolder.iconImageView.addAIconView(adIconView, staticNativeViewHolder, null);
        }

//        if (mediaView != null && adIconView != null) {
            baseNativeAd.registerChildViewsForInteraction(staticNativeViewHolder.mainView, mediaView, adIconView,clickableViews);
//        } else {
//            if (DEBUG) {
//                Log.d(TAG, "facebookNativePrepare:mediaView or adIconView is null");
//            }
//        }
    }

    void nativeAdPrepare(String urlBanner, String iconUrl) {
        if (staticNativeViewHolder.mediaView != null) {
            staticNativeViewHolder.mediaView.addMediaView(null, staticNativeViewHolder, urlBanner);
        }

        if (staticNativeViewHolder.iconImageView != null) {
            staticNativeViewHolder.iconImageView.addAIconView(staticNativeViewHolder, iconUrl);
        }

    }

    void bannerAdPrepare() {
        if (staticNativeViewHolder.adChoiceViewGroup != null
                && staticNativeViewHolder.adChoiceViewGroup instanceof FrameLayout) {
            mMoupubNativeAd.prepare(staticNativeViewHolder.adChoiceViewGroup);
        }
    }

    void admobNativePrepare(GooglePlayServicesNative.GooglePlayServicesNativeAd baseNativeAd,
                            List<View> views, String urlBanner, String iconUrl) {

        if (views == null || views.isEmpty()) {
            isCheckView = true;
        } else {
            isCheckValidClickView(NativeStaticViewHolder.adElementViewMap, views);
        }


        View mainView = staticNativeViewHolder.mainView;

        if (mainView != null && mainView instanceof ViewGroup) {
            ViewGroup outerFrame = (ViewGroup) mainView;
            View actualView = outerFrame.getChildAt(0);

            UnifiedNativeAd unifiedNativeAd = baseNativeAd.getUnifiedNativeAd();
            UnifiedNativeAdView admobNativeAdView = new UnifiedNativeAdView(context);

            FrameLayout.LayoutParams googleNativeAdViewParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            admobNativeAdView.setLayoutParams(googleNativeAdViewParams);

            // 从原布局中把整个广告卡片移出来。
            outerFrame.removeView(actualView);
            actualView.setTag(ModuleConfig.ID_NATIVE_VIEW);
            admobNativeAdView.setTag(ModuleConfig.ID_GOOGLE_NATIVE_VIEW);
            admobNativeAdView.addView(actualView);
            outerFrame.addView(admobNativeAdView);


            if (staticNativeViewHolder.mediaView != null) {
                staticNativeViewHolder.mediaView.removeAllViews();
                if (isCheckView || validViewMap == null || validViewMap.isEmpty() || (validViewMap.contains(AdElementType.MEDIA_VIEW))) {
                    com.google.android.gms.ads.formats.MediaView mediaView = new com.google.android.gms.ads.formats.MediaView(context);
                    mediaView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    staticNativeViewHolder.mediaView.addMediaView(mediaView, staticNativeViewHolder, null);
                    admobNativeAdView.setMediaView(mediaView);
                }else {
                    String mainImageUrl = ((GooglePlayServicesNative.GooglePlayServicesNativeAd) mMoupubNativeAd.getBaseNativeAd()).getMainImageUrl();
                    if(!TextUtils.isEmpty(mainImageUrl)){
                        ImageView imageView = new ImageView(context);
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        staticNativeViewHolder.mediaView.addView(imageView);
                        Glide.with(context).load(mainImageUrl).into(imageView);
                    }
                }

            }

            if (staticNativeViewHolder.iconImageView != null) {
                staticNativeViewHolder.iconImageView.addAIconView(staticNativeViewHolder, iconUrl);
            }
            setUnifiedNativeAdViewAdData(staticNativeViewHolder, admobNativeAdView);
            admobNativeAdView.setNativeAd(unifiedNativeAd);
        }

    }

    private boolean isCheckValidClickView(HashMap<Integer, NativeStaticViewHolder.AdElementEntry> targetViews, List<View> validViews) {
        if (targetViews.isEmpty()) {
            return false;
        }
        validViewMap = new HashSet<AdElementType>();
        for (View validView : validViews) {

            if (validView instanceof NativeMediaView) {
                NativeStaticViewHolder.AdElementEntry adElementEntry = targetViews.get(validView.getId());
                if (adElementEntry != null) {
                    validViewMap.add(adElementEntry.type);
                }
                continue;
            }

//            if (validView instanceof ViewGroup) {
//                continue;
//            }

            NativeStaticViewHolder.AdElementEntry adElementEntry = targetViews.get(validView.getId());
            if (adElementEntry != null) {
                validViewMap.add(adElementEntry.type);
            }
        }
        if (validViewMap.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }


    private void setUnifiedNativeAdViewAdData(@NonNull NativeStaticViewHolder staticNativeViewHolder, UnifiedNativeAdView unifiedNativeAdView) {
        if (isCheckView || validViewMap == null || validViewMap.isEmpty()) {
            unifiedNativeAdView.setHeadlineView(staticNativeViewHolder.titleView);
            unifiedNativeAdView.setBodyView(staticNativeViewHolder.textView);
            unifiedNativeAdView.setCallToActionView(staticNativeViewHolder.callToActionView);
            unifiedNativeAdView.setIconView(staticNativeViewHolder.iconImageView);
        } else {
            if (validViewMap.contains(AdElementType.TITLE)) {
                unifiedNativeAdView.setHeadlineView(staticNativeViewHolder.titleView);
            }
            if (validViewMap.contains(AdElementType.TEXT)) {
                unifiedNativeAdView.setBodyView(staticNativeViewHolder.textView);
            }
            if (validViewMap.contains(AdElementType.CALL_TO_ACTION)) {
                unifiedNativeAdView.setCallToActionView(staticNativeViewHolder.callToActionView);
            }
            if (validViewMap.contains(AdElementType.ICON_IMAGE)) {
                unifiedNativeAdView.setIconView(staticNativeViewHolder.iconImageView);
            }
        }
    }


    private void addAdChoiceView(View adChoiceView, FrameLayout.LayoutParams layoutParams) {
        if (adChoiceView != null && staticNativeViewHolder.adChoiceViewGroup != null) {
            ViewGroup rootView = staticNativeViewHolder.adChoiceViewGroup;
            rootView.removeAllViews();
            if (layoutParams != null) {
                rootView.addView(adChoiceView, layoutParams);
            } else {
                rootView.addView(adChoiceView);
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) adChoiceView.getLayoutParams();
            params.gravity = Gravity.RIGHT | Gravity.END;
            staticNativeViewHolder.adChoiceViewGroup.requestLayout();
        }
    }


}
