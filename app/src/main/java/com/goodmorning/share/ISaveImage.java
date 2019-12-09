package com.goodmorning.share;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 创建日期：2019/11/26 on 11:14
 * 描述:
 * 作者: lvzishen
 */
public interface ISaveImage {
    String saveImage(Context context, String picName, Bitmap bitmap);

    String saveVideo(Context context, String videoName, String videoUrl);
}
