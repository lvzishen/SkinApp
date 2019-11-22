package org.thanos.netcore.internal.requestparam;

import org.thanos.netcore.bean.ContentItem;
import org.thanos.netcore.bean.requestbean.BaseProtocol;
import org.thanos.netcore.internal.MorningDataCore;

/**
 * 推荐列表请求参数
 */
public class RecommendListRequestParam extends BaseRequestParam<RecommendListRequestParam.RecommendListRequestProtocol> {
    /**
     * 列表第几页
     */
    private final int page;
    private int category;
    private long id;
    private int resourceType;
    /**
     * 频道ID
     */
    private int channelID;
    private String lang;

    public RecommendListRequestParam(ContentItem contentListItem, int channelID, int page, boolean acceptCache) {
        this(contentListItem.category, contentListItem.id, contentListItem.type, channelID, page, acceptCache, 1);
    }


    public RecommendListRequestParam(ContentItem contentListItem, int channelID, int page, boolean acceptCache, int module) {
        this(contentListItem.category, contentListItem.id, contentListItem.type, channelID, page, acceptCache, module);
    }

    public RecommendListRequestParam(int category, long id, int resourceType, int channelID, int page, boolean acceptCache, int module) {
        super("RL", acceptCache, false, module);
        this.category = category;
        this.id = id;
        this.resourceType = resourceType;
        this.page = page;
        this.channelID = channelID;
        lang = MorningDataCore.getLang(false);
        this.module = module;

    }


    @Override
    public RecommendListRequestProtocol createProtocol() {
        RecommendListRequestProtocol recommendListRequestProtocol=new RecommendListRequestProtocol();
        recommendListRequestProtocol.lang=lang;
        recommendListRequestProtocol.category=category;
        recommendListRequestProtocol.id=id;
        recommendListRequestProtocol.page=this.page;
        recommendListRequestProtocol.resource_type=this.resourceType;
        recommendListRequestProtocol.channel2=this.channelID;
        recommendListRequestProtocol.module=this.module;
        return recommendListRequestProtocol;
    }

    /**
     * 设置推荐列表缓存的key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        return String.valueOf(this.category) + this.id + this.resourceType + this.page + this.channelID + this.lang + this.module;
    }

    @Override
    public String toString() {
        return MorningDataCore.DEBUG ? "RecommendListRequestParam{" +
                "page=" + page +
                ", category=" + category +
                ", id=" + id +
                ", lang=" + lang +
                ", resourceType=" + resourceType +
                ", channelID=" + channelID +
                ", requestModuleName='" + requestModuleName + '\'' +
                ", acceptCache=" + acceptCache +
                ", module=" + module +
                '}' : "";
    }

    public static class RecommendListRequestProtocol extends BaseProtocol {

        public int category;
        public String lang;
        public long id;
        public int resource_type;
        public int page;
        public int channel2;
        public int module;
    }
}