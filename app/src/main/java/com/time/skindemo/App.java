package com.time.skindemo;

import android.app.Application;

import com.time.skindemo.skin.SkinManager;

/**
 * 创建日期：2019/12/13 on 12:28
 * 描述:
 * 作者: lvzishen
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
