package com.goodmorning;


import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.ads.lib.commen.AdLifecyclerManager;
import com.baselib.ui.activity.BaseActivity;
import com.goodmorning.splash.SplashLifeMonitor;
import com.cleanerapp.supermanager.R;

import org.thanos.core.MorningDataAPI;
import org.thanos.core.ResultCallback;
import org.thanos.core.bean.ChannelList;
import org.thanos.core.bean.ContentDetail;
import org.thanos.core.bean.ContentList;
import org.thanos.core.internal.requestparam.ChannelListRequestParam;
import org.thanos.core.internal.requestparam.ContentDetailRequestParam;
import org.thanos.core.internal.requestparam.ContentListRequestParam;

import static org.thanos.ThanosSDK.DEBUG;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdLifecyclerManager.getInstance(getApplicationContext()).setFixedActivity(this);
        setContentView(R.layout.mainactivity_news);
        setStatusBarColor(getResources().getColor(R.color.transparent));
        setAndroidNativeLightStatusBar(true);
        //1
//        long cacheTime = CloudConstants.getChannelCacheTimeInSeconds();
        MorningDataAPI.requestChannelList(getApplicationContext(), new ChannelListRequestParam(false, 0L), new ResultCallback<ChannelList>() {
            @Override
            public void onSuccess(ChannelList data) {

            }

            @Override
            public void onLoadFromCache(ChannelList data) {

            }

            @Override
            public void onFail(Exception e) {

            }
        });
        //2
        int sessioID = (int) System.currentTimeMillis();
        ContentListRequestParam newsListRequestParam = new ContentListRequestParam(sessioID, 0, false, false, false);
        MorningDataAPI.requestContentList(getApplicationContext(), newsListRequestParam, new ResultCallback<ContentList>() {
            @Override
            public void onSuccess(ContentList data) {

            }

            @Override
            public void onLoadFromCache(ContentList data) {

            }

            @Override
            public void onFail(Exception e) {

            }
        });


        //3
        ContentDetailRequestParam.ContentDetailProtocol contentDetailProtocol = new ContentDetailRequestParam.ContentDetailProtocol();
        contentDetailProtocol.resourceID = 12345;
        MorningDataAPI.requestContentDetail(getApplicationContext(), new ContentDetailRequestParam(contentDetailProtocol), new ResultCallback<ContentDetail>() {
            @Override
            public void onLoadFromCache(ContentDetail data) {
            }

            @Override
            public void onSuccess(ContentDetail data) {
                if (data != null && data.item != null) {
                    if (DEBUG) {
                        Log.d(TAG, "有数据--->" + data.message);
                    }
                } else {
                    if (DEBUG) {
                        Log.d(TAG, "无数据--->");
                    }
                }
            }

            @Override
            public void onFail(Exception e) {
                Log.i("aasd", e.getMessage());
            }
        });

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
