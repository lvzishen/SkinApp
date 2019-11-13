package com.baselib.bitmap.core;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 可以立即回收的bitmap内存缓存（软引用）
 */
public class SoftMemoryCacheImpl implements IMemoryCache {

    private static HashMap<String, SoftReference<Bitmap>> mMemoryCache;

    /**
     * @param size 内存缓存大小
     */
    public SoftMemoryCacheImpl(int size) {
        mMemoryCache = new HashMap<>();
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        mMemoryCache.put(key, new SoftReference<>(bitmap));
    }

    @Override
    public Bitmap get(String key) {
        SoftReference<Bitmap> softReference = mMemoryCache.get(key);
        if (softReference != null) {
            return softReference.get();
        }
        return null;
    }

    @Override
    public void evictAll() {
        mMemoryCache.clear();
    }

    @Override
    public void remove(String key) {
        mMemoryCache.remove(key);
    }

}
