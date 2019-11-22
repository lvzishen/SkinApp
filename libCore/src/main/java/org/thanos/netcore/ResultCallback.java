package org.thanos.netcore;

/**
 * 创建日期：2019/11/14 on 20:30
 * 描述:
 * 作者: lvzishen
 */

import org.thanos.netcore.bean.ResponseData;

/**
 * 请求的回调接口
 *
 * @param <T> 请求对于Parser返回的业务对象，继承于ResponseData
 */
public interface ResultCallback<T extends ResponseData> {
    /**
     * 从本地加载到过期的缓存数据（同时服务器的数据还在请求中，会从onSuccess方法回来）
     */
    void onLoadFromCache(T data);

    /**
     * 此次请求拿到最终数据。数据可能是来自于本地缓存或网络，可通过data.fromCache字段判断。
     */
    void onSuccess(T data);

    /**
     * 此次请求加载失败
     */
    void onFail(Exception e);
}
