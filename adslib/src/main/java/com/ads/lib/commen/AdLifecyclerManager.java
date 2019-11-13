package com.ads.lib.commen;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ads.lib.ModuleConfig;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by zhaozhiwen on 2019/4/3.
 */
public class AdLifecyclerManager {

    public static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = "LifecycleManager";

    private static AdLifecyclerManager INSTANCE;

    @NonNull
    private final ConcurrentHashMap<String, LifecycleListener> mLifecycleListeners;
    @NonNull
    private WeakReference<Activity> mMainActivity;

    private WeakReference<Activity> mFixedActivity;

    private Activity mStrongActivity;


    private AdLifecyclerManager(Context context) {
        mLifecycleListeners = new ConcurrentHashMap<String, LifecycleListener>();
    }

    @NonNull
    public static synchronized AdLifecyclerManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AdLifecyclerManager(context);
        }
        return INSTANCE;
    }


    public void updateMainActivity(@Nullable Activity activity) {
        if (DEBUG) {
            Log.d(TAG, "updateMainActivity ");
        }
        if (activity != null) {
            if (mMainActivity != null) {
                mMainActivity.clear();
            }
            mMainActivity = new WeakReference<Activity>(activity);
        }
    }

    public Activity getFixedActivity() {
        Activity fixedActivity = mFixedActivity != null ? mFixedActivity.get() : null;
        if (fixedActivity == null && DEBUG) {
            throw new IllegalArgumentException("did you call #setFixedActivity");
        }
        return fixedActivity;
    }

    public void setFixedActivity(Activity fixedActivity) {
        mFixedActivity = new WeakReference<Activity>(fixedActivity);
    }

    public Activity getMainActivity() {

        if (mMainActivity != null) {
            Activity activity = mMainActivity.get();
            if (activity != null) {
                return activity;
            }
        }
        if(mFixedActivity != null){
            Activity activity = mFixedActivity.get();
            if(activity != null){
                return activity;
            }
        }
        if(mStrongActivity != null){
            return mStrongActivity;
        }
        return null;
    }

    public void setStrongActivity(Activity activity){
        this.mStrongActivity = activity;
    }

    public void recycleStrongActivity(){
        mStrongActivity = null;
    }

    public void destroyActivity() {

//        mMainActivity = null;
    }
}
