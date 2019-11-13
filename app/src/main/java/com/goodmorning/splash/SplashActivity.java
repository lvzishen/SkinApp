package com.goodmorning.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baselib.cloud.CloudPropertyManagerBridge;
import com.baselib.sp.SharedPref;
import com.baselib.statistic.StatisticConstants;
import com.baselib.statistic.StatisticLoggerX;
import com.baselib.ui.CommonConstants;
import com.baselib.ui.activity.BaseActivity;
import com.goodmorning.MainActivity;
import com.goodmorning.config.GlobalConfig;
import com.cleanerapp.supermanager.R;
import com.k.permission.CheckCallback;
import com.k.permission.PermissionCheck;
import com.k.permission.PermissionItem;
import com.k.permission.PermissionRequest;


import java.util.ArrayList;
import java.util.List;



public class SplashActivity extends BaseActivity implements View.OnClickListener, SplashLifeMonitor.ISplash {
    private boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "SplashActivity";
    private TextView mContent;

    private View mStartBtn;
    private ImageView mIvPrivacy, mIvUserAgreement, mLogo;
    boolean showPressAgree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SplashLifeMonitor.onSplashOpen(this);
        showPressAgree = 1 == CloudPropertyManagerBridge.getInt(this, CloudPropertyManagerBridge.PATH_OUT_APP_JUMP_MAIN_PROP, "op_charge_news", 1);
        if (isAgree()) {
//            if (showPressAgree && (!isShowedPressAgree())) {
//                gotoPressAgreeActivity();
//            } else {
            gotoHomeActivity();
//            }
            return;
        }
        setStatusBarColor(getResources().getColor(R.color.white));
        setAndroidNativeLightStatusBar(true);
        initView();
    }

    private boolean isAgree() {
        return SharedPref.getBoolean(getApplicationContext(), CommonConstants.KEY_HAS_AGREEMENT_SPLASH, false);
    }


    private void initView() {
        mStartBtn = findViewById(R.id.splash_start_btn);
        mContent = findViewById(R.id.tv_privacy_content);
        mIvPrivacy = findViewById(R.id.iv_privacy);
        mLogo = findViewById(R.id.splash_logo);
        mIvUserAgreement = findViewById(R.id.iv_useragree);
        findViewById(R.id.tv_privacy).setOnClickListener(this);
        findViewById(R.id.tv_useragree).setOnClickListener(this);
        findViewById(R.id.splash_start_btn).setOnClickListener(this);
        mIvPrivacy.setOnClickListener(this);
        mIvUserAgreement.setOnClickListener(this);
        mIvPrivacy.setSelected(true);
        mIvUserAgreement.setSelected(true);
        // Glide.with(this).load(R.drawable.drawable_splash_window).into(mLogo);
        String privacyContentStr = getApplicationContext().getString(R.string.user_terms_n_privacy_link_text);
        mContent.setText(SplashTextHelper.process(getApplicationContext(), privacyContentStr, CommonConstants.USER_AGREEMENT_URL, CommonConstants.PRIVACY_POLICY_URL));
        mContent.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void allowStorgePermissions() {
        final List<PermissionItem> list = new ArrayList<>();
        PermissionItem permissionItem = new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.string_storage), 0);
        permissionItem.needStayWindow = false;
        list.add(permissionItem);
        PermissionRequest permissionRequest = new PermissionRequest.Builder().title("").msg("").showSettingGuide(true).permissions(list).build();
        PermissionCheck.requestPermission(this, permissionRequest, new CheckCallback() {
            @Override
            public void onDeny(String permission, boolean willShowDialog) {
                Toast.makeText(getApplicationContext(), getString(R.string.usage_access_permission_fail_toast), Toast.LENGTH_SHORT).show();
//                if (showPressAgree && (!isShowedPressAgree())) {
//                    gotoPressAgreeActivity();
//                } else {
                gotoHomeActivity();
//                }
                StatisticLoggerX.logShowResult(StatisticConstants.Splash_Page_Guide, StatisticConstants.POPUP_WINDOW, StatisticConstants.FROM_SPLASH, 0, 0);

            }

            @Override
            public void onGranted(String permission) {
                StatisticLoggerX.logShowResult(StatisticConstants.Splash_Page_Guide, StatisticConstants.POPUP_WINDOW, StatisticConstants.FROM_SPLASH, 1, 0);
//                if (showPressAgree && (!isShowedPressAgree())) {
//                    gotoPressAgreeActivity();
//                } else {
                gotoHomeActivity();
//                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSetting(PermissionItem item) {
                if (DEBUG) {
                    Log.d(TAG, ":onSetting ");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_privacy:
                OpenUrlUtils.openDefaultBrower(SplashActivity.this, CommonConstants.PRIVACY_POLICY_URL);
                break;
            case R.id.tv_useragree:
                OpenUrlUtils.openDefaultBrower(SplashActivity.this, CommonConstants.USER_AGREEMENT_URL);
                break;
            case R.id.iv_privacy:
                if (mIvPrivacy.isSelected()) {
                    mIvPrivacy.setSelected(false);
                } else {
                    mIvPrivacy.setSelected(true);
                }
                break;
            case R.id.iv_useragree:
                if (mIvUserAgreement.isSelected()) {
                    mIvUserAgreement.setSelected(false);
                } else {
                    mIvUserAgreement.setSelected(true);
                }
                break;
            case R.id.splash_start_btn:
                if (!mIvPrivacy.isSelected()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_privacy), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mIvUserAgreement.isSelected()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_agreement), Toast.LENGTH_SHORT).show();
                    return;
                }
                doStart();
                break;
        }
    }

    private void doStart() {
        SharedPref.setBoolean(getApplicationContext(), CommonConstants.KEY_HAS_AGREEMENT_SPLASH, true);
//        if (GlobalConfig.needCompatPermission() && !PermissionCheck.checkPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
//            //没有权限,跳转
//            allowStorgePermissions();
//        } else {
        gotoHomeActivity();
//        }
    }

    private void gotoHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(CommonConstants.EXTRA_TYPE_ENTER_ANIM, CommonConstants.TYPE_ENTER_ANIM_RIGHT);
        startActivity(intent);

    }

    @Override
    protected boolean useFinishDefaultAnim() {
        return false;
    }

    @Override
    protected boolean useStartDefaultAnim() {
        return false;
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SplashLifeMonitor.closeSplash(getApplicationContext());
    }

//    private void startBgService() {
//        Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
//        intent.setAction(BackgroundService.ACTION_AGREE);
//        startService(intent);
//
//        intent = new Intent(getApplicationContext(), MainService.class);
//        intent.setAction(BackgroundService.ACTION_AGREE);
//        startService(intent);
////        bindService(new Intent(getApplicationContext(), BackgroundService.class), new ServiceConnection() {
////            @Override
////            public void onServiceConnected(ComponentName name, IBinder service) {
////                if (DEBUG) {
////                    Log.i(TAG, "onServiceConnected: ");
////                }
////            }
////
////            @Override
////            public void onServiceDisconnected(ComponentName name) {
////                if (DEBUG) {
////                    Log.i(TAG, "onServiceDisconnected: ");
////                }
////            }
////        }, BIND_AUTO_CREATE);
//    }

}
