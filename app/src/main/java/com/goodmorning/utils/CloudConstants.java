package com.goodmorning.utils;

import org.thanos.netcore.ThanosCloudConstants;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhaobingfeng on 2019-08-01.
 * 云控参数读取工具
 */
public class CloudConstants {

    /**
     * 获取频道列表数据的本地缓存时间
     */
    public static long getChannelCacheTimeInSeconds() {
        //频道列表默认支持缓存10天
        return ThanosCloudConstants.getLong(ThanosCloudConstants.CHANNEL_LIST_CACHE_TIME_IN_HOURS, TimeUnit.DAYS.toHours(10)) * 3600L;
    }
}
