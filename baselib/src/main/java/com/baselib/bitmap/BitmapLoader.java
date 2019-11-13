package com.baselib.bitmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.baselib.bitmap.core.BitmapCache;
import com.baselib.bitmap.core.BitmapDisplayConfig;
import com.baselib.bitmap.core.BitmapProcess;
import com.baselib.bitmap.display.Displayer;
import com.baselib.bitmap.display.SimpleDisplayer;
import com.baselib.bitmap.download.Downloader;
import com.baselib.bitmap.download.SimpleDownloader;
import com.baselib.bitmap.util.DeviceUtil;
import com.baselib.utils.ModuleConfig;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class BitmapLoader {

    public static boolean DEBUG = ModuleConfig.DEBUG;
    private static BitmapLoader mBitmapLoader;

    private final BitmapConfig mConfig;
    private BitmapCache mImageCache;
    private BitmapProcess mBitmapProcess;
    private boolean mExitTasksEarly = false;
    private boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();
    private final Context mContext;
    private boolean mInit = false;
    private Executor bitmapLoadAndDisplayExecutor;
    private Downloader mDefaultDownloader;
    private Displayer mDefaultDisplayer;

    /**
     * 入口：创建BitmapUtil
     *
     * @param context 上下文
     * @return 返回BitmapUtil
     */
    public static synchronized BitmapLoader create(Context context) {
        if (mBitmapLoader == null) {
            mBitmapLoader = new BitmapLoader(context.getApplicationContext(),
                    DeviceUtil.getDiskCacheDir(context, "bitmapLoaderCache")
                              .getAbsolutePath());
            mBitmapLoader.configExecutor(ThreadPoolExecutorMgr.THREAD_POOL_EXECUTOR);
        }
        return mBitmapLoader;
    }

    /**
     * 入口：创建BitmapUtil
     *
     * @param context       上下文
     * @param diskCachePath 磁盘缓存路径
     * @return 返回BitmapUtil
     */
    public static synchronized BitmapLoader create(Context context, String diskCachePath) {
        if (mBitmapLoader == null) {
            mBitmapLoader = new BitmapLoader(context.getApplicationContext(),
                    diskCachePath);
            mBitmapLoader.configExecutor(ThreadPoolExecutorMgr.THREAD_POOL_EXECUTOR);
        }
        return mBitmapLoader;
    }

    private BitmapLoader(Context context, String diskCachePath) {
        this.mContext = context;
        this.mConfig = new BitmapConfig(context);
        this.configDiskCachePath(diskCachePath);// 配置缓存路径
        if (mDefaultDisplayer == null) {
            mDefaultDisplayer = new SimpleDisplayer();
        }
        this.configDisplayer(mDefaultDisplayer);// 配置显示器

        if (mDefaultDownloader == null) {
            mDefaultDownloader = new SimpleDownloader();
        }
        this.configDownlader(mDefaultDownloader);// 配置下载器
    }

    public Downloader getDefaultDownloader() {
        return mDefaultDownloader;
    }

    public Displayer getDefaultDisplayer() {
        return mDefaultDisplayer;
    }

    /**
     * 设置图片正在加载的时候显示的图片
     */
    public BitmapLoader configLoadingImage(Bitmap bitmap) {
        this.mConfig.defaultDisplayConfig.setLoadingBitmap(bitmap);
        return this;
    }

    /**
     * 设置图片正在加载的时候显示的图片
     */
    public BitmapLoader configLoadingImageRes(int resId) {
        this.mConfig.defaultDisplayConfig.setLoadingBitmapRes(resId);
        return this;
    }

    /**
     * 设置图片加载失败时候显示的图片
     */
    public BitmapLoader configLoadfailImage(Bitmap bitmap) {
        this.mConfig.defaultDisplayConfig.setLoadFailBitmap(bitmap);
        return this;
    }

    /**
     * 配置是否开启图片加载动画
     */
    public BitmapLoader configAnimationEnable(boolean isEnable) {
        this.mConfig.animationEnable = isEnable;
        return this;
    }

    public void configDiskcacheEnable(boolean diskCacheEnable) {
        if (mConfig != null) {
            mConfig.diskCacheEnabled = diskCacheEnable;
        }
        if (this.mImageCache != null) {
            this.mImageCache.configDiskCacheEnable(diskCacheEnable);
        }
    }

    /**
     * 设置图片加载失败时候显示的图片
     */
    public BitmapLoader configLoadfailImageRes(int resId) {
        this.mConfig.defaultDisplayConfig.setLoadingFailBitmapRes(resId);
        return this;
    }

    /**
     * 配置默认图片的小的高度
     */
    public BitmapLoader configBitmapMaxHeight(int bitmapHeight) {
        this.mConfig.defaultDisplayConfig.setBitmapHeight(bitmapHeight);
        return this;
    }

    /**
     * 配置默认图片的小的宽度
     */
    public BitmapLoader configBitmapMaxWidth(int bitmapWidth) {
        this.mConfig.defaultDisplayConfig.setBitmapWidth(bitmapWidth);
        return this;
    }

    /**
     * 设置下载器，比如通过ftp或者其他协议去网络读取图片的时候可以设置这项
     */
    public BitmapLoader configDownlader(Downloader downlader) {
        this.mConfig.downloader = downlader;
        if (this.mBitmapProcess != null) {
            this.mBitmapProcess.setDownloader(downlader);
        }
        return this;
    }

    /**
     * 设置显示器，比如在显示的过程中显示动画等
     */
    public BitmapLoader configDisplayer(Displayer displayer) {
        this.mConfig.displayer = displayer;
        return this;
    }

    /**
     * 配置磁盘缓存路径
     */
    public BitmapLoader configDiskCachePath(String strPath) {
        if (!TextUtils.isEmpty(strPath)) {
            this.mConfig.cachePath = strPath;
        }
        return this;
    }

    /**
     * 配置线程池
     */
    public BitmapLoader configExecutor(Executor executor) {
        this.bitmapLoadAndDisplayExecutor = executor;
        return this;
    }

    public BitmapLoader configMemoryCacheEnable(boolean memoryCacheEnabled) {
        if (mConfig != null) {
            mConfig.memoryCacheEnable = memoryCacheEnabled;
        }
        if (mImageCache != null) {
            mImageCache.configMemoryCacheEnable(memoryCacheEnabled);
        }
        return this;
    }

    /**
     * 配置内存缓存大小 大于2MB以上有效
     *
     * @param size 缓存大小
     */
    public BitmapLoader configMemoryCacheSize(int size) {
        this.mConfig.memCacheSize = size;
        return this;
    }

    /**
     * 设置应缓存的在APK总内存的百分比，优先级大于configMemoryCacheSize
     *
     * @param percent 百分比，值的范围是在 0.05 到 0.8之间
     */
    public BitmapLoader configMemoryCachePercent(float percent) {
        this.mConfig.memCacheSizePercent = percent;
        return this;
    }

    /**
     * 设置磁盘缓存大小 5MB 以上有效
     */
    public BitmapLoader configDiskCacheSize(int size) {
        this.mConfig.diskCacheSize = size;
        return this;
    }

    /**
     * 设置加载图片的线程并发数量
     */
    public BitmapLoader configBitmapLoadThreadSize(int size) {
        if (size >= 1) {
            this.mConfig.poolSize = size;
        }
        return this;
    }

    /**
     * 配置是否立即回收图片资源
     */
    public BitmapLoader configRecycleImmediately(boolean recycleImmediately) {
        this.mConfig.recycleImmediately = recycleImmediately;
        return this;
    }

    /**
     * 初始化
     */
    private BitmapLoader init() {
        if (!this.mInit) {
            BitmapCache.ImageCacheParams imageCacheParams = new BitmapCache.ImageCacheParams(this.mConfig.cachePath);
            if (this.mConfig.memCacheSizePercent > 0.05 && this.mConfig.memCacheSizePercent < 0.8) {
                imageCacheParams.setMemCacheSizePercent(this.mContext, this.mConfig.memCacheSizePercent);
            } else {
                if (this.mConfig.memCacheSize > 1024 * 1024 * 2) {
                    imageCacheParams.setMemCacheSize(this.mConfig.memCacheSize);
                } else {
                    // 设置默认的内存缓存大小
                    imageCacheParams.setMemCacheSizePercent(this.mContext, 0.3f);
                }
            }
            if (this.mConfig.diskCacheSize > 1024 * 1024 * 5) {
                imageCacheParams.setDiskCacheSize(this.mConfig.diskCacheSize);
            }

            imageCacheParams.setRecycleImmediately(this.mConfig.recycleImmediately);
            imageCacheParams.memoryCacheEnabled = mConfig.memoryCacheEnable;
            imageCacheParams.diskCacheEnabled = mConfig.diskCacheEnabled;
            // init Cache
            this.mImageCache = new BitmapCache(imageCacheParams);
            if (mConfig.useThreadExecutor) {
                if (bitmapLoadAndDisplayExecutor == null) {
                    // init Executors
                    this.bitmapLoadAndDisplayExecutor = Executors.newFixedThreadPool(
                            this.mConfig.poolSize, new ThreadFactory() {
                                @Override
                                public Thread newThread(Runnable r) {
                                    Thread t = new Thread(r);
                                    // 设置线程的优先级别，让线程先后顺序执行（级别越高，抢到cpu执行的时间越多）
                                    t.setPriority(Thread.NORM_PRIORITY - 1);
                                    return t;
                                }
                            });
                }
            }

            // init BitmapProcess
            this.mBitmapProcess = new BitmapProcess(this.mConfig.downloader, this.mImageCache);

            this.mInit = true;
        }
        return this;
    }

    private Displayer getDisplayer() {
        if (mConfig != null) {
            return mConfig.displayer;
        }
        return null;
    }

    private Downloader getDownloader() {
        if (mConfig != null) {
            return mConfig.downloader;
        }
        return null;
    }

    public void display(View imageView, String uri) {
        this.display(imageView, uri, getDisplayer());
    }

    public void display(View imageView, String uri, Downloader downloader) {
        this.display(imageView, uri, getDisplayer(), downloader);
    }

    public void display(View imageView, String uri, Displayer displayer) {
        this.display(imageView, uri, displayer, getDownloader());
    }

    public void display(View imageView, String uri, Displayer displayer, Downloader downloader) {
        this.doDisplay(imageView, uri, null, 0, displayer, downloader);
    }

    public void display(View imageView, String uri, int loadingBitmapRes, Displayer displayer, Downloader downloader) {
        this.doDisplay(imageView, uri, null, loadingBitmapRes, displayer, downloader);
    }

    public void display(View imageView, String uri, int loadingBitmapRes) {
        this.display(imageView, uri, loadingBitmapRes, getDisplayer());
    }

    public void display(View imageView, String uri, int loadingBitmapRes, Displayer displayer) {
        this.doDisplay(imageView, uri, null, loadingBitmapRes, displayer, getDownloader());
    }

    public void display(View imageView, String uri, int imageWidth, int imageHeight) {
        BitmapDisplayConfig displayConfig = this.configMap.get(imageWidth + "_" + imageHeight);
        if (displayConfig == null) {
            displayConfig = this.getDisplayConfig();
            displayConfig.setBitmapHeight(imageHeight);
            displayConfig.setBitmapWidth(imageWidth);
            this.configMap.put(imageWidth + "_" + imageHeight, displayConfig);
        }

        this.doDisplay(imageView, uri, displayConfig, 0, getDisplayer(), getDownloader());
    }

    public void display(View imageView, String uri, Bitmap loadingBitmap) {
        this.display(imageView, uri, loadingBitmap, getDisplayer());
    }

    public void display(View imageView, String uri, Bitmap loadingBitmap, Displayer displayer) {
        BitmapDisplayConfig displayConfig = this.configMap.get(String.valueOf(loadingBitmap));
        if (displayConfig == null) {
            displayConfig = this.getDisplayConfig();
            displayConfig.setLoadingBitmap(loadingBitmap);
            this.configMap.put(String.valueOf(loadingBitmap), displayConfig);
        }

        this.doDisplay(imageView, uri, displayConfig, 0, displayer, getDownloader());
    }

    public void display(View imageView, String uri, Bitmap loadingBitmap, Bitmap laodfailBitmap) {
        BitmapDisplayConfig displayConfig = this.configMap.get(String
                .valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap));
        if (displayConfig == null) {
            displayConfig = this.getDisplayConfig();
            displayConfig.setLoadingBitmap(loadingBitmap);
            displayConfig.setLoadFailBitmap(laodfailBitmap);
            this.configMap.put(
                    String.valueOf(loadingBitmap) + "_"
                            + String.valueOf(laodfailBitmap), displayConfig);
        }

        this.doDisplay(imageView, uri, displayConfig, 0, getDisplayer(), getDownloader());
    }

    public void display(View imageView, String uri, int imageWidth,
                        int imageHeight, Bitmap loadingBitmap, Bitmap laodfailBitmap) {
        BitmapDisplayConfig displayConfig = this.configMap.get(imageWidth + "_"
                + imageHeight + "_" + String.valueOf(loadingBitmap) + "_"
                + String.valueOf(laodfailBitmap));
        if (displayConfig == null) {
            displayConfig = this.getDisplayConfig();
            displayConfig.setBitmapHeight(imageHeight);
            displayConfig.setBitmapWidth(imageWidth);
            displayConfig.setLoadingBitmap(loadingBitmap);
            displayConfig.setLoadFailBitmap(laodfailBitmap);
            this.configMap.put(imageWidth + "_" + imageHeight + "_" + String.valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap), displayConfig);
        }

        this.doDisplay(imageView, uri, displayConfig, 0, getDisplayer(), getDownloader());
    }

    public void display(View imageView, String uri, BitmapDisplayConfig config) {
        this.doDisplay(imageView, uri, config, 0, getDisplayer(), null);
    }

    private void doDisplay(View imageView, String uri, BitmapDisplayConfig displayConfig, int loadingBitmapRes, Displayer displayer, Downloader downloader) {
        if (!this.mInit) {
            this.init();
        }

        if (TextUtils.isEmpty(uri) || imageView == null) {
            return;
        }

        if (displayConfig == null) {
            displayConfig = this.mConfig.defaultDisplayConfig;
        }

        Bitmap bitmap = null;

        if (this.mImageCache != null) {
            bitmap = this.mImageCache.getBitmapFromMemoryCache(uri);
        }

        if (bitmap != null) {
            if (imageView instanceof ImageView) {
                ((ImageView) imageView).setImageBitmap(bitmap);
            } else {
                imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        } else if (checkImageTask(uri, imageView)) {
            final BitmapLoadAndDisplayTask task = new BitmapLoadAndDisplayTask(imageView, displayConfig, displayer, downloader);

            AsyncDisplayTask asyncDisplayTask = new AsyncDisplayTask(task);
            imageView.setTag(asyncDisplayTask);

            // 设置默认图片
            if (loadingBitmapRes > 0) {
                if (imageView instanceof ImageView) {
                    ((ImageView) imageView).setImageResource(loadingBitmapRes);
                } else {
                    imageView.setBackgroundResource(loadingBitmapRes);
                }
            } else if (displayConfig.getLoadingBitmapRes() > 0) {
                if (imageView instanceof ImageView) {
                    ((ImageView) imageView).setImageResource(displayConfig.getLoadingBitmapRes());
                } else {
                    imageView.setBackgroundResource(displayConfig.getLoadingBitmapRes());
                }
            } else if (displayConfig.getLoadingBitmap() != null) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), displayConfig.getLoadingBitmap());
                if (imageView instanceof ImageView) {
                    ((ImageView) imageView).setImageDrawable(bitmapDrawable);
                } else {
                    imageView.setBackgroundDrawable(bitmapDrawable);
                }
            } else {
                Drawable bitmapDrawable = new ColorDrawable(Color.TRANSPARENT);
                if (imageView instanceof ImageView) {
                    ((ImageView) imageView).setImageDrawable(bitmapDrawable);
                } else {
                    imageView.setBackgroundDrawable(bitmapDrawable);
                }
            }

            if (mConfig.useThreadExecutor) {
                task.executeOnExecutor(this.bitmapLoadAndDisplayExecutor, uri);
            } else {
                task.execute(uri);
            }
        }
    }

    private final HashMap<String, BitmapDisplayConfig> configMap = new HashMap<>();

    private BitmapDisplayConfig getDisplayConfig() {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setAnimation(this.mConfig.defaultDisplayConfig.getAnimation());
        config.setAnimationType(this.mConfig.defaultDisplayConfig.getAnimationType());
        config.setBitmapHeight(this.mConfig.defaultDisplayConfig.getBitmapHeight());
        config.setBitmapWidth(this.mConfig.defaultDisplayConfig.getBitmapWidth());
        config.setLoadFailBitmap(this.mConfig.defaultDisplayConfig.getLoadFailBitmap());
        config.setLoadingBitmap(this.mConfig.defaultDisplayConfig.getLoadingBitmap());
        return config;
    }

    private void clearCacheInternalInBackgroud() {
        if (this.mImageCache != null) {
            this.mImageCache.clearCache();
        }
    }

    private void clearDiskCacheInBackgroud() {
        if (this.mImageCache != null) {
            this.mImageCache.clearDiskCache();
        }
    }

    private void clearCacheInBackgroud(String key) {
        if (this.mImageCache != null) {
            this.mImageCache.clearCache(key);
        }
    }

    private void clearDiskCacheInBackgroud(String key) {
        if (this.mImageCache != null) {
            this.mImageCache.clearDiskCache(key);
        }
    }

    /**
     * 执行过此方法后，缓存已经失效,建议通过create()获取新的实例
     */
    private void closeCacheInternalInBackgroud() {
        if (this.mImageCache != null) {
            this.mImageCache.close();
            this.mImageCache = null;
            mBitmapLoader = null;
        }
    }

    /**
     * 网络加载bitmap
     */
    private Bitmap processBitmap(String uri, BitmapDisplayConfig config, Downloader downloader) {
        if (this.mBitmapProcess != null) {
            return this.mBitmapProcess.getBitmap(mContext, uri, config, downloader);
        }
        return null;
    }

    /**
     * 从缓存（内存缓存和磁盘缓存）中直接获取bitmap，注意这里有io操作，最好不要放在ui线程执行
     */
    public Bitmap getBitmapFromCache(String key) {
        Bitmap bitmap = this.getBitmapFromMemoryCache(key);
        if (bitmap == null) {
            bitmap = this.getBitmapFromDiskCache(key);
        }
        return bitmap;
    }

    /**
     * 从内存缓存中获取bitmap
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        if (mImageCache != null) {
            return this.mImageCache.getBitmapFromMemoryCache(key);
        }
        return null;
    }

    /**
     * 从磁盘缓存中获取bitmap，，注意这里有io操作，最好不要放在ui线程执行
     */
    public Bitmap getBitmapFromDiskCache(String key) {
        return this.getBitmapFromDiskCache(key, null);
    }

    public Bitmap getBitmapFromDiskCache(String key, BitmapDisplayConfig config) {
        if (mBitmapProcess != null) {
            return this.mBitmapProcess.getFromDisk(key, config);
        }
        return null;
    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        this.mExitTasksEarly = exitTasksEarly;
    }

    /**
     * activity onResume的时候调用这个方法，让加载图片线程继续
     */
    public void onResume() {
        this.setExitTasksEarly(false);
    }

    /**
     * activity onPause的时候调用这个方法，让线程暂停
     */
    public void onPause() {
        this.setExitTasksEarly(true);
    }

    /**
     * activity onDestroy的时候调用这个方法，释放缓存
     * 执行过此方法后,缓存已经失效,建议通过create()获取新的实例
     */
    public void onDestroy() {
        this.closeCache();
    }

    /**
     * 清除所有缓存（磁盘和内存）
     */
    public void clearCache() {
        new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR);
    }

    /**
     * 根据key清除指定的内存缓存和Disk缓存
     */
    public void clearCache(String key) {
        new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR_KEY, key);
    }

    /**
     * 清除缓存
     */
    public void clearMemoryCache() {
        if (this.mImageCache != null) {
            this.mImageCache.clearMemoryCache();
        }
    }

    /**
     * 根据key清除指定的内存缓存
     */
    public void clearMemoryCache(String key) {
        if (this.mImageCache != null) {
            this.mImageCache.clearMemoryCache(key);
        }
    }

    /**
     * 清除磁盘缓存
     */
    public void clearDiskCache() {
        new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR_DISK);
    }

    /**
     * 根据key清除指定的Disk缓存
     */
    public void clearDiskCache(String key) {
        new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR_KEY_IN_DISK, key);
    }

    /**
     * 关闭缓存 执行过此方法后,缓存已经失效,建议通过create()获取新的实例
     */
    public void closeCache() {
        new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLOSE);
    }

    /**
     * 退出正在加载的线程，程序退出的时候调用词方法
     */
    public void exitTasksEarly(boolean exitTasksEarly) {
        this.mExitTasksEarly = exitTasksEarly;
        if (exitTasksEarly) {
            this.pauseWork(false);// 让暂停的线程结束
        }
    }

    /**
     * 暂停正在加载的线程，监听listview或者gridview正在滑动的时候条用词方法
     *
     * @param pauseWork true停止暂停线程，false继续线程
     */
    public void pauseWork(boolean pauseWork) {
        synchronized (this.mPauseWorkLock) {
            this.mPauseWork = pauseWork;
            if (!this.mPauseWork) {
                this.mPauseWorkLock.notifyAll();
            }
        }
    }

    private static BitmapLoadAndDisplayTask getBitmapTaskFromImageView(View imageView) {
        if (imageView != null) {
            Object o = imageView.getTag();
            if (o instanceof AsyncDisplayTask) {
                final AsyncDisplayTask asyncDisplayTask = (AsyncDisplayTask) o;
                return asyncDisplayTask.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * 检测 imageView中是否已经有线程在运行
     *
     * @return true 没有 false 有线程在运行了
     */
    public static boolean checkImageTask(Object data, View imageView) {
        final BitmapLoadAndDisplayTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);

        if (bitmapWorkerTask != null) {
            final Object bitmapData = bitmapWorkerTask.data;
            if (bitmapData == null || !bitmapData.equals(data)) {
                bitmapWorkerTask.cancel(true);
            } else {
                // 同一个线程已经在执行
                return false;
            }
        }
        return true;
    }

    private static class AsyncDisplayTask {
        private final WeakReference<BitmapLoadAndDisplayTask> bitmapWorkerTaskReference;

        public AsyncDisplayTask(BitmapLoadAndDisplayTask bitmapWorkerTask) {
            this.bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapLoadAndDisplayTask getBitmapWorkerTask() {
            return this.bitmapWorkerTaskReference.get();
        }
    }

    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapLoadAndDisplayTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapLoadAndDisplayTask bitmapWorkerTask) {
            super(res, bitmap);
            this.bitmapWorkerTaskReference = new WeakReference<>(
                    bitmapWorkerTask);
        }

        public BitmapLoadAndDisplayTask getBitmapWorkerTask() {
            return this.bitmapWorkerTaskReference.get();
        }
    }

    private class CacheExecutecTask extends AsyncTask<Object, Void, Void> {
        public static final int MESSAGE_CLEAR = 1;
        public static final int MESSAGE_CLOSE = 2;
        public static final int MESSAGE_CLEAR_DISK = 3;
        public static final int MESSAGE_CLEAR_KEY = 4;
        public static final int MESSAGE_CLEAR_KEY_IN_DISK = 5;

        @Override
        protected Void doInBackground(Object... params) {
            switch ((Integer) params[0]) {
                case MESSAGE_CLEAR:
                    BitmapLoader.this.clearCacheInternalInBackgroud();
                    break;
                case MESSAGE_CLOSE:
                    BitmapLoader.this.closeCacheInternalInBackgroud();
                    break;
                case MESSAGE_CLEAR_DISK:
                    BitmapLoader.this.clearDiskCacheInBackgroud();
                    break;
                case MESSAGE_CLEAR_KEY:
                    BitmapLoader.this.clearCacheInBackgroud(String.valueOf(params[1]));
                    break;
                case MESSAGE_CLEAR_KEY_IN_DISK:
                    BitmapLoader.this.clearDiskCacheInBackgroud(String.valueOf(params[1]));
                    break;
            }
            return null;
        }
    }

    /**
     * bitmap下载显示的线程
     */
    private class BitmapLoadAndDisplayTask extends AsyncTask<Object, Void, Bitmap> {
        private Object data;
        private final WeakReference<View> imageViewReference;
        private final BitmapDisplayConfig displayConfig;
        public Displayer mDisplayer;
        private Downloader mDownloader;

        public BitmapLoadAndDisplayTask(View imageView, BitmapDisplayConfig config, Displayer displayer, Downloader downloader) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.displayConfig = config;
            this.mDisplayer = displayer;
            this.mDownloader = downloader;
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            this.data = params[0];
            final String dataString = String.valueOf(this.data);
            Bitmap bitmap = null;

            synchronized (BitmapLoader.this.mPauseWorkLock) {
                while (BitmapLoader.this.mPauseWork && !this.isCancelled()) {
                    try {
                        BitmapLoader.this.mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

            if (!this.isCancelled() && this.getAttachedImageView() != null && !BitmapLoader.this.mExitTasksEarly) {
                bitmap = BitmapLoader.this.processBitmap(dataString, this.displayConfig, mDownloader);
            }

            if (bitmap != null && BitmapLoader.this.mImageCache != null) {
                BitmapLoader.this.mImageCache.addToMemoryCache(dataString, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (this.isCancelled() || BitmapLoader.this.mExitTasksEarly) {
                bitmap = null;
            }

            // 判断线程和当前的imageview是否是匹配
            final View imageView = this.getAttachedImageView();
            if (imageView != null) {
                imageView.setTag(null);
            }
            if (bitmap != null && imageView != null) {
                if (mDisplayer != null) {
                    mDisplayer.loadCompletedDisplay(imageView, bitmap, this.displayConfig, BitmapLoader.this.mConfig.animationEnable);
                } else {
                    SimpleDisplayer.setBitmap(imageView, bitmap);
                }
            } else if (bitmap == null && imageView != null) {
                if (mDisplayer != null) {
                    mDisplayer.loadFailDisplay(imageView, this.displayConfig.getLoadFailBitmap());
                } else {
                    SimpleDisplayer.setBitmap(imageView, this.displayConfig.getLoadFailBitmap());
                }
            }
        }

        @SuppressLint("NewApi")
        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
            synchronized (BitmapLoader.this.mPauseWorkLock) {
                BitmapLoader.this.mPauseWorkLock.notifyAll();
            }
        }

        /**
         * 获取线程匹配的imageView,防止出现闪动的现象
         */
        private View getAttachedImageView() {
            final View imageView = this.imageViewReference.get();
            final BitmapLoadAndDisplayTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }
    }

    private class BitmapConfig {
        public String cachePath;
        public Displayer displayer;
        public Downloader downloader;
        public BitmapDisplayConfig defaultDisplayConfig;
        public float memCacheSizePercent;// 缓存百分比，android系统分配给每个apk内存的大小
        public int memCacheSize;// 内存缓存百分比
        public int diskCacheSize;// 磁盘百分比
        public int poolSize = 3;// 默认的线程池线程并发数量
        public boolean memoryCacheEnable = BitmapCache.DEFAULT_MEM_CACHE_ENABLED;
        public boolean diskCacheEnabled = BitmapCache.DEFAULT_DISK_CACHE_ENABLED;
        public boolean recycleImmediately = BitmapCache.DEFAULT_RECYCLE_IMMEDIATELY;// 是否立即回收内存
        public boolean animationEnable = true;
        public boolean useThreadExecutor = true;// 是否使用自定义线程池：默认不使用自定义线程池，使用AsyncTask自己的线程池

        public BitmapConfig(Context context) {
            this.defaultDisplayConfig = new BitmapDisplayConfig();

            this.defaultDisplayConfig.setAnimation(null);
            this.defaultDisplayConfig.setAnimationType(BitmapDisplayConfig.AnimationType.fadeIn);

            // 设置图片的显示最大尺寸（为屏幕的大小,默认为屏幕宽度的1/2）
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int defaultWidth = (int) Math.floor(displayMetrics.widthPixels / 2);
            this.defaultDisplayConfig.setBitmapHeight(defaultWidth);
            this.defaultDisplayConfig.setBitmapWidth(defaultWidth);
        }
    }
}
