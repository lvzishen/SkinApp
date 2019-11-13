package com.ads.lib.concurrent;


import android.os.Build;

import com.google.android.gms.common.util.concurrent.NamedThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class AdExecutors {

    public static final ThreadPoolExecutor BACKGROUND_EXECUTOR = newNamedThreadPool("background", 5, 20);


    public static ThreadPoolExecutor newNamedThreadPool(String name, int corePoolSize, int maxPoolSize) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize,
                maxPoolSize,
                5, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                new NamedThreadFactory(name));
        allowCoreThreadTimeout(executor, true);
        return executor;
    }


    public static void allowCoreThreadTimeout(ThreadPoolExecutor executor, boolean value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            executor.allowCoreThreadTimeOut(value);
        }
    }
}