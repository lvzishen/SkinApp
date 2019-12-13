package com.time.skindemo.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 创建日期：2019/12/11 on 16:06
 * 描述:
 * 作者: lvzishen
 */
public class SkinResource {

    //资源都是通过这个对象获取
    private Resources mSkinResource;
    private String mSkinPackageName;

    public SkinResource(Context context, String skinPath) {
        //创建AssetManager
        try {
            Log.i("SkinType", "skinPath ->" + skinPath);
            Resources superRes = context.getResources();
            AssetManager asset = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(asset, skinPath);
            mSkinResource = new Resources(asset, superRes.getDisplayMetrics(), superRes.getConfiguration());
            //获取skinPath包名
            mSkinPackageName = context.getPackageManager()
                    .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        } catch (Exception e) {
            Log.i("SkinType", "Exception ->" + e);
            e.printStackTrace();
        }

    }

    /**
     * 通过名称获取Drawable对象
     *
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName) {
        try {
            int resId = mSkinResource.getIdentifier(resName, "drawable", mSkinPackageName);
            Log.i("SkinType", "packagename ->" + mSkinPackageName);
            Drawable drawable = mSkinResource.getDrawable(resId);
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过名称获取ColorStateList对象
     *
     * @param resName
     * @return
     */
    public ColorStateList getColorByName(String resName) {
        try {
            int resId = mSkinResource.getIdentifier(resName, "color", mSkinPackageName);
            ColorStateList colorStateList = mSkinResource.getColorStateList(resId);
            return colorStateList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
