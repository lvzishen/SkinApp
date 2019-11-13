package com.baselib.bitmap.download;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.baselib.bitmap.util.BitmapUtil;
import com.cleanerapp.supermanager.baselib.R;


public class PackageIconDownloader implements Downloader {

    @Override
    public byte[] download(Context context, String urlString) {
        Drawable drawable = getAppIcon(context, urlString);
        if (drawable != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            try {
                return BitmapUtil.Bitmap2Bytes(bitmapDrawable.getBitmap());
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Drawable getAppIcon(Context context, String packageName) {
        Drawable drawable = null;
        if (context != null) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
                if (info != null && info.applicationInfo != null) {
                    drawable = info.applicationInfo.loadIcon(pm);
                }
            } catch (Exception e) {
            }
            try {
                if (drawable == null) {
                    drawable = getDefaultAppIcon(context);
                }
            } catch (Exception e) {
            }
        }
        return drawable;
    }

    public static Drawable getDefaultAppIcon(Context context) {
        try {
            return context.getResources().getDrawable(R.drawable.default_apk_icon);
        } catch (Exception e) {
        }
        return null;
    }
}
