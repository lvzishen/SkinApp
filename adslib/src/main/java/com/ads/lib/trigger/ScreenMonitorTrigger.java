package com.ads.lib.trigger;


import com.ads.lib.trigger.listener.EventType;
import com.ads.lib.trigger.screen.ScreenMonitorObserver;
import com.ads.lib.trigger.screen.ScreenMonitorReceiver;

/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 下午12:02 18-12-21
 */
public class ScreenMonitorTrigger extends AbsTrigger implements ScreenMonitorObserver {

    @Override
    public void oepnTrigger() {
        registerScreenMonitorTrigger();

    }

    @Override
    public void terminationTrigger() {
        unregisterScreenMonitorTrigger();

    }

    /**
     * 注册电量变化触发器
     */
    private void registerScreenMonitorTrigger() {
        ScreenMonitorReceiver.registerReceiver(mContext);
        ScreenMonitorReceiver.registerObserver(this);

    }


    /**
     * 取消注册电量变化触发器
     */
    private void unregisterScreenMonitorTrigger() {
        ScreenMonitorReceiver.unregisterObserver(this);
        ScreenMonitorReceiver.unregisterReceiver(mContext);

    }



    @Override
    public void onScreenOn() {
        callbackListener();
    }

    @Override
    public void onScreenOff() {
        callbackListener();

    }

    @Override
    public void onUserPresent() {
        callbackListener();

    }

    private void callbackListener(){
        if (mEventListener != null) {
            mEventListener.onEventComming(EventType.EVENT_SCREEN_STATUS_CHANGE);
        }
    }
}
