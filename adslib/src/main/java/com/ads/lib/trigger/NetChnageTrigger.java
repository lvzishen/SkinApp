package com.ads.lib.trigger;

import com.ads.lib.trigger.listener.EventType;
import com.ads.lib.trigger.network.NetStateChangeObserver;
import com.ads.lib.trigger.network.NetStateChangeReceiver;
import com.ads.lib.trigger.network.NetworkType;

/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 上午10:38 18-12-21
 */
public class NetChnageTrigger extends AbsTrigger implements NetStateChangeObserver {


    @Override
    public void oepnTrigger() {
        registerNetChangeTrigger();
    }

    @Override
    public void terminationTrigger() {
        unregisterNetChangeTrigger();
    }

    /**
     * 注册网络变化触发器
     */
    private void registerNetChangeTrigger() {
        NetStateChangeReceiver.registerReceiver(mContext);
        NetStateChangeReceiver.registerObserver(this);
    }

    /**
     * 取消注册网络变化触发器
     */
    private void unregisterNetChangeTrigger() {
        NetStateChangeReceiver.unregisterObserver(this);
        NetStateChangeReceiver.unregisterReceiver(mContext);
    }

    @Override
    public void onNetDisconnected() {
        if (mEventListener != null) {
            mEventListener.onEventComming(EventType.EVENT_NET_CHANGE);
        }
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        if (mEventListener != null) {
            mEventListener.onEventComming(EventType.EVENT_NET_CHANGE);
        }
    }


}
