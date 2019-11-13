package com.ads.lib.mediation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NativeAdContainer extends FrameLayout{

    public NativeAdContainer(@NonNull Context context) {
        super(context);
    }

    public NativeAdContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NativeAdContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NativeAdContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
