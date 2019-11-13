package com.ads.lib.trigger.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 上午11:34 18-12-21
 */
public class BatteryChangeReceiver extends BroadcastReceiver {


    private static class InstanceHolder {
        private static final BatteryChangeReceiver INSTANCE = new BatteryChangeReceiver();
    }

    private List<BatteryChnageObserver> mObservers = new ArrayList<>();

    private BatteryChangeReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            notifyObservers(PowerType.POWER_CONNECT);
        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            notifyObservers(PowerType.POWER_DISCONNECT);
        } else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            notifyObservers(PowerType.POWER_CHANGE);
        }
    }


    /**
     * 注册电量监听
     */
    public static void registerReceiver(@NonNull Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        try {
            context.registerReceiver(BatteryChangeReceiver.InstanceHolder.INSTANCE, intentFilter);
        } catch (Exception e) {

        }
    }

    /**
     * 取消电量监听
     */
    public static void unregisterReceiver(@NonNull Context context) {
        context.unregisterReceiver(BatteryChangeReceiver.InstanceHolder.INSTANCE);
    }

    /**
     * 注册电量变化Observer
     */
    public static void registerObserver(BatteryChnageObserver observer) {
        if (observer == null)
            return;
        if (!BatteryChangeReceiver.InstanceHolder.INSTANCE.mObservers.contains(observer)) {
            BatteryChangeReceiver.InstanceHolder.INSTANCE.mObservers.add(observer);
        }
    }

    /**
     * 取消网络变化Observer的注册
     */
    public static void unregisterObserver(BatteryChnageObserver observer) {
        if (observer == null)
            return;
        if (BatteryChangeReceiver.InstanceHolder.INSTANCE.mObservers == null)
            return;
        BatteryChangeReceiver.InstanceHolder.INSTANCE.mObservers.remove(observer);
    }


    /**
     * 通知所有的Observer电量状态变化
     */
    private void notifyObservers(PowerType powerType) {
        if (powerType == PowerType.POWER_DISCONNECT) {
            for (BatteryChnageObserver observer : mObservers) {
                observer.onPowerDisconnected();
            }
        } else {
            for (BatteryChnageObserver observer : mObservers) {
                observer.onPowerConnected(powerType);
            }
        }
    }


}
