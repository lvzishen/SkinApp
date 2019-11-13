package com.goodmorning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hao on 2017/3/30.
 */

public abstract class ListBaseAdapter<T> extends RecyclerView.Adapter<SuperViewHolder> {
    private LayoutInflater mInflater;
    protected List<T> mDataList = new ArrayList<>();

    public ListBaseAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(getLayoutId(viewType),parent,false);
        return new SuperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        onBindItemHolder(holder,position);
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position, List payloads) {
        if (payloads.isEmpty()){
            onBindItemHolder(holder,position);
        }else {
            onBindItemHolder(holder,position,payloads);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<T> getDataList(){
        return mDataList;
    }

    public void setDataList(Collection<T> list){
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(T data){
        this.mDataList.add(data);
        notifyDataSetChanged();
//        int lastIndex = this.mDataList.size();
//        if (this.mDataList.add(data)){
//            notifyItemRangeInserted(lastIndex,1);
//        }
    }

    public void addAll(Collection<T> list){
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)){
            notifyItemRangeInserted(lastIndex,list.size());
        }
    }

    public void addFromBottom(T data){
        int lastIndex = this.mDataList.size();
        this.mDataList.add(0,data);
        notifyItemRangeInserted(0,1);
    }

    public void remove(int position){
        this.mDataList.remove(position);
        notifyItemRemoved(position);
        if (position != (getDataList().size())){//如果移除的是最后一个忽略
            notifyItemRangeChanged(position,this.mDataList.size()-position);
        }
    }

    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    public abstract int getLayoutId(int viewType);

    public abstract void onBindItemHolder(SuperViewHolder holder,int position);

    public void onBindItemHolder(SuperViewHolder holder, int position, List<Object> payloads){}

}
