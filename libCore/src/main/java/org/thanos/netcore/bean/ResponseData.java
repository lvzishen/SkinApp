package org.thanos.netcore.bean;

import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.netcore.CollectStatus;

/**
 * Created by zhaobingfeng on 2019-07-11.
 * 接口响应数据的基类
 */
public class ResponseData {
    public final int code;
    public final String message;
    public final String requestId;
    /**
     * 数据是否来自本地缓存
     */
    public boolean fromCache;

    public ResponseData(JSONObject jo) throws JSONException {
        this.code = jo.getInt("code");
        this.message = jo.getString("message");
        if (jo.has("requestId")) {// 一般是这个字段
            this.requestId = jo.optString("requestId");
        } else {// 用户行为上报接口回来的是这个字段。。。
            this.requestId = jo.optString("rqstid");
        }
    }

    public boolean needCache() {
        return false;
    }

    public static <T extends ResponseData> T create(Class<? extends ResponseData> clazz, JSONObject jsonObject) throws JSONException {
        if (clazz == ChannelList.class) {
            return (T) new ChannelList(jsonObject);
        } else if (clazz == ContentList.class) {
            return (T) new ContentList(jsonObject);
        } else if (clazz == ContentDetail.class) {
            return (T) new ContentDetail(jsonObject);
        } else if (clazz == ResponseData.class) {
            return (T) new ResponseData(jsonObject);
        } else if (clazz == CollectStatus.class) {
            return (T) new CollectStatus(jsonObject);
        } else if (clazz == CollectDetail.class) {
            return (T) new CollectDetail(jsonObject);
        }
        return null;
    }
}
