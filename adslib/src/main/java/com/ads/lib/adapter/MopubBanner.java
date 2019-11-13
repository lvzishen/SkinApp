package com.ads.lib.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ads.lib.ModuleConfig;
import com.ads.lib.base.BaseCustomNativeEventNetwork;
import com.ads.lib.base.ImpressionTracker;
import com.ads.lib.init.MoPubStarkInit;
import com.ads.lib.mediation.bean.AbsNativeAdWrapper;
import com.ads.lib.mediation.bean.AdErrorCode;
import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.mediation.loader.natives.NativeAdListener;
import com.ads.lib.mediation.loader.natives.NativeEventListener;
import com.ads.lib.mediation.widget.NativeStaticViewHolder;
import com.baselib.sp.SharedPref;
import com.mopub.common.MoPub;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.mopub.nativeads.ImpressionInterface;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;

/**
 * Created by zhaozhiwen on 2019/4/3.
 */
public class MopubBanner extends AbsNativeAdWrapper implements BaseCustomNativeEventNetwork, Observer {
    public static final String TAG = "MopubBanner";
    public static final boolean DEBUG = ModuleConfig.DEBUG;

    private boolean retryLoad;
    private boolean isLoading;
    private Context mContext;
    private String mPlacementID;
    private String mUnitId;
    private String mPositionId;
    private NativeAdListener bannerListener;
    private MoPubView moPubView;
    private boolean isLoadedBannerAd;
    private ViewGroup container;
    private ImpressionTracker mImpressionTracker;
    private NativeEventListener bannerEventListener;
    private FrameLayout.LayoutParams mLayoutParams;
    public boolean isDisplay;
    public boolean isDestroy;

    public MopubBanner(Context context, String unitId, String placementID, String positionId) {
        MoPubStarkInit.getInstance().addObservers(this);
        mContext = context;
        mPlacementID = placementID;
        mUnitId = unitId;
        this.mPositionId = positionId;
        moPubView = new MoPubView(mContext);
    }

    public void setAdListener(NativeAdListener listener) {
        bannerListener = listener;
    }

    @Override
    public boolean isExpired() {
        return ExpireTimeUtils.isExpired(mContext, SharedPref.SP_KEY_LAST_LOAD_SUCCESS_BANNER_TIME);
    }

    @Override
    public boolean isDestroyed() {
        return isDestroy;
    }

    @Override
    public boolean isImpress() {
        return isDisplay;
    }

    @Override
    public void clear(View mainView) {
        destroy();
    }

    @Override
    public void setNativeEventListener(NativeEventListener listener) {
        bannerEventListener = listener;
    }

    @Override
    public void destroy() {

        if (mImpressionTracker != null) {
            mImpressionTracker.clear();
        }

        if (container != null) {
            container.removeAllViews();
        }

        if (moPubView != null) {
            moPubView.destroy();
        }
        isDestroy = true;
    }

    @Override
    public void loadAd() {
        //初始化结束前，尝试请求过广告
        retryLoad = true;

        if (!MoPub.isSdkInitialized()) {
            MoPubStarkInit.getInstance().init(mContext, mPlacementID);

        } else {
            if (isLoading || !MoPubStarkInit.isInitAfterOneMinute) {

                if (DEBUG) {
                    Log.d(TAG, "loadAd: 正在请求过程中，或者初始化完成未超过1s，不再重复请求");
                }
                return;
            }
            startLoad();
        }
    }

    public void startLoad() {

        isDisplay = false;
        isDestroy = false;

        moPubView.setAutorefreshEnabled(false);
        moPubView.setAdUnitId(mPlacementID);
        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView moPubView) {
                isLoading = false;
                if (DEBUG) {
                    Log.d(TAG, "AdMob: onAdLoaded");
                }
                SharedPref.setLong(mContext,SharedPref.SP_KEY_LAST_LOAD_SUCCESS_BANNER_TIME,System.currentTimeMillis());
                if (isLoadedBannerAd) {
                    if (moPubView != null) {
                        ViewGroup group = (ViewGroup) moPubView.getParent();
                        if (group != null) {
                            group.requestLayout();
                        }
                    }

                    if (DEBUG) {
                        Log.d(TAG, "onAdLoaded: AdView RequestLayout");
                    }

                    return;
                }

                isLoadedBannerAd = true;

                if (bannerListener != null) {
                    // TODO: 2019/4/4 待添加mopub banner onAdLoaded方法
                    NativeAd nativeAd = new NativeAd.Builder(MopubBanner.this)
                            .isBanner(true)
                            .setContext(mContext)
                            .setUnitId(mUnitId)
                            .setPlacementID(mPlacementID)
                            .setPositionId(mPositionId)
                            .setmExpireTime(System.currentTimeMillis())
                            .build();
                    bannerListener.onAdLoaded(nativeAd);
                }
            }

            @Override
            public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {
                isLoading = false;
                AdErrorCode errorCode;
                switch (moPubErrorCode) {
                    case ADAPTER_CONFIGURATION_ERROR:
                        errorCode = AdErrorCode.NATIVE_ADAPTER_NOT_FOUND;
                        break;
                    case NO_FILL:
                    case NETWORK_NO_FILL:
                    case WARMUP:
                        errorCode = AdErrorCode.NETWORK_NO_FILL;
                        break;
                    case SERVER_ERROR:
                    case NETWORK_INVALID_STATE:
                        errorCode = AdErrorCode.SERVER_ERROR;
                        break;
                    case NETWORK_TIMEOUT:
                        errorCode = AdErrorCode.NETWORK_TIMEOUT;
                        break;
                    case NO_CONNECTION:
                        errorCode = AdErrorCode.CONNECTION_ERROR;
                        break;
                    default:
                        errorCode = AdErrorCode.UNSPECIFIED;
                }

                if (bannerListener != null) {
                    if (DEBUG) {
                        Log.d(TAG, "onBannerFailed: reason is" + moPubErrorCode.toString());
                    }
                    bannerListener.onAdFail(errorCode);
                }

            }

            @Override
            public void onBannerClicked(MoPubView moPubView) {

                if (bannerEventListener != null) {
                    if (DEBUG) {
                        Log.d(TAG, ":onAdClicked ");
                    }
                    bannerEventListener.onAdClicked();
                }
            }

            @Override
            public void onBannerExpanded(MoPubView moPubView) {
                if (DEBUG) {
                    Log.d(TAG, "#onBannerExpanded");
                }
            }

            @Override
            public void onBannerCollapsed(MoPubView moPubView) {
                if (DEBUG) {
                    Log.d(TAG, "#onBannerCollapsed");
                }
            }
        });
        if (mLayoutParams == null) {
            mLayoutParams = getDefaultLayoutParams();
        }
        moPubView.setLayoutParams(mLayoutParams);

        moPubView.loadAd();
        isLoading = true;
    }


    private FrameLayout.LayoutParams getDefaultLayoutParams(){
        float density = mContext.getResources().getDisplayMetrics().density;
        int width = (int) (300.0F * density + 1.0F);
        int height = (int) (250.0F * density + 1.0F);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.gravity = 1;
        return layoutParams;
    }


    public void setLayoutParams(FrameLayout.LayoutParams layoutParams){
        mLayoutParams = layoutParams;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (DEBUG) {
            Log.d(TAG, "update: mopub init end");
        }

        if (retryLoad) {
            startLoad();
        }
    }

    @Override
    public void onPrepare(@NonNull NativeStaticViewHolder nativeStaticViewHolder, @NonNull List<? extends View> viewList) {
        if (DEBUG) {
            Log.d(TAG, "prepare: ");
        }
        try {

            if (nativeStaticViewHolder.adChoiceViewGroup != null
                    && nativeStaticViewHolder.adChoiceViewGroup instanceof FrameLayout) {
                container = nativeStaticViewHolder.adChoiceViewGroup;
                container.removeAllViews();
                if (container.getChildCount() == 0) {
                    try {
                        if (moPubView != null) {
                            ViewGroup group = (ViewGroup) moPubView.getParent();
                            if (group != null) {
                                group.removeAllViews();
                            }
                            container.addView(moPubView);
                        }
                    } catch (Exception e) {
                        if (DEBUG) {
                            Log.e(TAG, "onPrepare error", e);
                        }
                    }
                }
            }
            onSupplementImpressionTracker(nativeStaticViewHolder, viewList);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "prepare error", e);
            }
        }
    }

    private void onSupplementImpressionTracker(NativeStaticViewHolder staticNativeViewHolder, List<? extends View> views) {
        //监听展示事件
        if (staticNativeViewHolder.mainView == null) {
            return;
        }
        if (mImpressionTracker == null) {
            mImpressionTracker = new ImpressionTracker(staticNativeViewHolder.mainView);
        }
        if (staticNativeViewHolder.adChoiceViewGroup != null) {
            mImpressionTracker.addView(staticNativeViewHolder.adChoiceViewGroup, new ImpressionInterface() {
                @Override
                public int getImpressionMinPercentageViewed() {
                    return 0;
                }

                @Override
                public Integer getImpressionMinVisiblePx() {
                    return null;
                }

                @Override
                public int getImpressionMinTimeViewed() {
                    return 0;
                }

                @Override
                public void recordImpression(View view) {
                    if (bannerEventListener != null) {
                        if (DEBUG) {
                            Log.d(TAG, "recordImpression: banner impress");
                        }
                        bannerEventListener.onAdImpressed();
                        isDisplay = true;
                    }
                }

                @Override
                public boolean isImpressionRecorded() {
                    return false;
                }

                @Override
                public void setImpressionRecorded() {

                }
            });
        }
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public void show() {

    }
}
