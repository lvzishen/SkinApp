package com.ads.lib.trigger.network;

/**
 * @author: tyf1946
 * @description 网络状态变化观察者
 * @date: Create in 上午10:11 18-12-21
 */
public interface NetStateChangeObserver {

    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);
}
