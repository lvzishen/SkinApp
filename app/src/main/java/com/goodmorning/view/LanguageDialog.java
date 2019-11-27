package com.goodmorning.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baselib.sp.SharedPref;
import com.creativeindia.goodmorning.R;
import com.goodmorning.adapter.LanguageAdapter;
import com.goodmorning.decoration.DividerDecoration;
import com.goodmorning.decoration.SpacesItemDecoration;
import com.goodmorning.utils.ResUtils;

import org.thanos.netcore.bean.ChannelList;
import org.thanos.netcore.helper.JsonHelper;

import java.util.ArrayList;

public class LanguageDialog extends Dialog {

    private RecyclerView rvLanguage;
    private LanguageAdapter languageAdapter;
    private JsonHelper<ArrayList<ChannelList.LanguageItem> > jsonHelper;
    public LanguageDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        View view = ResUtils.getInflater().inflate(R.layout.language_layout, null);
        setContentView(view);
        initView();
        initDialog();
    }

    private void initView(){
        rvLanguage = findViewById(R.id.rv_language);
        languageAdapter = new LanguageAdapter(getContext());
        jsonHelper = new JsonHelper<ArrayList<ChannelList.LanguageItem>>() {
        };
        ArrayList<ChannelList.LanguageItem> languageItems = jsonHelper.getJsonObject(SharedPref.getString(getContext(), SharedPref.LANGUAGE_TYPE,""));
        languageAdapter.addAll(languageItems);
        rvLanguage.setAdapter(languageAdapter);
        DividerDecoration divider = new DividerDecoration.Builder(getContext())
                .setHeight(ResUtils.getDimension(R.dimen.qb_px_1)/2)
                .setPadding(ResUtils.getDimension(R.dimen.qb_px_14))
                .setColorResource(R.color.setting_line_color)
                .build();
        rvLanguage.addItemDecoration(divider);
        rvLanguage.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setOnSwitchLanguage(LanguageAdapter.OnSwitchLanguage onSwitchLanguage){
        if (languageAdapter != null){
            languageAdapter.setOnSwitchLanguage(onSwitchLanguage);
        }
    }

    private void initDialog(){
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.gravity = Gravity.CENTER;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

}
