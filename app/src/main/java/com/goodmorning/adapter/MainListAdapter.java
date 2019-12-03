package com.goodmorning.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.creativeindia.goodmorning.R;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.utils.ImageUtil;
import com.goodmorning.utils.ImageUtilHandle;
import com.goodmorning.utils.ScreenUtils;

import java.util.List;


public class MainListAdapter extends ListBaseAdapter<DataListItem> {
    private int layId;
    private Activity mContext;
    private int[] defaultDrawables = {R.drawable.shape_list_item_default_1,R.drawable.shape_list_item_default_2,
            R.drawable.shape_list_item_default_3,R.drawable.shape_list_item_default_4,
            R.drawable.shape_list_item_default_5,R.drawable.shape_list_item_default_6};
    private int posid = 0;

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
            case DataListItem.DATA_TYPE_4:
                layId = R.layout.layout_item_no_collect;
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
        }else if (DataListItem.DATA_TYPE_4 == mDataList.get(position).getType()){

        }
    }

    private void refreshUI(ImageView imageView,DataListItem dataListItem){
        ViewGroup.LayoutParams layoutParams =  imageView.getLayoutParams();
        float itemWidth = (ScreenUtils.screenActualPix(mContext)[0] - 20*3)/2;
        layoutParams.width = (int) itemWidth;
        float scale = (itemWidth+0f)/dataListItem.getWidth();
        layoutParams.height = (int) (dataListItem.getHeight()*scale);
        imageView.setLayoutParams(layoutParams);
        ImageUtil.displayImageView(mContext,imageView,dataListItem.getPicUrl(),defaultDrawables[posid],layoutParams.width, layoutParams.height);
        posid++;
        if (posid >= defaultDrawables.length){
            posid = 0;
        }
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

    /**
     * 获取列表内容数量
     * @return
     */
    public int getDataSize(){
        return mDataList.size();
    }

    public void refresh(List<DataListItem> dataListItems){
        mDataList = dataListItems;
        notifyDataSetChanged();
    }
}
