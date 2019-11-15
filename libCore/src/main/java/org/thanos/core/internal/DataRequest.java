package org.thanos.core.internal;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.interlaken.common.utils.PackageInfoUtil;
import org.interlaken.common.utils.ProcessUtil;
import org.thanos.core.ResultCallback;
import org.thanos.core.bean.ResponseData;
import org.thanos.core.data.GoodMorningBaseParser;
import org.thanos.core.data.GoodMorningRequest;
import org.thanos.core.internal.requestparam.BaseRequestParam;
import org.zeus.ZeusNetworkCallback;
import org.zeus.ZeusNetworkLayer;
import org.zeus.ZeusRequestResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.CRC32;

import static org.thanos.core.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-15.
 */
class DataRequest {
    private static final String CACHE_FILE_CHARSET_NAME = "utf8";
    private static final String TAG = MorningDataCore.LOG_PREFIX + "DataRequest";
    /**
     * 更新本地缓存的任务，在一个单线程队列里顺序执行
     */
    private static ExecutorService cacheUpdateThread = Executors.newSingleThreadExecutor();

    static <T extends ResponseData> void request(Context context, GoodMorningRequest request, final GoodMorningBaseParser<T> parser, final ResultCallback<T> callback) {
        if (DEBUG) {
            Log.d(TAG, "request() called with: context = [" + context + "], request = [" + request + "], parser = [" + parser + "], callback = [" + callback + "]");
        }
        String serverUrl = request.getServerUrl();
        File cacheFile = null;
        BaseRequestParam baseRequestParam = request.getBaseRequestParam();
        boolean acceptCache = baseRequestParam.acceptCache;
        boolean cacheResponse = baseRequestParam.cacheResponse;
        if (acceptCache || cacheResponse) {
            String cacheKey = createCacheKey(serverUrl, request);
            cacheFile = getCacheFile(context, cacheKey);
            if (acceptCache && cacheFile.exists()) {
                // 缓存有效时间优先使用请求参数里，其次使用全局的
                long cacheTimeMills = (baseRequestParam.cacheLiveTimeInSeconds > 0 ? baseRequestParam.cacheLiveTimeInSeconds : MorningDataCore.getThanosCoreConfig().getCacheTimeSeconds()) * 1000;
                if (cacheTimeMills > 0) {
                    if (cacheKey != null) {
                        long lastCacheTime = getLastCacheTime(cacheFile);
                        boolean cacheInvalid = System.currentTimeMillis() - lastCacheTime < cacheTimeMills;
                        parser.setRequest(request);
                        try {
                            ZeusRequestResult<T> cacheResult = parser.parseFromCache(fileToString(cacheFile));
                            if (cacheResult.obj != null) {
                                cacheResult.obj.fromCache = true;
                                if (DEBUG) {
                                    Log.i(TAG, "request: 返回本地缓存数据, " + serverUrl);
                                }
                                if (cacheInvalid) {
                                    // 本地缓存有效，就不继续请求网络了
                                    callback.onSuccess(cacheResult.obj);
                                    return;
                                } else {
                                    // 本地缓存无效，但是先把缓存数据吐出去
                                    callback.onLoadFromCache(cacheResult.obj);
                                }
                            }
                        } catch (Exception e) {
                            if (DEBUG) {
                                Log.e(TAG, "request: ", e);
                            }
                        }
                    }
                }
            }
        }

        if (DEBUG) {
            Log.i(TAG, "request: 走网络请求，" + serverUrl);
        }
        ZeusNetworkLayer<T> networkLayer = new ZeusNetworkLayer<T>(context, request, new ParserProxy<T>(context, parser, cacheFile));
        networkLayer.asyncExecute(new ZeusNetworkCallback<T>() {
            @Override
            public void onFinished(ZeusRequestResult<T> zeusRequestResult) {
                //添加判空处理。
                T data = zeusRequestResult.obj;
                if (data != null) {
                    callback.onSuccess(data);
                } else {
                    callback.onFail(new RuntimeException());
                }
            }

            @Override
            public void onFail(Exception e) {
                callback.onFail(e);
            }
        });
    }

    private static @Nullable
    String createCacheKey(String serverUrl, GoodMorningRequest request) {
        return serverUrl + request.getBaseRequestParam().getCacheKey();
    }

    private static String fileToString(File cacheFile) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(cacheFile);
            return IOUtils.toString(fis, "utf8");
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "fileToString: ", e);
            }
        } finally {
            if (fis != null) {
                IOUtils.closeQuietly(fis);
            }
        }
        return null;
    }

    /**
     * 获取指定URL请求的本地缓存最新数据
     *
     * @return 毫秒
     */
    private static long getLastCacheTime(File cacheFile) {
        if (cacheFile.exists()) {
            return cacheFile.lastModified();
        }
        return 0;
    }

    private static File getCacheFile(Context context, String cacheKey) {
        File cacheDir = getCacheFileDir(context);

        long value = crc32(cacheKey);
        return new File(cacheDir, String.valueOf(value));
    }

    private static long crc32(String serverUrl) {
        CRC32 crc32 = new CRC32();
        crc32.update(serverUrl.getBytes());
        long value = crc32.getValue();
        if (value < 0) {// 转成正数
            value = value + Integer.MAX_VALUE;
        }
        return value;
    }

    public static List<File> getAllCacheFileDir(Context context) {
        List<File> cacheFiles = new ArrayList<>();
        File cacheDir = context.getCacheDir();
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(PackageInfoUtil.getSelfFirstInstallTime(context) + "_")) {
                    cacheFiles.add(file);
                }
            }
        }
        return cacheFiles;
    }

    public static File getCacheFileDir(Context context) {
        // 每个进程的缓存目录不同
        File cacheDir = new File(context.getCacheDir(), PackageInfoUtil.getSelfFirstInstallTime(context) + "_" + crc32(ProcessUtil.getCurrentProcessName()));
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    static void cleanCacheFileIfNeed(Context context) {
        File cacheFileDir = getCacheFileDir(context);
        File[] files = cacheFileDir.listFiles();
        if (files != null) {
            long totalLength = 0;
            for (File file : files) {
                totalLength += file.length();
            }
            long localCacheStorageMaxBytes = MorningDataCore.getThanosCoreConfig().getLocalCacheStorageMaxBytes();
            if (DEBUG) {
                Log.i(TAG, "cleanCacheFileIfNeed: " + totalLength + "/" + localCacheStorageMaxBytes);
            }
            if (totalLength > localCacheStorageMaxBytes) {
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        long t1 = o1.lastModified();
                        long t2 = o2.lastModified();
                        if (t1 == t2) {
                            return 0;
                        } else if (t1 < t2) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                for (File file : files) {
                    long length = file.length();
                    if (DEBUG) {
                        Log.i(TAG, "cleanCacheFileIfNeed: Delete " + file.getAbsolutePath());
                    }
                    if (file.delete()) {
                        if (DEBUG) {
                            Log.i(TAG, "cleanCacheFileIfNeed: Delete " + file.getAbsolutePath() + " success, release " + length + " bytes");
                        }
                        totalLength -= length;
                        if (totalLength < localCacheStorageMaxBytes) {
                            break;
                        }
                    } else {
                        if (DEBUG) {
                            Log.e(TAG, "cleanCacheFileIfNeed: Delete " + file.getAbsolutePath() + " FAIL");
                        }
                    }
                }
            }
        }
    }

    static class ParserProxy<T extends ResponseData> extends GoodMorningBaseParser<T> {

        private Context context;
        private File cacheFile;
        private String body;

        public ParserProxy(Context context, GoodMorningBaseParser<T> parser, File cacheFile) {
            super(parser.getResponseDataItemClazz());
            this.context = context;
            this.cacheFile = cacheFile;
        }

        @Override
        protected void beforeDecrypt(String body) {
            this.body = body;
            super.beforeDecrypt(body);
        }

        @Override
        protected void afterParse(ZeusRequestResult<T> result) {
            super.afterParse(result);
            if (result != null) {
                T obj = result.obj;
                if (obj != null && obj.needCache() && cacheFile != null) {
                    cacheUpdateThread.submit(new CacheUpdateTask(context, cacheFile, body));
                }
            }
        }
    }

    static class CacheUpdateTask implements Runnable {

        private final File cacheFile;
        private final String body;
        private Context context;

        CacheUpdateTask(Context context, File cacheFile, String body) {
            this.context = context;
            this.cacheFile = cacheFile;
            this.body = body;
        }

        @Override
        public void run() {
            File parentFile = cacheFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(cacheFile);
                fos.write(this.body.getBytes(CACHE_FILE_CHARSET_NAME));
                if (DEBUG) {
                    Log.i(TAG, "更新本地缓存成功, " + this.cacheFile.getAbsolutePath());
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "更新本地缓存失败: " + this.cacheFile.getAbsolutePath(), e);
                }
            } finally {
                if (fos != null) {
                    IOUtils.closeQuietly(fos);
                }
            }
            cleanCacheFileIfNeed(context);
        }
    }
}
