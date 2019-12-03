package com.goodmorning.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baselib.language.LanguageUtil;
import com.baselib.sp.SharedPref;
import com.baselib.ui.activity.BaseActivity;
import com.creativeindia.goodmorning.R;
import com.goodmorning.adapter.LanguageAdapter;
import com.goodmorning.adapter.LanguageSetAdapter;
import com.goodmorning.decoration.DividerDecoration;
import com.goodmorning.manager.ContentManager;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.utils.AppUtils;
import com.goodmorning.utils.ResUtils;

import org.thanos.netcore.bean.ChannelList;
import org.thanos.netcore.helper.JsonHelper;

import java.util.ArrayList;

public class LanguageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivLanguageBack;
    private RecyclerView rvSettingLanguage;
    private LanguageSetAdapter languageSetAdapter;
    private JsonHelper<ArrayList<ChannelList.LanguageItem>> jsonHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        setStatusBarColor(ResUtils.getColor(R.color.white));
        setAndroidNativeLightStatusBar(true);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        ivLanguageBack = findViewById(R.id.iv_language_back);
        rvSettingLanguage = findViewById(R.id.rv_setting_language);
    }

    private void initData(){
        languageSetAdapter = new LanguageSetAdapter(getApplicationContext());
        jsonHelper = new JsonHelper<ArrayList<ChannelList.LanguageItem>>() {
        };
        ArrayList<ChannelList.LanguageItem> languageItems = jsonHelper.getJsonObject(SharedPref.getString(getApplicationContext(), SharedPref.LANGUAGE_TYPE,""));
        languageSetAdapter.addAll(languageItems);
        rvSettingLanguage.setAdapter(languageSetAdapter);
        DividerDecoration divider = new DividerDecoration.Builder(getApplicationContext())
                .setHeight(ResUtils.getDimension(R.dimen.qb_px_1)/2)
                .setPadding(ResUtils.getDimension(R.dimen.qb_px_14))
                .setColorResource(R.color.setting_line_color)
                .build();
        rvSettingLanguage.addItemDecoration(divider);
        rvSettingLanguage.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void setListener(){
        ivLanguageBack.setOnClickListener(this);
        languageSetAdapter.setOnSwitchLanguage(new LanguageAdapter.OnSwitchLanguage() {
            @Override
            public void onLanguage(String languge) {
                if (languge.equals(LanguageUtil.getLanguage())){
                    ContentManager.getInstance().setChangeLang(false);
                }else {
                    ContentManager.getInstance().setChangeLang(true);
                    AppUtils.changeLanguage(LanguageActivity.this,languge);
                }
                ActivityCtrl.gotoActivityOpenSimple(LanguageActivity.this,SettingActivity.class);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_language_back:
                ActivityCtrl.gotoActivityOpenSimple(this,SettingActivity.class);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ActivityCtrl.gotoActivityOpenSimple(this,SettingActivity.class);
        finish();
    }
}
