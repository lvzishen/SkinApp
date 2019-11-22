package com.goodmorning.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.creativeindia.goodmorning.R;
import com.goodmorning.config.GlobalConfig;

import org.interlaken.common.XalContext;
import org.thanos.netcore.MorningDataAPI;


/**
 * ShareUtils
 *
 * @author baishixian
 * @date 2018/3/29 11:00
 */

public class ShareUtils {

    private static final String TAG = "ShareUtils";

    /**
     * Current activity
     */
    private Context context;

    /**
     * Share content resourceType
     */
    private @ShareContentType
    String contentType;

    /**
     * Share title
     */
    private String title;
    /**
     * Share content text
     */
    private String contentText;
    /**
     * Share complete onActivityResult requestCode
     */
    private int requestCode;

    /**
     * Forced Use System Chooser
     */
    private boolean forcedUseSystemChooser;

    private ShareUtils(@NonNull Builder builder) {
        this.context = builder.context;
        this.contentType = builder.contentType;
        this.title = builder.title;
        this.contentText = builder.textContent;
        this.requestCode = builder.requestCode;
        this.forcedUseSystemChooser = builder.forcedUseSystemChooser;
    }

    /**
     * 分享纯文本
     */
    public static void share(Context context, String title, String content) {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(textIntent, title));
    }

    /**
     * shareBySystem
     */
    public void shareBySystem() {
        if (checkShareParam()) {
            Intent shareIntent = createShareIntent();

            if (shareIntent == null) {
                if (GlobalConfig.DEBUG) Log.e(TAG, "shareBySystem cancel.");
                return;
            }

            if (title == null) {
                title = "";
            }

            if (forcedUseSystemChooser) {
                shareIntent = Intent.createChooser(shareIntent, title);
            }

            if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                try {
                    if (requestCode != -1 && context instanceof Activity) {
                        ((Activity) context).startActivityForResult(shareIntent, requestCode);
                    } else {
                        context.startActivity(shareIntent);
                    }
                } catch (Exception e) {
                    if (GlobalConfig.DEBUG) {
                        Log.e(TAG, Log.getStackTraceString(e));
                    }
                }
            }
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addCategory("android.intent.category.DEFAULT");

        switch (contentType) {
            case ShareContentType.TEXT:
                shareIntent.putExtra(Intent.EXTRA_TEXT, contentText);
                shareIntent.setType("text/plain");
                break;
            case ShareContentType.IMAGE:
            case ShareContentType.AUDIO:
            case ShareContentType.VIDEO:
            case ShareContentType.FILE:
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addCategory("android.intent.category.DEFAULT");
                shareIntent.setType(contentType);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                break;
            default:
                if (GlobalConfig.DEBUG) {
                    Log.e(TAG, contentType + " is not support share resourceType.");
                }
                shareIntent = null;
                break;
        }

        return shareIntent;
    }

    private boolean checkShareParam() {
        if (this.context == null) {
            if (GlobalConfig.DEBUG) {
                Log.e(TAG, "context is null.");
            }
            return false;
        }

        if (TextUtils.isEmpty(this.contentType)) {
            if (GlobalConfig.DEBUG) {
                Log.e(TAG, "Share content resourceType is empty.");
            }
            return false;
        }

        if (ShareContentType.TEXT.equals(contentType)) {
            if (TextUtils.isEmpty(contentText)) {
                if (GlobalConfig.DEBUG) {
                    Log.e(TAG, "Share text context is empty.");
                }
                return false;
            }
        }

        return true;
    }

    public static class Builder {
        private Context context;
        private @ShareContentType
        String contentType = ShareContentType.FILE;
        private String title;
        private String textContent;
        private int requestCode = -1;
        private boolean forcedUseSystemChooser = true;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set Content Type
         *
         * @param contentType {@link ShareContentType}
         * @return Builder
         */
        public Builder setContentType(@ShareContentType String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * Set Title
         *
         * @param title title
         * @return Builder
         */
        public Builder setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        /**
         * Set text content
         *
         * @param textContent textContent
         * @return Builder
         */
        public Builder setTextContent(String textContent) {
            this.textContent = textContent;
            return this;
        }

        /**
         * build
         *
         * @return ShareUtils
         */
        public ShareUtils build() {
            return new ShareUtils(this);
        }

    }


    public static String[] getShareStringByType(Context context,int type) {
        String watch = null;
        String download = null;
        String[] share = new String[2];
        if (MorningDataAPI.isYouTuBeVideo(type) || type == MorningDataAPI.RESOURCE_TYPE_MSN) {
            watch = context.getResources().getString(R.string.share_video_watch, XalContext.getAppName());
            download = context.getResources().getString(R.string.share_video_download, XalContext.getAppName());
        }

        if (MorningDataAPI.isPicType(type) || type == MorningDataAPI.RESOURCE_TYPE_NEWS) {
            watch = context.getResources().getString(R.string.share_news_watch, XalContext.getAppName());
            download = context.getResources().getString(R.string.share_news_download, XalContext.getAppName());
        }
        share[0] = watch;
        share[1] = download;
        return share;
    }
}
