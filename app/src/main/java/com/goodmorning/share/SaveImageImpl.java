package com.goodmorning.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

/**
 * 创建日期：2019/11/26 on 11:14
 * 描述:
 * 作者: lvzishen
 */
public class SaveImageImpl implements ISaveImage {


    @Override
    public String saveImage(Context context, String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator);
        if (GlobalConfig.DEBUG) {
            Log.i("SaveImageImpl", "Save Path: " + appDir.getAbsolutePath());
        }
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
