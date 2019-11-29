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
     * 内容状态：0：上线，1：下线
     */
    private int status;

    /**
     * 内容下线状态
     */
    public static final int STATUS_TYPE_1 = 1;

    /**
     * 内容上线状态
     */
    public static final int STATUS_TYPE_0 = 0;

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

    /**
     * 下线状态
     */
    public static final int DATA_TYPE_4 = 4;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
