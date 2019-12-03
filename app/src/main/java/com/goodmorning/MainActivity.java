package com.goodmorning;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.ads.lib.commen.AdLifecyclerManager;
import com.baselib.language.LanguageUtil;
import com.baselib.sp.SharedPref;
import com.baselib.ui.activity.BaseActivity;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.manager.ContentManager;
import com.goodmorning.splash.SplashLifeMonitor;
import com.creativeindia.goodmorning.R;
import com.goodmorning.ui.activity.SettingActivity;
import com.goodmorning.ui.fragment.HomeFragment;
import com.goodmorning.ui.fragment.MyFragment;
import com.goodmorning.utils.AppUtils;
import com.goodmorning.utils.ResUtils;
import com.goodmorning.view.dialog.PicDialog;
import com.goodmorning.view.tab.BottomBarLayout;
import com.w.sdk.push.PushBindManager;

import org.n.account.core.AccountSDK;
import org.n.account.core.api.NjordAccountManager;
import org.n.account.core.constant.Constant;
import org.n.account.core.contract.ILoginCallback;
import org.n.account.core.contract.LoginApi;
import org.n.account.core.exception.NotAllowLoginException;
import org.n.account.core.model.Account;
import org.n.account.core.ui.LoadingDialog;
import org.n.account.core.utils.DialogUtils;
import org.n.account.net.NetCode;

import java.util.ArrayList;
import java.util.List;
import static com.goodmorning.utils.ActivityCtrl.TRANSFER_DATA;
import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    public static final String CONTENT = "content";
    public static final String CHANNEL_NAME = "channel_name";
    public static final String KEY_EXTRA_ISMINE = "key_extra_ismine";
    private ViewPager mVpContent;
    private BottomBarLayout mBottomBarLayout;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private GoodAdapter goodAdapter;
    private LoginApi mLoginApi;
    protected LoadingDialog mLoadingDialog;
    private String mLoadingStr = "";
    MyFragment myFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppUtils.changeTheme(this);
        AdLifecyclerManager.getInstance(getApplicationContext()).setFixedActivity(this);
        setContentView(R.layout.mainactivity_news);
        setStatusBarColor(ResUtils.getColor(R.color.transparent));
        setAndroidNativeLightStatusBar(true);
        if (getIntent() != null) {
            boolean isFromNoti = getIntent().getBooleanExtra("is_from_noti", false);
            DataListItem dataListItem = (DataListItem) getIntent().getSerializableExtra(TRANSFER_DATA);
            if (isFromNoti && dataListItem != null) {
                PicDialog picDialog = new PicDialog(this);
                picDialog.setDataListItem(dataListItem);
                picDialog.show();
            }
        }
        initView();
        initData();

        //匿名账号登录，按需初始化，一般会在主界面进行初始化
        NjordAccountManager.registerGuest(this, null);

    }

    private void initView() {
        mVpContent = findViewById(R.id.vp_content);
        mBottomBarLayout = findViewById(R.id.bottombar_layout);
    }

    private void initData() {
        mFragmentList.clear();
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(CONTENT, getString(R.string.tab_greeting));
        homeFragment.setArguments(bundle1);
        mFragmentList.add(homeFragment);

        myFragment = new MyFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(CONTENT, getString(R.string.tab_my));
        myFragment.setArguments(bundle2);
        mFragmentList.add(myFragment);


        goodAdapter = new GoodAdapter(getSupportFragmentManager());
        mVpContent.setAdapter(goodAdapter);
        mBottomBarLayout.setViewPager(mVpContent);

        Intent intent = getIntent();
        if (intent != null){
            boolean isMine = intent.getBooleanExtra(KEY_EXTRA_ISMINE,false);
            if (isMine){
                mVpContent.setCurrentItem(1);
            }
        }
        updateData(intent);

    }

    class GoodAdapter extends FragmentStatePagerAdapter {
        public GoodAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        updateData(intent);
    }

    private void updateData(Intent intent){
        boolean isRefresh = intent.getBooleanExtra(CONTENT,false);
        boolean isQuit = intent.getBooleanExtra(SettingActivity.KEY_QUIT_EXTRA,false);
        if (isRefresh){
            AppUtils.changeLanguage(this, LanguageUtil.getLanguage());
        }

        ContentManager.getInstance().setChangeLang(false);
        if (isQuit && getSupportFragmentManager().getFragments().size() >= 2) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof MyFragment) {
                    MyFragment myFragment = (MyFragment) fragment;
                    myFragment.quitLogin();
                }
            }

        }
    }

    @Override
    protected boolean useStartDefaultAnim() {
        return false;
    }

    public void startMyLogin(FragmentActivity activity) {
        try {
            mLoginApi = LoginApi.Factory.create(activity, Constant.LoginType.FACEBOOK);
        } catch (NotAllowLoginException e) {
            if(GlobalConfig.DEBUG){
                throw new IllegalArgumentException(e);
            }
//                        finish();
            return;
        }

        mLoginApi.login(mLoginCallback);
        if (mLoadingDialog != null) {
            mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
//                                finish();
                    if (DEBUG) {
                        Log.i(TAG, "mLoadingDialog onCancel: ");
                    }
                }
            });
        }
    }

    private ILoginCallback mLoginCallback = new ILoginCallback() {

        @Override
        public void onPrePrepare(@Constant.LoginType int type) {
            if (type == Constant.LoginType.GOOGLE) {
                showLoading(mLoadingStr, true);
            }
            if (DEBUG) {
                Log.i(TAG, "onPrePrepare: ");
            }
        }

        @Override
        public void onPrepareFinish() {

            if (DEBUG) {
                Log.i(TAG, "onPrepareFinish: ");
            }
            dismissLoading();
        }

        @Override
        public void onPreLogin(@Constant.LoginType int type) {
            if (DEBUG) {
                Log.i(TAG, "onPreLogin: ");
            }
            showLoading(mLoadingStr);
        }

        @Override
        public void onLoginSuccess(Account account) {
            if (DEBUG) {
                Log.i(TAG, "onLoginSuccess: ");
            }
            dismissLoading();
            if (account != null) {
                if (myFragment != null) {
                    myFragment.showAccountInfo(account);
                }
            }
//            initShareLink();
//            if(UIController.getInstance().getAlexLogWatcher() != null){
//                Bundle alexBundle = new Bundle();
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_NAME_STRING, CreditStatistics.Alex.INVITE_FRIEND_OPERATION_NEW);
//                alexBundle.putString(AlexEventsConstant.XALEX_OPERATION_ACTION_STRING, "share");
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_CATEGORY_STRING, mTargetPackage);
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_FLAG_STRING, "logined");
//                UIController.getInstance().getAlexLogWatcher().log(AlexEventsConstant.XALEX_CLICK,alexBundle);
//            }
        }

        @Override
        public void onLoginFailed(int error, String msg) {
            dismissLoading();
            if (DEBUG) {
                Log.i(TAG, "onLoginFailed: ");
            }
            if (AccountSDK.getExceptionHandler() != null) {
                AccountSDK.getExceptionHandler().handleException(getApplicationContext(),
                        NetCode.NEED_TOAST, TextUtils.concat(getString(R.string.common_unknown_error
                                , getString(R.string.login_network_failed)), "(", String.valueOf(error), ")").toString());
            }
//            if(UIController.getInstance().getAlexLogWatcher() != null){
//                Bundle alexBundle = new Bundle();
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_NAME_STRING, CreditStatistics.Alex.INVITE_FRIEND_OPERATION_NEW);
//                alexBundle.putString(AlexEventsConstant.XALEX_OPERATION_ACTION_STRING, "share");
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_CATEGORY_STRING, mTargetPackage);
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_FLAG_STRING, "login_fail");
//                UIController.getInstance().getAlexLogWatcher().log(AlexEventsConstant.XALEX_CLICK,alexBundle);
//            }
//            finish();
        }
    };
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mLoginApi != null)mLoginApi.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 64206 && resultCode == 0) {
//            finish();//Facebook取消
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(mLoginApi != null)mLoginApi.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onDestroy() {
        if(mLoginApi != null)mLoginApi.onDestroy();
        super.onDestroy();
    }
    public void dismissLoading() {
        DialogUtils.dismissDialog(mLoadingDialog);
        mLoadingDialog = null;
    }

    public void showLoading(String msg) {
        showLoading(msg,false);
    }

    public void showLoading(String msg,boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, true);
        }
        mLoadingDialog.setMsg(msg);
        mLoadingDialog.setCancelable(cancelable);
        if(cancelable){
            mLoadingDialog.setOnKeyListener(null);
        }else {
            mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == KeyEvent.ACTION_UP) {
                        return true;
                    }
                    return false;
                }
            });
        }
        DialogUtils.showDialog(mLoadingDialog);
    }

}
