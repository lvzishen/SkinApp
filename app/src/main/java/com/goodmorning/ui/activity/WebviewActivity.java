package com.goodmorning.ui.activity;
import android.os.Bundle;
import com.baselib.ui.activity.BaseActivity;
import com.cleanerapp.supermanager.R;
import com.goodmorning.view.XWebView;

public class WebviewActivity extends BaseActivity {
    private XWebView xWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView(){
        xWebView = findViewById(R.id.xwebview);
    }
}
