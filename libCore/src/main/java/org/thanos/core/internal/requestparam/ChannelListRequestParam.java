package org.thanos.core.internal.requestparam;

import org.thanos.core.bean.requestbean.BaseProtocol;

/**
 * 创建日期：2019/11/15 on 9:57
 * 描述:
 * 作者: lvzishen
 */
public class ChannelListRequestParam extends BaseRequestParam {

    public ChannelListRequestParam(boolean acceptCache, long cacheTimeInSeconds) {
        this(acceptCache, cacheTimeInSeconds, 1);
    }


    public ChannelListRequestParam(boolean acceptCache, long cacheTimeInSeconds, int module) {
        super("CL", acceptCache, true, module);
        this.cacheLiveTimeInSeconds = cacheTimeInSeconds;
        this.onlyAcceptUserChooseNewsCountry = true;
        this.module = module;
    }

    @Override
    public BaseProtocol createProtocol() {
        return new ChannelListProtocol();
    }

    @Override
    public String getCacheKey() {
        return String.valueOf(module);
    }

    public static class ChannelListProtocol extends BaseProtocol {

    }

}
