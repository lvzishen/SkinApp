package com.goodmorning.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import org.interlaken.common.XalContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 创建日期：2019/11/22 on 11:03
 * 描述:
 * 作者: lvzishen
 */
public class DownloadPicUtils {


    /**
     * 将URL转化成bitmap形式
     *
     * @param url
     * @return bitmap type
     */
    public static Bitmap returnBitMap(String url) {
        URL myFileUrl;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存图片到本地相册
     */
    private static void saveImageToPhotos(Context context, Bitmap bmp, IDownloadResultListener iDownloadResultListener) {
        Handler sHandler = new Handler(Looper.getMainLooper());
        String appName = XalContext.getAppName();
        File appDir = new File(Environment.getExternalStorageDirectory(), appName);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (iDownloadResultListener != null) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDownloadResultListener.onFailure();
                    }
                });
            }
            return;
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        if (iDownloadResultListener != null) {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    iDownloadResultListener.onSuccess();
                }
            });
        }
    }

    public interface IDownloadResultListener {
        void onSuccess();

        void onFailure();
    }

}
