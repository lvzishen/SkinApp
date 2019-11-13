package com.ads.lib.mediation.bean;

import android.view.View;

import com.ads.lib.mediation.loader.natives.NativeEventListener;
import com.ads.lib.mediation.widget.NativeStaticViewHolder;

import java.util.List;

import androidx.annotation.NonNull;

public abstract class AbsNativeAdWrapper {


    public abstract boolean isExpired();


    public abstract boolean isDestroyed();


    public abstract boolean isImpress();

    public abstract void clear(View mainView);

    public abstract void setNativeEventListener(NativeEventListener listener);

    protected abstract void onPrepare(@NonNull NativeStaticViewHolder staticNativeViewHolder, @NonNull List<? extends View> viewList);

}
