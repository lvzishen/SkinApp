package com.goodmorning.adapter;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.statistic.StatisticLoggerX;
import com.creativeindia.goodmorning.R;
import com.goodmorning.manager.ContentManager;
import com.goodmorning.utils.ResUtils;

import org.thanos.netcore.bean.ChannelList;

public class LanguageAdapter extends ListBaseAdapter<ChannelList.LanguageItem> {
    OnSwitchLanguage onSwitchLanguage;
    private String lang = "";
    public LanguageAdapter(Context context) {
        super(context);
        lang = ContentManager.getInstance().getLang();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_item_language;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tvLanguage = holder.getView(R.id.tv_language);
        ImageView ivSelect = holder.getView(R.id.iv_select_lang);
        RelativeLayout llLanguage = holder.getView(R.id.ll_language);
        tvLanguage.setText(mDataList.get(position).text);
        if (lang .equals(mDataList.get(position).text)){
            ivSelect.setVisibility(View.VISIBLE);
            tvLanguage.setTextColor(ResUtils.getColor(R.color.lang_select_txt_color));
            StatisticLoggerX.logShowUpload("","lang popup",mDataList.get(position).lang,"","");

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
                    StatisticLoggerX.logClickUpload("","lang popup",lang,"","");
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
