package com.goodmorning.share;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.creativeindia.goodmorning.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.goodmorning.bean.DataListItem;

/**
 * 创建日期：2019/11/26 on 13:58
 * 描述:
 * 作者: lvzishen
 */
public class ShareTypeManager {

    public static void shareWithWhatsapp(int mType, Activity activity, DataListItem mDataItem, Bitmap mBitmap) {
        if (mType == DataListItem.DATA_TYPE_1) {
            RWhatsAppManager.getInstance().shareText(activity, mDataItem.getData());
        }
        if (mType == DataListItem.DATA_TYPE_2 && mBitmap != null) {
            RWhatsAppManager.getInstance().shareImageAndText(activity, mBitmap, "123456");
        }
        if (mType == DataListItem.DATA_TYPE_3) {
            RWhatsAppManager.getInstance().shareText(activity, mDataItem.getVideoUrl());
        }
    }

    public static void shareWithInstagam(Activity activity, Bitmap mBitmap) {
        if (mBitmap == null) return;
        SavePicProxy insProxy = new SavePicProxy();
        insProxy.savePic(activity, System.currentTimeMillis() + "", mBitmap, SavePicProxy.INS_TYPE);
    }

    public static void shareWithMessage(Activity activity, String data) {
        RMessageManager.getInstance().shareText(activity, data);
    }

    public static void shareWithMore(int mType, Activity activity, String data, Bitmap mBitmap, String videoUrl) {
        if (mType == DataListItem.DATA_TYPE_1) {
            Intent sendIntent = ShareManager.getInstance().getShareStringIntent(activity, data);
            activity.startActivity(sendIntent);
        }
        if (mType == DataListItem.DATA_TYPE_2 && mBitmap != null) {
            SavePicProxy moreProxy = new SavePicProxy();
            moreProxy.savePic(activity, System.currentTimeMillis() + "", mBitmap, SavePicProxy.MORE_TYPE);
        }
        if (mType == DataListItem.DATA_TYPE_3) {
            Intent sendIntent = ShareManager.getInstance().getShareStringIntent(activity, videoUrl);
            activity.startActivity(sendIntent);
        }
    }

    public static void shareWithCopy(Activity activity, String data) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", data);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.is_copy), Toast.LENGTH_SHORT).show();
    }

    public static void shareWithImage(int mType, Activity activity, Bitmap mBitmap) {
        if (mType == DataListItem.DATA_TYPE_2 && mBitmap != null) {
            SavePicProxy savePicProxy = new SavePicProxy();
            savePicProxy.savePic(activity, System.currentTimeMillis() + "", mBitmap, SavePicProxy.SAVE_TYPE);
        }
    }

    public static void shareWithFaceBook(int mType, Activity activity, DataListItem mDataItem, Bitmap mBitmap, String tag) {
        CallbackManager callbackManager = CallbackManager.Factory.create();
        FacebookCallback<Sharer.Result> callback =
                new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onCancel() {
                        Log.i(tag, "Canceled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.i(tag, String.format("Error: %s", error.toString()));
                    }

                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Log.i(tag, "Success!");
                    }
                };
        ShareDialog shareDialog = new ShareDialog(activity);
        shareDialog.registerCallback(callbackManager, callback);
        if (mType == DataListItem.DATA_TYPE_1) {
            ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://developers.facebook.com")).build();
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        }
        if (mType == DataListItem.DATA_TYPE_2 && mBitmap != null) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(mBitmap)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        }
        if (mType == DataListItem.DATA_TYPE_3) {
            ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse(mDataItem.getVideoUrl())).build();
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        }
    }
}
