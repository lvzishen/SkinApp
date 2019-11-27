package org.thanos.netcore.internal.requestparam;

import org.thanos.netcore.bean.requestbean.BaseProtocol;
import org.thanos.netcore.data.GoodMorningCollectRequest;
import org.thanos.netcore.internal.MorningDataCore;

/**
 * 推荐列表请求参数
 */
public class CollectRequestParam extends BaseRequestParam<CollectRequestParam.CollectRequestProtocol> {
    private String app_id;
    private int resource_id;
    private boolean cancel;


    public CollectRequestParam(int resource_id, boolean cancel, boolean acceptCache, int module) {
        super("RL", acceptCache, false, module);
        this.app_id = GoodMorningCollectRequest.APPID;
        this.resource_id = resource_id;
        this.cancel = cancel;
        this.module = module;

    }


    @Override
    public CollectRequestProtocol createProtocol() {
        CollectRequestProtocol collectRequestProtocol = new CollectRequestProtocol();
        collectRequestProtocol.app_id = Integer.valueOf(app_id);
        collectRequestProtocol.resource_id = resource_id;
        collectRequestProtocol.type = 1;
        collectRequestProtocol.cancel = cancel;
        return collectRequestProtocol;
    }

    /**
     * 设置推荐列表缓存的key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        return String.valueOf(this.app_id) + this.resource_id + this.cancel + this.module;
    }

    @Override
    public String toString() {
        return MorningDataCore.DEBUG ? "CollectRequestParam{" +
                "app_id=" + app_id +
                ", resource_id=" + resource_id +
                ", cancel=" + cancel +
                ", acceptCache=" + acceptCache +
                ", module=" + module +
                '}' : "";
    }

    public static class CollectRequestProtocol extends BaseProtocol {

        public int app_id;
        public int resource_id;
        public int type;
        public boolean cancel;
    }
}