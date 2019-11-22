package com.goodmorning.adapter;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creativeindia.goodmorning.R;

import org.thanos.netcore.bean.ChannelList;

public class LanguageAdapter extends ListBaseAdapter<ChannelList.LanguageItem> {
    OnSwitchLanguage onSwitchLanguage;
    public LanguageAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_item_language;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tvLanguage = holder.getView(R.id.tv_language);
        LinearLayout llLanguage = holder.getView(R.id.ll_language);
        tvLanguage.setText(mDataList.get(position).text);
        llLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSwitchLanguage != null){
                    String lang = "en";
                    if ("ta_in".equals(mDataList.get(position).lang)){
                        lang = "ta";
                    }else {
                        lang = mDataList.get(position).lang;
                    }
                    onSwitchLanguage.onLanguage(lang);
                }
            }
        });
    }

    public void setOnSwitchLanguage(OnSwitchLanguage onSwitchLanguage){
        this.onSwitchLanguage = onSwitchLanguage;
    }

    public interface OnSwitchLanguage{
        void onLanguage(String languge);
    }
}
