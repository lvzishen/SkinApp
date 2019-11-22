package com.goodmorning.bean;

import java.io.Serializable;

public class DataListItem implements Serializable {
    private int type;
    private String data;
    private String videoUrl;
    private String picUrl;
    private String videoThumbUrl;
    private int width;
    private int height;
    public static final int DATA_TYPE_1 = 1;
    public static final int DATA_TYPE_2 = 2;
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
}
