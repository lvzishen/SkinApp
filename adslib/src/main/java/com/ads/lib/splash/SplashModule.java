package com.ads.lib.splash;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ads.lib.R;
import com.ads.lib.mediation.bean.NativeViewBinder;
import com.ads.lib.splash.model.SplashNativeAd;

import org.saturn.splash.api.INativeLayoutDelegate;
import org.saturn.splash.api.ITurnToMainListener;
import org.saturn.splash.api.SplashSdk;
import org.saturn.splash.loader.interf.IInterstitialAdLoader;
import org.saturn.splash.loader.interf.INativeAdLoader;
import org.saturn.splash.api.SplashConfigParam;
import org.saturn.splash.widget.TradeRatingBar;

import java.util.ArrayList;

public class SplashModule {

    public void init(Context context,final Class homeActivityClazz){
        SplashConfigParam splashConfigParam = new SplashConfigParam.Builder()
                .setDefaultApusLogoType(SplashSdk.WITHOUT_APUS_LOGO)
                .setAppIconResourceId(R.drawable.drawable_splash_window)
                .setTurnToMainListener(new ITurnToMainListener() {

                    @Override
                    public Class getHomeActivityClass() {
                        return homeActivityClazz;
                    }
                }).setAdLoaderCreator(new org.saturn.splash.api.IAdLoaderCreator() {
                    @Override
                    public INativeAdLoader createNativeAdLoader(Context context, String s, String s1) {
                        return new SplashNativeAdLoader(context, s, s1);
                    }

                    @Override
                    public IInterstitialAdLoader createInterstitialAdLoader(Context context, String s, String s1) {
                        return new SplashInterstitialAdLoader(context, s, s1);
                    }
                }).setNativeLayoutDelegate(new INativeLayoutDelegate<SplashNativeAd>() {
                    @Override
                    public int getButtonResourceId() {

                        return R.layout.splash_activity_open_fullscreen_bottom_custom;
                    }

                    @Override
                    public int getTopResourceId() {
                        return R.layout.splash_activity_open_fullscreen_top_custom;
                    }

                    @Override
                    public TextView getSkipView(View view) {
                        return view.findViewById(R.id.button_skip);
                    }

                    @Override
                    public TextView getActionView(View view) {
                        return view.findViewById(R.id.button_action);
                    }

                    @Override
                    public ImageView getBannerReflectionView(View view) {
                        return view.findViewById(R.id.imageView_banner_reflection);
                    }

                    @Override
                    public View getReplaceIconView(View view) {
                        return view.findViewById(R.id.replace_view);
                    }

                    @Override
                    public LinearLayout getGpLayout(View view) {
                        return view.findViewById(R.id.gp_layout);
                    }

                    @Override
                    public TradeRatingBar getStarView(View view) {
                        return view.findViewById(R.id.star_view);
                    }

                    @Override
                    public View getAdRootView(View view) {
                        return view.findViewById(R.id.ad_root_view);
                    }

                    @Override
                    public FrameLayout getBannerView(View view) {
                        return view.findViewById(R.id.imageView_banner);
                    }

                    @Override
                    public FrameLayout getIconView(View view) {
                        return view.findViewById(R.id.imageView_icon);
                    }

                    @Override
                    public TextView getTitlt(View view) {
                        return view.findViewById(R.id.textview_title);
                    }

                    @Override
                    public TextView getSummaryView(View view) {
                        return view.findViewById(R.id.textview_summary);
                    }

                    @Override
                    public void inflateView(SplashNativeAd ad, View view) {
                        NativeViewBinder binder = new NativeViewBinder.Builder(getAdRootView(view))
                                .mediaViewId(R.id.imageView_banner)
                                .iconImageId(R.id.imageView_icon)
                                .titleId(R.id.textview_title)
                                .textId(R.id.textview_summary)
                                .callToActionId(R.id.button_action)
                                .adChoiceViewGroupId(R.id.ad_choice)
                                .build();
                        ad.prepare(binder,new ArrayList<View>());
                    }
                }).build();
        SplashSdk.init(splashConfigParam,context, "38d4e848661346eab13110c2924e801c:5", "1ae586caa64548d58b17f23013f1411b:5", "cad036ce193f4e3ea23af2d5c5a9bc96:5", "385b3b96d5914c469d79bfbc1b49dc55:5");
    }


}
