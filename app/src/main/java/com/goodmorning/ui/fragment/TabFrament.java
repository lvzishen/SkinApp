package com.goodmorning.ui.fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.adapter.MainListAdapter;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.view.recyclerview.CommonRecyclerView;
import com.goodmorning.view.recyclerview.CommonRecyclerViewAdapter;
import com.goodmorning.view.recyclerview.decoration.DiverItemDecoration;
import com.goodmorning.view.recyclerview.interfaces.OnItemClickListener;
import com.goodmorning.view.recyclerview.interfaces.OnLoadMoreListener;
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

public class TabFrament extends Fragment {

    private CommonRecyclerView mRecyclerView;
    private MainListAdapter mainListAdapter;
    private CommonRecyclerViewAdapter mRecyclerViewAdapter;
    private Handler handler = new Handler();
    private int sessionId;
    private int channelId;
    private boolean isLoadMore = false;
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
    }

    private void initData(){
        channelId = getArguments().getInt(MainActivity.CONTENT);
        mainListAdapter = new MainListAdapter(getActivity());
//        mainListAdapter.addAll(addData());
        mRecyclerViewAdapter = new CommonRecyclerViewAdapter(mainListAdapter);
        CustomRefreshHeader customRefreshHeader = new CustomRefreshHeader(getContext());
        mRecyclerView.setRefreshHeader(customRefreshHeader);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //防止item位置互换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DiverItemDecoration(10,2));
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
                Log.e("TabFragment","加载更多===sessionId="+sessionId+" ;channelID="+channelId);
                isLoadMore = true;
                requestData();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainListAdapter.addAll(addData());
//                        mRecyclerView.setNoMore(true);
//                    }
//                },3000);
            }
        });

        mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void requestData(){
        ContentListRequestParam newsListRequestParam = new ContentListRequestParam(sessionId, channelId, false, false, false);
        MorningDataAPI.requestContentList(getApplicationContext(), newsListRequestParam, new ResultCallback<ContentList>() {
            @Override
            public void onSuccess(ContentList data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshUIData(data);
                    }
                });
            }

            @Override
            public void onLoadFromCache(ContentList data) {

            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private void refreshUIData(ContentList data){
        if (data != null){
            ArrayList<ContentItem> contentItems = data.items;
            if (contentItems.size() == 0){
                mRecyclerView.setNoMore(true);
                return;
            }
            Log.e("TabFragment","contentItems="+contentItems.size()+" ;channelID="+channelId);
            List<DataListItem> datas = new ArrayList<>();
            if (contentItems != null){
                for (ContentItem contentItem : contentItems){
                    DataListItem dataItem = new DataListItem();
                    if ("NEWS".equals(contentItem.contentType)){
                        dataItem.setType(DataListItem.DATA_TYPE_1);
                    }else if ("PHOTO".equals(contentItem.contentType)){
                        dataItem.setType(DataListItem.DATA_TYPE_2);
                    }else if ("VIDEO".equals(contentItem.contentType)){
                        dataItem.setType(DataListItem.DATA_TYPE_3);
                    }
                    if (contentItem instanceof VideoItem){
                        VideoItem videoItem = (VideoItem) contentItem;
                        dataItem.setVideoUrl(videoItem.sourceUrl);
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
                mRecyclerView.refreshComplete(20);
//                if (isLoadMore){
//                    isLoadMore = false;
//                    mRecyclerView.refreshComplete(20);
//                }

            }
        }
    }
    private List<DataListItem> addData(){
        List<DataListItem> datas = new ArrayList<>();
        for (int i=0;i<20;i++){
            DataListItem dataItem = new DataListItem();
            dataItem.setData(""+i);
            if (i%2==0){
                dataItem.setType(DataListItem.DATA_TYPE_2);
            }else if (i%3==0){
                dataItem.setType(DataListItem.DATA_TYPE_3);
            }else {
                dataItem.setType(DataListItem.DATA_TYPE_1);
            }
            datas.add(dataItem);
        }
        return datas;
    }

}
