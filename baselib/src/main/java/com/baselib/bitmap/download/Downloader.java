package com.baselib.bitmap.download;

import android.content.Context;

public interface Downloader {

    /**
     * 加载图片接口
     *
     * @param urlString 图片地址
     */
    byte[] download(Context context, String urlString);

}
