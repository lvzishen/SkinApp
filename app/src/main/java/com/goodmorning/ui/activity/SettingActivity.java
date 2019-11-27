package com.goodmorning.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.ui.CommonConstants;
import com.baselib.ui.activity.BaseActivity;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.adapter.LanguageAdapter;
import com.goodmorning.manager.ContentManager;
import com.goodmorning.splash.OpenUrlUtils;
import com.goodmorning.utils.AppUtils;
import com.goodmorning.utils.ResUtils;
import com.goodmorning.view.dialog.LanguageDialog;
import com.nox.Nox;

import org.n.account.core.api.NjordAccountManager;
import org.thanos.netcore.bean.ChannelList;
import org.thanos.netcore.helper.JsonHelper;
import java.util.ArrayList;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivSetBack,ivSetTip,ivSetArrow;
    private RelativeLayout rlSetLanguage;
    private RelativeLayout rlSetUpdate;
    private RelativeLayout rlSetAgreement;
    private RelativeLayout rlSetPrivacy;
    private RelativeLayout rlSetQuit;
    private TextView tvSetLanguage,tvSetVersion;
    private JsonHelper<ArrayList<ChannelList.LanguageItem>> jsonHelper;
    private LanguageDialog languageDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppUtils.changeTheme(this);
        setContentView(R.layout.activity_setting);
        setStatusBarColor(ResUtils.getColor(R.color.white));
        setAndroidNativeLightStatusBar(true);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        ivSetBack = findViewById(R.id.iv_setting_back);
        ivSetTip = findViewById(R.id.setting_iv_tip);
        ivSetArrow = findViewById(R.id.setting_iv_update_arraw);
        rlSetLanguage = findViewById(R.id.rl_setting_language);
        rlSetAgreement = findViewById(R.id.rl_setting_agreement);
        rlSetUpdate = findViewById(R.id.rl_setting_update);
        rlSetPrivacy = findViewById(R.id.rl_setting_privacy);
        rlSetQuit = findViewById(R.id.rl_setting_quit);
        tvSetLanguage = findViewById(R.id.setting_tv_language);
        tvSetVersion = findViewById(R.id.setting_tv_version);
    }

    private void setListener(){
        ivSetBack.setOnClickListener(this);
        rlSetLanguage.setOnClickListener(this);
        rlSetAgreement.setOnClickListener(this);
        rlSetUpdate.setOnClickListener(this);
        rlSetPrivacy.setOnClickListener(this);
        rlSetQuit.setOnClickListener(this);

        languageDialog.setOnSwitchLanguage(new LanguageAdapter.OnSwitchLanguage() {
            @Override
            public void onLanguage(String languge) {
                languageDialog.dismiss();
                AppUtils.changeLanguage(SettingActivity.this,languge);
            }
        });
    }

    private void initData(){
        languageDialog = new LanguageDialog(this);
        if (Nox.canUpdate(getApplicationContext())){//AppUtils.isUpdate(this)
            ivSetTip.setVisibility(View.VISIBLE);
            ivSetArrow.setVisibility(View.VISIBLE);
            rlSetUpdate.setClickable(true);
            rlSetUpdate.setEnabled(true);
        }else {
            ivSetTip.setVisibility(View.GONE);
            ivSetArrow.setVisibility(View.GONE);
            rlSetUpdate.setClickable(false);
            rlSetUpdate.setEnabled(false);
        }
        tvSetLanguage.setText(ContentManager.getInstance().getLang());
        tvSetVersion.setText("V"+AppUtils.versionName(this));

        if (NjordAccountManager.isLogined(this)){
            rlSetQuit.setVisibility(View.VISIBLE);
        }else {
            rlSetQuit.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_setting_back:
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra(MainActivity.CONTENT,true);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_setting_language:
                //切换到语言列表
                languageDialog.show();
                break;
            case R.id.rl_setting_update:
                //跳转到GP页面
                AppUtils.launchAppDetail(SettingActivity.this,AppUtils.getPackageName(SettingActivity.this));
                break;
            case R.id.rl_setting_agreement:
                //跳转到用户协议
                OpenUrlUtils.openDefaultBrower(SettingActivity.this, CommonConstants.USER_AGREEMENT_URL);
                break;
            case R.id.rl_setting_privacy:
                //跳转到隐私协议
                OpenUrlUtils.openDefaultBrower(SettingActivity.this, CommonConstants.PRIVACY_POLICY_URL);
                break;
            case R.id.rl_setting_quit:
                //退出登录提示
                NjordAccountManager.localLogout(SettingActivity.this);
                Intent quitIntent = new Intent(SettingActivity.this,MainActivity.class);
                quitIntent.putExtra(MainActivity.CONTENT,true);
                startActivity(quitIntent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
        intent.putExtra(MainActivity.CONTENT,true);
        startActivity(intent);
        finish();
    }
}
