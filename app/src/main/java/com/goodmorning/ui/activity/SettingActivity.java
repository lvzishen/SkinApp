package com.goodmorning.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.language.LanguageUtil;
import com.baselib.statistic.StatisticLoggerX;
import com.baselib.ui.CommonConstants;
import com.baselib.ui.activity.BaseActivity;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.adapter.LanguageAdapter;
import com.goodmorning.manager.ContentManager;
import com.goodmorning.splash.OpenUrlUtils;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.utils.AppUtils;
import com.goodmorning.utils.ResUtils;
import com.goodmorning.view.dialog.CommonDialog;
import com.goodmorning.view.dialog.CommonDialogClickListener;
import com.goodmorning.view.dialog.LanguageDialog;
import com.nox.Nox;

import org.n.account.core.api.NjordAccountManager;
import org.n.account.core.contract.ILoginCallback;
import org.n.account.core.model.Account;
import org.n.account.net.impl.INetCallback;
import org.thanos.netcore.bean.ChannelList;
import org.thanos.netcore.helper.JsonHelper;
import java.util.ArrayList;
import java.util.Set;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivSetBack,ivSetTip,ivSetArrow;
    private RelativeLayout rlSetLanguage;
    private RelativeLayout rlSetUpdate;
    private RelativeLayout rlSetAgreement;
    private RelativeLayout rlSetPrivacy;
    private RelativeLayout rlSetQuit;
    private TextView tvSetLanguage,tvSetVersion;
    private JsonHelper<ArrayList<ChannelList.LanguageItem>> jsonHelper;
    private CommonDialog commonDialog;
    public static final String KEY_QUIT_EXTRA = "key_quit_extra";
//    private boolean isLogin;
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

    }

    private void initData(){
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
        Intent intent = getIntent();
//        if (intent != null){
//            isLogin = intent.getBooleanExtra(ActivityCtrl.KEY_LOGIN_EXTRA,false);
//        }
        Account account = NjordAccountManager.getCurrentAccount(getApplicationContext());
        if (account != null && !account.isGuest()){
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
                intent.putExtra(MainActivity.CONTENT,ContentManager.getInstance().isChangeLang());
                intent.putExtra(MainActivity.KEY_EXTRA_ISMINE,true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_setting_language:
                //切换到语言列表
                ActivityCtrl.gotoActivityOpenSimple(SettingActivity.this,LanguageActivity.class);
                finish();
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
                if (commonDialog != null && commonDialog.isShowing()) {
                    return;
                }
                commonDialog = new CommonDialog.Builder(this)
                        .setTitle(this.getString(R.string.ask_exit))
                        .setLeftBtnStr(this.getString(R.string.btn_cancel))
                        .setRightBtnStr(this.getString(R.string.btn_confirm))
                        .addClickListener(new CommonDialogClickListener() {
                            @Override
                            public void onClickLeft() {
                                commonDialog.dismiss();
                            }

                            @Override
                            public void onClickRight() {
                                commonDialog.dismiss();
//                                Intent quitIntent = new Intent(SettingActivity.this,MainActivity.class);
//                                quitIntent.putExtra(MainActivity.CONTENT,ContentManager.getInstance().isChangeLang());
//                                quitIntent.putExtra(SettingActivity.KEY_QUIT_EXTRA,true);
//                                quitIntent.putExtra(MainActivity.KEY_EXTRA_ISMINE,true);
//                                quitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(quitIntent);
                                NjordAccountManager.logout(getApplicationContext(), new INetCallback<String>() {
                                    public void onStart() {
                                        showLoading("");
                                    }

                                    public void onFinish() {
                                        dismissLoading();
                                    }

                                    public void onSuccess(String result) {
                                        NjordAccountManager.registerGuest(getApplicationContext(), new ILoginCallback() {
                                            @Override
                                            public void onPrePrepare(int i) {
                                                showLoading("");
                                            }

                                            @Override
                                            public void onPrepareFinish() {

                                            }

                                            @Override
                                            public void onPreLogin(int i) {

                                            }

                                            @Override
                                            public void onLoginSuccess(Account account) {
                                                dismissLoading();
//                                                account = NjordAccountManager.getCurrentAccount(getApplicationContext());
//                                                showAccountInfo(account);
//                                                finish();
                                                onBackPressed();
                                            }

                                            @Override
                                            public void onLoginFailed(int i, String s) {
                                                dismissLoading();
                                            }
                                        });
                                    }

                                    public void onFailure(int errorCode, String msg) {
                                    }
                                });
//                                finish();
                            }
                        }).show();

                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
        intent.putExtra(MainActivity.CONTENT,ContentManager.getInstance().isChangeLang());
        intent.putExtra(MainActivity.KEY_EXTRA_ISMINE,true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
