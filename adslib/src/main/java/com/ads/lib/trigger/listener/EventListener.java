package com.ads.lib.trigger.listener;

/**
 * @author: tyf1946
 * @description 事件监听器
 * @date: Create in 上午10:56 18-12-21
 */
public interface EventListener {

    /**
     * 事件到来的监听
     */
    void onEventComming(EventType eventType);

}
