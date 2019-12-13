package com.time.skindemo.skin;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.time.skindemo.skin.attr.SkinAttr;
import com.time.skindemo.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期：2019/12/11 on 16:06
 * 描述: 皮肤属性解析类
 * 作者: lvzishen
 */
public class SkinAttrsSupport {

    private static final String TAG = "SkinAttrsSupport";

    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        //解析background src textcolor
        List<SkinAttr> skinAttrs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //获取名称和值
            String attrsName = attrs.getAttributeName(i);
            String attrsValue = attrs.getAttributeValue(i);
            Log.e(TAG, "attrsName -> " + attrsName + " ;attrsValue -> " + attrsValue);
            //只获取重要的
            SkinType skinType = getSkinType(attrsName);
            if (skinType != null) {
                //资源名称 目前只有attrsValue 是一个@int类型 background ;attrsValue -> @2130968615
                String resName = getResName(context, attrsValue);
                if (TextUtils.isEmpty(resName)) {
                    //不是 @符号开头
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(resName, skinType);
                skinAttrs.add(skinAttr);
            }
        }

        return skinAttrs;
    }

    /**
     * 获取资源的名称
     *
     * @param context
     * @param attrsValue
     * @return
     */
    private static String getResName(Context context, String attrsValue) {
        if (attrsValue.startsWith("@")) {
            //src ;attrsValue -> @2131099734
            attrsValue = attrsValue.substring(1);
            int resId = Integer.valueOf(attrsValue);
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    /**
     * 通过名称获取SkinType
     *
     * @param attrsName
     * @return
     */
    private static SkinType getSkinType(String attrsName) {
        String[] changeNames = SkinType.values();
        for (String changeName : changeNames) {
            if (changeName.equals(attrsName)) {
                return new SkinType(attrsName);
            }
        }
        return null;
    }
}
