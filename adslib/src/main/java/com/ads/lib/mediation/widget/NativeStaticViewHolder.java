package com.ads.lib.mediation.widget;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ads.lib.ModuleConfig;
import com.ads.lib.adapter.AdElementType;
import com.ads.lib.mediation.bean.NativeViewBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NativeStaticViewHolder {


    private static final String TAG = "Stark.NativeViewHolder";
    private static final boolean DEBUG = ModuleConfig.DEBUG;

    @Nullable
    public View mainView;
    @Nullable
    public TextView titleView;
    @Nullable
    public TextView textView;
    @Nullable
    public TextView callToActionView;
    @Nullable
    public ImageView mainImageView;
    @Nullable
    public AdIconView iconImageView;
    @Nullable
    public ViewGroup adChoiceViewGroup;
    @Nullable
    public String defaultCallToAction;
    @Nullable
    public NativeMediaView mediaView;

    public static HashMap<Integer,AdElementEntry> adElementViewMap = new HashMap<>();

    private List<View> mViewList = new ArrayList<>();

    private static NativeStaticViewHolder emptyViewHolder = new NativeStaticViewHolder();

    public List<View> getViewList() {
        return mViewList;
    }

    public void clearList() {
        mViewList.clear();
    }

    private NativeStaticViewHolder() {
    }

    @NonNull
    public static NativeStaticViewHolder fromViewBinder(@NonNull final View view, @NonNull final NativeViewBinder viewBinder) {
        final NativeStaticViewHolder nativeStaticViewHolder = new NativeStaticViewHolder();
        nativeStaticViewHolder.mainView = view;
        try {
            nativeStaticViewHolder.titleView = (TextView) view.findViewById(viewBinder.titleId);
            nativeStaticViewHolder.textView = (TextView) view.findViewById(viewBinder.textId);
            nativeStaticViewHolder.callToActionView = (TextView) view.findViewById(viewBinder.callToActionId);
            nativeStaticViewHolder.mainImageView = (ImageView) view.findViewById(viewBinder.mainImageId);
            nativeStaticViewHolder.iconImageView = (AdIconView) view.findViewById(viewBinder.iconImageId);
            nativeStaticViewHolder.adChoiceViewGroup = (ViewGroup) view.findViewById(viewBinder.adChoiceViewGroupId);
            nativeStaticViewHolder.defaultCallToAction = viewBinder.defaultCallToAction;
            nativeStaticViewHolder.mediaView = (NativeMediaView) view.findViewById(viewBinder.mediaViewId);
            if (nativeStaticViewHolder.titleView != null) {
                nativeStaticViewHolder.getViewList().add(nativeStaticViewHolder.titleView);
                adElementViewMap.put(viewBinder.titleId, new AdElementEntry(AdElementType.TITLE, nativeStaticViewHolder.titleView, viewBinder.titleId));
            }
            if (nativeStaticViewHolder.textView != null) {
                nativeStaticViewHolder.getViewList().add(nativeStaticViewHolder.textView);
                adElementViewMap.put(viewBinder.textId, new AdElementEntry(AdElementType.TEXT, nativeStaticViewHolder.textView, viewBinder.textId));
            }
            if (nativeStaticViewHolder.callToActionView != null) {
                nativeStaticViewHolder.getViewList().add(nativeStaticViewHolder.callToActionView);
                adElementViewMap.put(viewBinder.callToActionId, new AdElementEntry(AdElementType.CALL_TO_ACTION, nativeStaticViewHolder.callToActionView, viewBinder.callToActionId));

            }
            if (nativeStaticViewHolder.mainImageView != null) {
                nativeStaticViewHolder.getViewList().add(nativeStaticViewHolder.mainImageView);
                adElementViewMap.put(viewBinder.mainImageId, new AdElementEntry(AdElementType.MAIN_IMAGE, nativeStaticViewHolder.mainImageView, viewBinder.mainImageId));

            }
            if (nativeStaticViewHolder.iconImageView != null) {
                nativeStaticViewHolder.getViewList().add(nativeStaticViewHolder.iconImageView);
                adElementViewMap.put(viewBinder.iconImageId, new AdElementEntry(AdElementType.ICON_IMAGE, nativeStaticViewHolder.iconImageView, viewBinder.iconImageId));

            }
            if (nativeStaticViewHolder.mediaView != null) {
                nativeStaticViewHolder.getViewList().add(nativeStaticViewHolder.mediaView);
                adElementViewMap.put(viewBinder.mediaViewId, new AdElementEntry(AdElementType.MEDIA_VIEW, nativeStaticViewHolder.mediaView, viewBinder.mediaViewId));
            }

            return nativeStaticViewHolder;
        } catch (ClassCastException exception) {
            if (DEBUG) {
                Log.e(TAG, "Could not cast from id in ViewBinder to expected View type", exception);
            }
            return emptyViewHolder;
        }
    }

    public static class AdElementEntry{
        public AdElementType type;
        public View view;
        public int ID;

        public AdElementEntry(AdElementType type,View view,int ID){
            this.type = type;
            this.view = view;
            this.ID = ID;
        }
    }
}
