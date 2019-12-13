package com.time.skindemo.skin.attr;

import android.view.View;

/**
 * 创建日期：2019/12/11 on 17:29
 * 描述:
 * 作者: lvzishen
 */
public class SkinAttr {
    private String mResourceName;

    private SkinType mSkinType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResourceName = resName;
        this.mSkinType = skinType;
    }

    public void skin(View mView) {
        mSkinType.skin(mView, mResourceName);
    }
}
