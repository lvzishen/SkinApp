package com.goodmorning.bean;

import java.io.Serializable;

public class DayPicture implements Serializable {
    private long startTime;
    private long endTime;
    private String picUrl;
    private int width;
    private int height;

    @Override
    public String toString() {
        return "DayPicture{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", picUrl='" + picUrl + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", id=" + id +
                '}';
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
