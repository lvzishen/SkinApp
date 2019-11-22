package org.thanos.netcore.bean.requestbean;

/**
 * 创建日期：2019/11/14 on 14:02
 * 描述:
 * 作者: lvzishen
 *
 *"product":"int, 产品id, 分配的产品id值，必填",                            ### 必填
 *"module":"int, 模块id， 分配的模块id值，必填",                           ### 必填
 *"packageName":"string, 包名",                                      ### 选填，无使用
 *"channelId":"string, 渠道名",            公共参数 channel_id          ### 选填，无使用
 *"versionCode":"int， 版本号",           公共参数 version_code        ### 必填
 *"versionName":"string， 版本名",        公共参数version_name         ### 必填
 *"installTime":"long int， 毫秒时间戳客户端安装时间",                ### 选填，无使用
 *"updateTime":"long int, 毫秒时间戳，客户端最近一次升级时间"                 ### 选填，无使用
 *
 */
public class AppInfo {
    public int product;
    public int module;
    public String packageName;
    public String channelId;
    public int versionCode;
    public String versionName;
    public long installTime;
    public long updateTime;
}
