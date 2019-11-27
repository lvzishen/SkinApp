package org.thanos.netcore;

import android.content.Context;

import androidx.annotation.NonNull;

import org.thanos.netcore.bean.ChannelList;
import org.thanos.netcore.bean.CollectDetail;
import org.thanos.netcore.bean.ContentDetail;
import org.thanos.netcore.bean.ContentList;
import org.thanos.netcore.bean.ResponseData;
import org.thanos.netcore.internal.MorningDataCore;
import org.thanos.netcore.internal.requestparam.ChannelListRequestParam;
import org.thanos.netcore.internal.requestparam.CollectListRequestParam;
import org.thanos.netcore.internal.requestparam.CollectRequestParam;
import org.thanos.netcore.internal.requestparam.CollectStatusRequestParam;
import org.thanos.netcore.internal.requestparam.ContentDetailRequestParam;
import org.thanos.netcore.internal.requestparam.ContentListRequestParam;
import org.thanos.netcore.internal.requestparam.RecommendListRequestParam;
import org.thanos.netcore.internal.requestparam.UserBehaviorUploadParam;
import org.thanos.netcore.internal.requestparam.UserFeedbackUploadRequestParam;


/**
 * Created by zhaobingfeng on 2019-07-12.
 * Thanos数据模块，对外暴露的入口API
 */
public class MorningDataAPI {

    /**
     * 资源类型：新闻
     */
    public static final int RESOURCE_TYPE_NEWS = 1;
    /**
     * 资源类型：Youtube视频
     */
    public static final int RESOURCE_TYPE_YOUTUBE_VIDEO = 6;

    /**
     * 资源类型：图集  30000-39999 和 5
     */
    public static final int RESOURCE_TYPE_PICTURE = 5;


    /**
     * 资源类型：MSN视频
     */
    public static final int RESOURCE_TYPE_MSN = 20014;


    /**
     * 图集区间开始值
     */
    public static final int RESOURCE_TYPE_PICTURE_START = 30000;

    /**
     * 图集区间结束值
     */
    public static final int RESOURCE_TYPE_PICTURE_END = 39999;


    /**
     * 资源类型：模块
     */
    public static final int RESOURCE_TYPE_MODULE = 31;
    /**
     * 特殊频道ID，短视频频道
     */
    public static final int CHANNEL_ID_PORTRAIT_VIDEO = 400;
    /**
     * 特殊频道ID  频道名称Videos
     */
    public static final int CHANNEL_ID_VIDEOS = 300;

    /**
     * 健康产品专有频道 正式线。
     */
    public static final int CHANNEL_ID_FITNESS = 2040;
    /**
     * 健康产品特殊分类 测试线
     */
    public static final int CHANNEL_ID_FITNESS_TEST = 1125;

    /**
     * 视频卡片, 横屏
     */
    public static final int SHOW_TYPE_VIDEO_ACROSS = 25;
    /**
     * 视频卡片，竖屏
     */
    public static final int SHOW_TYPE_VIDEO_VERTICAL = 26;

    /**
     * FITNESS 特殊频道 包含 新闻，图集，视频  本地自己定义的show 2019.10.28
     */
    public static final int SHOW_TYPE_FITNESS_VERTICAL = 88;


    public static void init(IThanosCoreConfig thanosCoreConfig) {
        MorningDataCore.init(thanosCoreConfig);
    }

    /**
     * 获取频道列表
     */
    public static void requestChannelList(@NonNull Context context, ChannelListRequestParam channelListRequestParam, ResultCallback<ChannelList> callback) {
        MorningDataCore.requestChannelList(context, channelListRequestParam, callback);
    }

    /**
     * 获取内容列表数据
     */
    public static void requestContentList(Context context, ContentListRequestParam newsListRequestParam, ResultCallback<ContentList> callback) {
        MorningDataCore.requestContentList(context, newsListRequestParam, callback);
    }

    /**
     * 上传或取消收藏状态
     */
    public static void requestCollectUpLoad(Context context, CollectRequestParam collectRequestParam, ResultCallback<CollectDetail> callback) {
        MorningDataCore.requestCollectUpLoad(context, collectRequestParam, callback);
    }

    /**
     * 获取收藏列表
     */
    public static void requestCollectList(Context context, CollectListRequestParam collectListRequestParam, ResultCallback<ContentList> callback) {
        MorningDataCore.requestCollectList(context, collectListRequestParam, callback);
    }


    /**
     * 获取收藏状态
     */
    public static void requestCollectStatus(Context context, CollectStatusRequestParam collectStatusRequestParam, ResultCallback<CollectStatus> callback) {
        MorningDataCore.requestCollectStatus(context, collectStatusRequestParam, callback);
    }


    /**
     * 获取内容详情信息
     */
    public static void requestContentDetail(Context context, ContentDetailRequestParam contentDetailRequestParam, ResultCallback<ContentDetail> callback) {
        MorningDataCore.requestContentDetail(context, contentDetailRequestParam, callback);
    }


    /**
     * 获取推荐内容列表
     */
    public static void requestRecommendContentList(Context context, RecommendListRequestParam recommendListRequestParam, ResultCallback<ContentList> callback) {
        MorningDataCore.requestRecommendContentList(context, recommendListRequestParam, callback);
    }

    /**
     * 上报用户行为(阅读、点赞、吐槽、评论、分享、收藏等)
     */
    public static void uploadUserBehavior(Context context, UserBehaviorUploadParam userBehaviorUploadParam, ResultCallback<ResponseData> callback) {
        MorningDataCore.uploadUserBehavior(context, userBehaviorUploadParam, callback);
    }

    /**
     * 上报用户反馈(新闻新闻不感兴趣、假新闻等)
     */
    public static void uploadUserFeedback(Context context, UserFeedbackUploadRequestParam userFeedbackUploadParam, ResultCallback<ResponseData> callback) {
        MorningDataCore.uploadUserFeedback(context, userFeedbackUploadParam, callback);
    }

    /**
     * 清除本地缓存数据
     *
     * @return true清除成功，false清除失败
     */
    public static boolean clearLocalCache(Context context) {
        return MorningDataCore.clearLocalCache(context);
    }

    public static boolean isYouTuBeVideo(int resourceType) {
        return resourceType == 6
                || (resourceType >= 20080 && resourceType <= 20099)
                || resourceType == 20002
                || resourceType == 20010
                || resourceType == 20011
                || resourceType == 20012;
    }

    /**
     * 是否是图集
     *
     * @param type
     * @return
     */
    public static boolean isPicType(int type) {
        return type > RESOURCE_TYPE_PICTURE_START && type < RESOURCE_TYPE_PICTURE_END || type == RESOURCE_TYPE_PICTURE || type == SHOW_TYPE_FITNESS_VERTICAL;

    }


    /**
     * 频道列表请求参数
     */
//    public static class ChannelListBaseRequestParam extends BaseRequestParam {
//        private int module;
//
//        public ChannelListBaseRequestParam(boolean acceptCache, long cacheTimeInSeconds) {
//            this(acceptCache, cacheTimeInSeconds, 1);
//        }
//
//
//        public ChannelListBaseRequestParam(boolean acceptCache, long cacheTimeInSeconds, int module) {
//            super("CL", acceptCache, true, module);
//            this.cacheLiveTimeInSeconds = cacheTimeInSeconds;
//            this.onlyAcceptUserChooseNewsCountry = true;
//            this.module = module;
//        }
//
//        @Override
//        public JSONObject createJSONObject() throws JSONException {
//            return null;
//        }
//
//        @Override
//        public String getCacheKey() {
//            return String.valueOf(module);
//        }
//
//        @Override
//        public String toString() {
//            return  DEBUG ? "ChannelListBaseRequestParam{" +
//                    "requestModuleName='" + requestModuleName + '\'' +
//                    ", acceptCache=" + acceptCache +
//                    ", module=" + module +
//                    '}' : "";
//        }
//    }

    /**
     * 内容列表请求参数
     */
//    public static class ContentListBaseRequestParam extends BaseRequestParam {
//        private final int sessionId;
//        private final int channelId;
//        private final boolean withChannels;
//        private int[] subscribeNewsCategories = new int[0];
//        private int[] subscribeVideoCategories = new int[0];
//        private String lang;
//        private int module;
//
//
//        public ContentListBaseRequestParam(int sessionId, int channelId, boolean acceptCache, boolean cacheResponse, boolean withChannels) {
//            this(sessionId, channelId, acceptCache, cacheResponse, false, 1);
//        }
//
//        //todo 这里的api 频道列表
//        public ContentListBaseRequestParam(int sessionId, int channelId, boolean acceptCache, boolean cacheResponse, boolean withChannels, int module) {
//            super("CTL", acceptCache, cacheResponse, module);
//            this.sessionId = sessionId;
//            this.channelId = channelId;
//            this.withChannels = withChannels;
//            this.module = module;
//            lang = MorningDataCore.getLang(false);
//        }
//
//        public void setSubscribeNewsCategories(int[] subscribeNewsCategories) {
//            if (subscribeNewsCategories == null) {
//                return;
//            }
//            this.subscribeNewsCategories = subscribeNewsCategories;
//        }
//
//        public void setSubscribeVideoCategories(int[] subscribeVideoCategories) {
//            if (subscribeVideoCategories == null) {
//                return;
//            }
//            this.subscribeVideoCategories = subscribeVideoCategories;
//        }
//
//        public JSONObject createJSONObject() throws JSONException {
//            JSONObject jo = new JSONObject();
//            jo.put("session_id", this.sessionId);
//            jo.put("channel", this.channelId);// 频道ID
//            jo.put("lang", lang);// 语言
//            jo.put("module", module);// 语言
//            jo.put("withChannel", withChannels ? 1 : 0);// 响应里是否把频道列表也带回来
//            JSONObject subscribeObj = new JSONObject();
//
//            // 客户端订阅的分类信息
//            jo.put("subscribe", subscribeObj);
//            JSONArray newsCateArray = new JSONArray();
//            if (subscribeNewsCategories.length > 0) {
//                for (int news_category : subscribeNewsCategories) {
//                    newsCateArray.put(news_category);
//                }
//            }
//            subscribeObj.put("news_categories", newsCateArray);
//            newsCateArray = new JSONArray();
//            if (subscribeVideoCategories.length > 0) {
//                for (int video_category : subscribeVideoCategories) {
//                    newsCateArray.put(video_category);
//                }
//            }
//            subscribeObj.put("video_categories", newsCateArray);
//            return jo;
//        }
//
//        /**
//         * 设置列表缓存的key
//         *
//         * @return
//         */
//        @Override
//        public String getCacheKey() {
//            return this.channelId + lang + module;
//        }
//
//        @Override
//        public String toString() {
//            return DEBUG ? "NewsListRequestParam{" +
//                    "sessionId='" + sessionId + '\'' +
//                    ", channelId=" + channelId +
//                    '}' :"";
//        }
//    }

//    public static class ContentDetailBaseRequestParam extends BaseRequestParam {
//        private long contentID;
//
//
//        public ContentDetailBaseRequestParam(long contentID, boolean acceptCache) {
//            this(contentID, acceptCache, 1);
//        }
//
//        public ContentDetailBaseRequestParam(long contentID, boolean acceptCache, int module) {
//            super("CD", acceptCache, false, module);
//            this.contentID = contentID;
//            this.module = module;
//        }
//
//        @Override
//        public JSONObject createJSONObject() throws JSONException {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("resource_id", String.valueOf(this.contentID));
//            jsonObject.put("module", module);
//
//            return jsonObject;
//        }
//
//        /**
//         * 设置内容详情缓存的key
//         *
//         * @return
//         */
//        @Override
//        public String getCacheKey() {
//            return String.valueOf(this.contentID + module);
//        }
//
//        @Override
//        public String toString() {
//            return DEBUG ? "ContentDetailBaseRequestParam{" +
//                    "contentID=" + contentID +
//                    ", requestModuleName='" + requestModuleName + '\'' +
//                    ", acceptCache=" + acceptCache +
//                    ", module=" + module +
//                    '}' :"";
//        }
//    }

//    /**
//     * 推荐列表请求参数
//     */
//    public static class RecommendListBaseRequestParam extends BaseRequestParam {
//        /**
//         * 列表第几页
//         */
//        private final int page;
//        private int category;
//        private long id;
//        private int resourceType;
//        /**
//         * 频道ID
//         */
//        private int channelID;
//        private String lang;
//
//        public RecommendListBaseRequestParam(ContentItem contentListItem, int channelID, int page, boolean acceptCache) {
//            this(contentListItem.category, contentListItem.id, contentListItem.type, channelID, page, acceptCache, 1);
//        }
//
//
//        public RecommendListBaseRequestParam(ContentItem contentListItem, int channelID, int page, boolean acceptCache, int module) {
//            this(contentListItem.category, contentListItem.id, contentListItem.type, channelID, page, acceptCache, module);
//        }
//
//        public RecommendListBaseRequestParam(int category, long id, int resourceType, int channelID, int page, boolean acceptCache, int module) {
//            super("RL", acceptCache, false, module);
//            this.category = category;
//            this.id = id;
//            this.resourceType = resourceType;
//            this.page = page;
//            this.channelID = channelID;
//            lang = MorningDataCore.getLang(false);
//            this.module = module;
//
//        }
//
//        @Override
//        public JSONObject createJSONObject() throws JSONException {
//            JSONObject jo = new JSONObject();
//
//            jo.put("lang", lang);
//            jo.put("category", category);
//            jo.put("id", id);
//            jo.put("page", this.page);
//            jo.put("resource_type", this.resourceType);
//            // 频道ID
//            jo.put("channel2", this.channelID);
//            jo.put("module", this.module);
//            return jo;
//        }
//
//        /**
//         * 设置推荐列表缓存的key
//         *
//         * @return
//         */
//        @Override
//        public String getCacheKey() {
//            return String.valueOf(this.category) + this.id + this.resourceType + this.page + this.channelID + this.lang + this.module;
//        }
//
//        @Override
//        public String toString() {
//            return DEBUG ? "RecommendListBaseRequestParam{" +
//                    "page=" + page +
//                    ", category=" + category +
//                    ", id=" + id +
//                    ", lang=" + lang +
//                    ", resourceType=" + resourceType +
//                    ", channelID=" + channelID +
//                    ", requestModuleName='" + requestModuleName + '\'' +
//                    ", acceptCache=" + acceptCache +
//                    ", module=" + module +
//                    '}' :"";
//        }
//    }
//
//    /**
//     * 用户反馈上报参数
//     */
//    public static class UserFeedbackUploadParamBase extends BaseRequestParam {
//        /**
//         * 新闻不感兴趣
//         */
//        public static final UserFeedbackReason NEWS_NOT_INTERESTED = new UserFeedbackReason(1, 0);
//        /**
//         * 新闻屏蔽来源
//         */
//        public static final UserFeedbackReason NEWS_BLOCK_SOURCE = new UserFeedbackReason(2, 0);
//        /**
//         * 新闻内容虚假
//         */
//        public static final UserFeedbackReason NEWS_FAKE_NEWS = new UserFeedbackReason(3, 1);
//        /**
//         * 新闻内容色情
//         */
//        public static final UserFeedbackReason NEWS_PORN = new UserFeedbackReason(3, 2);
//        /**
//         * 新闻标题党
//         */
//        public static final UserFeedbackReason NEWS_CLICKBAIT = new UserFeedbackReason(3, 3);
//        /**
//         * 新闻过旧或重复
//         */
//        public static final UserFeedbackReason NEWS_OLD_OR_REPETITIVE = new UserFeedbackReason(3, 4);
//        /**
//         * 视频不感兴趣
//         */
//        public static final UserFeedbackReason VIDEO_NOT_INTERESTED = new UserFeedbackReason(1, 0);
//        /**
//         * 视频屏蔽作者
//         */
//        public static final UserFeedbackReason VIDEO_BLOCK_SOURCE = new UserFeedbackReason(2, 0);
//        /**
//         * 视频内容虚假
//         */
//        public static final UserFeedbackReason VIDEO_FAKE_OR_MISLEADING = new UserFeedbackReason(3, 0);
//        /**
//         * 视频内容色情
//         */
//        public static final UserFeedbackReason VIDEO_PORN = new UserFeedbackReason(4, 0);
//        /**
//         * 视频内容重复
//         */
//        public static final UserFeedbackReason VIDEO_REPETITIV = new UserFeedbackReason(5, 0);
//        /**
//         * 视频内容引起不适
//         */
//        public static final UserFeedbackReason VIDEO_INAPPROPRIATE = new UserFeedbackReason(6, 0);
//        /**
//         * 视频屏蔽关键词
//         */
//        public static final UserFeedbackReason VIDEO_BLOCK_KEYWORDS = new UserFeedbackReason(7, 0);
//        /**
//         * 视频播放画质不清晰
//         */
//        public static final UserFeedbackReason VIDEO_PLAY_NOT_CLEAR = new UserFeedbackReason(8, 1);
//        /**
//         * 视频播放多次加载
//         */
//        public static final UserFeedbackReason VIDEO_PLAY_MULTIPLE_LOAD = new UserFeedbackReason(8, 2);
//        /**
//         * 视频播放失败黑屏
//         */
//        public static final UserFeedbackReason VIDEO_PLAY_BLACK_SCREEN = new UserFeedbackReason(8, 3);
//        /**
//         * 视频播放音视频不同步或者没声音
//         */
//        public static final UserFeedbackReason VIDEO_PLAY_NOT_SYNC_OR_NO_SOUND = new UserFeedbackReason(8, 4);
//
//        public final ContentItem contentItem;
//        private UserFeedbackReason userFeedbackReason;
//
//
//        public UserFeedbackUploadParamBase(ContentItem contentItem, UserFeedbackReason userFeedbackReason, boolean acceptCache) {
//            this(contentItem, userFeedbackReason, acceptCache, 1);
//        }
//
//        public UserFeedbackUploadParamBase(ContentItem contentItem, UserFeedbackReason userFeedbackReason, boolean acceptCache, int module) {
//            super("UFU", acceptCache, false, module);
//            this.contentItem = contentItem;
//            this.userFeedbackReason = userFeedbackReason;
//            this.module = module;
//        }
//
//        /**
//         * 新闻屏蔽关键词
//         *
//         * @param keywords 要屏蔽的关键词
//         */
//        public static UserFeedbackReason createNewsBlockKeywordsReason(String keywords) {
//            return new UserFeedbackReason(4, 0, keywords);
//        }
//
//        /**
//         * 视频屏蔽关键词
//         *
//         * @param keywords 要屏蔽的关键词
//         */
//        public static UserFeedbackReason createVideoBlockKeywordsReason(String keywords) {
//            return new UserFeedbackReason(7, 0, keywords);
//        }
//
//        @Override
//        public JSONObject createJSONObject() throws JSONException {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("resource_id", this.contentItem.id);
//            int resourceType = this.contentItem.type == MorningDataAPI.RESOURCE_TYPE_NEWS ? 1 : 2;
//            jsonObject.put("resource_type", resourceType);
//            JSONArray labelArray = new JSONArray();
//            jsonObject.put("labels", labelArray);
//            labelArray.put(userFeedbackReason.createJsonObject());
//            return jsonObject;
//        }
//
//        @Override
//        public String getCacheKey() {
//            return "";
//        }
//
//        @Override
//        public String toString() {
//            return DEBUG ?"UserFeedbackUploadParamBase{" +
//                    "contentItem=" + contentItem +
//                    ", userFeedbackReason=" + userFeedbackReason +
//                    ", requestModuleName='" + requestModuleName + '\'' +
//                    ", acceptCache=" + acceptCache +
//                    '}' :"";
//        }
//
//        public static class UserFeedbackReason {
//            public final int firstId;
//            public final int secondId;
//            public final String text;
//
//            UserFeedbackReason(int firstId, int secondId) {
//                this(firstId, secondId, "");
//            }
//
//            public UserFeedbackReason(int firstId, int secondId, String text) {
//                this.firstId = firstId;
//                this.secondId = secondId;
//                this.text = text;
//            }
//
//            JSONObject createJsonObject() throws JSONException {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("first_id", this.firstId);
//                jsonObject.put("second_id", this.secondId);
//                // 只有这两种情况允许传text
//                // 1. 新闻屏蔽关键词 Block keywords first_id=4 second_id=0 text="xxx"
//                // 2. 视频屏蔽关键词：最多3个. first_id=7 second_id=0 text="xxx"
//                if (firstId == 4 && secondId == 0 || firstId == 7 && secondId == 0) {
//                    jsonObject.put("text", this.text);
//                } else {
//                    jsonObject.put("text", "");
//                }
//                return jsonObject;
//            }
//
//            @Override
//            public String toString() {
//                return DEBUG ? "UserFeedbackReason{" +
//                        "firstId=" + firstId +
//                        ", secondId=" + secondId +
//                        ", text='" + text + '\'' +
//                        '}':"";
//            }
//        }
//    }
//
//    /**
//     * 用户行为上报参数
//     */
//    public static class UserBehaviorUploadParamBase extends BaseRequestParam {
//        private ContentItem contentListItem;
//        private UserBehavior userBehavior;
//        private boolean cancel;
//
//        public UserBehaviorUploadParamBase(ContentItem contentListItem, UserBehavior userBehavior, boolean cancel) {
//            this(contentListItem, userBehavior, cancel, 1);
//        }
//
//
//        /**
//         * 用户行为
//         *
//         * @param contentListItem 用户行为的目标
//         * @param userBehavior    用户具体行为
//         * @param cancel          用户是是否是取消该行为
//         */
//        public UserBehaviorUploadParamBase(ContentItem contentListItem, UserBehavior userBehavior, boolean cancel, int module) {
//            super("UU", false, false, module);
//            this.contentListItem = contentListItem;
//            this.userBehavior = userBehavior;
//            this.cancel = cancel;
//            this.module = module;
//        }
//
//        @Override
//        public JSONObject createJSONObject() throws JSONException {
//            JSONObject jo = new JSONObject();
//            jo.put("client_id", XalContext.getClientId());
//            jo.put("id", this.contentListItem.id);
//            jo.put("type", this.userBehavior.type);
//
//            // 资源类型. 1新闻，2视频
//            int resourceType = this.contentListItem.type == MorningDataAPI.RESOURCE_TYPE_NEWS ? 1 : 2;
//            jo.put("resource_type", resourceType);
//            jo.put("cancel", this.cancel);
//
//            return jo;
//        }
//
//        @Override
//        public String getCacheKey() {
//            return "";
//        }
//
//        @Override
//        public String toString() {
//            return DEBUG ?"UserBehaviorUploadParamBase{" +
//                    "contentListItem=" + contentListItem +
//                    ", userBehavior=" + userBehavior +
//                    ", cancel=" + cancel +
//                    ", requestModuleName='" + requestModuleName + '\'' +
//                    ", acceptCache=" + acceptCache +
//                    ", module=" + module +
//                    '}':"";
//        }
//
//        /**
//         * 用户行为集合
//         */
//        public enum UserBehavior {
//            /**
//             * 阅读
//             */
//            READ(1),
//            /**
//             * 点赞
//             */
//            LIKE(2),
//            /**
//             * 吐槽
//             */
//            DISLIKE(3),
//            /**
//             * 评论
//             */
//            COMMENT(4),
//            /**
//             * 分享
//             */
//            SHARE(5),
//            /**
//             * 收藏
//             */
//            FAVORITE(6),
//            /**
//             * 下载
//             */
//            DOWNLOAD(7);
//
//            public final int type;
//
//            UserBehavior(int type) {
//                this.type = type;
//            }
//        }
//    }

    /**
     * Thanos数据层配置
     */
    public abstract static class IThanosCoreConfig {
        /**
         * 获取产品ID
         */
        public abstract int getProductID();


        public String getShareDownloadUrl() {
            return "";
        }

        /**
         * 获取新闻拉取的国家
         */
        public abstract String getNewsCountry();

        public abstract String getLang();

        /**
         * 获取请求地址里的域名
         */
        public String getServerUrlHost() {
            return null;
        }

        /**
         * 获取缓存时长，单位秒。负数和0表示不缓存，默认不缓存
         */
        public long getCacheTimeSeconds() {
            return -1;
        }

        /**
         * 获取本地缓存文件最大限制，单位字节，默认10M
         */
        public long getLocalCacheStorageMaxBytes() {
            return 1024L * 1024 * 10;
        }
    }
}
