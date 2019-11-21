package com.ads.lib.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.ads.lib.ModuleConfig;
import com.ads.lib.commen.AdLifecyclerManager;
import com.ads.lib.util.CoversControl;
import com.facebook.ads.AudienceNetworkActivity;

import org.interlaken.common.utils.Libs;

import java.lang.ref.WeakReference;

/**
 * Created by zhaozhiwen on 2019/4/3.
 */
public class FileAdSDK {

    public static final String TAG = "FileAdSDK";
    public static final boolean DEBUG = ModuleConfig.DEBUG;

    public static void init(Application application, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processSuffix = getProcessSuffix();
            if(DEBUG){
                Log.d(TAG,"processSuffix: " + processSuffix);
            }
            WebView.setDataDirectorySuffix(processSuffix);
        }
        if (application != null) {
            String mainActName = "com.creativeindia.goodmorning.ui.main.MainActivity";
            activityLifecycleCallbacks(application, mainActName);
        }
        //手动初始化广告源
//        AppLovinSdk.initializeSdk(context);
        try {
            AudienceNetworkInitializeHelper.initialize(context);
        } catch (Exception e) {

        }

    }

    private static String getProcessSuffix(){
        String processName = Libs.getCurrentProcessName();
        if(!TextUtils.isEmpty(processName)){
            int lastPointIndex = processName.lastIndexOf(".");
            if(lastPointIndex != -1){
                return processName.substring(lastPointIndex);
            }
            return processName;
        }
        return "process";
    }

    private static void activityLifecycleCallbacks(Application application, final String sMainActName) {
        ActivityLifecycleCallbackImpl mgr = new ActivityLifecycleCallbackImpl(sMainActName);
        CoversControl.getInstance().setActivityCapture(mgr);
        application.registerActivityLifecycleCallbacks(mgr);


    }

    public static class ActivityLifecycleCallbackImpl implements Application.ActivityLifecycleCallbacks, CoversControl.IActivityCapture {

        private String mainActivityName;
        private String captureActivityName;
        private WeakReference<Activity> captureActivityRef;

        public ActivityLifecycleCallbackImpl(String mainActivityName) {
            this.mainActivityName = mainActivityName;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (!activity.getClass().getName().contains("com.cleanerapp")
                    && !activity.getClass().getName().contains("org.saturn.splash.view.SplashMainActivity")
                    && !activity.getClass().getName().contains(AudienceNetworkActivity.class.getName())) {
                return;
            }
            AdLifecyclerManager.getInstance(activity.getApplicationContext()).updateMainActivity(activity);
            if (DEBUG) {
                Log.d(TAG, "sMainActName = " + mainActivityName);
                Log.d(TAG, "activity = " + activity.getLocalClassName());
                Log.d(TAG, "onActivityCreated");
            }

            if (activity.getClass().getName().equals(captureActivityName)) {
                captureActivityRef = new WeakReference<>(activity);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (DEBUG) {
                Log.d(TAG, "onActivityStarted");
            }

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (DEBUG) {
                Log.d(TAG, "onActivityResumed");
            }
            if (!activity.getClass().getName().contains("com.cleanerapp")
                    && !activity.getClass().getName().contains("org.saturn.splash.view.SplashMainActivity")) {
                return;
            }
            AdLifecyclerManager.getInstance(activity.getApplicationContext()).updateMainActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (DEBUG) {
                Log.d(TAG, "activity name = onActivityPaused=" + activity.getClass().getSimpleName());
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (DEBUG) {
                Log.d(TAG, "activity name = onActivityStopped=" + activity.getClass().getSimpleName());
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (DEBUG) {
                Log.d(TAG, "activity name = onActivityDestroyed = " + activity.getClass().getSimpleName());
            }
            AdLifecyclerManager.getInstance(activity.getApplicationContext()).destroyActivity();
            if(activity.getClass().getName().equals(captureActivityName)){
                CoversControl.getInstance().destroy();
            }
        }

        @Override
        public void initCapture(String captureActivityName) {
            this.captureActivityName = captureActivityName;
        }

        @Override
        public Activity getCaptureActivity() {
            if (captureActivityRef != null) {
                return captureActivityRef.get();
            }
            return null;
        }
    }

}
