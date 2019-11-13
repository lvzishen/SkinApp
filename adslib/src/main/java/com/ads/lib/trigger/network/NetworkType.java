package com.ads.lib.trigger.network;

/**
 * @author: tyf1946
 * @description 网络类型
 * @date: Create in 上午10:11 18-12-21
 */
public enum NetworkType {
    NETWORK_ETHERNET("Ethernet"),
    NETWORK_WIFI("WiFi"),
    NETWORK_4G("4G"),
    NETWORK_3G("3G"),
    NETWORK_2G("2G"),
    NETWORK_UNKNOWN("Unknown"),
    NETWORK_NO("No network");

    private String desc;
    NetworkType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }

}
