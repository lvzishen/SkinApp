package com.ads.lib.trigger.battery;

/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 上午11:40 18-12-21
 */
public interface BatteryChnageObserver {

    void onPowerDisconnected();

    void onPowerConnected(PowerType powerType);

}
