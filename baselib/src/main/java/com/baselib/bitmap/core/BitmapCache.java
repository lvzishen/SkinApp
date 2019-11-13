package com.baselib.bitmap.core;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.baselib.bitmap.BitmapLoader;
import com.baselib.bitmap.util.BitmapUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapCache {

    public static boolean DEBUG = BitmapLoader.DEBUG;
    private static final String TAG = "BitmapCache";

    // 默认的内存缓存大小
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 8; // 8MB

    // 默认的磁盘缓存大小
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 50; // 50MB
    private static final int DEFAULT_DISK_CACHE_COUNT = 1000 * 10; // 缓存的图片数量

    // BitmapCache的一些默认配置
    public static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    public static final boolean DEFAULT_DISK_CACHE_ENABLED = false;
    public static final boolean DEFAULT_RECYCLE_IMMEDIATELY = false;

    private DiskCache mDiskCache;
    private IMemoryCache mMemoryCache;
    private ImageCacheParams imageCacheParams;

    public BitmapCache(ImageCacheParams cacheParams) {
        this.init(cacheParams);
    }

    private void init(ImageCacheParams cacheParams) {
        this.imageCacheParams = cacheParams;

        initMemoryCache();

        initDiskCache();
    }

    private void initMemoryCache() {
        if (imageCacheParams == null)
            return;
        // 是否启用内存缓存
        if (this.imageCacheParams.memoryCacheEnabled) {
            // 是否立即回收内存
            if (this.imageCacheParams.recycleImmediately) {
                this.mMemoryCache = new SoftMemoryCacheImpl(
                        this.imageCacheParams.memCacheSize);
            } else {
                this.mMemoryCache = new BaseMemoryCacheImpl(
                        this.imageCacheParams.memCacheSize);
            }
        } else {
            this.mMemoryCache = null;
        }
    }

    private void initDiskCache() {
        if (imageCacheParams == null)
            return;
        // 是否启用sdcard缓存
        if (this.imageCacheParams.diskCacheEnabled) {
            String path = this.imageCacheParams.diskCacheDir.getAbsolutePath();
            try {
                this.mDiskCache = new DiskCache(path,
                        this.imageCacheParams.diskCacheCount,
                        this.imageCacheParams.diskCacheSize, false);
            } catch (IOException e) {
                // ignore
            }
        } else {
            this.mDiskCache = null;
        }
    }

    public void configMemoryCacheEnable(boolean memoryCacheEnabled) {
        if (imageCacheParams != null) {
            imageCacheParams.memoryCacheEnabled = memoryCacheEnabled;
        }
        if (memoryCacheEnabled) {
            initMemoryCache();
        } else {
            this.mMemoryCache = null;
        }
    }

    public void configDiskCacheEnable(boolean diskCacheEnable) {
        if (imageCacheParams != null) {
            imageCacheParams.diskCacheEnabled = diskCacheEnable;
        }
        if (diskCacheEnable) {
            initDiskCache();
        } else {
            this.mDiskCache = null;
        }
    }

    /**
     * 添加图片到内存缓存中
     *
     * @param url    Url 地址
     * @param bitmap 图片数据
     */
    public void addToMemoryCache(String url, Bitmap bitmap) {
        if (url == null || bitmap == null) {
            return;
        }
        if (this.mMemoryCache != null) {
            this.mMemoryCache.put(url, bitmap);
        }
    }

    /**
     * 添加 数据到sdcard缓存中
     *
     * @param url  url地址
     * @param data 数据信息
     */
    public void addToDiskCache(String url, byte[] data) {
        if (this.mDiskCache == null || url == null || data == null) {
            return;
        }
        // Add to disk cache
        byte[] key = BitmapUtil.makeKey(url);
        long cacheKey = BitmapUtil.crc64Long(key);
        ByteBuffer buffer = ByteBuffer.allocate(key.length + data.length);
        buffer.put(key);
        buffer.put(data);
        synchronized (this.mDiskCache) {
            try {
                this.mDiskCache.insert(cacheKey, buffer.array());
            } catch (IOException ex) {
                // ignore.
            }
        }

    }

    /**
     * 从sdcard中获取内存缓存
     *
     * @param url    图片url地址
     * @param buffer 填充缓存区
     * @return 是否获得图片
     */
    public boolean getImageData(String url, BytesBufferPool.BytesBuffer buffer) {
        if (this.mDiskCache == null) {
            return false;
        }

        byte[] key = BitmapUtil.makeKey(url);
        long cacheKey = BitmapUtil.crc64Long(key);
        try {
            DiskCache.LookupRequest request = new DiskCache.LookupRequest();
            request.key = cacheKey;
            request.buffer = buffer.data;
            synchronized (this.mDiskCache) {
                if (!this.mDiskCache.lookup(request)) {
                    return false;
                }
            }
            if (BitmapUtil.isSameKey(key, request.buffer)) {
                buffer.data = request.buffer;
                buffer.offset = key.length;
                buffer.length = request.length - buffer.offset;
                return true;
            }
        } catch (IOException ex) {
            // ignore.
        }
        return false;
    }

    /**
     * 从内存缓存中获取bitmap.
     */
    public Bitmap getBitmapFromMemoryCache(String data) {
        if (this.mMemoryCache != null) {
            return this.mMemoryCache.get(data);
        }
        return null;
    }

    /**
     * 清空内存缓存和sdcard缓存
     */
    public void clearCache() {
        this.clearMemoryCache();
        this.clearDiskCache();
    }

    public void clearDiskCache() {
        if (this.mDiskCache != null) {
            this.mDiskCache.delete();
        }
    }

    public void clearMemoryCache() {
        if (this.mMemoryCache != null) {
            this.mMemoryCache.evictAll();
        }
    }

    public void clearCache(String key) {
        this.clearMemoryCache(key);
        this.clearDiskCache(key);
    }

    public void clearDiskCache(String url) {
        this.addToDiskCache(url, new byte[0]);
    }

    public void clearMemoryCache(String key) {
        if (this.mMemoryCache != null) {
            this.mMemoryCache.remove(key);
        }
    }

    /**
     * Closes the disk cache associated with this ImageCache object. Note that
     * this includes disk access so this should not be executed on the main/UI
     * thread.
     */
    public void close() {
        if (this.mDiskCache != null) {
            this.mDiskCache.close();
        }
    }

    /**
     * 图片缓存的配置信息
     */
    public static class ImageCacheParams {
        /**
         * 内存缓存大小
         */
        public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
        /**
         * 磁盘缓存大小
         */
        public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
        /**
         * 磁盘缓存的图片数量
         */
        public int diskCacheCount = DEFAULT_DISK_CACHE_COUNT;
        /**
         * 是否使用内存缓存
         */
        public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
        /**
         * 是否使用sdcard缓存
         */
        public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
        public boolean recycleImmediately = DEFAULT_RECYCLE_IMMEDIATELY;

        public File diskCacheDir;

        public ImageCacheParams(File diskCacheDir) {
            this.diskCacheDir = diskCacheDir;
        }

        public ImageCacheParams(String diskCacheDir) {
            this.diskCacheDir = new File(diskCacheDir);
        }

        /**
         * 设置缓存大小 的百分比
         *
         * @param context
         * @param percent 百分比，值的范围是在 0.05 到 0.8之间
         */
        public void setMemCacheSizePercent(Context context, float percent) {
            if (percent < 0.05f || percent > 0.8f) {
                throw new IllegalArgumentException(
                        "setMemCacheSizePercent - percent must be "
                                + "between 0.05 and 0.8 (inclusive)");
            }
            int memoryClass = getMemoryClass(context);
            if (DEBUG) {
                Log.d(TAG, "setMemCacheSizePercent-->memoryClass:" + memoryClass + " percent:" + percent);
            }
            setMemCacheSize(Math.round(percent * memoryClass * 1024 * 1024));
        }

        /**
         * 设置缓存大小
         *
         * @param memCacheSize 缓存大小
         */
        public void setMemCacheSize(int memCacheSize) {
            this.memCacheSize = memCacheSize;
            if (DEBUG) {
                Log.d(TAG, "setMemCacheSize-->memCacheSize:" + memCacheSize);
            }
        }

        /**
         * 设置磁盘缓存大小
         *
         * @param diskCacheSize 磁盘缓存大小
         */
        public void setDiskCacheSize(int diskCacheSize) {
            this.diskCacheSize = diskCacheSize;
        }

        /**
         * 设置磁盘缓存图片的数量
         *
         * @param diskCacheCount 图片数量
         */
        public void setDiskCacheCount(int diskCacheCount) {
            this.diskCacheCount = diskCacheCount;
        }

        private static int getMemoryClass(Context context) {
            return ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
        }

        /**
         * 设置是否立即回收内存
         *
         * @param recycleImmediately
         */
        public void setRecycleImmediately(boolean recycleImmediately) {
            this.recycleImmediately = recycleImmediately;
        }
    }
}
