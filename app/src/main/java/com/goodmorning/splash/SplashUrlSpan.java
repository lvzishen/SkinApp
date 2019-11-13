package com.goodmorning.splash;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.view.View;

import com.cleanerapp.supermanager.R;


public class SplashUrlSpan extends URLSpan {
    private final int color;
    private final View.OnClickListener clickListener;
    private Context mContext;

    public SplashUrlSpan(Context context, String url) {
        this(context, url, null);
        mContext = context;
    }

    public SplashUrlSpan(Context context, String url, View.OnClickListener clickListener) {
        super(url);
        this.color = context.getResources().getColor(R.color.color_80333333);
        this.clickListener = clickListener;
        mContext = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
        ds.setUnderlineText(true);
    }

    @Override
    public void onClick(View widget) {
        String url = getURL();
        if (!TextUtils.isEmpty(url) && url.startsWith("http://")) {
            OpenUrlUtils.openDefaultBrower(mContext, url);
            super.onClick(widget);
        }
        if (clickListener != null) {
            clickListener.onClick(widget);
        }
    }
}