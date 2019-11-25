package com.goodmorning.view.recyclerview.normal;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseRecyclerViewAdapter
 * Created by yeguolong on 17-7-25.
 */
public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<IItem> mList;

    public BaseRecyclerAdapter(Context context, List<IItem> list) {
        this.mContext = context;
        mList = new ArrayList<>();
        setItemList(list);
    }

    public void setItemList(List<IItem> list) {
        this.mList.clear();
        this.mList.addAll(list);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return onCreateViewHolder(mContext, parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null)
            return;
        IItem item = getItem(position);
        if (item != null) {
            IViewHolder iViewHolder = (IViewHolder) holder;
            iViewHolder.bind(item);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public IItem getItem(int position) {
        if (mList != null && position >= 0 && position < mList.size()) {
            try {
                return mList.get(position);
            } catch (Exception e) {
                // do nothing.
            }
        }
        return null;
    }

    public void notifySetListDataChanged(List<IItem> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null && position >= 0 && position < mList.size()) {
            IItem iItem = null;
            try {
                iItem = mList.get(position);
            } catch (Exception e) {
                // do nothing.
            }
            if (iItem != null) {
                Log.i("BaseRecyclerAdapter", "position" + iItem.getType());
                return iItem.getType();
            }
        }
        return super.getItemViewType(position);
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType);
}
