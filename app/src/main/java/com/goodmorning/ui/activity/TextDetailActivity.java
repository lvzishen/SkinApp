package com.goodmorning.ui.activity;

import android.view.View;

import com.creativeindia.goodmorning.R;
import com.goodmorning.bean.DataListItem;

/**
 * 创建日期：2019/11/25 on 15:22
 * 描述:
 * 作者: lvzishen
 */
public class TextDetailActivity extends BaseDetailActivity {

    @Override
    protected int getType() {
        return DataListItem.DATA_TYPE_1;
    }

    @Override
    protected String getTextTitle() {
        return getResources().getString(R.string.string_app_name);
    }

    @Override
    protected void onClickCollect(View v) {

    }

}
