package com.goodmorning.bean;

import java.io.Serializable;

public class DataListItem implements Serializable {
    /**
     * 内容类型：1：文本，2：图片，3：视频
     */
    private int type;
    /**
     * 文本数据
     */
    private String data;
    /**
     * 视频URL
     */
    private String videoUrl;
    /**
     * 图片URL
     */
    private String picUrl;
    /**
     * 视频缩略图
     */
    private String videoThumbUrl;
    /**
     * 图片宽度
     */
    private int width;
    /**
     * 图片高度
      */
    private int height;
    /**
     * 资源id
     */
    private long resourceId;
    /**
     * 资源id
     */
    private long id;
    /**
     * 文本
     */
    public static final int DATA_TYPE_1 = 1;
    /**
     * 图片
     */
    public static final int DATA_TYPE_2 = 2;
    /**
     * 视频
     */
    public static final int DATA_TYPE_3 = 3;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getVideoThumbUrl() {
        return videoThumbUrl;
    }

    public void setVideoThumbUrl(String videoThumbUrl) {
        this.videoThumbUrl = videoThumbUrl;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
