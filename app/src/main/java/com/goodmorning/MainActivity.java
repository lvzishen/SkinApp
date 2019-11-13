package com.goodmorning;


import android.os.Bundle;

import com.ads.lib.commen.AdLifecyclerManager;
import com.baselib.ui.activity.BaseActivity;
import com.goodmorning.splash.SplashLifeMonitor;
import com.cleanerapp.supermanager.R;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdLifecyclerManager.getInstance(getApplicationContext()).setFixedActivity(this);
        setContentView(R.layout.mainactivity_news);
        setStatusBarColor(getResources().getColor(R.color.transparent));
        setAndroidNativeLightStatusBar(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            if (DEBUG) {
//                Log.i(TAG, "onWindowFocusChanged: ");
//            }
            SplashLifeMonitor.closeSplash(getApplicationContext());
        }
    }

    @Override
    protected boolean useStartDefaultAnim() {
        return false;
    }
}
