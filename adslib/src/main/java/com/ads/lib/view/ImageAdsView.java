//package com.ads.lib.view;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.ads.lib.AdsConfig;
//import com.ads.lib.R;
//
///**
// * 图片背景的广告布局
// * Created by yeguolong on 17-7-13.
// */
//public class ImageAdsView extends BaseAdsView {
//
//    private View mTitleBg;
//
//    public ImageAdsView(Context context) {
//        super(context);
//    }
//
//    public ImageAdsView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public ImageAdsView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @Override
//    protected void initView(Context context) {
//        inflate(context, R.layout.layout_ads_view_image, this);
//        mTitleBg = findViewById(R.id.layout_image_ads_title_bg);
//        initImageLayout();
//    }
//
//    private void initImageLayout() {
//        View view = findViewById(R.id.layout_image_ads_img_parent);
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.height = (int) (getResources().getDisplayMetrics().widthPixels -
//                getResources().getDimension(R.dimen.qb_px_24) * 2)
//                * AdsConfig.BASE_ADS_IMG_HEIGHT / AdsConfig.BASE_ADS_IMG_WIDTH;
//        view.setLayoutParams(layoutParams);
//    }
//
//    @Override
//    public int getLogoId() {
//        return R.id.layout_image_ads_logo;
//    }
//
//    @Override
//    public int getIconId() {
//        return 0;
//    }
//
//    @Override
//    public int getTitleId() {
//        return R.id.layout_image_ads_title;
//    }
//
//    @Override
//    public int getDescId() {
//        return 0;
//    }
//
//    @Override
//    public int getBtnId() {
//        return R.id.layout_image_ads_btn;
//    }
//
//    @Override
//    public int getChoiceId() {
//        return R.id.layout_image_ads_choice;
//    }
//
//    @Override
//    public int getImageId() {
//        return R.id.layout_image_ads_img;
//    }
//
//    @Override
//    public void setTitle(CharSequence text) {
//        super.setTitle(text);
//        if (mTitleBg != null) {
//            if (!TextUtils.isEmpty(text)) {
//                mTitleBg.setVisibility(GONE);
//            } else {
//                mTitleBg.setVisibility(VISIBLE);
//            }
//        }
//    }
//
//    @Override
//    public void setBtnText(CharSequence text) {
//        super.setBtnText(text);
//        if (mBtn != null) {
//            if (!TextUtils.isEmpty(text)) {
//                mBtn.setBackgroundResource(R.drawable.selector_green_btn);
//            } else {
//                mBtn.setBackgroundColor(getResources().getColor(R.color.color_image_ads_default_bg));
//            }
//        }
//    }
//
//    @Override
//    public void reset() {
//        super.reset();
//        setLogoVisible(false);
//    }
//    @Override
//    public View getRootAdsView() {
//        return findViewById(R.id.pop_ad_root);
//    }
//}
