package com.goodmorning.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.baselib.ui.activity.BaseActivity;
import com.creativeindia.goodmorning.R;
import com.goodmorning.adapter.MainListAdapter;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.utils.ResUtils;
import com.goodmorning.view.recyclerview.CommonRecyclerView;
import com.goodmorning.view.recyclerview.CommonRecyclerViewAdapter;
import com.goodmorning.view.recyclerview.decoration.DiverItemDecoration;
import com.goodmorning.view.recyclerview.interfaces.OnItemClickListener;
import com.goodmorning.view.recyclerview.view.CustomLoadingFooter;
import com.goodmorning.view.recyclerview.view.CustomRefreshHeader;

public class MyCollectActivity extends BaseActivity {
    private CommonRecyclerView rvCollectList;
    private MainListAdapter mainListAdapter;
    private CommonRecyclerViewAdapter mRecyclerViewAdapter;
    private FrameLayout myCollectBack;
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
                }
            }
        });
    }
}
