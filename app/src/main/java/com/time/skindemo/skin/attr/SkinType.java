package com.time.skindemo.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.skindemo.skin.SkinManager;
import com.time.skindemo.skin.SkinResource;

/**
 * 创建日期：2019/12/11 on 17:30
 * 描述:
 * 作者: lvzishen
 */
public class SkinType {

    private static final String TEXT_COLOR = "textColor";
    private static final String BACKGROUND = "background";
    private static final String SRC = "src";
    private String mAttrName;
    private static String[] attrs = new String[]{TEXT_COLOR, BACKGROUND, SRC};
    private static final String TAG = "SkinType";

    public SkinType(String mAttrName) {
        this.mAttrName = mAttrName;
    }

    public static String[] values() {
        return attrs;
    }

    public void skin(View mView, String resName) {
        Log.i(TAG, "attrname -> " + mAttrName + ",resName -> " + resName);
        SkinResource skinResource = getSkinResource();
        if (mAttrName.equals(TEXT_COLOR)) {
            ColorStateList colorStateList = skinResource.getColorByName(resName);
            if (colorStateList == null) {
                return;
            }
            TextView textView = (TextView) mView;
            textView.setTextColor(colorStateList);
        }
        if (mAttrName.equals(BACKGROUND)) {
            //背景可能是颜色或图片
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                ImageView imageView = (ImageView) mView;
                imageView.setBackgroundDrawable(drawable);
                return;
            }
            //可能是颜色
            ColorStateList colorStateList = skinResource.getColorByName(resName);
            if (colorStateList != null) {
                mView.setBackgroundColor(colorStateList.getDefaultColor());
            }
        }
        if (mAttrName.equals(SRC)) {
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                ImageView imageView = (ImageView) mView;
                imageView.setImageDrawable(drawable);
            }
        }

    }

    private SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }

}
