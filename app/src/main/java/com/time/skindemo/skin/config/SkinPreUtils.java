package com.time.skindemo.skin.config;

import android.content.Context;


/**
 * 创建日期：2019/12/13 on 17:43
 * 描述:
 * 作者: lvzishen
 */
public class SkinPreUtils {
    private static volatile SkinPreUtils mInstance;
    private Context mContext;
    //SP NAME
    public static final String SKIN_INFO_NAME = "skinInfo";
    public static final String SKIN_PATH_NAME = "skinPath";
    public static final int SKIN_NOTNEED_CHANGE = -1;
    //皮肤包不存在
    public static final int SKIN_FILE_NOT_EXSIST = -2;
    //文件错误，可能不是一个APK
    public static final int SKIN_FILE_ERROR = -3;
    public static final int SKIN_CHANGE_SUCCESS = 1;

    private SkinPreUtils(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static SkinPreUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SkinPreUtils.class) {
                if (mInstance == null) {
                    mInstance = new SkinPreUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存当前皮肤路径
     *
     * @param skinPath
     */
    public void saveSkinPath(String skinPath) {
        mContext.getSharedPreferences(SKIN_INFO_NAME, Context.MODE_PRIVATE)
                .edit().putString(SKIN_PATH_NAME, skinPath).apply();
    }

    /**
     * 获取皮肤路径
     *
     * @return 当前皮肤路径
     */
    public String getSkinPath() {
        return mContext.getSharedPreferences(SKIN_INFO_NAME, Context.MODE_PRIVATE)
                .getString(SKIN_PATH_NAME, "");
    }

    /**
     * 清空皮肤路径
     */
    public void clearSkinInfo() {
        saveSkinPath("");
    }
}
