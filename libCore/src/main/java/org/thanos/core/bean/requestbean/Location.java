package org.thanos.core.bean.requestbean;

/**
 * 创建日期：2019/11/14 on 12:23
 * 描述:
 * 作者: lvzishen
 */
public class Location {

    /**
     * localTime : string, 本地时间，例如:20160422234412
     * localZone : string, 本地时区，分钟值，如北京是480
     * latitude : float, 纬度
     * longitude : float, 经度
     * altitude : float, 高度
     * accuracy : float, 准确度, 单位米
     */

    public String localTime;
    public String localZone;
    public double latitude;
    public double longitude;
    public double altitude;
    public double accuracy;

    
}
