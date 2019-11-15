package org.thanos.core.internal;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.interlaken.common.XalContext;
import org.interlaken.common.utils.FileUtil;
import org.interlaken.common.utils.PackageInfoUtil;
import org.interlaken.common.utils.StringCodeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.thanos.core.BuildConfig;
import org.thanos.core.MorningDataAPI;
import org.thanos.core.ResultCallback;
import org.thanos.core.bean.ChannelList;
import org.thanos.core.bean.ContentDetail;
import org.thanos.core.bean.ContentItem;
import org.thanos.core.bean.ContentList;
import org.thanos.core.bean.ResponseData;
import org.thanos.core.data.GoodMorningBaseParser;
import org.thanos.core.data.GoodMorningRequest;
import org.thanos.core.internal.requestparam.ChannelListRequestParam;
import org.thanos.core.internal.requestparam.ContentDetailRequestParam;
import org.thanos.core.internal.requestparam.ContentListRequestParam;
import org.thanos.core.internal.requestparam.RecommendListRequestParam;
import org.thanos.core.internal.requestparam.UserBehaviorUploadParam;
import org.thanos.core.internal.requestparam.UserFeedbackUploadRequestParam;
import org.thanos.core.utils.ThanosDataCorePrefUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhaobingfeng on 2019-07-11.
 */
public class MorningDataCore {
    public static final boolean DEBUG = BuildConfig.LIBRARY_DEBUG;
    public static final String LOG_PREFIX = "Thanos.Core.";
    private static final String TAG = LOG_PREFIX + "Core";
    private static MorningDataAPI.IThanosCoreConfig sThanosCoreConfig;
    private static AtomicBoolean sInit = new AtomicBoolean(false);

    public static MorningDataAPI.IThanosCoreConfig getThanosCoreConfig() {
        return sThanosCoreConfig;
    }

    /**
     * 接口协议是否加密，默认加密。
     */
    public static boolean isProtocolEncrypt() {
        if (!DEBUG) {
            return true;
        }
        Properties testProp = getTestProp();
        if (testProp != null) {
            try {
                return Boolean.parseBoolean(testProp.getProperty("encrypt", "false"));
            } catch (Exception e) {
                Log.e(TAG, "isEnableEncrypt: ", e);
            }
        }
        return false;
    }

    /**
     * 初始化
     */
    public static void init(MorningDataAPI.IThanosCoreConfig thanosCoreConfig) {
        if (DEBUG) {
            Log.d(TAG, "init() called with: thanosCoreConfig = [" + thanosCoreConfig + "]");
        }
        if (!sInit.compareAndSet(false, true)) {
            if (DEBUG) {
                throw new IllegalStateException("MorningDataAPI.init已经调用过，此次调用无效");
            }
            return;
        }
        sThanosCoreConfig = thanosCoreConfig;
    }

    private static boolean checkHasInit() {
        if (sInit.get()) {
            return true;
        }
        if (DEBUG) {
            throw new IllegalStateException("请求数据之前一定要调用MorningDataAPI.init");
        }
        return false;
    }

    /**
     * 获取频道列表，默认缓存24小时
     */
    public static void requestChannelList(@NonNull final Context context, final ChannelListRequestParam channelListRequestParam, final ResultCallback<ChannelList> callback) {
        if (DEBUG) {
            Log.d(TAG, "requestChannelList() called with: context = [" + context + "], channelListRequestParam = [" + channelListRequestParam + "], callback = [" + callback + "]");
        }
        if (!checkHasInit()) {
            callback.onFail(new IllegalStateException());
            return;
        }

        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                GoodMorningRequest request = new GoodMorningRequest(context, channelListRequestParam, getUrl(CoreCloudConstants.URL_CHANNEL_LIST, UrlConfig.CHANNEL_LIST));
                GoodMorningBaseParser<ChannelList> goodMorningBaseParser = new GoodMorningBaseParser<>(ChannelList.class);
                DataRequest.request(context, request, goodMorningBaseParser, new ResultCallback<ChannelList>() {
                    ChannelList cacheData;

                    @Override
                    public void onSuccess(ChannelList data) {
                        processChannelList(data);
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onLoadFromCache(ChannelList data) {
                        // 本地缓存的数据，先不使用，等网络失败了再用
                        cacheData = data;
                    }

                    private void processChannelList(ChannelList channelList) {
                        // 回来的频道列表有多份时，需要根据语言找出最合适的
                        if (channelList != null && !channelList.langCategoryInfos.isEmpty()) {
                            ChannelList.LangCategoryInfo bestOne = null;
                            if (channelList.langCategoryInfos.size() > 1) {
                                // 当前使用的语言
                                String currentLang = getLang(true);
                                if (TextUtils.isEmpty(currentLang)) {
                                    // 用户没有设置，就使用设备当前语言
                                    currentLang = Locale.getDefault().getLanguage();
                                    if (DEBUG) {
                                        Log.i(TAG, "processChannelList: 用户没有设置语言，使用系统语言 " + currentLang);
                                    }
                                } else if (DEBUG) {
                                    Log.i(TAG, "requestChannelList: 用户设置了语言，" + currentLang);
                                }
                                for (ChannelList.LangCategoryInfo langCategoryInfo : channelList.langCategoryInfos) {
                                    if (!TextUtils.isEmpty(currentLang) && currentLang.equals(langCategoryInfo.lang)) {
                                        // 找到了匹配的语言
                                        bestOne = langCategoryInfo;
                                        if (DEBUG) {
                                            Log.i(TAG, "requestChannelList: 根据语言找到了合适的频道列表: " + langCategoryInfo);
                                        }
                                        break;
                                    }
                                }
                                if (bestOne == null) {
                                    // 没有找到语言匹配的，那就找默认的
                                    for (ChannelList.LangCategoryInfo langCategoryInfo : channelList.langCategoryInfos) {
                                        if (langCategoryInfo.isDefault == 1) {
                                            // 找到了默认的
                                            bestOne = langCategoryInfo;
                                            if (DEBUG) {
                                                Log.i(TAG, "requestChannelList: 根据isDefault是否为1找到了合适的频道列表: " + langCategoryInfo);
                                            }
                                            break;
                                        }
                                    }
                                    if (bestOne == null) {
                                        bestOne = channelList.langCategoryInfos.get(0);
                                        if (DEBUG) {
                                            Log.i(TAG, "processChannelList: 没办法，选第一个作为最终使用的频道列表");
                                        }
                                    }
                                }
                                if (bestOne != null) {
                                    channelList.langCategoryInfos.clear();
                                    channelList.langCategoryInfos.add(bestOne);
                                }
                            } else {
                                bestOne = channelList.langCategoryInfos.get(0);
                            }

                            // 保存国家语言时，语言优先使用匹配后的语言
                            ThanosDataCorePrefUtils.setNewsCountryAndLangByServer(XalContext.getContext(), channelList.newsCountry, bestOne.lang);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        if (this.cacheData != null) {
                            // 如果网络请求失败了，并且有本地缓存数据，那就吐缓存数据
                            if (DEBUG) {
                                Log.i(TAG, "onFail: 网络请求失败，使用本地缓存数据");
                            }
                            onSuccess(this.cacheData);
                            cacheData = null;
                            return;
                        }
                        callback.onFail(e);
                    }
                });
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                Exception error = task.getError();
                if (error != null) {
                    callback.onFail(error);
                }
                return null;
            }
        });
    }

    /**
     * 获取内容列表数据
     */
    public static void requestContentList(final Context context, final ContentListRequestParam newsListRequestParam, final ResultCallback<ContentList> callback) {
        if (DEBUG) {
            Log.d(TAG, "requestContentList() called with: context = [" + context + "], newsListRequestParam = [" + newsListRequestParam + "]");
        }
        if (!checkHasInit()) {
            callback.onFail(new IllegalStateException());
            return;
        }
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                GoodMorningRequest request = new GoodMorningRequest(context, newsListRequestParam, getUrl(CoreCloudConstants.URL_CONTENT_LIST, UrlConfig.CONTENT_LIST));
                GoodMorningBaseParser<ContentList> goodMorningBaseParser = new GoodMorningBaseParser<>(ContentList.class);
                DataRequest.request(context, request, goodMorningBaseParser, callback);
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                Exception error = task.getError();
                if (error != null) {
                    callback.onFail(error);
                }
                return null;
            }
        });
    }

    /**
     * 获取内容详情信息
     */
    public static void requestContentDetail(final Context context, final ContentDetailRequestParam contentDetailRequestParam, final ResultCallback<ContentDetail> callback) {
        if (DEBUG) {
            Log.d(TAG, "requestContentDetail() called with: context = [" + context + "], contentDetailRequestParam = [" + contentDetailRequestParam + "]");
        }
        if (!checkHasInit()) {
            callback.onFail(new IllegalStateException());
            return;
        }

        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                GoodMorningRequest request = new GoodMorningRequest(context, contentDetailRequestParam, getUrl(CoreCloudConstants.URL_CONTENT_DETAILS, UrlConfig.CONTENT_DETAILS));
                GoodMorningBaseParser<ContentDetail> goodMorningBaseParser = new GoodMorningBaseParser<>(ContentDetail.class);
                DataRequest.request(context, request, goodMorningBaseParser, callback);
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                Exception error = task.getError();
                if (error != null) {
                    callback.onFail(error);
                }
                return null;
            }
        });
    }

    /**
     * 获取推荐内容列表
     */
    public static void requestRecommendContentList(final Context context, final RecommendListRequestParam recommendListRequestParam, final ResultCallback<ContentList> callback) {
        if (DEBUG) {
            Log.d(TAG, "requestRecommendContentList() called with: context = [" + context + "], recommendListRequestParam = [" + recommendListRequestParam + "]");
        }
        if (!checkHasInit()) {
            callback.onFail(new IllegalStateException());
            return;
        }
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                GoodMorningRequest request = new GoodMorningRequest(context, recommendListRequestParam, getUrl(CoreCloudConstants.URL_RECOMMEND_LIST, UrlConfig.RECOMMEND_LIST));
                GoodMorningBaseParser<ContentList> goodMorningBaseParser = new GoodMorningBaseParser<>(ContentList.class);
                DataRequest.request(context, request, goodMorningBaseParser, callback);
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                Exception error = task.getError();
                if (error != null) {
                    callback.onFail(error);
                }
                return null;
            }
        });
    }

    /**
     * 上报用户行为
     */
    public static void uploadUserBehavior(final Context context, final UserBehaviorUploadParam userBehaviorUploadParam, final ResultCallback<ResponseData> callback) {
        if (DEBUG) {
            Log.d(TAG, "uploadUserBehavior() called with: context = [" + context + "], userBehaviorUploadParam = [" + userBehaviorUploadParam + "]");
        }

        if (!checkHasInit()) {
            callback.onFail(new IllegalStateException());
            return;
        }
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                GoodMorningRequest request = new GoodMorningRequest(context, userBehaviorUploadParam, getUrl(CoreCloudConstants.URL_USER_BEHAVIOR_UPLOAD, UrlConfig.USER_BEHAVIOR_UPLOAD));
                GoodMorningBaseParser<ResponseData> goodMorningBaseParser = new GoodMorningBaseParser<>(ResponseData.class);
                DataRequest.request(context, request, goodMorningBaseParser, callback);
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                Exception error = task.getError();
                if (error != null) {
                    callback.onFail(error);
                }
                return null;
            }
        });
    }

    /**
     * 上报用户反馈
     */
    public static void uploadUserFeedback(final Context context, final UserFeedbackUploadRequestParam userFeedbackUploadParam, final ResultCallback<ResponseData> callback) {
        if (DEBUG) {
            Log.d(TAG, "uploadUserFeedback() called with: context = [" + context + "], userFeedbackUploadParam = [" + userFeedbackUploadParam + "]");
        }
        if (!checkHasInit()) {
            callback.onFail(new IllegalStateException());
            return;
        }

        // 上报请求
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 本地记录不喜欢这条内容，避免用户再次在缓存数据里看到这条内容
                saveDislikeContent(context, userFeedbackUploadParam.contentItem);


                GoodMorningRequest request = new GoodMorningRequest(context, userFeedbackUploadParam, getUrl(CoreCloudConstants.URL_USER_FEEDBACK_UPLOAD, UrlConfig.USER_FEEDBACK_UPLOAD));
                GoodMorningBaseParser<ResponseData> goodMorningBaseParser = new GoodMorningBaseParser<>(ResponseData.class);
                DataRequest.request(context, request, goodMorningBaseParser, callback);
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                Exception error = task.getError();
                if (error != null) {
                    callback.onFail(error);
                }
                return null;
            }
        });
    }

    public static HashSet<Long> getDislikeContentIds(Context context) {
        HashSet<Long> ids = new HashSet<>();

        File dislikeIdsFile = new File(DataRequest.getCacheFileDir(context), PackageInfoUtil.getSelfFirstInstallTime(context) + "d");
        if (dislikeIdsFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(dislikeIdsFile);
                String fileContent = IOUtils.toString(fis, "utf8");
                JSONArray jsonArray = new JSONArray(fileContent);
                for (int i = 0, len = jsonArray.length(); i < len; i++) {
                    ids.add(jsonArray.getJSONObject(i).getLong("i"));
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "getDislikeContentIds: ", e);
                }
            } finally {
                if (fis != null) {
                    IOUtils.closeQuietly(fis);
                }
            }
        }
        return ids;
    }

    private static void saveDislikeContent(Context context, ContentItem contentItem) {
        if (DEBUG) {
            Log.d(TAG, "saveDislikeContent() called with: context = [" + context + "], contentItem = [" + contentItem + "]");
        }
        long id = contentItem.id;
        File dislikeIdsFile = new File(DataRequest.getCacheFileDir(context), PackageInfoUtil.getSelfFirstInstallTime(context) + "d");
        JSONArray jsonArray = new JSONArray();
        if (dislikeIdsFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(dislikeIdsFile);
                String fileContent = IOUtils.toString(fis, Charset.forName("utf8"));
                if (!TextUtils.isEmpty(fileContent)) {
                    JSONArray tmpJsonArray = new JSONArray(fileContent);
                    // 取最新的500条记录，其余的舍弃
                    int length = tmpJsonArray.length();
                    for (int i = Math.max(0, length - 500); i < length; i++) {
                        jsonArray.put(tmpJsonArray.getJSONObject(i));
                    }
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "saveDislikeContent: ", e);
                }
            } finally {
                if (fis != null) {
                    IOUtils.closeQuietly(fis);
                }
            }
        }
        JSONObject object = new JSONObject();
        FileOutputStream fos = null;
        try {
            object.put("t", System.currentTimeMillis());
            object.put("i", id);
            jsonArray.put(object);

            fos = new FileOutputStream(dislikeIdsFile);
            fos.write(jsonArray.toString().getBytes("utf8"));
            if (DEBUG) {
                Log.i(TAG, "saveDislikeContent: 更新成功, " + dislikeIdsFile.getAbsolutePath());
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "saveDislikeContent: ", e);
            }
        } finally {
            if (fos != null) {
                IOUtils.closeQuietly(fos);
            }
        }
    }

    public static String getLang(boolean onlyAcceptUserChoice) {
        // 优先使用用户选择的
        String userChooseLang = getThanosCoreConfig().getLang();
        if (onlyAcceptUserChoice || !TextUtils.isEmpty(userChooseLang)) {
            return userChooseLang;
        }
        // 用户没有选择，则使用之前服务器返回的
        return ThanosDataCorePrefUtils.getLangByServer(XalContext.getContext());
    }

    /**
     * 获取接口里上传的newsCountry字段值
     *
     * @param onlyAcceptUserChoice 是否只获取用户选择的结果
     */
    public static @Nullable
    String getNewsCountry(boolean onlyAcceptUserChoice) {
        // 优先使用用户选择的国家
        String newsCountry = getThanosCoreConfig().getNewsCountry();
        if (onlyAcceptUserChoice || !TextUtils.isEmpty(newsCountry)) {
            return newsCountry;
        }
        // 其次使用之前服务器返回的国家
        return ThanosDataCorePrefUtils.getNewsCountryByServer(XalContext.getContext());
    }

    public static boolean clearLocalCache(Context context) {
        try {
            List<File> cacheFileDirs = DataRequest.getAllCacheFileDir(context);
            if (DEBUG) {
                Log.i(TAG, "clearLocalCache: " + cacheFileDirs);
            }
            for (File cacheFileDir : cacheFileDirs) {
                FileUtil.deleteDirectory(cacheFileDir);
                if (DEBUG) {
                    Log.i(TAG, "clearLocalCache: " + cacheFileDir.getAbsolutePath() + ", Success");
                }
            }
            return true;
        } catch (IOException e) {
            if (DEBUG) {
                Log.e(TAG, "clearLocalCache: ", e);
            }
            return false;
        }
    }

    /**
     * 获取测试配置
     */
    public static Properties getTestProp() {
        Properties properties = new Properties();
        if (!DEBUG) {
            return properties;
        }
        File propFile = new File(Environment.getExternalStorageDirectory(), "thanos_config.prop");
        if (propFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(propFile);
                properties.load(fis);
                Log.i(TAG, "getTestProp: " + properties);
            } catch (IOException e) {
                Log.e(TAG, "getTestProp: ", e);
            } finally {
                IOUtils.closeQuietly(fis);
            }
        }
        return properties;
    }

    private static String getUrl(String cloudKey, String urlPath) {
        String serverHost = getThanosCoreConfig().getServerUrlHost();
        if (TextUtils.isEmpty(serverHost)) {
            serverHost = StringCodeUtils.decodeString(XalContext.isAPUSBrand() ? UrlConfig.HOST_APUS : UrlConfig.HOST_NON_APUS);
        }
        // 优先云控下发的，没有则使用默认的
        return XalContext.getCloudAttribute(cloudKey, String.format(urlPath, serverHost));
    }

    public static class UrlConfig {
        /**
         * APUS域名
         */
        static final byte[] HOST_APUS = new byte[]{-122, 71, 71, 7, 55, -93, -14, -14, 102, 86, 86, 70, -30, 22, 7, 87, 55, 22, 7, 7, 55, -30, 54, -10, -42};
        /**
         * 非APUS域名
         */
        static final byte[] HOST_NON_APUS = new byte[]{-122, 71, 71, 7, 55, -93, -14, -14, 102, 86, 86, 70, -30, 55, 87, 38, 54, 70, -26, -30, 54, -10, -42};
        /**
         * 频道列表接口地址
         */
        static final String CHANNEL_LIST = "%s/router?method=channel.list";
        /**
         * 内容列表接口地址
         */
        static final String CONTENT_LIST = "%s/router?method=news.list";
        /**
         * 内容详情接口地址
         */
        static final String CONTENT_DETAILS = "%s/router?method=news.detail";
        /**
         * 推荐列表接口
         */
        static final String RECOMMEND_LIST = "%s/router?method=video.recommend.list";
        /**
         * 用户行为上报
         */
        static final String USER_BEHAVIOR_UPLOAD = "%s/router?method=user.behavior";
        /**
         * 用户反馈上报
         */
        static final String USER_FEEDBACK_UPLOAD = "%s/router?method=user.feedback";
    }

    private static class CoreCloudConstants {
        /**
         * 频道列表接口地址
         */
        static final String URL_CHANNEL_LIST = "EHqPLp3";
        /**
         * 内容列表接口地址
         */
        static final String URL_CONTENT_LIST = "bG7pxSB";
        /**
         * 内容详情接口地址
         */
        static final String URL_CONTENT_DETAILS = "QaCnuTa";
        /**
         * 推荐列表接口地址
         */
        static final String URL_RECOMMEND_LIST = "DPNRiHw";
        /**
         * 用户行为上报接口地址
         */
        static final String URL_USER_BEHAVIOR_UPLOAD = "k7ND78";
        /**
         * 用户反馈上报接口地址
         */
        static final String URL_USER_FEEDBACK_UPLOAD = "ZPemjhl";
    }
}
