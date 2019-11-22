package org.thanos.netcore.internal.requestparam;

import org.thanos.netcore.bean.requestbean.BaseProtocol;
import org.thanos.netcore.internal.MorningDataCore;
import org.thanos.netcore.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期：2019/11/15 on 10:09
 * 描述:
 * 作者: lvzishen
 */
public class ContentListRequestParam extends BaseRequestParam {
    private final int sessionId;
    private final int channelId;
    private final boolean withChannels;
    private int[] subscribeNewsCategories = new int[0];
    private int[] subscribeVideoCategories = new int[0];
    private String lang;
    private int module;


    public ContentListRequestParam(int sessionId, int channelId, boolean acceptCache, boolean cacheResponse, boolean withChannels) {
        this(sessionId, channelId, acceptCache, cacheResponse, false, 1);
    }

    //todo 这里的api 频道列表
    public ContentListRequestParam(int sessionId, int channelId, boolean acceptCache, boolean cacheResponse, boolean withChannels, int module) {
        super("CTL", acceptCache, cacheResponse, module);
        this.sessionId = sessionId;
        this.channelId = channelId;
        this.withChannels = withChannels;
        this.module = module;
        lang = MorningDataCore.getLang(false);
    }

    public void setSubscribeNewsCategories(int[] subscribeNewsCategories) {
        if (subscribeNewsCategories == null) {
            return;
        }
        this.subscribeNewsCategories = subscribeNewsCategories;
    }

    public void setSubscribeVideoCategories(int[] subscribeVideoCategories) {
        if (subscribeVideoCategories == null) {
            return;
        }
        this.subscribeVideoCategories = subscribeVideoCategories;
    }

    public ContentListRequestProtocal createProtocol() {
        ContentListRequestProtocal contentListRequestProtocal = new ContentListRequestProtocal();
        contentListRequestProtocal.session_id = this.sessionId;
        contentListRequestProtocal.channel = this.channelId;
        contentListRequestProtocal.lang = this.lang;
        contentListRequestProtocal.module = this.module;
        contentListRequestProtocal.withChannel = withChannels ? 1 : 0;// 响应里是否把频道列表也带回来
        // 客户端订阅的分类信息
        contentListRequestProtocal.subscribe = new ContentListRequestSubscribe();

        if (subscribeNewsCategories.length > 0) {
            for (int news_category : subscribeNewsCategories) {
                contentListRequestProtocal.subscribe.news_categories.add(news_category);
            }
        }

        if (subscribeVideoCategories.length > 0) {
            for (int video_category : subscribeVideoCategories) {
                contentListRequestProtocal.subscribe.video_categories.add(video_category);
            }
        }
        return contentListRequestProtocal;
    }


    /**
     * 设置列表缓存的key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        return this.channelId + lang + module;
    }

    @Override
    public String toString() {
        return BuildConfig.DEBUG ? "NewsListRequestParam{" +
                "sessionId='" + sessionId + '\'' +
                ", channelId=" + channelId +
                '}' : "";
    }

    public static class ContentListRequestProtocal extends BaseProtocol {
        public int session_id;
        public int channel;
        public String lang;
        public int withChannel;
        public int module;
        public ContentListRequestSubscribe subscribe;
    }

    public static class ContentListRequestSubscribe {
        public List<Integer> news_categories = new ArrayList<>();
        public List<Integer> video_categories = new ArrayList<>();
    }
}
