package com.goodmorning.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeindia.goodmorning.R;
import com.goodmorning.manager.ContentManager;
import com.goodmorning.utils.ResUtils;

import org.thanos.netcore.bean.ChannelList;

public class LanguageSetAdapter extends ListBaseAdapter<ChannelList.LanguageItem> {
    private String lang = "";
    LanguageAdapter.OnSwitchLanguage onSwitchLanguage;
    public LanguageSetAdapter(Context context) {
        super(context);
        lang = ContentManager.getInstance().getLang();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_item_setting_language;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tvLanguage = holder.getView(R.id.tv_setting_language);
        ImageView ivSelect = holder.getView(R.id.iv_setting_select_lang);
        RelativeLayout llLanguage = holder.getView(R.id.rl_item_language);
        tvLanguage.setText(mDataList.get(position).text);
        if (lang .equals(mDataList.get(position).text)){
            ivSelect.setVisibility(View.VISIBLE);
            tvLanguage.setTextColor(ResUtils.getColor(R.color.lang_select_txt_color));
        }else {
            ivSelect.setVisibility(View.GONE);
            tvLanguage.setTextColor(ResUtils.getColor(R.color.setting_txt_tag_color));
        }

        llLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSwitchLanguage != null){
                    lang = mDataList.get(position).lang;
                    onSwitchLanguage.onLanguage(lang);
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void setOnSwitchLanguage(LanguageAdapter.OnSwitchLanguage onSwitchLanguage){
        this.onSwitchLanguage = onSwitchLanguage;
    }
}
