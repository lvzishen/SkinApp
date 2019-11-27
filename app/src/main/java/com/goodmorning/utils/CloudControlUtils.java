package com.goodmorning.utils;

import android.content.Context;

import com.baselib.cloud.CloudPropertyManager;

public class CloudControlUtils {

    /**
     * 获取云控字符串数据
     * @param context
     * @param name
     * @param key
     * @return
     */
    public static String getCloudData(Context context , String name, String key){
        String data = CloudPropertyManager.getString(context,name,key,"");
        return data;
    }

}
