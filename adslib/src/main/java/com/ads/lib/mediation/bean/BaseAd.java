package com.ads.lib.mediation.bean;

import android.content.Context;
import android.text.TextUtils;

import com.ads.lib.prop.AdIDMappingProp;

public abstract class BaseAd {


    protected Context mContext;
    protected String mUnitId;
    protected String mPlacementID;
    protected String mPositionId;
    protected String mPoolPositionId;
    protected boolean isFromCache;


    public abstract boolean isExpired();

    public String getPoolPositionId(){
        if(TextUtils.isEmpty(mPoolPositionId)){
            mPoolPositionId = AdIDMappingProp.getInstance(mContext).getCachePoolPositionId(mPositionId);
            if(mPoolPositionId == null || mPoolPositionId.contains("mp")){
                mPoolPositionId = mPositionId;
            }
        }
        return mPoolPositionId;
    }


    public boolean isFromCache(){
        return isFromCache;
    }

    public void setFromCache(boolean isFromCache){
        this.isFromCache = isFromCache;
    }

    public void setPlacementID(String placementID) {
        this.mPlacementID = placementID;
    }


    public String getPlacementId(){
        return mPlacementID;
    }

    public String getPositionId(){
        return mPositionId;
    }

}
