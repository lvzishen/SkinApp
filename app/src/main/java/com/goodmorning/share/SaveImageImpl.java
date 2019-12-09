package com.goodmorning.share;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.creativeindia.goodmorning.R;
import com.goodmorning.config.GlobalConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 创建日期：2019/11/26 on 11:14
 * 描述:
 * 作者: lvzishen
 */
public class SaveImageImpl implements ISaveImage {


    @Override
    public String saveImage(Context context, String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator);
        if (Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor")) {
            appDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + "SunnyDay" + File.separator);
        }
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
            if (!(Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor"))) {
                if (GlobalConfig.DEBUG) {
                    Log.i("SaveImageImpl", "Not huawei");
                }
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            file.getAbsolutePath(), fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                Uri localUri = Uri.fromFile(file);

                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);

                context.sendBroadcast(localIntent);
            }
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.save_to_album), Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public String saveVideo(Context context, String videoName, String videoUrl) {
        File appDir = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator);
        if (GlobalConfig.DEBUG) {
            Log.i("SaveImageImpl", "Save Path: " + appDir.getAbsolutePath());
        }
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        int mediaLength;
        FileOutputStream fos = null;
        InputStream is = null;
        String fileName = videoName + ".mp4";
        File file = new File(appDir, fileName);
        long readSize = file.length();
        try {
            fos = new FileOutputStream(file);

            URL url = new URL(videoName);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "NetFox");
            httpConnection.setRequestProperty("RANGE", "bytes=" + readSize + "-");

            is = httpConnection.getInputStream();

            mediaLength = httpConnection.getContentLength();
            if (mediaLength == -1) {
                return "";
            }
            byte buf[] = new byte[4 * 1024];
            int size = 0;
            while ((size = is.read(buf)) != -1) {   //
                try {
                    fos.write(buf, 0, size);   // 把网络视频文件写入SD卡中
                    readSize += size;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }

        try {
            fos.flush();
            fos.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Uri localUri = Uri.fromFile(file);

        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);

        context.sendBroadcast(localIntent);
        Toast.makeText(context.getApplicationContext(), context.getString(R.string.save_to_album), Toast.LENGTH_SHORT).show();
        return file.getAbsolutePath();
    }
}
