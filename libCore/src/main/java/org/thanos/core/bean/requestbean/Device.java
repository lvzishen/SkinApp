package org.thanos.core.bean.requestbean;

/**
 * 创建日期：2019/11/14 on 12:30
 * 描述:
 * 作者: lvzishen
 * <p>
 * "clientId":"string, 设备id，必填",                                     ### 必填，跟GDPR调整一致，客户端逻辑不变
 * "locale":"string, 客户端语言，必填",                                    ### 必填，客户端逻辑不变
 * "newsCountry":"string, 客户端通过列表接口或频道接口拿到的国家码",      ### 请求频道时为空由投票逻辑决定国家，非空时客户端直接可以指定；请求数据时必填
 * "mccCode":"string, mcc对应国家码，必填",                               ### 公共参数允许上传，去掉后影响投票逻辑（主要由IP决定影响较小），建议保留
 * "ip":"客户端ip"
 */
public class Device {
    public String clientId;
    public String locale;
    public String newsCountry;
    public String mccCode;
    public String ip;
}
