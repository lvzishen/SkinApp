package com.goodmorning.share.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;


import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Created by jiataibin on 2016/12/2.
 */

public class FileOpenUtil {


    public static void audio(Context ctx, String path) {
        video(ctx, path, false);
    }

    public static void video(Context ctx, String path) {
        video(ctx, path, true);
    }

    private static void video(Context ctx, String path, boolean isVideo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, isVideo ? "video/*" : "audio/*");
        try {
            ctx.startActivity(intent);
        } catch (Throwable t) {

        }
    }

    /**
     * 打开文件
     *
     * @param file
     */
    public static void openFile(Context ctx, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //data是file类型,忘了复制过来
            uri = FileProvider.getUriForFile(
                    ctx,
                    ctx.getPackageName() + ".FileProvider",
                    file);
            intent.setDataAndType(/*uri*/uri, type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
            intent.setDataAndType(/*uri*/uri, type);
        }
        List<ResolveInfo> resInfo = ctx.getPackageManager().queryIntentActivities(intent, 0);
        if (resInfo.isEmpty()) {
//            ToastUtils.showToast(Toast.makeText(ctx, R.string.no_suitable_app_found, Toast.LENGTH_LONG));
            return;
        }
        try {
            ctx.startActivity(intent);
        } catch (Throwable t) {

        }

    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fileName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex <= 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String ext = fileName.substring(dotIndex + 1).toLowerCase(Locale.US);
        if (TextUtils.isEmpty(ext)) {
            return type;
        }

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(ext);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMETypeByMap(File file) {

        String type = "*/*";
        String fileName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex <= 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fileName.substring(dotIndex, fileName.length()).toLowerCase(Locale.US);
        if (end == null || end.length() == 0) {
            return type;
        }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MAP.length; i++) {
            if (end.equals(MIME_MAP[i][0])) {
                type = MIME_MAP[i][1];
            }
        }
        return type;
    }

    private static final String[][] MIME_MAP = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "video/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

//    public static String getFileSuffix(String filePath) {
//        String suffix = "";
//        File file = new File(filePath);
//        String fileName = file.getName();
//        int dotIndex = fileName.lastIndexOf('.');
//        if (dotIndex <= 0) {
//            return suffix;
//        }
//        if (!TextUtils.isEmpty(fileName)) {
//            suffix = fileName.substring(dotIndex, fileName.length()).toLowerCase(Locale.ENGLISH);
//        }
//        return suffix;
//    }

//    public static boolean isVideoSuffix(String suffix) {
//        switch (suffix) {
//            case ".avi":
//            case ".flv":
//            case ".f4v":
//            case ".m4v":
//            case ".m4u":
//            case ".mp4":
//            case ".wmv":
//            case ".mpg":
//            case ".mpg4":
//            case ".mpe":
//            case ".mpeg":
//            case ".3gp":
//            case ".3gpp":
//            case ".rm":
//            case ".rmvb":
//            case ".mov":
//            case ".asx":
//            case ".navi":
//            case ".mkv":
//            case ".ts":
//            case ".dat":
//                return true;
//            default:
//                return false;
//        }
//
//    }
//
//    public static boolean isImageSuffix(String suffix) {
//        switch (suffix) {
//            case ".png":
//            case ".jpg":
//            case ".jpeg":
//            case ".bmp":
//            case ".gif":
//                return true;
//            default:
//                return false;
//        }
//    }
//
//    public static int getDrawableIdBySuffix(String suffix) {
//        int resId = R.drawable.ic_unkown_file;
//        switch (suffix) {
//            case ".doc":
//            case ".docx":
//                resId = R.drawable.ic_word;
//                break;
//            case ".xls":
//            case ".xlsx":
//                resId = R.drawable.ic_excel;
//                break;
//            case ".ppt":
//            case ".pptx":
//                resId = R.drawable.ic_ppt;
//                break;
//            case ".mp3":
//            case ".m4a":
//            case ".m4b":
//            case ".m4p":
//            case ".mpga":
//            case ".mp2":
//            case ".wma":
//            case ".wav":
//            case ".flac":
//            case ".aiff":
//            case ".ape":
//            case ".asf":
//            case ".real":
//            case ".m3u":
//                resId = R.drawable.ic_bigfile_audio;
//                break;
//            case ".avi":
//            case ".flv":
//            case ".f4v":
//            case ".m4v":
//            case ".m4u":
//            case ".mp4":
//            case ".wmv":
//            case ".mpg":
//            case ".mpg4":
//            case ".mpe":
//            case ".mpeg":
//            case ".3gp":
//            case ".3gpp":
//            case ".rm":
//            case ".rmvb":
//            case ".mov":
//            case ".asx":
//            case ".navi":
//            case ".mkv":
//            case ".ts":
//            case ".dat":
//                resId = R.drawable.ic_bigfile_video;
//                break;
//            default:
//                break;
//        }
//        return resId;
//    }
//
//    public static boolean isIdentifiable(String suffix) {
//        if (TextUtils.isEmpty(suffix)) {
//            return false;
//        }
//        switch (suffix) {
//            case ".doc":
//            case ".docx":
//            case ".xls":
//            case ".xlsx":
//            case ".ppt":
//            case ".pptx":
////                resId = R.drawable.ic_dc_file_text2;
////                break;
//            case ".txt":
////                resId = R.drawable.ic_dc_file_text;
////                break;
//                return false;
//            case ".mp3":
//            case ".m4a":
//            case ".m4b":
//            case ".m4p":
//            case ".mpga":
//            case ".mp2":
////            case ".wma":
//            case ".wav":
//            case ".flac":
//            case ".aiff":
//            case ".ape":
//            case ".asf":
//            case ".real":
//            case ".m3u":
////                resId = R.drawable.ic_dc_file_audios;
////                break;
//            case ".avi":
//            case ".flv":
//            case ".f4v":
//            case ".m4v":
//            case ".m4u":
//            case ".mp4":
////            case ".wmv":
//            case ".mpg":
//            case ".mpg4":
//            case ".mpe":
//            case ".mpeg":
//            case ".3gp":
//            case ".3gpp":
//            case ".rm":
//            case ".rmvb":
//            case ".mov":
////            case ".asx":
////            case ".navi":
//            case ".mkv":
////            case ".ts":
////            case ".dat":
////                resId = R.drawable.ic_dc_file_video;
////                break;
//
//            case ".png":
//            case ".jpg":
//            case ".jpeg":
////                resId = R.drawable.ic_dc_file_photo;
////                break;
//                return true;
//            case ".zip":
////            case ".rar":
////                resId = R.drawable.ic_dc_file_package;
////                break;
//            default:
//                break;
//        }
//        return false;
//    }
}
