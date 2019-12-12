package com.goodmorning.utils;

import android.text.TextUtils;
import android.util.Log;

import com.baselib.cloud.CloudPropertyManager;
import com.baselib.sp.SharedPref;
import com.goodmorning.bean.CloudPicture;

import org.thanos.netcore.helper.JsonHelper;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class CheckUtils {
//    public static String keyStartTime;
    /**
     * 检查是否显示每日一图
     * 1、当日早上6点到次日早上6点
     * 2、展示过就不展示了
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return true 显示，false 不显示
     */
    public static boolean isShowPic(long startTime,long endTime){
        long currentTime = System.currentTimeMillis();
        if (currentTime >= startTime && currentTime <= endTime){
            //是否显示过了
            boolean isShowed  = SharedPref.getBoolean(getApplicationContext(),String.valueOf(startTime),false);
            if (isShowed){
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否显示每日一图
     * @param startTime
     * @return
     */
    public static boolean isShowPic(String startTime){
        boolean isShowed  = SharedPref.getBoolean(getApplicationContext(),String.valueOf(startTime),false);
        if (isShowed){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 是否显示语言列表
     * @return true 显示，false 不显示
     */
    public static boolean isShowLanguage(){
        //是否首次启动
        boolean isShow = SharedPref.getBoolean(getApplicationContext(),SharedPref.ISFIRSTSTART,true);
        if (isShow){
            SharedPref.setBoolean(getApplicationContext(),SharedPref.ISFIRSTSTART,false);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检查是否显示云控图片
     * @param cloudKey 云控key
     * @return
     */
        public static CloudPicture checkCloudPic(String cloudKey){
        String json = CloudControlUtils.getCloudData(getApplicationContext(), CloudPropertyManager.PATH_CLOUD_PIC_PROP,cloudKey);
        JsonHelper<CloudPicture> jsonHelper = new JsonHelper<CloudPicture>() {
        };
        CloudPicture cloudPicture = jsonHelper.getJsonObject(json);
        long curTime =  TimeUtils.dateToStamp(TimeUtils.getCurrentDate());
        long startTime = 0;
        long endTime = 0;
        if (cloudPicture != null){
            if (!TextUtils.isEmpty(cloudPicture.getStartTime()) && !TextUtils.isEmpty(cloudPicture.getEndTime())){
                startTime  = TimeUtils.dateToStamp(cloudPicture.getStartTime());
                endTime = TimeUtils.dateToStamp(cloudPicture.getEndTime());
            }
        }
        if (curTime >= startTime && curTime <= endTime && !TextUtils.isEmpty(cloudPicture.getPicUrl())){
            //满足条件显示
            return cloudPicture;
        }else {
            //不满足条件不显示
            return null;
        }
    }
}
