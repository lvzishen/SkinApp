package com.goodmorning.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativeindia.goodmorning.R;
import com.goodmorning.bean.ShareItem;
import com.goodmorning.view.recyclerview.normal.BaseViewHolder;


/**
 * Created by lvzishen on 2018/3/28.
 */

public class ShareCommonHolder extends BaseViewHolder {
    private View mParent;
    private TextView mTitle;
    private ImageView mImage;
    private ShareItem mItem;

    public ShareCommonHolder(View itemView, OnClickListener onClickListener) {
        super(itemView);
        if (itemView != null) {
            this.mOnClickListener = onClickListener;
            mTitle = itemView.findViewById(R.id.share_text);
            mImage = itemView.findViewById(R.id.share_image);
            mParent = itemView.findViewById(R.id.share_parent);
        }
    }

    private OnClickListener mOnClickListener;


    @Override
    public void bind(Object o) {
        if (o == null || !(o instanceof ShareItem))
            return;
        mItem = (ShareItem) o;
        mTitle.setText(mItem.text);
        mImage.setImageResource(mItem.imageResource);
        //点击事件
        mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(mItem);
                }
            }
        });
    }

    public interface OnClickListener {
        void onClick(ShareItem shareItem);
    }
}
