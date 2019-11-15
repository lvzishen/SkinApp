package com.goodmorning;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ads.lib.commen.AdLifecyclerManager;
import com.baselib.ui.activity.BaseActivity;
import com.goodmorning.splash.SplashLifeMonitor;
import com.cleanerapp.supermanager.R;
import com.goodmorning.ui.fragment.HomeFragment;
import com.goodmorning.ui.fragment.MyFragment;
import com.goodmorning.view.tab.BottomBarLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    public static final String CONTENT = "content";
    private ViewPager mVpContent;
    private BottomBarLayout mBottomBarLayout;
    private List<Fragment> mFragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdLifecyclerManager.getInstance(getApplicationContext()).setFixedActivity(this);
        setContentView(R.layout.mainactivity_news);
        setStatusBarColor(getResources().getColor(R.color.transparent));
        setAndroidNativeLightStatusBar(true);
        initView();
        initData();
    }

    private void initView(){
        mVpContent = findViewById(R.id.vp_content);
        mBottomBarLayout = findViewById(R.id.bottombar_layout);
    }

    private void initData(){
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

        mVpContent.setAdapter(new GoodAdapter(getSupportFragmentManager()));
        mBottomBarLayout.setViewPager(mVpContent);
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
    protected boolean useStartDefaultAnim() {
        return false;
    }
}
