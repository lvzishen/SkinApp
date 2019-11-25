package com.goodmorning.view.recyclerview.normal;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by yeguolong on 17-3-24.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements IViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }
    
}
