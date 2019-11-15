package com.goodmorning.bean;

import java.io.Serializable;

public class DataListItem implements Serializable {
    private int type;
    private String data;
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
}
