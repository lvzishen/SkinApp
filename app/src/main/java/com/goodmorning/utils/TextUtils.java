package com.goodmorning.utils;

public class TextUtils {

    /**
     * 获取频道内容
     * @param msg 需要解析的内容
     * @return 返回解析后的数组内容
     */
    public static String[] channelText(String msg){
        String[] strs = msg.split(";");
        return strs;
    }
}
