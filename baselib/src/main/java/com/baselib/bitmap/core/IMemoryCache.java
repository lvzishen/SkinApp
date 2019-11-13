package com.baselib.bitmap.core;

import android.graphics.Bitmap;

public interface IMemoryCache {

    void put(String key, Bitmap bitmap);

    Bitmap get(String key);

    void evictAll();

    void remove(String key);

}
