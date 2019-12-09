package com.goodmorning.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;


import com.goodmorning.share.util.RFileHelper;
import com.goodmorning.share.util.RPlatformHelper;

/**
 * Instagram <a href=https://www.instagram.com/developer/mobile-sharing/android-intents/>Instagram
 * Android分享</a>
 */
final public class RInstagramManager extends RShare implements ISaveImage {

    private final String TAG = "RInstagramManager===>";

    private static RInstagramManager mManager;

    private RInstagramManager() {
    }

    public static RInstagramManager getInstance() {
        if (mManager == null) {
            synchronized (RInstagramManager.class) {
                if (mManager == null) {
                    mManager = new RInstagramManager();
                }
            }
        }
        return mManager;
    }

    public void shareVideo(Context context, Uri localVideoUrl) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Instagram)) {
            Toast.makeText(context, "Instagram 未安装", Toast.LENGTH_SHORT).show();
            return;
        }

        RFileHelper.detectFileUriExposure();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_STREAM, localVideoUrl);
        intent.setPackage("com.instagram.android");
        context.startActivity(intent);
    }


    @Override
    public String saveImage(Context context, String picName, Bitmap bitmap) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Instagram)) {
            Toast.makeText(context, "Instagram 未安装", Toast.LENGTH_SHORT).show();
            return null;
        }
        RFileHelper.deleteExternalShareDirectory(context);
        String path = RFileHelper.saveBitmapToExternalSharePath(context, bitmap);
        RFileHelper.detectFileUriExposure();
        /**
         *
         * 直接启动 Instagram 客户端分享.
         * */
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        Uri uri = RFileHelper.getExternalSharePathFileUris(context, path).get(0);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setPackage("com.instagram.android");
        context.startActivity(intent);
        return null;
    }

    @Override
    public String saveVideo(Context context, String videoName, String videoUrl) {
        return null;
    }
}
