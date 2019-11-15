package org.thanos.core.push;

/**
 * Created by zhaobingfeng on 2019-07-16.
 * PUSH相关配置和常量字段，用于多个模块使用。非PUSH模块不要直接使用PUSH模块里的API
 * 模块ID定义：
 * 100-新闻、200-视频、300-主题、400-deeplink、500-自研播放器、600-youtube/Fb视频、1100-新闻(桌面浮窗)、1200-自研播放器(桌面浮窗)、1300-YouTube/Facebook(桌面浮窗)
 */
public class ThanosPushConfig {
    /**
     * PUSH通知点击Intent里的附加信息，内容ID，可用此来查询详情
     */
    public static final String EXTRA_PUSH_MESSAGE_CONTENT_ID = "extra_push_content_id";
    /**
     * 通知点击后Intent的Action
     */
    public static final String ACTION_CLICK_PUSH_NOTIFY = "thanos_action_click_push_notify";

    /**
     * PUSH模块ID：新闻
     */
    public static final int PUSH_MODULE_ID_NEWS = 100;
    /**
     * PUSH模块ID：视频, 用新闻详情页打开
     */
    public static final int PUSH_MODULE_ID_VIDEO = 200;
    /**
     * PUSH模块ID：图集, 用图集详情页打开
     */
//    public static final int PUSH_MODULE_ID_PIC = 700;

    /**
     * PUSH模块ID：DeepLink
     */
    public static final int PUSH_MODULE_ID_DEEP_LINK = 400;
    /**
     * PUSH模块ID：自定义视频，竖屏视频
     */
    public static final int PUSH_MODULE_ID_LOCAL_VIDEOS = 500;
    /**
     * PUSH模块ID：Youtube/FB视频
     */
    public static final int PUSH_MODULE_ID_YTO_VIDEOS = 600;

    /**
     * PUSH模块ID：MSN视频
     */
//    public static final int PUSH_MODULE_ID_MSN_VIDEOS = 800;

    /**
     * PUSH模块ID：新闻（桌面浮窗）
     */
    public static final int PUSH_MODULE_ID_OVERLAY_NEWS = 1100;  // 新闻
    /**
     * PUSH模块ID：自研播放器（桌面浮窗），竖屏视频
     */
    public static final int PUSH_MODULE_ID_OVERLAY_VMATE_VIDEO = 1200;  //VAMTE
    /**
     * PUSH模块ID：Youtube/FB视频（桌面浮窗）
     */
    public static final int PUSH_MODULE_ID_OVERLAY_YTO_VIDEO = 1300;  // YOUTUBE

    /**
     * PUSH模块ID：图集(桌面浮窗口)
     */
//    public static final int PUSH_MODULE_ID_OVERLAY_PIC = 1400;  // 图集
//
//    /**
//     * PUSH模块ID：MSN视频(桌面浮窗口)
//     */
//    public static final int PUSH_MODULE_ID_MSN_VIDEO= 1500;  // MSN视频

}
