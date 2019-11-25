package com.goodmorning.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creativeindia.goodmorning.R;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.utils.ImageUtil;
import com.goodmorning.utils.ScreenUtils;


public class MainListAdapter extends ListBaseAdapter<DataListItem> {
    private int layId;
    private Activity mContext;

    public MainListAdapter(Context context) {
        super(context);
        mContext = (Activity) context;
    }

    @Override
    public int getLayoutId(int viewType) {
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
        if(DataListItem.DATA_TYPE_1 == mDataList.get(position).getType()){
            //文本
            TextView textView = holder.getView(R.id.tv_txt);
            textView.setText(mDataList.get(position).getData());
        }else if (DataListItem.DATA_TYPE_2 == mDataList.get(position).getType()){
            //图片
            ImageView ivPic = holder.getView(R.id.iv_pic);
            refreshUI(ivPic,mDataList.get(position));

        }else if (DataListItem.DATA_TYPE_3 == mDataList.get(position).getType()){
            //视频
            ImageView ivVideo = holder.getView(R.id.iv_video);
            refreshUI(ivVideo,mDataList.get(position));
        }
    }

    private void refreshUI(ImageView imageView,DataListItem dataListItem){
        ViewGroup.LayoutParams layoutParams =  imageView.getLayoutParams();
        float itemWidth = (ScreenUtils.screenActualPix(mContext)[0] - 20*3)/2;
        layoutParams.width = (int) itemWidth;
        float scale = (itemWidth+0f)/dataListItem.getWidth();
        layoutParams.height = (int) (dataListItem.getHeight()*scale);
        imageView.setLayoutParams(layoutParams);
        ImageUtil.displayImageView(mContext,imageView,dataListItem.getPicUrl(),layoutParams.width, layoutParams.height);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getType();
    }

    /**
     * 获取数据item
     * @param position 位置
     * @return 数据内容
     */
    public DataListItem getDataItem(int position){
        return mDataList.get(position);
    }
}
