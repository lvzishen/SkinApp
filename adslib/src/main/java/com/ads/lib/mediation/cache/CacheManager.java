package com.ads.lib.mediation.cache;

import android.content.Context;

import com.ads.lib.ModuleConfig;
import com.ads.lib.mediation.bean.BaseAd;
import com.ads.lib.mediation.bean.InterstitialWrapperAd;
import com.ads.lib.mediation.bean.NativeAd;
import com.ads.lib.mediation.helper.LogHelper;
import com.ads.lib.prop.AdIDMappingProp;
import com.ads.lib.prop.AdPositionIdProp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class CacheManager extends Observable {

    public static final String TAG = "CacheManager";
    public static final boolean DEBUG = ModuleConfig.DEBUG;

    private Map<String, List<BaseAd>> mCache = Collections.synchronizedMap(new HashMap<String, List<BaseAd>>());
    private Map<String, List<BaseAd>> mTempCache = Collections.synchronizedMap(new HashMap<String, List<BaseAd>>());

    private static CacheManager mInstance;
    private Context mContext;

    private CacheManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public static CacheManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CacheManager.class) {
                if (mInstance == null) {
                    mInstance = new CacheManager(context);
                }
            }
        }
        return mInstance;
    }

    public void enqueueAd2TempCache(String adPositionId, String unitId, BaseAd ad) {
        String poolPositionId = convert2PoolPositionId(adPositionId);
        List<BaseAd> ads;
        if (mTempCache.containsKey(poolPositionId)) {
            ads = mTempCache.get(poolPositionId);
        } else {
            ads = Collections.synchronizedList(new ArrayList<BaseAd>());
            mTempCache.put(poolPositionId, ads);
        }
        LogHelper.logD(TAG, String.format("#enqueueAd2TempCache adPositionId = [%s],poolPositionId = [%s]",adPositionId,poolPositionId));

        ads.add(ad);
    }

    public void flush(String adPositionId, String unitId){
        String poolPositionId = convert2PoolPositionId(adPositionId);
        List<BaseAd> baseAds = mTempCache.get(poolPositionId);
        if(baseAds != null){
            List<BaseAd> ads;
            if (mCache.containsKey(poolPositionId)) {
                ads = mCache.get(poolPositionId);
            } else {
                ads = Collections.synchronizedList(new ArrayList<BaseAd>());
                mCache.put(poolPositionId, ads);
            }
            ads.addAll(baseAds);
            baseAds.clear();
        }
    }


    public void enqueueAd(String adPositionId, String unitId, BaseAd ad) {
        String poolPositionId = convert2PoolPositionId(adPositionId);
        List<BaseAd> ads;
        if (mCache.containsKey(poolPositionId)) {
            ads = mCache.get(poolPositionId);
        } else {
            ads = Collections.synchronizedList(new ArrayList<BaseAd>());
            mCache.put(poolPositionId, ads);
        }
        LogHelper.logD(TAG, String.format("#enqueueAd adPositionId = [%s],poolPositionId = [%s]",adPositionId,poolPositionId));

        ads.add(ad);
        notifySizeChange(poolPositionId, unitId, ads.size());
    }

    private String convert2PoolPositionId(String positionId){
        return AdIDMappingProp.getInstance(mContext).getCachePoolPositionId(positionId);
    }

    private void notifySizeChange(String groupId, String unitId, int count) {
        setChanged();
        CacheModel cacheModel = new CacheModel(groupId, unitId, count);
        notifyObservers(cacheModel);
    }

    public BaseAd dequeueAd(String unitId, String positionId) {
        String poolPositionId = convert2PoolPositionId(positionId);
        List<BaseAd> ads = mCache.get(poolPositionId);
        removeExipreAd(unitId, poolPositionId);
        if (ads != null && ads.size() > 0) {
            LogHelper.logD(TAG, String.format("#dequeueAd positionId = [%s], poolPositionId = [%s]",positionId,poolPositionId));
            BaseAd ad = ads.remove(0);
            notifySizeChange(poolPositionId, unitId, ads.size());
            return ad;
        }
        LogHelper.logD(TAG, String.format("#dequeueAd return null positionId = [%s], poolPositionId = [%s]",positionId,poolPositionId));

        return null;
    }


    private void removeExipreAd(String unitId, String positionId) {
        String poolPositionId = convert2PoolPositionId(positionId);
        List<BaseAd> ads = mCache.get(poolPositionId);
        if (ads != null) {
            List<BaseAd> tempAds = new ArrayList<BaseAd>(ads);
            for (BaseAd tempAd : tempAds) {
                if (tempAd.isExpired()) {
                    LogHelper.logD(TAG, String.format("#removeExipreAd positionId = [%s],poolPositionId = [%s]",positionId,poolPositionId));
                    ads.remove(tempAd);
                }
            }
        }
    }


    public NativeAd dequeueNativeAd(String unitId, String positionId) {
        BaseAd ad = dequeueAd(unitId, positionId);
        if (ad == null) {
            return null;
        }
        LogHelper.logD(TAG, String.format("#dequeueNativeAd return null positionId = [%s]",positionId));
        if (ad instanceof NativeAd) {
            LogHelper.logD(TAG, String.format("#dequeueNativeAd return NativeAd positionId = [%s]",positionId));
            return (NativeAd) ad;
        }
        LogHelper.logD(TAG, String.format("#dequeueNativeAd return null positionId = [%s]",positionId));
        return null;
    }

    public InterstitialWrapperAd dequeueInterstitialWrapperAd(String unitId, String positionId) {
        BaseAd ad = dequeueAd(unitId, positionId);
        if (ad == null) {
            return null;
        }
        LogHelper.logD(TAG, String.format("#dequeueInterstitialWrapperAd return null positionId = [%s]",positionId));
        if (ad instanceof InterstitialWrapperAd) {
            LogHelper.logD(TAG, String.format("#dequeueInterstitialWrapperAd return InterstitialWrapperAd positionId = [%s]",positionId));
            return (InterstitialWrapperAd) ad;
        }
        LogHelper.logD(TAG, String.format("#dequeueInterstitialWrapperAd return null positionId = [%s]",positionId));
        return null;
    }

    public int getAdCount(String unitId, String positionId) {
        String poolPositionId = convert2PoolPositionId(positionId);
        List<BaseAd> ads = mCache.get(poolPositionId);
        if (ads != null) {
            removeExipreAd(unitId, poolPositionId);
            int size = ads.size();
            LogHelper.logD(TAG, String.format("#getAdCount size = [%d] positionId = [%s] , poolPositionId = [%s]", size,positionId,poolPositionId));
            return size;
        }
        LogHelper.logD(TAG, "#getAdCount size = [0]");
        return 0;

    }

    public int getTempCacheAdCount(String unitId, String positionId){
        String poolPositionId = convert2PoolPositionId(positionId);
        List<BaseAd> ads = mTempCache.get(poolPositionId);
        if (ads != null) {
            removeExipreAd(unitId, poolPositionId);
            int size = ads.size();
            LogHelper.logD(TAG, String.format("#getTempCacheAdCount size = [%d] positionId = [%s] , poolPositionId = [%s]", size,positionId,poolPositionId));
            return size;
        }
        LogHelper.logD(TAG, "#getTempCacheAdCount size = [0]");
        return 0;
    }

    public boolean hasCache(String unitId, String positionId) {
        return getAdCount(unitId, positionId) > 0;
    }

}
