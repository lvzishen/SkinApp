package com.goodmorning.adapter;
import android.content.Context;
import com.creativeindia.goodmorning.R;

import org.thanos.core.bean.ChannelList;

public class LanguageAdapter extends ListBaseAdapter<ChannelList.LanguageItem> {
    public LanguageAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_item_language;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

    }

}
