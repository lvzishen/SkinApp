package com.goodmorning.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.baselib.sp.SharedPref;
import com.baselib.ui.CommonConstants;
import com.baselib.ui.activity.BaseActivity;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.utils.ResUtils;

import org.saturn.splash.view.SplashMainActivity;

public class SplashDispatcherActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_dispatcher);
        setStatusBarColor(ResUtils.getColor(R.color.white));
        setAndroidNativeLightStatusBar(true);
        Intent intent = new Intent();
        if (isAgree()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoHomeActivity();
                }
            },1500);

//            boolean showPressAgree = 1 == CloudPropertyManagerBridge.getInt(this, CloudPropertyManagerBridge.PATH_OUT_APP_JUMP_MAIN_PROP, "op_charge_news", 0);
//            if (showPressAgree && (!isShowedPressAgree())) {
//                intent.setClass(this, SplashAllowPressActivity.class);
//            } else {
//            intent.setClass(this, SplashMainActivity.class);
//            }
        } else {
            intent.setClass(this, SplashActivity.class);
            intent.putExtra(CommonConstants.EXTRA_TYPE_ENTER_ANIM, CommonConstants.TYPE_ENTER_ANIM_RIGHT);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
//        CleanTotalUtils.saveInstallTime(this.getApplicationContext());

    }

    private void gotoHomeActivity() {
        Intent intent = new Intent(SplashDispatcherActivity.this, MainActivity.class);
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

    /**
     * 设置状态栏颜色
     *
     * @param color 颜色值
     */
    protected void setStatusBarColor(int color) {
        getWindow().setStatusBarColor(color);
    }

    protected void setAndroidNativeLightStatusBar(boolean dark) {
        //6.0以下不起作用
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return;
//        }
        View decor = this.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
