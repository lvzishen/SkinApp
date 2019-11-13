package com.ads.lib.trigger;

import android.content.Context;
import android.util.Log;

import com.ads.lib.concurrent.ThreadHelper;
import com.ads.lib.trigger.config.EventTriggerConfig;
import com.ads.lib.trigger.listener.EventListener;
import com.ads.lib.trigger.listener.EventType;
import com.hotvideo.config.GlobalConfig;


/**
 * @author: tyf1946
 * @description please add a description here
 * @date: Create in 上午10:23 18-12-21
 */
public class EventTrigger implements EventListener {

    private static final String TAG = "Stark.EventTrigger";

    private static volatile EventTrigger mInstance;

    private static final boolean DEBUG = GlobalConfig.DEBUG;

    private Context mContext;

    /**
     * 事件回调
     */
    private EventListener mEventListener;
    /**
     * 网络变化触发器
     */
    private NetChnageTrigger mNetChnageTrigger;
    /**
     * 电量变化触发器
     */
    private BatteryChangeTrigger mBatteryChangeTrigger;
    /**
     * 定时任务触发器
     */
    private TimesUpTrigger mTimesUpTrigger;
    private ScreenMonitorTrigger mScreenMonitorTrigger;


    private EventTrigger(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static EventTrigger getInstance(Context context) {
        if (mInstance == null) {
            synchronized (EventTrigger.class) {
                if (mInstance == null) {
                    mInstance = new EventTrigger(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置事件回调
     *
     * @param eventListener
     */
    public void setEventListener(EventListener eventListener) {
        this.mEventListener = eventListener;
    }

    /**
     * 开启所有的触发器
     */
    public void openAllTrigger() {
        if(!EventTriggerConfig.getInstance(mContext).isModuleEnable()){
            if(DEBUG){
                Log.d(TAG,"#openAllTrigger trigger disable");
            }
            return;
        }
        openTimerTrigger();
        openBatteryChangeTrigger();
        openNetChangeTrigger();
        openScreenMonitorTrigger();

    }

    /**
     * 终止所有的触发器
     */
    public void terminationAllTrigger() {
        terminationTimerTrigger();
        terminationBatteryChangeTrigger();
        terminationNetChangeTrigger();
        terminationScreenMonitorTrigger();
    }


    /**
     * 开启屏幕状态监控触发器
     */
    private void openScreenMonitorTrigger() {
        if(!EventTriggerConfig.getInstance(mContext).isScreenTriggerEnable()){
            if(DEBUG){
                Log.d(TAG,"#openScreenMonitorTrigger trigger disable");
            }
            return;
        }
        if(mScreenMonitorTrigger == null){
            mScreenMonitorTrigger = new ScreenMonitorTrigger();
            mScreenMonitorTrigger.initTrigger(mContext ,this);
            mScreenMonitorTrigger.oepnTrigger();
        }
    }


    /**
     * 关闭屏幕状态监控触发器
     */
    private void terminationScreenMonitorTrigger() {
        if (mScreenMonitorTrigger != null) {
            mScreenMonitorTrigger.terminationTrigger();
        }
    }


    /**
     * 开启网络变化触发器
     */
    private void openNetChangeTrigger() {
        if(!EventTriggerConfig.getInstance(mContext).isNetChangeTriggerEnable()){
            if(DEBUG){
                Log.d(TAG,"#openNetChangeTrigger trigger disable");
            }
            return;
        }
        if (mNetChnageTrigger == null) {
            mNetChnageTrigger = new NetChnageTrigger();
            mNetChnageTrigger.initTrigger(mContext, this);
            mNetChnageTrigger.oepnTrigger();
        }
    }

    /**
     * 关闭网络变化触发器
     */
    private void terminationNetChangeTrigger() {
        if (mNetChnageTrigger != null) {
            mNetChnageTrigger.terminationTrigger();
        }
    }

    /**
     * 开启电量变化触发器
     */
    private void openBatteryChangeTrigger() {
        if(!EventTriggerConfig.getInstance(mContext).isBatteryTriggerEnable()){
            if(DEBUG){
                Log.d(TAG,"#openBatteryChangeTrigger trigger disable");
            }
            return;
        }
        if (mBatteryChangeTrigger == null) {
            mBatteryChangeTrigger = new BatteryChangeTrigger();
            mBatteryChangeTrigger.initTrigger(mContext, this);
            mBatteryChangeTrigger.oepnTrigger();
        }
    }

    /**
     * 关闭电量变化触发器
     */
    private void terminationBatteryChangeTrigger() {
        if (mBatteryChangeTrigger != null) {
            mBatteryChangeTrigger.terminationTrigger();
        }
    }


    /**
     * 开启定时任务触发器
     */
    private void openTimerTrigger() {
        if(!EventTriggerConfig.getInstance(mContext).isTimerTriggerEnable()){
            if(DEBUG){
                Log.d(TAG,"#openTimerTrigger trigger disable");
            }
            return;
        }
        if (mTimesUpTrigger == null) {
            mTimesUpTrigger = new TimesUpTrigger();
            mTimesUpTrigger.initTrigger(mContext, this);
            mTimesUpTrigger.oepnTrigger();
        }
    }

    /**
     * 关闭定时任务触发器
     */
    private void terminationTimerTrigger() {
        if (mTimesUpTrigger != null) {
            mTimesUpTrigger.terminationTrigger();
        }
    }

    @Override
    public void onEventComming(final EventType eventType) {
        if (mEventListener == null) {
            return;
        }
        ThreadHelper.getInstance().postMainThread(new Runnable() {
            @Override
            public void run() {
                mEventListener.onEventComming(eventType);
            }
        });

    }

}
