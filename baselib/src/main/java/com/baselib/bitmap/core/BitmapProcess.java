package com.baselib.bitmap.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.baselib.bitmap.BitmapLoader;
import com.baselib.bitmap.download.Downloader;

public class BitmapProcess {

    public static boolean DEBUG = BitmapLoader.DEBUG;
    private static final String TAG = "BitmapProcess";
    private Downloader mDownloader;
    private final BitmapCache mCache;

    private static final int BYTESBUFFE_POOL_SIZE = 4;
    private static final int BYTESBUFFER_SIZE = 200 * 1024;
    private static final BytesBufferPool sMicroThumbBufferPool = new BytesBufferPool(BYTESBUFFE_POOL_SIZE, BYTESBUFFER_SIZE);

    public BitmapProcess(Downloader downloader, BitmapCache cache) {
        setDownloader(downloader);
        this.mCache = cache;
    }

    public void setDownloader(Downloader downloader) {
        this.mDownloader = downloader;
    }

    public Bitmap getBitmap(String url, BitmapDisplayConfig config) {
        return getBitmap(null, url, config, mDownloader);
    }

    public Bitmap getBitmap(Context context, String url, BitmapDisplayConfig config, Downloader downloader) {
        if (downloader == null) {
            downloader = this.mDownloader;
        }

        Bitmap bitmap = this.getFromDisk(url, config);

        if (bitmap == null && downloader != null) {
            byte[] data = null;
            try {
                data = downloader.download(context, url);
                if (data != null && data.length > 0) {
                    if (config != null) {
                        bitmap = BitmapDecoder.decodeSampledBitmapFromByteArray(
                                data, 0, data.length, config.getBitmapWidth(),
                                config.getBitmapHeight());
                    } else {
                        return BitmapFactory.decodeByteArray(data, 0, data.length);
                    }

                    this.mCache.addToDiskCache(url, data);
                }
            } catch (Throwable t) {
                if (DEBUG) {
                    Log.e(TAG, "getBitmap-->Throwable:" + t.toString());
                }
            }
        }

        return bitmap;
    }

    public Bitmap getFromDisk(String key, BitmapDisplayConfig config) {
        BytesBufferPool.BytesBuffer buffer = sMicroThumbBufferPool.get();
        Bitmap b = null;
        try {
            boolean found = this.mCache.getImageData(key, buffer);
            if (found && buffer.length - buffer.offset > 0) {
                if (config != null) {
                    b = BitmapDecoder.decodeSampledBitmapFromByteArray(
                            buffer.data, buffer.offset, buffer.length,
                            config.getBitmapWidth(), config.getBitmapHeight());
                } else {
                    b = BitmapFactory.decodeByteArray(buffer.data,
                            buffer.offset, buffer.length);
                }
            }
        } finally {
            sMicroThumbBufferPool.recycle(buffer);
        }
        return b;
    }
}
