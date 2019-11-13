package com.ads.lib.concurrent;

import android.os.Handler;
import android.os.Looper;

public class ThreadHelper {

    private static volatile ThreadHelper mInstance;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private ThreadHelper() {
    }

    public static ThreadHelper getInstance() {
        if (mInstance == null) {
            synchronized (ThreadHelper.class) {
                if (mInstance == null) {
                    mInstance = new ThreadHelper();
                }
            }
        }
        return mInstance;
    }

    public void postMainThread(Runnable runnable){
        if(Looper.myLooper() == Looper.getMainLooper()){
            runnable.run();
        }else {
            mHandler.post(runnable);
        }
    }

}
