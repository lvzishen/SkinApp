package com.goodmorning.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.goodmorning.config.GlobalConfig;

import org.interlaken.common.utils.PackageInfoUtil;

import java.net.URLDecoder;


/*** 网页分享到NATIVE分享的接口 */
public class ShareWebUtils {
    private final static String TAG = "ShareWebUtils";

    private final static String FACEBOOK = "com.facebook.katana";
    private final static String GOOGLE = "com.google.android.apps.plus";
    private final static String WHATSAPP = "com.whatsapp";
    private final static String TWITTER = "com.twitter.android";

    /**
     * 根据url判断是否要调用客户端分享，如果调用客户端分享就返回true，拦截网页的处理，否则就返回false，交给网页分享
     *
     * @param context
     * @param webview
     * @param url
     * @return
     */
    @SuppressLint("LongLogTag")
    public static boolean handleShareLink(Context context, final WebView webview, String url) {
        boolean ret = false;

        if (GlobalConfig.DEBUG){
            Log.e(TAG,"fb分享"+handleFbShareUrl(context, url));
            Log.e(TAG,"whats分享"+handleWhatsappUrl(context, url));
            Log.e(TAG,"google分享"+handleGoogleUrl(context, url));
            Log.e(TAG,"tw分享"+handleTwitterUrl(context, url));
        }

        if (handleFbShareUrl(context, url) || handleWhatsappUrl(context, url) || handleGoogleUrl(context, url) || handleTwitterUrl(context, url)) {
            ret = true;
        }
        if (url != null && url.contains("injoy.fun://share") || "injoy.fun://".equals(url)) {
            if (GlobalConfig.DEBUG) {
                Log.e("NewsDetailActivity", ",shouldOverrideUrlLoading  处理分享链接不调用 = " + url);
            }
            return true;
        }
        if (ret && webview != null) {
            webview.stopLoading();
        }
        Log.e(TAG,"handleShareLink>>>>"+ ret+"");

        return ret;
    }


    private static boolean handleFbShareUrl(Context context, final String url) {
        boolean ret = false;
        if (handleNormalShareUrl(context, url, "https://www.facebook.com/sharer", "u=", FACEBOOK) || handleNormalShareUrl(context, url, "http://m.facebook.com/sharer", "u=", FACEBOOK) || handleNormalShareUrl(context, url, "https://m.facebook.com/dialog/feed", "link=", FACEBOOK)) {
            ret = true;
        }
        return ret;
    }

    @SuppressLint("LongLogTag")
    private static boolean handleWhatsappUrl(Context context, String url) {
        if (url.startsWith("whatsapp://send?text=")) {
            if (!isInstalled(context, WHATSAPP)) {
                return false;
            }

            try {
                String linkUrl = url.substring(21);
                if (GlobalConfig.DEBUG) {
                    Log.i(TAG, "linkUrl=" + linkUrl);
                }
                String jumpUrl = URLDecoder.decode(linkUrl, "UTF-8");
                shareBySystem(context, jumpUrl, "", null, WHATSAPP);
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private static boolean handleGoogleUrl(Context context, String url) {
        return handleNormalShareUrl(context, url, "https://plus.google.com/share", "url=", GOOGLE);
    }


    private static boolean handleTwitterUrl(Context context, String url) {
        return handleNormalShareUrl(context, url, "https://twitter.com/share", "url=", TWITTER);
    }

    @SuppressLint("LongLogTag")
    private static boolean handleNormalShareUrl(Context context, String url, String prefix, String split, String pkgName) {

        if (!isInstalled(context, pkgName)) {
            Log.d(TAG,"未安装应用");
            return false;
        }

        try {
            if (!url.contains(prefix)) {
                if (GlobalConfig.DEBUG) {
                    Log.d(TAG, "----------is not valid share url");
                }
                return false;
            }
            if (GlobalConfig.DEBUG) {
                Log.d(TAG, "----------is valid share url, prefix=" + prefix + " split=" + split);
            }

            String[] params = url.split("\\?");
            if (params.length != 2) {
                return false;
            }

            String realParams = params[1];
            String[] splits = realParams.split("&");
            String linkUrl;
            for (String split1 : splits) {
                if (!TextUtils.isEmpty(split1) && split1.startsWith(split)) {
                    linkUrl = split1.substring(2);
                    try {
                        linkUrl = URLDecoder.decode(linkUrl, "UTF-8");
                    } catch (Exception ignored) {
                    }
                    if (GlobalConfig.DEBUG) {
                        Log.d(TAG, "linkUrl=" + linkUrl);
                    }
                    shareBySystem(context, linkUrl, "", null, pkgName);
                    return true;
                }
            }
        } catch (Exception e) {
            if (GlobalConfig.DEBUG) {
                throw new RuntimeException("Exception e", e);
            }
            return false;
        }

        return false;
    }

    private static void shareBySystem(Context context, String message, String subject, Uri uri, String pkgName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (uri != null) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        } else {
            intent.setType("text/plain");
        }

        intent.setPackage(pkgName);

        if (!TextUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }

        if (!TextUtils.isEmpty(message)) {
            intent.putExtra(Intent.EXTRA_TEXT, message);
            if (uri != null) {
                intent.putExtra("sms_body", message);
                intent.putExtra("content", message);
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            if (GlobalConfig.DEBUG) {
                throw new RuntimeException("shareBySystem Exception e", e);
            }
        }
    }

    /**
     * 检测是否安装应用
     * @param c
     * @param pkgName
     * @return
     */
    private static boolean isInstalled(Context c, String pkgName) {
        return PackageInfoUtil.isInstalled(c, pkgName);
    }

}
