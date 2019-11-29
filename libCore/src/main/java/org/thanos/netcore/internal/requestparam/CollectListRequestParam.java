package org.thanos.netcore.internal.requestparam;

import org.thanos.netcore.bean.requestbean.BaseProtocol;
import org.thanos.netcore.data.GoodMorningCollectRequest;
import org.thanos.netcore.internal.MorningDataCore;

/**
 * 推荐列表请求参数
 * app_id":,    ## int类型，账号分配给客户端的唯一app的id
 * "type": 1,    ## int类型，用户行为类型
 * "offset": 0,    ## int类型，开始请求的偏移量
 * "limit": 10,    ## int类型，请求的数量，一次最多1000条
 */
public class CollectListRequestParam extends BaseRequestParam<CollectListRequestParam.CollectListRequestProtocol> {
    private int offset;
    private int limit;


    public CollectListRequestParam(int offset, int limit, boolean acceptCache, int module) {
        super("RL", acceptCache, false, module);
        this.offset = offset;
        this.limit = limit;
        this.module = module;

    }


    @Override
    public CollectListRequestProtocol createProtocol() {
        CollectListRequestProtocol collectRequestProtocol = new CollectListRequestProtocol();
        collectRequestProtocol.app_id = GoodMorningCollectRequest.APPID;
        collectRequestProtocol.limit = limit;
        collectRequestProtocol.type = 1;
        collectRequestProtocol.offset = offset;
        return collectRequestProtocol;
    }

    /**
     * 设置推荐列表缓存的key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        return GoodMorningCollectRequest.APPID + this.offset + this.limit + this.module;
    }

    @Override
    public String toString() {
        return MorningDataCore.DEBUG ? "CollectRequestParam{" +
                "app_id=" + GoodMorningCollectRequest.APPID +
                ", offset=" + offset +
                ", limit=" + limit +
                ", acceptCache=" + acceptCache +
                ", module=" + module +
                '}' : "";
    }

    public static class CollectListRequestProtocol extends BaseProtocol {

        public String app_id;
        public int offset;
        public int type;
        public int limit;
    }
}