package com.baselib.bitmap.download;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.baselib.bitmap.util.BitmapUtil;

public class ResourceDrawableDownloader implements Downloader {

    @Override
    public byte[] download(Context context, String urlString) {
        try {
            int resId = Integer.valueOf(urlString);
            Drawable drawable = context.getResources().getDrawable(resId);
            if (drawable != null) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                return BitmapUtil.Bitmap2Bytes(bitmapDrawable.getBitmap());
            }
        } catch (Exception e) {
        }
        return null;
    }
}
