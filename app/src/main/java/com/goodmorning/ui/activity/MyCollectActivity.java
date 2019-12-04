package com.goodmorning.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.baselib.ui.activity.BaseActivity;
import com.creativeindia.goodmorning.R;
import com.goodmorning.adapter.MainListAdapter;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.utils.ResUtils;
import com.goodmorning.view.recyclerview.CommonRecyclerView;
import com.goodmorning.view.recyclerview.CommonRecyclerViewAdapter;
import com.goodmorning.view.recyclerview.decoration.DiverItemDecoration;
import com.goodmorning.view.recyclerview.interfaces.OnItemClickListener;
import com.goodmorning.view.recyclerview.view.CustomLoadingFooter;
import com.goodmorning.view.recyclerview.view.CustomRefreshHeader;

import org.thanos.netcore.MorningDataAPI;
import org.thanos.netcore.ResultCallback;
import org.thanos.netcore.bean.ContentItem;
import org.thanos.netcore.bean.ContentList;
import org.thanos.netcore.bean.NewsItem;
import org.thanos.netcore.bean.VideoItem;
import org.thanos.netcore.internal.requestparam.CollectListRequestParam;

import java.util.ArrayList;
import java.util.List;

public class MyCollectActivity extends BaseActivity {
    private static final String TAG = "MyCollectActivity";
    private CommonRecyclerView rvCollectList;
    private MainListAdapter mainListAdapter;
    private CommonRecyclerViewAdapter mRecyclerViewAdapter;
    private FrameLayout myCollectBack;
    private LinearLayout llNoCollect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        setStatusBarColor(ResUtils.getColor(R.color.white));
        setAndroidNativeLightStatusBar(true);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        rvCollectList = findViewById(R.id.rv_collect_list);
        myCollectBack = findViewById(R.id.mycollection_back);
        llNoCollect = findViewById(R.id.ll_no_collect);
    }

    private void initData(){
        mainListAdapter = new MainListAdapter(this);
        mRecyclerViewAdapter = new CommonRecyclerViewAdapter(mainListAdapter);
        CustomRefreshHeader customRefreshHeader = new CustomRefreshHeader(this);
        rvCollectList.setRefreshHeader(customRefreshHeader);
        rvCollectList.setPullRefreshEnabled(false);
        rvCollectList.setAdapter(mRecyclerViewAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //防止item位置互换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvCollectList.setLayoutManager(layoutManager);
        rvCollectList.addItemDecoration(new DiverItemDecoration(20,2));
        rvCollectList.setLoadMoreEnabled(false);
        CustomLoadingFooter customLoadingFooter = new CustomLoadingFooter(this);
        rvCollectList.setLoadMoreFooter(customLoadingFooter,true);
        requestCollect();
    }

    private void setListener(){
        myCollectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //调换到详情页
                if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_1) {
                    ActivityCtrl.gotoOpenActivity(MyCollectActivity.this, TextDetailActivity.class, mainListAdapter.getDataItem(position));
                } else if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_2) {
                    ActivityCtrl.gotoOpenActivity(MyCollectActivity.this, PicDetailActivity.class, mainListAdapter.getDataItem(position));
                } else if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_3) {
                    ActivityCtrl.gotoOpenActivity(MyCollectActivity.this, VideoDetailActivity.class, mainListAdapter.getDataItem(position));
                } else if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_4){
                    ActivityCtrl.gotoOpenActivity(MyCollectActivity.this, PicDetailActivity.class, mainListAdapter.getDataItem(position));
                }
            }
        });
    }

    private void requestCollect(){
        MorningDataAPI.requestCollectList(getApplicationContext(),
            new CollectListRequestParam(0,
                    1000, false, 1), new ResultCallback<ContentList>() {
                @Override
                public void onSuccess(ContentList data) {
                    if (data != null && data.code == 0) {
                        if (GlobalConfig.DEBUG) {
                            Log.i(TAG, "用户收藏上报成功");
                        }
                        refreshUIData(data);
                    } else {
                        if (GlobalConfig.DEBUG) {
                            Log.i(TAG, "用户收藏上报失败");
                        }
                        showEmptyView();
                    }
                }

                @Override
                public void onLoadFromCache(ContentList data) {

                }

                @Override
                public void onFail(Exception e) {
                    if (GlobalConfig.DEBUG) {
                        Log.d(TAG, "用户收藏上报失败, [" + e + "]");
                    }
                    showEmptyView();
                }
            });
    }

    private void refreshUIData(ContentList data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data != null){
                    ArrayList<ContentItem> contentItems = data.items;
                    List<DataListItem> datas = new ArrayList<>();
                    for (ContentItem contentItem : contentItems){
                        DataListItem dataItem = new DataListItem();
                        if (DataListItem.STATUS_TYPE_0 == contentItem.status){
                            if ("NEWS".equals(contentItem.contentType)){
                                dataItem.setType(DataListItem.DATA_TYPE_1);
                            }else if ("PHOTO".equals(contentItem.contentType)){
                                dataItem.setType(DataListItem.DATA_TYPE_2);
                            }else if ("VIDEO".equals(contentItem.contentType)){
                                dataItem.setType(DataListItem.DATA_TYPE_3);
                            }
                        }else {
                            dataItem.setType(DataListItem.DATA_TYPE_4);
                        }
                        dataItem.setChannelName("collection");
                        dataItem.setResourceId(contentItem.resourceId);
                        dataItem.setId(contentItem.id);
                        dataItem.setStatus(contentItem.status);
                        if (contentItem instanceof VideoItem){
                            VideoItem videoItem = (VideoItem) contentItem;
                            dataItem.setVideoUrl(contentItem.url);
                            if (videoItem.photoInfos.size() >= 1){
                                dataItem.setPicUrl(videoItem.photoInfos.get(0).originUrl);
                                dataItem.setVideoThumbUrl(videoItem.photoInfos.get(0).originUrl);
                                dataItem.setWidth(videoItem.photoInfos.get(0).width);
                                dataItem.setHeight(videoItem.photoInfos.get(0).height);
                            }

                        }else if (contentItem instanceof NewsItem){
                            NewsItem newsItem = (NewsItem) contentItem;
                            dataItem.setData(newsItem.title);
                        }
                        datas.add(dataItem);
                    }
                    mainListAdapter.addAll(datas);
                    rvCollectList.refreshComplete(0);
                }
                showEmptyView();
            }
        });
    }

    /**
     * 显示空视图
     */
    private void showEmptyView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mainListAdapter.getDataSize() == 0){
                    llNoCollect.setVisibility(View.VISIBLE);
                }else {
                    llNoCollect.setVisibility(View.GONE);
                }
            }
        });
    }
}
