package com.goodmorning.share;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.creativeindia.goodmorning.R;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.share.util.DownloadUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 创建日期：2019/11/26 on 11:14
 * 描述:
 * 作者: lvzishen
 */
public class SaveImageImpl implements ISaveImage {


    @Override
    public String saveImage(Context context, String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + "SunnyDay" + File.separator);
        if (GlobalConfig.DEBUG) {
            Log.i("SaveImageImpl", "Save Path: " + appDir.getAbsolutePath());
        }
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            ContentValues values = new ContentValues();
            ContentResolver resolver = context.getContentResolver();
            values.put(MediaStore.Images.ImageColumns.DATA, file.getAbsolutePath());
            values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpg");
            //将图片的拍摄时间设置为当前的时间
            values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis() + "");
            Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                FileOutputStream fos = (FileOutputStream) resolver.openOutputStream(uri);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.save_to_album), Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public String saveVideo(Context context, String videoName, String videoUrl) {
        String saveDir = Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + "SunnyDay" + File.separator;
        if (GlobalConfig.DEBUG) {
            Log.i("SaveImageImpl", "saveDir :" + saveDir);
        }
        DownloadUtil.get().download(videoUrl, saveDir, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (GlobalConfig.DEBUG) {
                            Log.i("SaveImageImpl", "下载完成");
                        }
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                        ContentResolver localContentResolver = context.getContentResolver();
                        ContentValues localContentValues = getVideoContentValues(context, file, System.currentTimeMillis());
                        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
                        Toast.makeText(context.getApplicationContext(), context.getString(R.string.save_to_album), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDownloading(int progress) {
//                progressBar.setProgress(progress);
            }

            @Override
            public void onDownloadFailed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (GlobalConfig.DEBUG) {
                            Log.i("SaveImageImpl", "下载失败");
                        }
                        Toast.makeText(context.getApplicationContext(), context.getString(R.string.news_ui__save_failure), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return "";
    }

    /**
     * 视频存在本地
     *
     * @param paramContext
     * @param paramFile
     * @param paramLong
     * @return
     */
    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/3gp");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

}
