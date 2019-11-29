package org.thanos.netcore.bean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.netcore.MorningDataAPI;
import org.thanos.netcore.internal.MorningDataCore;

import java.io.Serializable;

import static org.thanos.netcore.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-12.
 */
public class ContentItem implements Serializable {
    /**
     * 新闻 - 普通 - 带一张图片
     */
    public static final int SHOW_TYPE_NEWS_NORMAL = 11;
    /**
     * 新闻 - 无图
     */
    public static final int SHOW_TYPE_NEWS_NO_IMAGE = 12;
    /**
     * 新闻 - 大图 - 带一张图片
     */
    public static final int SHOW_TYPE_NEWS_BIG_IMAGE = 13;
    /**
     * 新闻 - 组图 - 带三张图片
     */
    public static final int SHOW_TYPE_NEWS_MUL_IMAGE = 14;


    private static final long serialVersionUID = -8190595725228235560L;
    private static final String TAG = MorningDataCore.LOG_PREFIX + "ContentItem";
    /**
     * 资源类型
     */
    public final int type;
    /**
     * 资源ID
     */
    public final long id;

    public long resourceId;
    /**
     * 一级分类ID
     */
    public final int category;
    /**
     * 内容类型，服务器返回的一个字符串,VIDEO/NEWS这种
     */
    public final String contentType;
    /**
     * 打点用，由服务器下发，形如 "qfaczniz401107rjy101iuz1y9f4bf23-1564457519557-730208,787049-qfaczniz401107rjy101iuz1y9f4bf23-1564457519573"
     */
    public final String dotText;
    public final String statsExtInfo;
    public final String source;
    public final String requestId;
    public int status;
    public String url;
    /**
     * 显示样式
     */
    public int show;

    ContentItem(JSONObject jsonObject, String requestId) throws JSONException {
        type = jsonObject.optInt("type");
        id = jsonObject.optLong("id");
        resourceId = jsonObject.optLong("resource_id");
        show = jsonObject.optInt("show", -1);
        category = jsonObject.optInt("category");
        contentType = jsonObject.optString("content_type");
        dotText = jsonObject.optString("dot_text");
        statsExtInfo = jsonObject.optString("stats_ext_info");
        source = jsonObject.optString("source");
        this.requestId = requestId;
    }

    static ContentItem createFromJSONObject(JSONObject jsonObject, String requestId) throws JSONException {
        int type = jsonObject.optInt("type");
        int show = jsonObject.optInt("show");
        String contentType = jsonObject.optString("content_type");
        if ("NEWS".equals(contentType)) {
            return new NewsItem(jsonObject, requestId);
        } else if ("PHOTO".equals(contentType)) {
            return new VideoItem(jsonObject, requestId);
        } else if ("VIDEO".equals(contentType)) {
            return new VideoItem(jsonObject, requestId);
        }
        if (DEBUG) {
            Log.d(TAG, "返回数据的的type--->" + type + "返回数据的show-->" + show);
        }
        // 检查type和show是否搭配合理
        if ((type == MorningDataAPI.RESOURCE_TYPE_NEWS
                && show != ContentItem.SHOW_TYPE_NEWS_NORMAL
                && show != ContentItem.SHOW_TYPE_NEWS_NO_IMAGE
                && show != ContentItem.SHOW_TYPE_NEWS_BIG_IMAGE
                && show != ContentItem.SHOW_TYPE_NEWS_MUL_IMAGE) ||
                (MorningDataAPI.isYouTuBeVideo(type)// YOUTUBE视频，只支持25
                        && show != MorningDataAPI.SHOW_TYPE_VIDEO_ACROSS)
                || (type == MorningDataAPI.RESOURCE_TYPE_MSN) && show != MorningDataAPI.SHOW_TYPE_VIDEO_ACROSS
        ) {
            if (DEBUG) {
                Log.e(TAG, "createFromJSONObject: 不支持的type和show组合，type=" + type + ", show=" + show + ", " + jsonObject);
            }
            return null;
        }

        // 短视频只判断show
        if (show == MorningDataAPI.SHOW_TYPE_VIDEO_VERTICAL) {
            return new VideoItem(jsonObject, requestId);
        }

        //图集type
        if (MorningDataAPI.isPicType(type)) {
            if (DEBUG) {
                Log.d(TAG, "发现图集, type=" + type + ", " + jsonObject.toString());
            }
            return new VideoItem(jsonObject, requestId);
        }
        switch (type) {
            case MorningDataAPI.RESOURCE_TYPE_NEWS:
                return new NewsItem(jsonObject, requestId);

            case MorningDataAPI.RESOURCE_TYPE_YOUTUBE_VIDEO:// Youtube视频
            case MorningDataAPI.RESOURCE_TYPE_MSN:
                return new VideoItem(jsonObject, requestId);

            case MorningDataAPI.RESOURCE_TYPE_MODULE:
                return new ModuleItem(jsonObject, requestId);

            default:
                if (MorningDataAPI.isYouTuBeVideo(type)) {
                    return new VideoItem(jsonObject, requestId);
                }
                if (DEBUG) {
                    Log.e(TAG, "发现不支持的内容类型, type=" + type + ", " + jsonObject.toString());
                }
                return null;
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == getClass()) {
            return ((ContentItem) obj).id == this.id;
        }
        return false;
    }

    public String getPartner() {
        try {
            JSONObject jsonObject = new JSONObject(this.statsExtInfo);
            String m = jsonObject.optString("m", "");
            if ("v".equals(m)) {
                if (MorningDataAPI.isYouTuBeVideo(type)) {
                    m = "youtube";
                }
            }
            return m;
        } catch (JSONException e) {
            if (DEBUG) {
                Log.e(TAG, "getPartner: ", e);
            }
        }
        return "";
    }

    public static class AuthorInfo implements Serializable {
        private static final long serialVersionUID = 2154331458263894432L;
        public  String name;
        public  String icon;
        public  int id;

        AuthorInfo(JSONObject jo) throws JSONException {
            if (jo != null){
                name = jo.optString("name");
                icon = jo.optString("icon");
                id = jo.optInt("id");
            }

        }

        @Override
        public String toString() {
            return DEBUG ? "AuthorInfo{" +
                    "name='" + name + '\'' +
                    ", icon='" + icon + '\'' +
                    ", id=" + id +
                    '}' : "";
        }
    }
}
