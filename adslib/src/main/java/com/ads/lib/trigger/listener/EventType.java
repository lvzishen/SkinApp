package com.ads.lib.trigger.listener;

/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 上午10:57 18-12-21
 */
public enum EventType {
    EVENT_NET_CHANGE("Net Change"),
    EVENT_BATTERY_CHANGE("Battery Change"),
    EVENT_TIMES_UP("Times UP"),
    EVENT_DEBUG("Debug"),
    EVENT_SCREEN_STATUS_CHANGE("Screen Status Change"),
    EVENT_CUSTOM("Custom Event");

    private String desc;

    EventType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
