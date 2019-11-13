package com.ads.lib.trigger;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.ads.lib.trigger.config.EventTriggerConfig;
import com.ads.lib.trigger.listener.EventListener;
import com.ads.lib.trigger.listener.EventType;


/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 下午12:08 18-12-21
 */
public class TimesUpTrigger extends AbsTrigger {

    /**
     * 定时任务时间
     */
    public long TRIGGER_TIME;

    public static final int MESSAGE_TIMES_UP = 10;

    private HandlerThread mTimeTriggerThread;

    Handler mTriggerHandler;

    @Override
    public void initTrigger(Context context, EventListener eventListener) {
        this.mContext = context.getApplicationContext();
        this.mEventListener = eventListener;
        this.TRIGGER_TIME = EventTriggerConfig.getInstance(context).getTimerInterval();
        initThread();
        initTimerHandler();
    }

    /**
     * 初始化Handler
     */
    private void initTimerHandler() {
        mTriggerHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_TIMES_UP) {
                    if (mEventListener != null) {
                        mEventListener.onEventComming(EventType.EVENT_TIMES_UP);
                    }
                    runTimer();
                }
            }
        };
    }

    /**
     * 初始化异步
     */
    private void initThread() {
        mTimeTriggerThread = new HandlerThread("time_trigger");
        mTimeTriggerThread.start();
    }

    @Override
    public void oepnTrigger() {
        runTimer();
    }

    @Override
    public void terminationTrigger() {
        stopTimer();
    }

    private void runTimer() {
        mTriggerHandler.sendEmptyMessageDelayed(MESSAGE_TIMES_UP, TRIGGER_TIME);
    }

    private void stopTimer() {
        mTriggerHandler.removeMessages(MESSAGE_TIMES_UP);
    }

}
