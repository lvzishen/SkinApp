package com.goodmorning.ui.fragment;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private MainListAdapter mainListAdapter;
    private CommonRecyclerViewAdapter mRecyclerViewAdapter;
    private int sessionId;
    private int channelId;
    private Activity mActivity;
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
        mRecyclerView.addItemDecoration(new DiverItemDecoration(20,2));
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
                sessionId = Math.abs((int) System.currentTimeMillis());
                mainListAdapter.clear();
                requestData();
            }
        });

        mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //调换到详情页
                if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_1) {
                    ActivityCtrl.gotoOpenActivity(mActivity, TextDetailActivity.class, mainListAdapter.getDataItem(position));
                } else if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_2) {
                    ActivityCtrl.gotoOpenActivity(mActivity, PicDetailActivity.class, mainListAdapter.getDataItem(position));
                } else if (mainListAdapter.getDataItem(position).getType() == DataListItem.DATA_TYPE_3) {
                    ActivityCtrl.gotoOpenActivity(mActivity, VideoDetailActivity.class, mainListAdapter.getDataItem(position));
                }
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

            }
        });
    }

    private void refreshUIData(ContentList data){
        if (data != null){
            ArrayList<ContentItem> contentItems = data.items;
            List<DataListItem> datas = new ArrayList<>();
            if (contentItems.size() == 0){
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
            mRecyclerView.refreshComplete(0);
        }
    }

}
