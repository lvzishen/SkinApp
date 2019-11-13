//package com.ads.lib.view;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.ads.lib.R;
//import com.ads.lib.mediation.bean.NativeAd;
//import com.ads.lib.mediation.bean.NativeViewBinder;
//import com.ads.lib.mediation.widget.AdIconView;
//import com.ads.lib.mediation.widget.NativeMediaView;
//
///**
// * Created by yeguolong on 17-6-20.
// */
//public abstract class BaseAdsView extends FrameLayout implements IAdsView {
//
//    protected AdsLogoView mLogo;
//    protected AdIconView mIcon;
//    protected NativeMediaView mImage;
//    protected ImageView mIvDescArrow;
//    protected TextView mTitle;
//    protected TextView mDesc;
//    protected TextView mBtn;
//    protected FrameLayout mChoice;
//
//    protected NativeViewBinder mViewBinder;
//    protected int mLogoTextColor;
//    protected int mLogoBgColor;
//    private boolean hasTypeArray;
//    private FrameLayout bannerView;
//    private FrameLayout nativeAdView;
//    private TextView bannerAdBtn;
//
//    public BaseAdsView(Context context) {
//        this(context, null);
//    }
//
//    public BaseAdsView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public BaseAdsView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//        initAttributeSet(context, attrs, defStyleAttr);
//        initLogo();
//    }
//
//    private void init(Context context) {
//        initView(context);
//        mLogo = findViewById(getLogoId());
//        mIcon = findViewById(getIconId());
//        mImage = findViewById(getImageId());
//        mTitle = findViewById(getTitleId());
//        mDesc = findViewById(getDescId());
//        mIvDescArrow = findViewById(getDescArrowId());
//        mBtn = findViewById(getBtnId());
//        mChoice = findViewById(getChoiceId());
//        bannerView = findViewById(getBannerId());
//        nativeAdView = findViewById(getNativeViewId());
//    }
//
//    public int getDescArrowId() {
//        return R.id.iv_arrow_right;
//    }
//
//    public int getBannerId() {
//        return R.id.banner_fl;
//    }
//
//    public int getNativeViewId() {
//        return R.id.pop_ad_root;
//    }
//
//    private void initAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
//        if (context != null && attrs != null) {
//            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdsView, defStyleAttr, 0);
//            if (a != null) {
//                if (mLogo != null) {
//                    hasTypeArray = true;
//                    mLogoTextColor = a.getColor(R.styleable.AdsView_logo_text_color, mLogo.getLogoTextColor());
//                    mLogoBgColor = a.getColor(R.styleable.AdsView_logo_bg_color, mLogo.getLogoBgColor());
//                }
//                a.recycle();
//            }
//        }
//    }
//
//    private void initLogo() {
//        if (mLogo != null) {
//            if (hasTypeArray) {
//                mLogo.setTextColor(mLogoTextColor);
//                mLogo.setBackgroundColor(mLogoBgColor);
//            }
//        }
//    }
//
//    @Override
//    public AdsLogoView getLogo() {
//        return mLogo;
//    }
//
//    @Override
//    public NativeMediaView getImage() {
//        return mImage;
//    }
//
//    @Override
//    public AdIconView getIcon() {
//        return mIcon;
//    }
//
//    @Override
//    public FrameLayout getChoice() {
//        return mChoice;
//    }
//
//    @Override
//    public void setTitle(CharSequence text) {
//        if (mTitle != null) {
//            mTitle.setText(text);
//        }
//    }
//
//    @Override
//    public void setDesc(CharSequence text) {
//        if (mDesc != null) {
//            mDesc.setText(text);
//        }
//    }
//
//    @Override
//    public void setBtnText(CharSequence text) {
//        if (mBtn != null) {
//            mBtn.setText(text);
//        }
//    }
//
//    @Override
//    public void setLogoVisible(boolean visible) {
//        if (mLogo != null) {
//            mLogo.setVisibility(visible ? VISIBLE : GONE);
//        }
//    }
//
//    @Override
//    public NativeViewBinder getViewBinder(NativeAd nativeAd) {
//        if (mViewBinder == null) {
//            if (nativeAd.isBanner) {
//
//                mViewBinder = new NativeViewBinder.Builder(getBannerView())
//                        .adChoiceViewGroupId(getBannerId())
//                        .build();
//            } else {
//                mViewBinder = new NativeViewBinder.Builder(getRootAdsView())
//                        .titleId(getTitleId())
//                        .textId(getDescId())
//                        .iconImageId(getIconId())
//                        .mediaViewId(getImageId())
//                        .callToActionId(getBtnId())
//                        .adChoiceViewGroupId(getChoiceId())
//                        .build();
//            }
//        }
//        return mViewBinder;
//    }
//
//    @Override
//    public View getRootAdsView() {
//        return this;
//    }
//
//    @Override
//    public View getNativeAdsView() {
//        return nativeAdView;
//    }
//
//    @Override
//    public View getBannerView() {
//        return bannerView;
//    }
//
//    @Override
//    public void reset() {
//        setTitle("");
//        setDesc("");
//        setBtnText("");
////        if (mImage != null) {
////            mImage.
////            mImage.setImageResource(0);
////        }
////        if (mIcon != null) {
////            mIcon.setImageResource(0);
////        }
//    }
//
//    protected abstract void initView(Context context);
//}
