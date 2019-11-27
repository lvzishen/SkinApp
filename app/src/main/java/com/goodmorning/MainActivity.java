package com.goodmorning;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ads.lib.commen.AdLifecyclerManager;
import com.baselib.language.LanguageUtil;
import com.baselib.sp.SharedPref;
import com.baselib.ui.activity.BaseActivity;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.splash.SplashLifeMonitor;
import com.creativeindia.goodmorning.R;
import com.goodmorning.ui.fragment.HomeFragment;
import com.goodmorning.ui.fragment.MyFragment;
import com.goodmorning.utils.AppUtils;
import com.goodmorning.view.tab.BottomBarLayout;
import com.w.sdk.push.PushBindManager;


import org.n.account.core.api.NjordAccountManager;
import org.thanos.netcore.CollectStatus;
import org.thanos.netcore.bean.CollectDetail;
import org.thanos.netcore.MorningDataAPI;
import org.thanos.netcore.ResultCallback;
import org.thanos.netcore.bean.ContentList;
import org.thanos.netcore.internal.requestparam.CollectListRequestParam;
import org.thanos.netcore.internal.requestparam.CollectRequestParam;
import org.thanos.netcore.internal.requestparam.CollectStatusRequestParam;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    public static final String CONTENT = "content";
    private ViewPager mVpContent;
    private BottomBarLayout mBottomBarLayout;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.changeTheme(this);
        AdLifecyclerManager.getInstance(getApplicationContext()).setFixedActivity(this);
        setContentView(R.layout.mainactivity_news);
        setStatusBarColor(AppUtils.changeStatusColor());
        setAndroidNativeLightStatusBar(AppUtils.isStatusLight());
        initView();
        initData();

        //匿名账号登录，按需初始化，一般会在主界面进行初始化
        NjordAccountManager.registerGuest(this, null);

        //1
//        long cacheTime = CloudConstants.getChannelCacheTimeInSeconds();
//        MorningDataAPI.requestChannelList(getApplicationContext(), new ChannelListRequestParam(false, 0L), new ResultCallback<ChannelList>() {
//            @Override
//            public void onSuccess(ChannelList data) {
//
//            }
//
//            @Override
//            public void onLoadFromCache(ChannelList data) {
//
//            }
//
//            @Override
//            public void onFail(Exception e) {
//
//            }
//        });
        //2
//        int sessioID = (int) System.currentTimeMillis();
//        Log.e(TAG,"sessioID="+sessioID);
//        ContentListRequestParam newsListRequestParam = new ContentListRequestParam(sessioID, 6, false, false, false);
//        MorningDataAPI.requestContentList(getApplicationContext(), newsListRequestParam, new ResultCallback<ContentList>() {
//            @Override
//            public void onSuccess(ContentList data) {
//                for (ContentItem newsItem : data.items) {
//                    NewsItem newsItem1 = (NewsItem) newsItem;
//                    Log.i("NewsItem", newsItem1.toString());
//                }
//            }
//
//            @Override
//            public void onLoadFromCache(ContentList data) {
//
//            }
//
//            @Override
//            public void onFail(Exception e) {
//
//            }
//        });


//        //3
//        MorningDataAPI.requestContentDetail(getApplicationContext(), new ContentDetailRequestParam(false, 49879935), new ResultCallback<ContentDetail>() {
//            @Override
//            public void onLoadFromCache(ContentDetail data) {
//            }
//
//            @Override
//            public void onSuccess(ContentDetail data) {
//                if (data != null && data.item != null) {
//                    if (GlobalConfig.DEBUG) {
//                        Log.d(TAG, "有数据--->" + data.message);
//                    }
//                } else {
//                    if (GlobalConfig.DEBUG) {
//                        Log.d(TAG, "无数据--->");
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(Exception e) {
//                Log.i(TAG, e.getMessage());
//            }
//        });

        //4
//        int page=1;
//        RecommendListRequestParam requestParam = new RecommendListRequestParam(contentItem, mChannellId, page++, false, 1);
//        MorningDataAPI.requestRecommendContentList(getApplicationContext(), requestParam, new ResultCallback<ContentList>() {
//            @Override
//            public void onSuccess(ContentList data) {
//                ArrayList<UIContentItem> allList = new ArrayList<>();
//                try {
//                    for (ContentItem contentItem : data.items) {
//                        if (contentItem instanceof NewsItem) {
//                            UINewsItem uiNewsItem = new UINewsItem((NewsItem) contentItem);
//                            allList.add(uiNewsItem);
//                        } else if (contentItem instanceof VideoItem) {
//                            UIVideoItem uiVideoItem = new UIVideoItem((VideoItem) contentItem);
//                            allList.add(uiVideoItem);
//                        }
//                    }
//                } catch (Exception e) {
//                    if (DEBUG) {
//                        Log.i(TAG, "数据转换异常");
//                    }
//                }
//            }
//
//            @Override
//            public void onLoadFromCache(ContentList data) {
//            }
//
//            @Override
//            public void onFail(Exception e) {
//            }
//        });

        //5
//        //用户分享行为上报
//        MorningDataAPI.uploadUserBehavior(getApplicationContext(),
//                new UserBehaviorUploadParam(contentItem,
//                        ThanosDataAPI.UserBehaviorUploadParam.UserBehavior.SHARE, false, 1), new ThanosDataAPI.ThanosDataLoadCallback<ResponseData>() {
//                    @Override
//                    public void onSuccess(ResponseData data) {
//                        if (data != null && data.code == 0) {
//                            if (DEBUG) {
//                                Log.i(TAG, "图集分享行为上报成功" + contentItem.articleTitle);
//                            }
//                        } else {
//                            if (DEBUG) {
//                                Log.i(TAG, "图集分享行为上报失败, " + data + "空数据");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFromCache(ResponseData data) {
//
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        if (DEBUG) {
//                            Log.e(TAG, "图集分享行为上报成功", e);
//                        }
//                    }
//                });


        //6
//        MorningDataAPI.uploadUserFeedback(getApplicationContext(),
//                new UserFeedbackUploadRequestParam(contentItem,
//                        bean, false, 1), new ResultCallback<ResponseData>() {
//                    @Override
//                    public void onSuccess(ResponseData data) {
//                        if (data != null && data.code == 0) {
//                            if (DEBUG) {
//                                Log.i(TAG, "用户负反馈上报成功, " + bean.text);
//                            }
//                        } else {
//                            if (DEBUG) {
//                                Log.i(TAG, "用户负反馈上报失败, " + data);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFromCache(ResponseData data) {
//
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        if (DEBUG) {
//                            Log.d(TAG, "用户负反馈上报失败, [" + e + "]");
//                        }
//                    }
//                });
        //7
//        MorningDataAPI.requestCollectUpLoad(getApplicationContext(),
//                new CollectRequestParam(1,
//                        false, false, 1), new ResultCallback<CollectDetail>() {
//                    @Override
//                    public void onSuccess(CollectDetail data) {
//                        if (data != null && data.code == 0) {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "用户收藏上报成功");
//                            }
//                        } else {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "用户收藏上报失败");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFromCache(CollectDetail data) {
//
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        if (GlobalConfig.DEBUG) {
//                            Log.d(TAG, "用户收藏上报失败, [" + e + "]");
//                        }
//                    }
//                });

        //8
//        MorningDataAPI.requestCollectList(getApplicationContext(),
//                new CollectListRequestParam(0,
//                        1000, false, 1), new ResultCallback<ContentList>() {
//                    @Override
//                    public void onSuccess(ContentList data) {
//                        if (data != null && data.code == 0) {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "用户收藏上报成功");
//                            }
//                        } else {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "用户收藏上报失败");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFromCache(ContentList data) {
//
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        if (GlobalConfig.DEBUG) {
//                            Log.d(TAG, "用户收藏上报失败, [" + e + "]");
//                        }
//                    }
//                });

        //9
//        MorningDataAPI.requestCollectStatus(getApplicationContext(),
//                new CollectStatusRequestParam(0,
//                        false, 1), new ResultCallback<CollectStatus>() {
//                    @Override
//                    public void onSuccess(CollectStatus data) {
//                        if (data != null && data.code == 0) {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "用户收藏状态");
//                            }
//                        } else {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "用户收藏状态失败");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFromCache(CollectStatus data) {
//
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        if (GlobalConfig.DEBUG) {
//                            Log.d(TAG, "用户收藏状态失败, [" + e + "]");
//                        }
//                    }
//                });
    }

    private void initView() {
        mVpContent = findViewById(R.id.vp_content);
        mBottomBarLayout = findViewById(R.id.bottombar_layout);
    }

    private void initData() {
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

    /**
     * 如果是7.0以下，我们需要调用changeAppLanguage方法，
     * 如果是7.0及以上系统，直接把我们想要切换的语言类型保存在SharedPreferences中即可
     * 然后重新启动MainActivity
     *
     * @param language
     */
    public void changeLanguage(String language) {
        Log.e("changeLanguage", "language==" + language);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            LanguageUtil.changeAppLanguage(getApplicationContext(), language);
        }
        SharedPref.setString(getApplicationContext(), SharedPref.LANGUAGE, language);
        this.recreate();

        //更新push语言，重新绑定
        Bundle bundle = new Bundle();
        bundle.putString("ext_locale", language);
        PushBindManager.getInstance().setExtParam(bundle);
    }

    @Override
    protected boolean useStartDefaultAnim() {
        return false;
    }
}
