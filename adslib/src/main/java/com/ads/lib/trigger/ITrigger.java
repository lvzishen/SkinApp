package com.ads.lib.trigger;

import android.content.Context;

import com.ads.lib.trigger.listener.EventListener;


/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 上午10:36 18-12-21
 */
public interface ITrigger {

    /**
     * 初始化触发器
     * @param context
     * @param eventListener
     */
    void initTrigger(Context context, EventListener eventListener);

    /**
     * 开启触发器
     */
    void oepnTrigger();


    /**
     * 终止触发器
     */
    void terminationTrigger();


}
