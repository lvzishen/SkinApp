package com.goodmorning.adapter;

import android.content.Context;
import android.util.Log;

import com.creativeindia.goodmorning.R;
import com.goodmorning.bean.DataListItem;


public class MainListAdapter extends ListBaseAdapter<DataListItem> {
    private int layId;

    public MainListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        Log.e("MainListAdapter","viewType=="+viewType);
        switch (viewType){
            case DataListItem.DATA_TYPE_1:
                layId = R.layout.layout_item_text;
                break;
            case DataListItem.DATA_TYPE_2:
                layId = R.layout.layout_item_pic;
                break;
            case DataListItem.DATA_TYPE_3:
                layId = R.layout.layout_item_video;
                break;
        }
        return layId;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        Log.e("MainListAdapter","getItemCount=="+mDataList.size());
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("MainListAdapter","getItemViewType=="+mDataList.get(position).getType());
        return mDataList.get(position).getType();
    }
}
