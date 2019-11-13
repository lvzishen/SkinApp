package com.ads.lib.mediation.bean;

import android.view.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class NativeViewBinder {

    public final static class Builder {

        private View mainView;
        private final int layoutId;
        private int titleId;
        private int textId;
        private int callToActionId;
        private int mainImageId;
        private int iconImageId;
        private int adChoiceViewGroupId;
        private String defaultCallToAction;
        private int mediaViewId;
        @NonNull
        private Map<String, Integer> extras = Collections.emptyMap();

//        public Builder(final int layoutId) {
//            this.mainView = null;
//            this.layoutId = layoutId;
//            this.extras = new HashMap<String, Integer>();
//        }

        public Builder(final View mainView) {
            this.mainView = mainView;
            this.layoutId = 0;
            this.extras = new HashMap<String, Integer>();
        }

        @NonNull
        public final Builder titleId(final int titleId) {
            this.titleId = titleId;
            return this;
        }

        @NonNull
        public final Builder textId(final int textId) {
            this.textId = textId;
            return this;
        }

        @NonNull
        public final Builder callToActionId(final int callToActionId) {
            this.callToActionId = callToActionId;
            return this;
        }

        @NonNull
        public final Builder callToActionIdWithUnion(final int callToActionId, final String defaultCallToAction) {
            this.callToActionId = callToActionId;
            this.defaultCallToAction = defaultCallToAction;
            return this;
        }

        @NonNull
        public final Builder mainImageId(final int mediaLayoutId) {
            this.mainImageId = mediaLayoutId;
            return this;
        }

        @NonNull
        public final Builder iconImageId(final int iconImageId) {
            this.iconImageId = iconImageId;
            return this;
        }

        @NonNull
        public final Builder adChoiceViewGroupId(final int adChoiceViewGroupId) {
            this.adChoiceViewGroupId = adChoiceViewGroupId;
            return this;
        }

        @NonNull
        public final Builder addExtras(final Map<String, Integer> resourceIds) {
            this.extras = new HashMap<String, Integer>(resourceIds);
            return this;
        }

        @NonNull
        public final Builder addExtra(final String key, final int resourceId) {
            this.extras.put(key, resourceId);
            return this;
        }

        @NonNull
        public final Builder mediaViewId(final int mediaViewId) {
            this.mediaViewId = mediaViewId;
            return this;
        }

        @NonNull
        public final NativeViewBinder build() {
            return new NativeViewBinder(this);
        }
    }

    final public View mainView;
    final public int layoutId;
    final public int titleId;
    final public int textId;
    final public int callToActionId;
    final public int mainImageId;
    final public int iconImageId;
    final public int adChoiceViewGroupId;
    final public String defaultCallToAction;
    final public int mediaViewId;
    @NonNull
    final public Map<String, Integer> extras;

    private NativeViewBinder(@NonNull final Builder builder) {
        this.layoutId = builder.layoutId;
        this.titleId = builder.titleId;
        this.textId = builder.textId;
        this.callToActionId = builder.callToActionId;
        this.mainImageId = builder.mainImageId;
        this.iconImageId = builder.iconImageId;
        this.adChoiceViewGroupId = builder.adChoiceViewGroupId;
        this.extras = builder.extras;
        this.mainView = builder.mainView;
        this.defaultCallToAction = builder.defaultCallToAction;
        this.mediaViewId = builder.mediaViewId;
    }
}
