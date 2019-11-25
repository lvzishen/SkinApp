package com.goodmorning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.creativeindia.goodmorning.R;
import com.goodmorning.bean.ShareItem;


/**
 * Created by lvzishen on 2018/3/28.
 */

public class ShareCommonFactory {
    public static final int VIEW_TYPE_ITEM_SHARE_COMMON = ShareItem.NORMAL;

    private static View getView(Context context, ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM_SHARE_COMMON:
                return LayoutInflater.from(context).inflate(R.layout.layout_item_share, parent, false);
        }
        return null;
    }

    public static RecyclerView.ViewHolder getHolder(Context context, ViewGroup parent, int viewType, ShareCommonHolder.OnClickListener onClickListener) {
        View view = getView(context, parent, viewType);
        switch (viewType) {
            case VIEW_TYPE_ITEM_SHARE_COMMON:
                return new ShareCommonHolder(view, onClickListener);

        }
        return null;
    }


}
