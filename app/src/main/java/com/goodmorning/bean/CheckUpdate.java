package com.goodmorning.bean;

import java.io.Serializable;

public class CheckUpdate implements Serializable {
    private int versionCode;
    private String versionName;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
