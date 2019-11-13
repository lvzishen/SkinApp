package com.baselib.bitmap.image;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.LruCache;


public class ImageCache {

    private LruCache<String, Drawable> mMemoryCache;

    private static ImageCache sInstance;
    
    private static final int CACHEDDRAWABLESIZE = 10;

    private ImageCache() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        //final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
       // final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Drawable>(CACHEDDRAWABLESIZE) {
            @Override
            protected int sizeOf(String key, Drawable value) {
               /* final int bitmapSize = getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;*/
            	return value == null?0:1;
            }
        };
    }

   /* @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int getBitmapSize(Drawable value) {
        Bitmap bitmap = value.getBitmap();

        // From KitKat onward use getAllocationByteCount() as allocated bytes can potentially be
        // larger than bitmap byte count.
        if (Build.VERSION.SDK_INT >= 19) {
            int size = 1024;
            try {
                size = bitmap.getAllocationByteCount();
            } catch (Exception e) {
                size = bitmap.getByteCount();
            }
            return size;
        }

        return bitmap.getByteCount();
    }*/

    public static ImageCache getInstance() {
        if (sInstance == null) {
            sInstance = new ImageCache();
        }
        return sInstance;
    }

    public void addBitmapToMemoryCache(String key, Drawable bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Drawable getBitmapFromMemCache(String key) {
    	if(TextUtils.isEmpty(key))
    		return null;
        return mMemoryCache.get(key);
    }

    public void clearMemoryCache() {
    	try{
	        if (mMemoryCache != null) {
	            mMemoryCache.evictAll();
	        }
    	}catch(Throwable tr){
    		
    	}
    }
}
