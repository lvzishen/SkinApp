package com.ads.lib.mediation.bean;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.ads.lib.adapter.ExpireTimeUtils;
import com.ads.lib.log.AdStatisticLoggerX;
import com.ads.lib.log.LogEventUtilsTracking;
import com.ads.lib.mediation.loader.natives.NativeEventListener;
import com.ads.lib.mediation.widget.NativeStaticViewHolder;
import com.ads.lib.prop.AdIDMappingProp;
import com.ads.lib.util.PageUtil;

import java.util.List;

public class NativeAd extends BaseAd implements NativeEventListener {
    private String mMainImageUrl;
    private String mIconImageUrl;
    private String mCallToAction;
    private String mTitle;
    private String mText;
    public long mExpireTime;
    public boolean isBanner;
    public AbsNativeAdWrapper nativeAdWrapper;
    private NativeEventListener mEventListener;

    public NativeAd(Builder builder) {
        this.mMainImageUrl = builder.mMainImage;
        this.mIconImageUrl = builder.mIconImage;
        this.mCallToAction = builder.mCallToAction;
        this.mTitle = builder.mTitle;
        this.mText = builder.mText;
        this.mExpireTime = builder.mExpireTime;
        this.isBanner = builder.isBanner;
        this.nativeAdWrapper = builder.nativeAdWrapper;
        this.mContext = builder.mContext;
        this.mUnitId = builder.mUnitId;
        this.mPlacementID = builder.mPlacementID;
        this.mPositionId = builder.mPositionId;
        this.isFromCache = builder.isFromCache;
        if (nativeAdWrapper != null) {
            nativeAdWrapper.setNativeEventListener(this);
        }
    }

    public boolean isBanner() {
        return isBanner;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public String getCallToAction() {
        return mCallToAction;
    }

    @Override
    public boolean isExpired() {
        return ExpireTimeUtils.isExpired(mExpireTime);
    }

    public boolean isDestroyed() {
        if (nativeAdWrapper != null) {
            return nativeAdWrapper.isDestroyed();
        }
        return true;
    }

    public boolean isImpress() {
        if (nativeAdWrapper != null) {
            return nativeAdWrapper.isImpress();
        }
        return true;
    }

    public void setNativeEventListener(NativeEventListener eventListener) {
        this.mEventListener = eventListener;
    }

    public String getIconImageUrl() {
        return mIconImageUrl;
    }

    public void clear(View mainView) {
        if (nativeAdWrapper != null) {
            nativeAdWrapper.clear(mainView);
        }
    }

    public String getMainImageUrl() {
        return mMainImageUrl;
    }

    public String getUnitId() {
        return "";
    }


    @Override
    public void onAdImpressed() {
        AdStatisticLoggerX.logImpression(PageUtil.getPageName(mPositionId),
                mPositionId,
                AdStatisticLoggerX.ACTION_AD_IMPRESSION,
                AdStatisticLoggerX.AD_SOURCE_MOPUB,
                AdStatisticLoggerX.AD_TYPE_NATIVE,
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
                AdStatisticLoggerX.AD_TYPE_NATIVE,
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


    public static class Builder {
        String mMainImage;
        String mIconImage;
        String mCallToAction;
        String mTitle;
        String mText;
        long mExpireTime;
        boolean isBanner;
        AbsNativeAdWrapper nativeAdWrapper;
        Context mContext;
        String mUnitId;
        String mPlacementID;
        String mPositionId;
        boolean isFromCache;

        public Builder(AbsNativeAdWrapper nativeAdWrapper) {
            this.nativeAdWrapper = nativeAdWrapper;
        }

        public Builder setMainImageUrl(String mMainImage) {
            this.mMainImage = mMainImage;
            return this;
        }

        public Builder setIconImageUrl(String mIconImage) {
            this.mIconImage = mIconImage;
            return this;
        }

        public Builder setCallToAction(String mCallToAction) {
            this.mCallToAction = mCallToAction;
            return this;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder setText(String mText) {
            this.mText = mText;
            return this;
        }

        public Builder setmExpireTime(long mExpireTime) {
            this.mExpireTime = mExpireTime;
            return this;
        }

        public Builder isBanner(boolean isBanner) {
            this.isBanner = isBanner;
            return this;
        }

        public Builder setUnitId(String unitId) {
            this.mUnitId = unitId;
            return this;
        }

        public Builder setPlacementID(String placementID) {
            this.mPlacementID = placementID;
            return this;
        }

        public Builder setPositionId(String positionId) {
            this.mPositionId = positionId;
            return this;
        }

        public Builder setContext(Context context) {
            this.mContext = context;
            return this;
        }

        public NativeAd build() {
            return new NativeAd(this);
        }
    }

    public void prepare(NativeViewBinder viewBinder) {
        prepare(viewBinder, null);
    }

    public void prepare(NativeViewBinder viewBinder, List<View> viewList) {
        NativeStaticViewHolder nativeStaticViewHolder = NativeStaticViewHolder.fromViewBinder(viewBinder.mainView, viewBinder);
        if (nativeAdWrapper != null) {
            nativeAdWrapper.onPrepare(nativeStaticViewHolder, viewList);
        }
    }


}
