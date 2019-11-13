package com.ads.lib.trigger;

import android.content.Context;

import com.ads.lib.trigger.listener.EventListener;


/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 上午11:16 18-12-21
 */
public abstract class AbsTrigger implements ITrigger {

    protected Context mContext;

    protected EventListener mEventListener;

    @Override
    public void initTrigger(Context context, EventListener eventListener) {
        this.mContext = context.getApplicationContext();
        this.mEventListener = eventListener;
    }


}
