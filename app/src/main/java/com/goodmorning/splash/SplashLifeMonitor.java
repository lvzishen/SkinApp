package com.goodmorning.splash;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apus on 2018/9/7.
 *
 * 控制闪屏的退出比较麻烦，所有需要有个地方进行控制
 */

public class SplashLifeMonitor {

    /**
     * 闪屏应该去继承这个
     */
    public static interface ISplash{
        public void close();
    }

    private static List<ISplash>  sSplashes = new ArrayList<ISplash>();

    /**
     * 当需要闪屏关闭的时候调用
     * @param cxt
     */
    public static void closeSplash(Context cxt){
        synchronized (sSplashes){
            for(ISplash splash : sSplashes){
                splash.close();
            }
			sSplashes.clear();
        }
    }

    /**
     * 当闪屏打开的时候调用
     * @param splash
     */
    public static void onSplashOpen(ISplash splash){
        synchronized (sSplashes){
            sSplashes.add(splash);
        }
    }
}
