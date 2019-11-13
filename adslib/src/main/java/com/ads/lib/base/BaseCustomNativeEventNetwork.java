package com.ads.lib.base;

import android.view.View;

import com.ads.lib.mediation.widget.NativeStaticViewHolder;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by zhaozhiwen on 2019/4/3.
 */
public interface BaseCustomNativeEventNetwork {


    public void destroy();

    public void loadAd();

    public void onPrepare(NativeStaticViewHolder staticNativeViewHolder, @NonNull List<? extends View> viewList);

    public boolean isLoaded();

    public void show();
}
