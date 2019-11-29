package org.thanos.netcore.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.netcore.internal.MorningDataCore;

import java.io.Serializable;
import java.util.ArrayList;

import static org.thanos.netcore.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-12.
 * 视频数据
 */
public class VideoItem extends ContentItem implements Serializable, Cloneable {
    private static final String TAG = MorningDataCore.LOG_PREFIX + "VideoItem";
    private static final long serialVersionUID = -9194349402448195952L;
    public final String country;
    public final String lang;
    /**
     * 二级分类ID
     */
    public final int secondCategory;
    /**
     * 三级分类ID
     */
    public final int thirdCategory;
    public final String keywords;
    public final long ptime;
    public final int mark;
    public final long sourceId;
    public final String originSourceUrl;
    public final String sourceUrl;
    public final String shareUrl;
    public final String text;
    public final ArrayList<PhotoInfo> photoInfos = new ArrayList<>();
    public final String articleTitle;
    public final long showtime;
    public final long position;
    public final int userDownloads;
    public final int duration;
    public final AuthorInfo authorInfo;
    public final int userViews;
    public final int userDislikes;
    public final int userComments;
    public final int userFavorites;
    public final int viewCount;
    public final ArrayList<VideoResolutionInfo> resolutionInfos = new ArrayList<>();
    public final SpreadInfo spreadInfo;
    public int userShares;
    public int userLikes;


    VideoItem(JSONObject jsonObject, String requestId) throws JSONException {
        super(jsonObject, requestId);
        if (DEBUG) {
            Log.i(TAG, "VideoItem: 要解析的JSON串 " + jsonObject.toString());
        }
        status = jsonObject.optInt("status");
        url = jsonObject.optString("url");
        country = jsonObject.optString("country");
        lang = jsonObject.optString("lang");
        // categories 是老字段，不用管
        secondCategory = jsonObject.optInt("second_category");
        thirdCategory = jsonObject.optInt("third_category");
        keywords = jsonObject.optString("keywords");
        ptime = jsonObject.optLong("ptime");
        mark = jsonObject.optInt("mark");
        sourceId = jsonObject.optLong("source_id");
        originSourceUrl = jsonObject.optString("origin_source_url");
        sourceUrl = jsonObject.optString("source_url");
        shareUrl = jsonObject.optString("share_url");
        text = jsonObject.optString("text");
        articleTitle = jsonObject.optString("article_title");
        JSONArray photoArray = jsonObject.optJSONArray("photos");
        if (photoArray != null){
            for (int i = 0, len = photoArray.length(); i < len; i++) {
                photoInfos.add(new PhotoInfo(photoArray.optJSONObject(i)));
            }
        }
        JSONArray imageArray = jsonObject.optJSONArray("images");
        if (imageArray != null){
            for (int i = 0, len = imageArray.length(); i < len; i++) {
                photoInfos.add(new PhotoInfo(imageArray.optJSONObject(i)));
            }
        }
        showtime = jsonObject.optLong("showtime");
        position = jsonObject.optLong("position");
        userDownloads = jsonObject.optInt("user_downloads");
        duration = jsonObject.optInt("duration");
        JSONObject authorObj = jsonObject.optJSONObject("author");
        authorInfo = new AuthorInfo(authorObj);
        userViews = jsonObject.optInt("user_views");
        userLikes = jsonObject.optInt("user_likes");
        userDislikes = jsonObject.optInt("user_dislikes");
        userComments = jsonObject.optInt("user_comments");
        userShares = jsonObject.optInt("user_shares");
        userFavorites = jsonObject.optInt("user_favorites");
        viewCount = jsonObject.optInt("viewCount");

        JSONArray resolutionArray = jsonObject.optJSONArray("resolutions");
        if (resolutionArray != null) {
            for (int i = 0, len = resolutionArray.length(); i < len; i++) {
                VideoResolutionInfo videoResolutionInfo = new VideoResolutionInfo(resolutionArray.getJSONObject(i));
                if (DEBUG) {
                    Log.i(TAG, "VideoItem: " + i + ", " + videoResolutionInfo);
                }
                resolutionInfos.add(videoResolutionInfo);
            }
        }

        JSONObject spread_info = jsonObject.optJSONObject("spread_info");
        if (spread_info != null) {
            spreadInfo = new SpreadInfo(spread_info);
        } else {
            spreadInfo = null;
        }
    }

    public String getPlayUrl() {
        if (!resolutionInfos.isEmpty()) {
            return resolutionInfos.get(0).url;
        }
        return "";
    }

    @Override
    public String toString() {
        return DEBUG ? "VideoItem{" +
                "country='" + country + '\'' +
                ", lang='" + lang + '\'' +
                ", secondCategory=" + secondCategory +
                ", thirdCategory=" + thirdCategory +
                ", keywords='" + keywords + '\'' +
                ", ptime=" + ptime +
                ", mark=" + mark +
                ", source='" + source + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", originSourceUrl='" + originSourceUrl + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", text='" + text + '\'' +
                ", photoInfos=" + photoInfos +
                ", articleTitle='" + articleTitle + '\'' +
                ", showtime=" + showtime +
                ", position=" + position +
                ", userDownloads=" + userDownloads +
                ", duration=" + duration +
                ", authorInfo=" + authorInfo +
                ", userViews=" + userViews +
                ", userDislikes=" + userDislikes +
                ", userComments=" + userComments +
                ", userFavorites=" + userFavorites +
                ", viewCount=" + viewCount +
                ", dotText='" + dotText + '\'' +
                ", resolutionInfos=" + resolutionInfos +
                ", spreadInfo=" + spreadInfo +
                ", userShares=" + userShares +
                ", show=" + show +
                ", userLikes=" + userLikes +
                ", type=" + type +
                ", id=" + id +
                ", category=" + category +
                '}' : "";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static class SpreadInfo implements Serializable {
        private static final long serialVersionUID = -7544554175789471003L;
        public  boolean enable;
        public  String icon;
        public  String gp;
        public  String name;
        public  String description;
        public  String packageName;

        SpreadInfo(JSONObject jsonObject) {
            if (jsonObject != null){
                enable = jsonObject.optBoolean("enable");
                icon = jsonObject.optString("icon");
                gp = jsonObject.optString("gp");
                name = jsonObject.optString("name");
                description = jsonObject.optString("description");
                packageName = jsonObject.optString("package_name");
            }
        }

        @Override
        public String toString() {
            return DEBUG ? "SpreadInfo{" +
                    "enable=" + enable +
                    ", icon='" + icon + '\'' +
                    ", gp='" + gp + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", packageName='" + packageName + '\'' +
                    '}' : "";
        }
    }

    public static class VideoResolutionInfo implements Serializable {
        private static final long serialVersionUID = -1766026345138037474L;
        /**
         * 视频清晰度
         */
        public  String resolution;
        /**
         * 视频格式
         */
        public  String format;
        /**
         * 视频地址
         */
        public  String url;
        /**
         * 视频大小
         */
        public  int size;

        public VideoResolutionInfo(JSONObject jsonObject) {
            if (jsonObject != null){
                resolution = jsonObject.optString("resolution");
                format = jsonObject.optString("format");
                url = jsonObject.optString("url");
                size = jsonObject.optInt("size");
            }
        }

        @Override
        public String toString() {
            return DEBUG ? "VideoResolutionInfo{" +
                    "resolution='" + resolution + '\'' +
                    ", format='" + format + '\'' +
                    ", url='" + url + '\'' +
                    ", size=" + size +
                    '}' : "";
        }
    }

    public static class PhotoInfo extends NewsItem.ImageInfo implements Serializable {
        private static final long serialVersionUID = -5561886504124438144L;
        public String photoTitle;
        public String localUrl;
        public String originWidth;
        public String originHeight;
        public String originUrl;
        public int percent;
        public final ArrayList<PhotoSizeInfo> photoSizeInfos = new ArrayList<>();


        PhotoInfo(JSONObject jsonObject) throws JSONException {
            super(jsonObject);
            photoTitle = jsonObject.optString("photo_title");
            localUrl = jsonObject.optString("local_url");
            originWidth = jsonObject.optString("origin_width");
            originHeight = jsonObject.optString("origin_height");
            originUrl = jsonObject.optString("origin_url");

            JSONArray sizeArray = jsonObject.optJSONArray("sizes");
            for (int i = 0, len = sizeArray.length(); i < len; i++) {
                PhotoSizeInfo photoSizeInfo = new PhotoSizeInfo(sizeArray.getJSONObject(i));
                if (DEBUG) {
                    Log.i(TAG, "PhotoInfo: " + i + ", " + photoSizeInfo);
                }
                photoSizeInfos.add(photoSizeInfo);
            }
        }

        @Override
        public String toString() {
            return DEBUG ? "PhotoInfo{" +
                    "photoTitle='" + photoTitle + '\'' +
                    ", localUrl='" + localUrl + '\'' +
                    ", photoSizeInfos=" + photoSizeInfos +
                    ", height=" + height +
                    ", width=" + width +
                    ", url='" + url + '\'' +
                    '}' : "";
        }
    }

    public static class PhotoSizeInfo implements Serializable {

        public static final int IMAGE_TYPE_SMALL = 1; // 小图
        public static final int IMAGE_TYPE_NORMAL = 2; // 中图
        public static final int IMAGE_TYPE_BANNER = 3; // banner图
        public static final int IMAGE_TYPE_DETAIL = 4; // 详情页大图
        private static final long serialVersionUID = -3534333012523312406L;
        public final int size;
        public final int width;
        public final String url;
        public final int height;
        public final int imageType;


        PhotoSizeInfo(JSONObject jsonObject) throws JSONException {
            size = jsonObject.optInt("size");
            width = jsonObject.optInt("width");
            height = jsonObject.optInt("height");
            url = jsonObject.optString("url");
            imageType = jsonObject.optInt("image_type");
        }

        @Override
        public String toString() {
            return DEBUG ? "PhotoSizeInfo{" +
                    "size=" + size +
                    ", width=" + width +
                    ", url='" + url + '\'' +
                    ", height=" + height +
                    ", imageType=" + imageType +
                    '}' : "";
        }
    }
}
