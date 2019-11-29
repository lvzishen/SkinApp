package com.goodmorning.ui.activity;


import com.creativeindia.goodmorning.R;
import com.goodmorning.bean.DataListItem;

import org.n.account.core.api.NjordAccountManager;
import org.n.account.core.model.Account;

/**
 * 创建日期：2019/11/25 on 15:22
 * 描述:
 * 作者: lvzishen
 */
public class PicDetailActivity extends BaseDetailActivity {

    @Override
    protected int getType() {
        return DataListItem.DATA_TYPE_2;
    }

    @Override
    protected String getTextTitle() {
        return getResources().getString(R.string.string_app_name);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isGoLogin) {
            isGoLogin = false;
            Account account = NjordAccountManager.getCurrentAccount(this);
            if (account != null && (!account.isGuest() && NjordAccountManager.isLogined(getApplicationContext()))) {
                onClick(mShareItem);
            }
        }
    }
}
