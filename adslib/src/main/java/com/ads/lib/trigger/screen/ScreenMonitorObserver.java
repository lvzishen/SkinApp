package com.ads.lib.trigger.screen;

public interface ScreenMonitorObserver {

    /**
     * 此时屏幕已经点亮，但可能是在锁屏状态
     * 比如用户之前锁定了屏幕，按了电源键启动屏幕，则回调此方法
     */
    void onScreenOn();

    /**
     * 屏幕被锁定
     */
    void onScreenOff();

    /**
     * 屏幕解锁且可以正常使用
     */
    void onUserPresent();

}
