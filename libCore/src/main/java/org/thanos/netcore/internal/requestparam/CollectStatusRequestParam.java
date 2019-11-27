package org.thanos.netcore.internal.requestparam;

import org.thanos.netcore.bean.requestbean.BaseProtocol;
import org.thanos.netcore.data.GoodMorningCollectRequest;
import org.thanos.netcore.internal.MorningDataCore;

/**
 * 推荐列表请求参数
 * "app_id":1,    ## int类型，账号分配给客户端的唯一app的id
 * "type": 1,    ## int类型，用户行为类型
 * "resource_ids": [1531816782958026],    ## 数组类型，数组中元素为int64类型，资源id
 */
public class CollectStatusRequestParam extends BaseRequestParam<CollectStatusRequestParam.CollectStatusRequestProtocol> {
    private String app_id;
    private int resource_ids;


    public CollectStatusRequestParam(int resource_ids, boolean acceptCache, int module) {
        super("RL", acceptCache, false, module);
        this.app_id = GoodMorningCollectRequest.APPID;
        this.resource_ids = resource_ids;
        this.module = module;

    }


    @Override
    public CollectStatusRequestProtocol createProtocol() {
        CollectStatusRequestProtocol collectRequestProtocol = new CollectStatusRequestProtocol();
        collectRequestProtocol.app_id = Integer.valueOf(app_id);
        collectRequestProtocol.type = 1;
        collectRequestProtocol.resource_ids = resource_ids;
        return collectRequestProtocol;
    }

    /**
     * 设置推荐列表缓存的key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        return String.valueOf(this.app_id) + this.resource_ids + this.module;
    }

    @Override
    public String toString() {
        return "CollectStatusRequestParam{" +
                "app_id='" + app_id + '\'' +
                ", resource_ids=" + resource_ids +
                '}';
    }

    public static class CollectStatusRequestProtocol extends BaseProtocol {
        public int app_id;
        public int type;
        public int resource_ids;
    }
}