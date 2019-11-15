package org.thanos.core.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.core.internal.MorningDataCore;

import java.io.Serializable;
import java.util.ArrayList;

import static org.thanos.core.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-12.
 * 新闻信息
 */
public class NewsItem extends ContentItem implements Serializable {

    private static final String TAG = MorningDataCore.LOG_PREFIX + "NewsItem";

    private static final long serialVersionUID = 2422212830927562154L;
    /**
     * 标题
     */
    public final String title;
    /**
     * 外部跳转链接/原始跳转链接
     */
    public final String originUrl;
    /**
     * 分享用的url
     */
    public final String shareUrl;
    /**
     * 正文web链接
     */
    public final String dataUrl;
    /**
     * 摘要
     */
    public final String summary;
    /**
     * 来源ID
     */
    public final int sourceId;
    /**
     * 标记 0普通/1热门/2突发/3兴趣/4广告/5专题
     */
    public final int mark;
    /**
     * 评论数
     */
    public final int comments;
    /**
     * 发布时间戳，毫秒 ms
     */
    public final long pubtime;
    /**
     * 客户端展示的时间，毫秒
     */
    public final long showtime;
    /**
     * 投递策略
     */
    public final String dlvFrom;
    /**
     * 客户端展现位置, -1表示无效
     */
    public final int position;
    /**
     * 作者信息，可能为null
     */
    public final AuthorInfo authorInfo;
    /**
     * 用户阅读数
     */
    public final int userViews;
    /**
     * 用户点赞数
     */
    public int userLikes;
    /**
     * 用户吐槽数
     */
    public final int userDislikes;
    /**
     * 用户评论数
     */
    public final int userComments;
    /**
     * 用户分享数
     */
    public final int userShares;
    /**
     * 用户收藏数
     */
    public final int userFavorites;
    /**
     * 用户下载数
     */
    public final int userDownloads;
    /**
     * 内容合作方
     */
    public final String partner;
    /**
     * 二级分类
     */
    public final int secondCategory;
    /**
     * 下发策略，由服务器下发
     */
    public final String dotText;
    /**
     * 图片信息
     */
    public ArrayList<ImageInfo> imageInfos = new ArrayList<>();

    public NewsItem(JSONObject jo, String requestID) throws JSONException {
        super(jo, requestID);
        if (DEBUG) {
            Log.i(TAG, "NewsItem: 要解析的JSON串 " + jo.toString());
        }
        title = jo.optString("title");
        originUrl = jo.optString("ourl");
        shareUrl = jo.optString("surl");
        dataUrl = jo.optString("durl");
        summary = jo.optString("summary");
        sourceId = jo.optInt("source_id");
        mark = jo.optInt("mark");
        comments = jo.optInt("comments");
        pubtime = jo.optLong("pubtime");
        showtime = jo.optLong("showtime");
        dlvFrom = jo.optString("dlv_from");
        position = jo.optInt("position");
        JSONObject authorObj = jo.optJSONObject("author");
        if (authorObj != null) {
            authorInfo = new AuthorInfo(authorObj);
        } else {
            authorInfo = null;
        }
        userViews = jo.optInt("user_views");
        userLikes = jo.optInt("user_likes");
        userDislikes = jo.optInt("user_dislikes");
        userComments = jo.optInt("user_comments");
        userShares = jo.optInt("user_shares");
        userFavorites = jo.optInt("user_favorites");
        userDownloads = jo.optInt("user_downloads");
        secondCategory = jo.optInt("second_category");
        dotText = jo.optString("dot_text");
        JSONObject statsExtraObj = jo.optJSONObject("stats_ext_info");
        if (statsExtraObj != null) {
            partner = statsExtraObj.optString("m");
        } else {
            partner = null;
        }

        JSONArray imageArray = jo.optJSONArray("images");
        if (imageArray != null && imageArray.length() > 0) {
            for (int i = 0, len = imageArray.length(); i < len; i++) {
                ImageInfo imageInfo = new ImageInfo(imageArray.optJSONObject(i));
                if (DEBUG) {
                    Log.i(TAG, "NewsListItem: " + i + ", " + imageInfo);
                }
                imageInfos.add(imageInfo);
            }
        }
    }

    @Override
    public String toString() {
        return DEBUG ? "NewsItem{" +
                "show=" + show +
                ", title='" + title + '\'' +
                ", originUrl='" + originUrl + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", dataUrl='" + dataUrl + '\'' +
                ", summary='" + summary + '\'' +
                ", source='" + source + '\'' +
                ", sourceId=" + sourceId +
                ", mark=" + mark +
                ", comments=" + comments +
                ", pubtime=" + pubtime +
                ", showtime=" + showtime +
                ", dlvFrom='" + dlvFrom + '\'' +
                ", position=" + position +
                ", authorInfo=" + authorInfo +
                ", userViews=" + userViews +
                ", userLikes=" + userLikes +
                ", userDislikes=" + userDislikes +
                ", userComments=" + userComments +
                ", userShares=" + userShares +
                ", userFavorites=" + userFavorites +
                ", userDownloads=" + userDownloads +
                ", imageInfos=" + imageInfos +
                ", type=" + type +
                ", id=" + id +
                ", category=" + category +
                '}' :"";
    }

    public static class ImageInfo implements Serializable {
        private static final long serialVersionUID = 2125160013968003923L;
        public final int height;
        public final int width;
        public final String url;

        ImageInfo(JSONObject jo) {
            height = jo.has("height") ? jo.optInt("height") : jo.optInt("origin_height");
            width = jo.has("width") ? jo.optInt("width") : jo.optInt("origin_width");
            url = jo.has("url") ? jo.optString("url") : jo.optString("origin_url");
        }

        @Override
        public String toString() {
            return DEBUG ?"ImageInfo{" +
                    "height=" + height +
                    ", width=" + width +
                    ", url='" + url + '\'' +
                    '}' :"";
        }
    }
}
