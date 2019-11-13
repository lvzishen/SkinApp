package com.hotvideo.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.baselib.sp.SharedPref;
import com.baselib.ui.CommonConstants;
import com.cleanerapp.supermanager.R;

import org.saturn.splash.view.SplashMainActivity;

public class SplashDispatcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_dispatcher);
        Intent intent = new Intent();
        if (isAgree()) {
//            boolean showPressAgree = 1 == CloudPropertyManagerBridge.getInt(this, CloudPropertyManagerBridge.PATH_OUT_APP_JUMP_MAIN_PROP, "op_charge_news", 0);
//            if (showPressAgree && (!isShowedPressAgree())) {
//                intent.setClass(this, SplashAllowPressActivity.class);
//            } else {
            intent.setClass(this, SplashMainActivity.class);
//            }
        } else {
            intent.setClass(this, SplashActivity.class);
        }
//        CleanTotalUtils.saveInstallTime(this.getApplicationContext());
        intent.putExtra(CommonConstants.EXTRA_TYPE_ENTER_ANIM, CommonConstants.TYPE_ENTER_ANIM_RIGHT);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }


    private boolean isAgree() {
        return SharedPref.getBoolean(getApplicationContext(), CommonConstants.KEY_HAS_AGREEMENT_SPLASH, false);
    }

    private boolean isShowedPressAgree() {
        return SharedPref.getBoolean(getApplicationContext(), CommonConstants.KEY_HAS_SHOWED_PRESS_AGREEMENT, false);
    }

}
