package com.goodmorning.ui.fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cleanerapp.supermanager.R;
import com.goodmorning.adapter.MainListAdapter;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.view.recyclerview.CommonRecyclerView;
import com.goodmorning.view.recyclerview.CommonRecyclerViewAdapter;
import com.goodmorning.view.recyclerview.decoration.DiverItemDecoration;
import com.goodmorning.view.recyclerview.interfaces.OnItemClickListener;
import com.goodmorning.view.recyclerview.interfaces.OnLoadMoreListener;
import com.goodmorning.view.recyclerview.view.CustomLoadingFooter;
import com.goodmorning.view.recyclerview.view.CustomRefreshHeader;

import java.util.ArrayList;
import java.util.List;

public class TabFrament extends Fragment {

    private CommonRecyclerView mRecyclerView;
    private MainListAdapter mainListAdapter;
    private CommonRecyclerViewAdapter mRecyclerViewAdapter;
    private Handler handler = new Handler();
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
        mainListAdapter = new MainListAdapter(getContext());
        mainListAdapter.addAll(addData());
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
    }

    private void setListener(){
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainListAdapter.addAll(addData());
                        mRecyclerView.setNoMore(true);
                    }
                },3000);
            }
        });

        mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
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
