package com.ads.lib.trigger;

import com.ads.lib.trigger.battery.BatteryChangeReceiver;
import com.ads.lib.trigger.battery.BatteryChnageObserver;
import com.ads.lib.trigger.battery.PowerType;
import com.ads.lib.trigger.listener.EventType;

/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 下午12:02 18-12-21
 */
public class BatteryChangeTrigger extends AbsTrigger implements BatteryChnageObserver {

    @Override
    public void oepnTrigger() {
        registerBatteryChangeTrigger();

    }

    @Override
    public void terminationTrigger() {
        unregisterBatteryChangeTrigger();

    }

    /**
     * 注册电量变化触发器
     */
    private void registerBatteryChangeTrigger() {
        BatteryChangeReceiver.registerReceiver(mContext);
        BatteryChangeReceiver.registerObserver(this);

    }


    /**
     * 取消注册电量变化触发器
     */
    private void unregisterBatteryChangeTrigger() {
        BatteryChangeReceiver.unregisterObserver(this);
        BatteryChangeReceiver.unregisterReceiver(mContext);

    }


    @Override
    public void onPowerDisconnected() {
        if (mEventListener != null) {
            mEventListener.onEventComming(EventType.EVENT_BATTERY_CHANGE);
        }

    }

    @Override
    public void onPowerConnected(PowerType powerType) {
        if (mEventListener != null) {
            mEventListener.onEventComming(EventType.EVENT_BATTERY_CHANGE);
        }

    }
}
