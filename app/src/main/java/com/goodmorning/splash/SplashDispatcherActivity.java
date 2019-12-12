package com.goodmorning.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.lib.trigger.network.NetworkUtils;
import com.baselib.cloud.CloudPropertyManager;
import com.baselib.sp.SharedPref;
import com.baselib.ui.CommonConstants;
import com.baselib.ui.activity.BaseActivity;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.bean.CloudPicture;
import com.goodmorning.manager.ImageLoader;
import com.goodmorning.utils.CheckUtils;
import com.goodmorning.utils.CloudControlUtils;
import com.goodmorning.utils.ResUtils;

import org.saturn.splash.view.SplashMainActivity;
import org.thanos.netcore.helper.JsonHelper;

public class SplashDispatcherActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private ImageView ivSplashPic;
    private ImageView ivSplashIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_dispatcher);
        setStatusBarColor(ResUtils.getColor(R.color.transparent));
        setAndroidNativeLightStatusBar(true);
        initView();
        initData();
        Intent intent = new Intent();
        if (isAgree()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoHomeActivity();
                }
            },3000);

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

    private void initView(){
        ivSplashPic = findViewById(R.id.iv_splash_pic);
        ivSplashIcon = findViewById(R.id.splash_welcome_icon);
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

    private void initData(){
        CloudPicture cloudPicture = CheckUtils.checkCloudPic("splash_pic");
        if (cloudPicture != null){
            ivSplashPic.setVisibility(View.VISIBLE);
            ImageLoader.displayImageByName(this, cloudPicture.getPicUrl(),R.drawable.splash_bg,R.drawable.splash_bg,ivSplashPic);
        }else {
            ivSplashPic.setVisibility(View.GONE);
        }
    }
}
