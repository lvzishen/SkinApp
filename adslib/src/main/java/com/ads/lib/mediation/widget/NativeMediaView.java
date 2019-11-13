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


/**
 * Created by {@author kai}on 17-11-14.
 */

public class NativeMediaView extends FrameLayout {

    ImageView mainImageView;

    public NativeMediaView(@NonNull Context context) {
        this(context, null);
    }

    public NativeMediaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NativeMediaView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addMediaView(View mediaView, NativeStaticViewHolder staticViewHolder, String urlBanner) {
        if (staticViewHolder == null || staticViewHolder.mediaView == null) {
            return;
        }
        if (mediaView == null) {
            staticViewHolder.mediaView.removeAllViews();
            mainImageView = new ImageView(staticViewHolder.mediaView.getContext());
            mainImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mainImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            staticViewHolder.mediaView.addView(mainImageView);
            if (!TextUtils.isEmpty(urlBanner)) {
                NativeImageHelper.loadImage(mainImageView, urlBanner);
            }
        } else {
            staticViewHolder.mediaView.removeAllViews();
            mediaView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            staticViewHolder.mediaView.addView(mediaView);
        }
    }

    public void addMediaView(NativeStaticViewHolder staticViewHolder, String urlBanner) {
        addMediaView(null, staticViewHolder, urlBanner);
    }

    public ImageView getMainImageView() {
        return mainImageView;
    }
}
