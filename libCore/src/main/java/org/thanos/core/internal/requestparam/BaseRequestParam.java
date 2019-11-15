package org.thanos.core.internal.requestparam;


import org.thanos.core.bean.requestbean.BaseProtocol;

/**
 * Created by lvzishen on 2019-11-14.
 * 请求业务参数基类
 */
public abstract class BaseRequestParam<T extends BaseProtocol> {
    /**
     * 是否将服务器返回的数据缓存
     */
    public final boolean cacheResponse;
    /**
     * 此次请求是否允许使用本地缓存数据
     */
    public boolean acceptCache;
    /**
     * 缓存数据有效时间，单位秒
     */
    public long cacheLiveTimeInSeconds;
    /**
     * 是否只使用用户选择的国家，如果用户没有选择，就是空。默认false，即如果没有选择时，会考虑使用之前接口返回的国家
     */
    public boolean onlyAcceptUserChooseNewsCountry = false;

    /**
     * 模块id
     * 区分不同的入口
     */
    public int module = 1;

    public final String requestModuleName;

    public BaseRequestParam(String requestModuleName, boolean acceptCache, boolean cacheResponse, int module) {
        this.requestModuleName = requestModuleName;
        this.acceptCache = acceptCache;
        this.cacheResponse = cacheResponse;
        this.module = module;
    }

    public abstract T createProtocol();

    public abstract String getCacheKey();
}
