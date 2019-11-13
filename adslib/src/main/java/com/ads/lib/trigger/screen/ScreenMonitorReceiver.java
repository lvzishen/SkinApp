package com.ads.lib.trigger.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ScreenMonitorReceiver extends BroadcastReceiver {

    private static class InstanceHolder {
        private static final ScreenMonitorReceiver INSTANCE = new ScreenMonitorReceiver();
    }

    private ScreenMonitorReceiver(){}

    private List<ScreenMonitorObserver> mObservers = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            notifyObservers(ScreenStatus.SCREEN_ON);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            notifyObservers(ScreenStatus.SCREEN_OFF);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            notifyObservers(ScreenStatus.USER_PRESENT);
        }

    }

    /**
     * 注册屏幕监听
     */
    public static void registerReceiver(@NonNull Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        try {
            context.registerReceiver(ScreenMonitorReceiver.InstanceHolder.INSTANCE, filter);
        } catch (Exception e) {

        }
    }


    /**
     * 取消屏幕监听
     */
    public static void unregisterReceiver(@NonNull Context context) {
        context.unregisterReceiver(ScreenMonitorReceiver.InstanceHolder.INSTANCE);
    }


    /**
     * 注册屏幕变化Observer
     */
    public static void registerObserver(ScreenMonitorObserver observer) {
        if (observer == null)
            return;
        if (!ScreenMonitorReceiver.InstanceHolder.INSTANCE.mObservers.contains(observer)) {
            ScreenMonitorReceiver.InstanceHolder.INSTANCE.mObservers.add(observer);
        }
    }

    /**
     * 取消屏幕变化Observer的注册
     */
    public static void unregisterObserver(ScreenMonitorObserver observer) {
        if (observer == null)
            return;
        if (ScreenMonitorReceiver.InstanceHolder.INSTANCE.mObservers == null)
            return;
        ScreenMonitorReceiver.InstanceHolder.INSTANCE.mObservers.remove(observer);
    }

    /**
     * 通知所有的Observer屏幕状态变化
     */
    private void notifyObservers(ScreenStatus status) {
        if (status == ScreenStatus.SCREEN_ON) {
            for (ScreenMonitorObserver observer : mObservers) {
                observer.onScreenOn();
            }
        } else if (status == ScreenStatus.SCREEN_OFF){
            for (ScreenMonitorObserver observer : mObservers) {
                observer.onScreenOff();
            }
        } else if (status == ScreenStatus.USER_PRESENT){
            for (ScreenMonitorObserver observer : mObservers) {
                observer.onUserPresent();
            }
        }
    }

}
