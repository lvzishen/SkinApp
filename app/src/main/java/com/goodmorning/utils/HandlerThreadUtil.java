package com.goodmorning.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * 系统中需要使用handlerThread的地方比较多，干脆封装成一个通用的，这样
 * 避免造成资源浪费
 */

public class HandlerThreadUtil {
    static HandlerThread sHt = null;

    public static synchronized Looper getNonUiLooper(){
        if(sHt == null){
            sHt = new HandlerThread("nonUi");
            sHt.start();
        }
        return sHt.getLooper();
    }


    private static Handler sHandler = null;
    public static synchronized Handler getMainHandler(){
        if(sHandler == null){
            sHandler = new Handler(Looper.getMainLooper());
        }
        return sHandler;
    }
}
