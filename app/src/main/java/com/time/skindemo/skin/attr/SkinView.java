package com.time.skindemo.skin.attr;

import android.view.View;

import java.util.List;

/**
 * 创建日期：2019/12/11 on 17:29
 * 描述:
 * 作者: lvzishen
 */
public class SkinView {
    private View mView;
    private List<SkinAttr> mAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mAttrs = skinAttrs;
    }

    public void skin() {
        for (SkinAttr skinAttr : mAttrs) {
            skinAttr.skin(mView);
        }
    }
}
