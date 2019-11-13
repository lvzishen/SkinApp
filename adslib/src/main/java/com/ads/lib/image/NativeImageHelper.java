package com.ads.lib.image;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;

/**
 * 对外暴露图片下载方法
 * Created by kai on 17-11-1.
 *
 * @author kai
 */

public class NativeImageHelper {

    public static final String TAG = "NativeImageHelper";

    public static void loadImage(@NonNull final ImageView imageView,
                                 @NonNull final NativeImage nativeImage) {
        String imageUrl = nativeImage.getUrl();
        loadImage(imageView, imageUrl);
    }

    public static void loadImage(@NonNull final ImageView imageView,
                                 @NonNull final String imageUrl) {
        Context context = imageView.getContext();
        if (context == null) {
            Log.e(TAG, "#loadImage load fail context is null");
            return;
        }
        if (TextUtils.isEmpty(imageUrl)) {
            Log.e(TAG, "#loadImage load fail imageUrl is null");
            return;
        }
        Glide.with(context).load(imageUrl).into(imageView);

    }


}
