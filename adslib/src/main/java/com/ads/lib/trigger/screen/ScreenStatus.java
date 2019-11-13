package com.ads.lib.trigger.screen;

/**
 * @author: tyf1946
 * @description 屏幕状态
 * @date: Create in 上午11:43 18-12-21
 */
public enum ScreenStatus {

    SCREEN_ON("Screen On"),
    SCREEN_OFF("Screen Off"),
    USER_PRESENT("User Present");

    private String desc;

    ScreenStatus(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
