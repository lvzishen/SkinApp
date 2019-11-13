package com.ads.lib.trigger.battery;

/**
 * @author: tyf1946
 * @description 电量类型
 * @date: Create in 上午11:43 18-12-21
 */
public enum PowerType {

    POWER_CONNECT("Power Connect"),
    POWER_CHANGE("Power Change"),
    POWER_DISCONNECT("Power Disconnect");

    private String desc;

    PowerType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
