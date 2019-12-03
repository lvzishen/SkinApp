package com.goodmorning;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.ads.lib.commen.AdLifecyclerManager;
import com.baselib.language.LanguageUtil;
import com.baselib.sp.SharedPref;
import com.baselib.ui.activity.BaseActivity;
import com.goodmorning.bean.DataListItem;
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
import org.n.account.core.api.NjordAccountManager;
import java.util.ArrayList;
import java.util.List;
import static com.goodmorning.utils.ActivityCtrl.TRANSFER_DATA;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    public static final String CONTENT = "content";
    public static final String CHANNEL_NAME = "channel_name";
    public static final String KEY_EXTRA_ISMINE = "key_extra_ismine";
    private ViewPager mVpContent;
    private BottomBarLayout mBottomBarLayout;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private GoodAdapter goodAdapter;

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

        MyFragment myFragment = new MyFragment();
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
}
