package com.ads.lib.image;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class NativeImage {

    private Drawable drawable;
    private String url;

    public NativeImage() {
    }

    public NativeImage(String url) {
        this(url, null);
    }

    public NativeImage(String url, Drawable drawable) {
        this.drawable = drawable;
        this.url = url;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Drawable getDrawable() {
        if (drawable != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap()!=null && bitmapDrawable.getBitmap().isRecycled()){
                return null;
            }
        }
        return drawable;
    }

    public String getUrl() {
        return url;
    }

}
