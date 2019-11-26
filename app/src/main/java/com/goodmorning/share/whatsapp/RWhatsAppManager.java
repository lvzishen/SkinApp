package com.goodmorning.share.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.goodmorning.share.share.RShare;
import com.goodmorning.share.share.RSharePlatform;
import com.goodmorning.share.util.RFileHelper;
import com.goodmorning.share.util.RPlatformHelper;

import java.io.File;


final public class RWhatsAppManager extends RShare {

    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private final String TAG = "RWhatsAppManager===>";

    private static RWhatsAppManager mManager;

    private RWhatsAppManager() {
    }

    public static RWhatsAppManager getInstance() {
        if (mManager == null) {
            synchronized (RWhatsAppManager.class) {
                if (mManager == null) {
                    mManager = new RWhatsAppManager();
                }
            }
        }
        return mManager;
    }

    /**
     * 分享图文到 WhatsApp 客户端.
     *
     * @param context     上下文.
     * @param image       图片.
     * @param description 内容描述.
     */
    public void shareImageAndText(@NonNull Context context,
                                  @Nullable Bitmap image,
                                  @Nullable String description) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WhatsApp)) {
            Toast.makeText(context, "请先安装WhatsApp...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (image == null && description == null) {
            Log.e(TAG, "分享参数无效.");
        }

        RFileHelper.detectFileUriExposure();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        if (description != null) {
            intent.putExtra(Intent.EXTRA_TEXT, description);
            intent.setType("text/plain");
        }

        if (image != null) {
            RFileHelper.saveBitmapToExternalSharePath(context, image);
            intent.putExtra(Intent.EXTRA_STREAM, RFileHelper.getExternalSharePathFileUris(context).get(0));
            intent.setType("image/jpeg");
        }
        intent.setPackage(WHATSAPP_PACKAGE_NAME);
        context.startActivity(intent);
    }


    private void shareLink(Context context) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WhatsApp)) {
            Toast.makeText(context, "请先安装WhatsApp...", Toast.LENGTH_SHORT).show();
            return;
        }
        String type = "text/*";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(type);
        share.putExtra(Intent.EXTRA_TEXT, "分享链接：https://juejin.im/user/568e61dc60b2ad083a795554/activities");
        share.setPackage(WHATSAPP_PACKAGE_NAME);
        context.startActivity(Intent.createChooser(share, "Share to"));
    }
//    private void shareImg() {
//        if (!AppUtils.isAppInstalled(WHATSAPP_PACKAGE_NAME)) {
//            Toast.makeText(this, "请先安装WhatsApp...", Toast.LENGTH_SHORT).show();
//        }
//
//        String type = "image/*";
//        Uri uri = Uri.parse(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
//
//        Intent share = new Intent(Intent.ACTION_SEND);
//        // Set the MIME type
//        share.setType(type);
//        share.putExtra(Intent.EXTRA_TEXT, "分享说明");
//        // Add the URI to the Intent.
//        share.putExtra(Intent.EXTRA_STREAM, uri);
//        share.setPackage(WHATSAPP_PACKAGE_NAME);
//        // Broadcast the Intent.
//        startActivity(Intent.createChooser(share, "Share to"));
//    }

    private void shareVideo(Context context, String mediaPath) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WhatsApp)) {
            Toast.makeText(context, "请先安装WhatsApp...", Toast.LENGTH_SHORT).show();
            return;
        }
        String type = "video/*";
//        String mediaPath = "/storage/emulated/0/相机/729d2e15-0204-4e14-95a6-086436aa805b.mp4";
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);
        // Set the MIME type
        share.setType(type);
        share.putExtra(Intent.EXTRA_TEXT, "分享说明");
        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);
        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setPackage(WHATSAPP_PACKAGE_NAME);
        // Broadcast the Intent.
        context.startActivity(Intent.createChooser(share, "Share to"));
    }
}