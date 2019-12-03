package com.goodmorning.ui.fragment;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baselib.bitmap.util.DeviceUtil;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.adapter.MainListAdapter;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.ui.activity.PicDetailActivity;
import com.goodmorning.ui.activity.TextDetailActivity;
import com.goodmorning.ui.activity.VideoDetailActivity;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.view.recyclerview.CommonRecyclerView;
import com.goodmorning.view.recyclerview.CommonRecyclerViewAdapter;
import com.goodmorning.view.recyclerview.SwipeRecyclerView;
import com.goodmorning.view.recyclerview.SwipeRecyclerViewAdapter;
import com.goodmorning.view.recyclerview.decoration.DiverItemDecoration;
import com.goodmorning.view.recyclerview.interfaces.OnItemClickListener;
import com.goodmorning.view.recyclerview.interfaces.OnLoadMoreListener;
import com.goodmorning.view.recyclerview.interfaces.OnRefreshListener;
import com.goodmorning.view.recyclerview.view.CustomLoadingFooter;
import com.goodmorning.view.recyclerview.view.CustomRefreshHeader;
import org.thanos.netcore.bean.NewsItem;
import org.thanos.netcore.bean.VideoItem;
import org.thanos.netcore.MorningDataAPI;
import org.thanos.netcore.ResultCallback;
import org.thanos.netcore.bean.ContentItem;
import org.thanos.netcore.bean.ContentList;
import org.thanos.netcore.internal.requestparam.ContentListRequestParam;
import java.util.ArrayList;
import java.util.List;
import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class TabFragment extends Fragment {

    private CommonRecyclerView mRecyclerView;
    private LinearLayout llListRetry;
    private Button listRetryBtn;
    private MainListAdapter mainListAdapter;
    private CommonRecyclerViewAdapter mRecyclerViewAdapter;
    private int sessionId;
    private int channelId;
    private Activity mActivity;
    private boolean isRefresh = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_layout, container, false);
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initView(View view){
        mRecyclerView = view.findViewById(R.id.rv_list);
        llListRetry = view.findViewById(R.id.ll_list_retry);
        listRetryBtn = view.findViewById(R.id.list_retry_btn);
    }

    private void initData(){
        mActivity = getActivity();
        if (getArguments() != null){
            channelId = getArguments().getInt(MainActivity.CONTENT);
        }
        mainListAdapter = new MainListAdapter(mActivity);
        mRecyclerViewAdapter = new CommonRecyclerViewAdapter(mainListAdapter);
        CustomRefreshHeader customRefreshHeader = new CustomRefreshHeader(getContext());
        mRecyclerView.setRefreshHeader(customRefreshHeader);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //防止item位置互换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setPadding(15,10,15,10);
        mRecyclerView.addItemDecoration(new DiverItemDecoration(25,2));
        mRecyclerView.setLoadMoreEnabled(true);
        CustomLoadingFooter customLoadingFooter = new CustomLoadingFooter(getContext());
        mRecyclerView.setLoadMoreFooter(customLoadingFooter,true);
        sessionId = Math.abs((int) System.currentTimeMillis());
        requestData();
    }

    private void setListener(){
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestData();
            }
        });

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                sessionId = Math.abs((int) System.currentTimeMillis());
                mainListAdapter.clear();
                requestData();
            }
        });

        mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //调换到详情页
                Log.e("TabFragment","position="+position);
                if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_1) {
                    ActivityCtrl.gotoOpenActivity(mActivity, TextDetailActivity.class, mainListAdapter.getDataItem(position));
                } else if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_2) {
                    ActivityCtrl.gotoOpenActivity(mActivity, PicDetailActivity.class, mainListAdapter.getDataItem(position));
                } else if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_3) {
                    ActivityCtrl.gotoOpenActivity(mActivity, VideoDetailActivity.class, mainListAdapter.getDataItem(position));
                }
            }
        });

        listRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionId = Math.abs((int) System.currentTimeMillis());
                mainListAdapter.clear();
                requestData();
            }
        });
    }

    private void requestData(){
        ContentListRequestParam newsListRequestParam = new ContentListRequestParam(sessionId, channelId, false, false, false);
        MorningDataAPI.requestContentList(getApplicationContext(), newsListRequestParam, new ResultCallback<ContentList>() {
            @Override
            public void onSuccess(ContentList data) {
                if (mActivity == null){
                    return;
                }
                mActivity.runOnUiThread(() -> refreshUIData(data));
            }

            @Override
            public void onLoadFromCache(ContentList data) {

            }

            @Override
            public void onFail(Exception e) {
                showEmpty();
            }
        });
    }

    private void refreshUIData(ContentList data){
        if (data != null){
            ArrayList<ContentItem> contentItems = data.items;
            List<DataListItem> datas = new ArrayList<>();
            if (contentItems.size() == 0 && mainListAdapter.getDataSize() != 0){
                mRecyclerView.setNoMore(true);
            }
            for (ContentItem contentItem : contentItems){
                DataListItem dataItem = new DataListItem();
                if ("NEWS".equals(contentItem.contentType)){
                    dataItem.setType(DataListItem.DATA_TYPE_1);
                }else if ("PHOTO".equals(contentItem.contentType)){
                    dataItem.setType(DataListItem.DATA_TYPE_2);
                }else if ("VIDEO".equals(contentItem.contentType)){
                    dataItem.setType(DataListItem.DATA_TYPE_3);
                }
                dataItem.setResourceId(contentItem.resourceId);
                dataItem.setId(contentItem.id);
                if (contentItem instanceof VideoItem){
                    VideoItem videoItem = (VideoItem) contentItem;
                    dataItem.setVideoUrl(videoItem.sourceUrl);
                    if (videoItem.photoInfos.size() >= 1){
                        dataItem.setPicUrl(videoItem.photoInfos.get(0).originUrl);
                        dataItem.setVideoThumbUrl(videoItem.photoInfos.get(0).originUrl);
                        dataItem.setWidth(videoItem.photoInfos.get(0).width);
                        dataItem.setHeight(videoItem.photoInfos.get(0).height);
                    }
                    if (dataItem.getWidth() != 0 && dataItem.getHeight() != 0){
                        datas.add(dataItem);
                    }
                }else if (contentItem instanceof NewsItem){
                    NewsItem newsItem = (NewsItem) contentItem;
                    dataItem.setData(newsItem.title);
                    datas.add(dataItem);
                }

            }
            if (isRefresh){
                isRefresh = false;
                mainListAdapter.refresh(datas);
            }else {
                mainListAdapter.addAll(datas);
            }

            mRecyclerView.refreshComplete(0);
        }
        showEmpty();
    }

    /**
     * 是否显示空页面
     */
    private void showEmpty(){
        if (mActivity == null){
            return;
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mainListAdapter.getDataSize() == 0){
                    llListRetry.setVisibility(View.VISIBLE);
                }else {
                    llListRetry.setVisibility(View.GONE);
                }
            }
        });
    }

}
