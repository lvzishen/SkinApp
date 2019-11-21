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

import com.creativeindia.goodmorning.R;
import com.goodmorning.adapter.LanguageAdapter;
import com.goodmorning.utils.ResUtils;

public class LanguageDialog extends Dialog {

    private RecyclerView rvLanguage;
    private LanguageAdapter languageAdapter;

    public LanguageDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        View view = ResUtils.getInflater().inflate(R.layout.language_layout, null);
        setContentView(view);
        initView();
        initDialog();
    }

    private void initView(){
        rvLanguage = findViewById(R.id.rv_language);
        languageAdapter = new LanguageAdapter(getContext());
        rvLanguage.setAdapter(languageAdapter);
        rvLanguage.setLayoutManager(new LinearLayoutManager(getContext()));
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
