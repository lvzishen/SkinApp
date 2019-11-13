package com.ads.lib.mediation.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ads.lib.image.NativeImageHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdIconView extends FrameLayout {

    ImageView mAdIconView;

    public AdIconView(@NonNull Context context) {
        this(context, null);
    }

    public AdIconView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdIconView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void addAIconView(View adIconView, NativeStaticViewHolder staticViewHolder, String urlBanner) {
        if (staticViewHolder == null || staticViewHolder.iconImageView == null) {
            return;
        }
        if (adIconView == null) {
            staticViewHolder.iconImageView.removeAllViews();
            mAdIconView = new ImageView(staticViewHolder.iconImageView.getContext());
            mAdIconView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mAdIconView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            staticViewHolder.iconImageView.addView(mAdIconView);
            if (!TextUtils.isEmpty(urlBanner)) {
                NativeImageHelper.loadImage(mAdIconView, urlBanner);
            }
        } else {
            staticViewHolder.iconImageView.removeAllViews();
            adIconView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            staticViewHolder.iconImageView.addView(adIconView);
        }
    }

    public void addAIconView(NativeStaticViewHolder staticViewHolder, String urlBanner) {
        addAIconView(null, staticViewHolder, urlBanner);
    }

    public ImageView getAdIconImageView() {
        return mAdIconView;
    }

}