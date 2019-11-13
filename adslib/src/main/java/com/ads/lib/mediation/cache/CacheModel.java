package com.ads.lib.mediation.cache;

public class CacheModel {


    private String cachePoolName;
    private int cacheCount;
    private String unitId;

    public String getCachePoolName() {
        return cachePoolName;
    }

    public int getCacheCount() {
        return cacheCount;
    }

    public String getUnitId(){
        return unitId;
    }

    public CacheModel(String cachePoolName, String unitId, int cacheCount) {
        this.cachePoolName = cachePoolName;
        this.cacheCount = cacheCount;
        this.unitId = unitId;
    }

    @Override
    public String toString() {
        return "CacheModel{" +
                "cachePoolName='" + cachePoolName + '\'' +
                ", cacheCount=" + cacheCount +
                ", unitId='" + unitId + '\'' +
                '}';
    }
}
