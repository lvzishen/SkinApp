package com.time.skindemo.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.time.skindemo.BaseActivity;
import com.time.skindemo.skin.attr.SkinView;
import com.time.skindemo.skin.config.SkinPreUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建日期：2019/12/11 on 16:06
 * 描述:
 * 作者: lvzishen
 */
public class SkinManager {
    private SkinResource mSkinResource;
    private Context mContext;
    private static volatile SkinManager skinManager;
    private Map<Activity, List<SkinView>> mSkinViews = new HashMap<>();

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        if (skinManager == null) {
            synchronized (SkinManager.class) {
                if (skinManager == null) {
                    skinManager = new SkinManager();
                }
            }
        }
        return skinManager;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        //每次打开应用都会到这里来，防止皮肤被删除，做一些措施
        String currentSkinPath = SkinPreUtils.getInstance(context).getSkinPath();
        File file = new File(currentSkinPath);
        if (!file.exists()) {
            //不存在，清空皮肤
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }
        //做一些初始化的工作
        //判断是否能获取包名，没有包名说明不是APK
        String skinPackageName = context.getPackageManager()
                .getPackageArchiveInfo(currentSkinPath, PackageManager.GET_ACTIVITIES).packageName;
        if (TextUtils.isEmpty(skinPackageName)) {
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }
        // 最好校验签名

        initSkinResouce(mContext, currentSkinPath);
    }

    /**
     * 加载皮肤
     *
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {
        //初始化资源管理
        initSkinResouce(mContext, skinPath);
        for (Activity activity : mSkinViews.keySet()) {
            List<SkinView> skinViews = mSkinViews.get(activity);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
        //保存皮肤的状态
        saveSkinStatus(skinPath);
        return 0;
    }

    private void saveSkinStatus(String skinPath) {
        SkinPreUtils.getInstance(mContext).saveSkinPath(skinPath);
    }

    private void initSkinResouce(Context context, String skinPath) {
        mSkinResource = new SkinResource(context, skinPath);
    }

    /**
     * 获取当前的皮肤资源
     *
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }


    /**
     * 恢复默认皮肤
     *
     * @return
     */
    public int restoreDefault() {
        //判断当前是否有皮肤，没有皮肤就不用执行任何方法
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(currentSkinPath)) {
            return SkinPreUtils.SKIN_NOTNEED_CHANGE;
        }
        //当前手机运行APK的资源路径
        String skinPath = mContext.getPackageResourcePath();
        initSkinResouce(mContext, skinPath);
        for (Activity activity : mSkinViews.keySet()) {
            List<SkinView> skinViews = mSkinViews.get(activity);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
        SkinPreUtils.getInstance(mContext).clearSkinInfo();
        return SkinPreUtils.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 通过Activity
     *
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    public void register(Activity activity, List<SkinView> skinViews) {
        mSkinViews.put(activity, skinViews);
    }

    /**
     * 检测是否需要换肤
     *
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        //如果当前有皮肤，也就是保存了皮肤路径
        String skinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (!TextUtils.isEmpty(skinPath)) {
            //切换一下
            skinView.skin();
        }
    }
}
