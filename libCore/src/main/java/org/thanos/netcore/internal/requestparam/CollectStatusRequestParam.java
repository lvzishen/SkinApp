package org.thanos.netcore.internal.requestparam;

import org.thanos.netcore.bean.requestbean.BaseProtocol;
import org.thanos.netcore.data.GoodMorningCollectRequest;
import org.thanos.netcore.internal.MorningDataCore;

/**
 * 推荐列表请求参数
 * "app_id":,    ## string类型，账号系统分配给客户端的唯一app的id
 * "type": 1,    ## int类型，用户行为类型
 * "resource_ids": [1531816782958026],    ## 数组类型，数组中元素为int64类型，资源id
 */
public class CollectStatusRequestParam extends BaseRequestParam<CollectStatusRequestParam.CollectStatusRequestProtocol> {
    private long resource_ids;


    public CollectStatusRequestParam(long resource_ids, boolean acceptCache, int module) {
        super("RL", acceptCache, false, module);
        this.resource_ids = resource_ids;
        this.module = module;

    }


    @Override
    public CollectStatusRequestProtocol createProtocol() {
        CollectStatusRequestProtocol collectRequestProtocol = new CollectStatusRequestProtocol();
        collectRequestProtocol.app_id = GoodMorningCollectRequest.APPID;
        collectRequestProtocol.type = 1;
        collectRequestProtocol.resource_ids[0] = resource_ids;
        return collectRequestProtocol;
    }

    /**
     * 设置推荐列表缓存的key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        return  GoodMorningCollectRequest.APPID + this.resource_ids + this.module;
    }

    @Override
    public String toString() {
        return "CollectStatusRequestParam{" +
                "app_id='" +  GoodMorningCollectRequest.APPID + '\'' +
                ", resource_ids=" + resource_ids +
                '}';
    }

    public static class CollectStatusRequestProtocol extends BaseProtocol {
        public String app_id;
        public int type;
        public long[] resource_ids = new long[1];
    }
}