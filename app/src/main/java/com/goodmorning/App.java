package com.goodmorning;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.OverallBinderProxy;
import com.ads.lib.init.MoPubStarkInit;
import com.ads.lib.mediation.config.IAllowLoaderAdListener;
import com.ads.lib.splash.SplashModule;
import com.ads.lib.util.AdSwitchUtil;
import com.baselib.BaseModule;
import com.baselib.LocaleUtils;
import com.baselib.cloud.CloudPropertyManager;
import com.baselib.language.LanguageType;
import com.baselib.language.LanguageUtil;
import com.baselib.sp.SharedPref;
import com.baselib.statistic.StatisticLogger;
import com.baselib.statistic.StatisticLoggerX;
import com.baselib.ui.CommonConstants;
import com.baselib.utils.ModuleConfig;
import com.clean.binder.mgr.BinderManager;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.xal.NeptuneReporter;
import com.goodmorning.xal.StatisticSettingCollectorForXAL;
import com.cleanerapp.supermanager.BuildConfig;
import com.cleanerapp.supermanager.R;
import com.lachesis.common.AlexListener;
import com.lachesis.common.LachesisBuilder;
import com.lachesis.common.RemoteConfigSupplier;
import com.lachesis.daemon.LachesisDaemonSDK;
import com.lachesis.gcm.daemon.GcmDaemon;
import com.lachesis.model.AccountLachesisDaemon;
import com.lachesis.module.jobscheduler.daemon.JobSchedulerDaemon;
import com.mob.MobSDK;
import com.nox.Nox;
import com.nox.update.NeptuneDownloader;
import com.vision.lib.ScenesSdk;
import com.vision.lib.common.logger.ILogger;
import com.w.sdk.push.DefaultPushExtension;
import com.w.sdk.push.IConfiguration;
import com.w.sdk.push.PushSdk;
import com.w.sdk.push.model.PushMessage;
import com.w.sdk.push.model.PushMessageBody;

import org.adoto.xrg.AdotoActivateConfigBuilder;
import org.adoto.xrg.AdotoActivateSDK;
import org.alex.analytics.Alex;
import org.alex.analytics.AlexConfigBuilder;
import org.cloud.library.AbstractCloudConfig;
import org.cloud.library.Cloud;
import org.hera.crash.BaseCollector;
import org.hera.crash.HeraCrash;
import org.hera.crash.HeraCrashConfig;
import org.hera.crash.HeraStore;
import org.interlaken.common.XalContext;
import org.interlaken.common.utils.ParamUtils;
import org.interlaken.common.utils.ProcessUtil;
import org.thanos.ThanosSDK;
import org.thanos.core.MorningDataAPI;
import org.thanos.core.internal.ThanosDataCore;
import org.thanos.push.ThanosPush;
import org.thanos.utils.Utils;
import org.xal.config.NoxInitConfig;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import bolts.Continuation;
import bolts.Task;

public class App extends Application {
    private final static boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "App";
    private Context mContext;
    private String mCurrProcessName;
    public static Context sContext;
    public static App app;
    private static final int FLAG_PROCESS_UI = 0x1;
    private static final int FLAG_PROCESS_BACKGROUND = 0x2;
    private static final int FLAG_PROCESS_CRASH = 0x40;

    private static final int FLAG_UNKNOWN = 0x10000000;
    private static int mFlag = FLAG_UNKNOWN;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        if (DEBUG) {
            Log.i(TAG, "onCreate: ");
//            Debug.startMethodTracing("/sdcard/trace.trace");
        }
        XalContext.install(this, BuildConfig.VERSION_CODE, BuildConfig.VERSION_FULL, true, BuildConfig.PERSIST_PROCESS_NAME, R.string.app_name, R.drawable.ic_launcher1);

        mCurrProcessName = ProcessUtil.getCurrentProcessName();
        mFlag = getFlag(context, mCurrProcessName);
        if (HeraCrash.isCrashGuardProcess()) {
            // 崩溃进程，不处理相关的业务逻辑初始化，所以直接return
            return;
        }
        //为保证跨进程打点的正常使用，请在Application 的attachBaseContext中执行Alex.install()方法。
        BinderManager.setBinderManagerProxy(new OverallBinderProxy(this));

    }

    private static int getFlag(Context cxt, String procName) {
        if (procName.equals(cxt.getPackageName()))
            return FLAG_PROCESS_UI;
        if (procName.equals(cxt.getPackageName() + ".background")) {
            return FLAG_PROCESS_BACKGROUND;
        }

        if ("org.hera.crash".equals(procName)) {
            return FLAG_PROCESS_CRASH;
        }
        return FLAG_UNKNOWN;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        app = this;
        if (HeraCrash.isCrashGuardProcess()) {
            // 崩溃进程，不处理相关的业务逻辑初始化，所以直接return
            return;
        }

        mContext = this;
//        LocaleUtils.LocaleBean _UserLocale = LocaleUtils.getUserLocale(this);
//        LocaleUtils.updateLocale(this, _UserLocale);
        /**
         * 对于7.0以下，需要在Application创建的时候进行语言切换
         */
        String language = SharedPref.getString(sContext, SharedPref.LANGUAGE, LanguageType.ENGLISH.getLanguage());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            LanguageUtil.changeAppLanguage(sContext, language);
        }
        setProcessWebViewPath();
        MoPubStarkInit.getInstance().setAllowLoaderAdListener(new IAllowLoaderAdListener() {
            @Override
            public boolean isAllowLoaderAd(String unitId, String adpositionId) {
                return AdSwitchUtil.isOpen(mContext);
            }
        });
        MoPubStarkInit.getInstance().init(this, "333be164417b41a1b8a1e21382ccacd8");
        if (matchProcess(FLAG_PROCESS_BACKGROUND)) {
            BinderManager.setIsHostProcess(true);
        }

        //SDK初始化
//        FileAdSDK.init(this, mContext);

        initCrash();
        initAlex();
        initNeptunePlus();
        initLachesis(mCurrProcessName);
        BinderManager.bindBinderServiceIfNecessary(mContext);
        //todo
        //Statistics.setSettingCollector(new StatisticSettingCollector());
        ParamUtils.initParms(String.valueOf(AppConfig.UIVERSION));

        GlobalConfig.init(getApplicationContext());
        BaseModule.init(new BaseModule.ExternalConfig() {
            @Override
            public String[] listFileX(File file) {
                try {
                    return file.list();//OS.listFile(mContext, file);
                } catch (Exception e) {
                    if (DEBUG) {
                        Log.e(TAG, "listFileX: ", e);
                    }
                }
                return new String[0];
            }
        });
        //ShareSDK
        MobSDK.init(this);

        ModuleConfig.VERSION_NAME = com.cleanerapp.supermanager.BuildConfig.VERSION_FULL;
        new SplashModule().init(getApplicationContext(), MainActivity.class);

        initAdSDK(mContext);

        initXALApkUpdate();

        if (matchProcess(FLAG_PROCESS_BACKGROUND)) {
            new Thread() {
                @Override
                public void run() {
                    //初始化，必须在子线程中处理，有读hot取文件操作
                    ScenesSdk.init(sContext, new ILogger() {
                        @Override
                        public void logEvent(int eventId, Bundle bundle) {
                            Alex.newLogger("scenes_sdk").logEvent(eventId, bundle);
                        }
                    });
                }
            }.start();
        }

        MorningDataAPI.init(new MorningDataAPI.IThanosCoreConfig() {

            @Override
            public int getProductID() {
                return AppConfig.UIVERSION;
            }

            @Override
            public String getNewsCountry() {
                if (DEBUG) {
                    Properties testProp = ThanosDataCore.getTestProp();
                    String newsCountry = testProp.getProperty("newsCountry", null);
                    if (!TextUtils.isEmpty(newsCountry)) {
                        if (DEBUG) {
                            Log.i(TAG, "getNewsCountry: Use " + newsCountry + " for TEST");
                        }
                        return newsCountry;
                    }
                }
                return Utils.getNewsCountry(XalContext.getContext());
            }

            @Override
            public String getLang() {
                return Utils.getLang(XalContext.getContext());
            }

            @Override
            public String getServerUrlHost() {
                return "http://test.feed.subcdn.com";
            }
        });

        initThanos();
        // 只在常驻进程初始化PUSH
        if (XalContext.isPersistProcess()) {
            initPush();
        }
    }

    private void setProcessWebViewPath() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (FLAG_PROCESS_UI != getFlag(this, mCurrProcessName)) {
                WebView.setDataDirectorySuffix(setPath(processName, mCurrProcessName));
            }
        }
    }

    private String setPath(String process, String path) {
        if (!TextUtils.isEmpty(process) && !TextUtils.isEmpty(path)) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(process);
            stringBuffer.append(path);
            return stringBuffer.toString();
        }
        return "";
    }

    private void initPush() {
        try {
            PushSdk.newBuilder(this).initConfiguration(new IConfiguration() {
                @Override
                public String getAppId() {
                    return "100237";
                }

                @Override
                public String getPid() {
                    return AppConfig.UIVERSION + "";
                }

                @Override
                public String getHost() {
                    return "http://push.hotvideo360.com/v1/";
                }
            }).build();

            PushSdk.registerPushExtensions("245", new DefaultPushExtension() {
                protected boolean intercept(Context context, PushMessage message, PushMessageBody messageBody) {
                    if (ThanosSDK.isAllowShowNews(getApplicationContext())) {
                        if (!ThanosPush.handlePushMessage(context, message)) {
                            Log.i(TAG, "intercept: 产品自行处理PUSH消息");
                        } else {
                            Log.i(TAG, "intercept: 产品不需要处理PUSH消息");
                        }
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "initPush: ", e);
        }
    }

    private void initThanos() {


        ThanosSDK.init(this, new ThanosSDK.IThanosSDKConfig() {
            @Override
            public boolean openDeepLink(String s) {
                return false;
            }

            @Override
            public int getAppLogoResId() {
                return R.drawable.ic_launcher1;
            }

            @Override
            public int getPushNotificationDefaultBigImageResId() {
                return R.drawable.ic_launcher1;
            }

            @Override
            public int getPushNotificationSmallIconResId() {
                return R.drawable.ic_launcher1;
            }

            @Override
            public Class<? extends Activity> getMainActivityClass() {
                return MainActivity.class;
            }

            @Override
            public int getProductID() {
                return AppConfig.UIVERSION;
            }

            @Override
            public String getServerUrlHost() {
                return "http://test.feed.subcdn.com";
            }
        });
    }


    /**
     * 当前进程是否flag
     *
     * @param flag
     * @return
     */
    private static boolean matchProcess(int flag) {
        if ((flag & mFlag) != 0)
            return true;
        return false;
    }


    private void initAlex() {
        Alex.init(this, XALConfigBuidler.class);
        try {
            Alex.registerCallBack(new Alex.InitCallback() {
                @Override
                public void onCollectStatus(Bundle parameters) {
                    //处理状态打点
                    StatisticSettingCollectorForXAL.onCollectData(getApplicationContext(), parameters);
                }
            });
        } catch (Exception e) {

        }
    }

    private void initCrash() {
        // 崩溃保护
        HeraCrashConfig config = new HeraCrashConfig() {
            @Override
            protected String onCreateCrashUrlHost() {
                if (DEBUG) {//"http://test.u2.api.apuscn.com/v6/c/u"
                    return CloudPropertyManager.getString(getApplicationContext(), "booster_profile.prop",
                            "cloud.crash", "crash.hotvideo360.com");
                }
                return "crash.hotvideo360.com";
            }
        };

//        HeraCrashConfig config = new HeraCrashConfig(this, getString(R.string.app_name), BuildConfig.VERSION_FULL) {
//
//            @Override
//            protected String onCreateCrashUrlHost() {
//                return GlobalProp.getInstance(mContext).getCrashServerHost2();
//            }
//
//            @Override
//            public String getClientId() {
//                return RegistrationUtil.getClientId(mContext);
//            }
//
//            @Override
//            public String getChannelId() {
//                return RegistrationUtil.getChannelId(mContext);
//            }
//
//            @Override
//            protected IStatisticsListener createStatisticListener() {
//                return new IStatisticsListener() {
//                    @Override
//                    public void logEvent(int event, String mode, Bundle bundle) {
//                        // 在这里调用XAL打点上报崩溃次数
//                        StatisticLogger.logForCrashReport(event, mode, bundle);
//                    }
//                };
//            }
//
//            @Override
//            public String getCurrentProcessName() {
//                return mCurrProcessName;
//            }
//        };

        config.addCustomCollector(new BaseCollector() {
            @Override
            public InterceptorResult onPreHandle(Thread thread, Throwable throwable) {
                final Looper looper = Looper.myLooper();
                if (looper != Looper.getMainLooper()) {
                    final String message = throwable.getMessage();
                    if (message != null && message.contains("Results have already been set")) {
                        final StackTraceElement[] elements = thread.getStackTrace(); // always not null
                        if (elements.length > 0) {
                            final String trace = elements[0].toString();
                            if (trace.contains("com.google.android.gms")) {
                                if (DEBUG) {
                                    Log.w(TAG, "we ignore a gms throwable: " + throwable);
                                }
                                debugReport(throwable);
                                return InterceptorResult.SKIP;
                            }
                        }
                    }
                }
                return InterceptorResult.CONTINUE;
            }

            private void debugReport(Throwable throwable) {
                //TODO 用打点统计该信息
                StatisticLoggerX.logShow("Results Crash", "", "google gms");
                // 用静默上报上传信息
                HeraCrash.silentUploadThrowable(throwable);
            }

            @Override
            public void onHandle(HeraStore heraStore, Thread thread, Throwable throwable) {
            }
        });

        HeraCrash.init(this, config);

    }

    public static class XALConfigBuidler extends AlexConfigBuilder {
        @Override
        public String getClientID() {
            return XalContext.getClientId();
        }

        @Override
        public String getOldClientID() {
            // 由产品接入方提供老的TLV 协议的客户端Id，用于做兼容。
            // 没有时可以传null 或“”。
            return XalContext.getOldClientId();
        }

        @Override
        public String getChannel() {
            // 由产品接入方提供渠道号。
            return XalContext.getChannelId();
        }

        @Override
        public List<String> getTags() {
            return XalContext.getTags();
        }

        @Override
        public String getAdvertisementServerUrl() {
            if (DEBUG) {//"http://test.u2.api.apuscn.com/v6/c/u"
                if (XalContext.getApplicationContext() != null) {
                    String adServerUrl = CloudPropertyManager.getString(XalContext.getApplicationContext(), "booster_profile.prop",
                            "alex.ad", "https://sbiz.hotvideo360.com/v5/s/w");
                    if (DEBUG) {
                        Log.d(TAG, ": " + adServerUrl);
                    }
                    return adServerUrl;
                }

            }
            return "https://sbiz.hotvideo360.com/v5/s/w"; //上传广告打点的地址
        }

        @Override
        public String getServerUrl() {
            if (DEBUG) {//"http://test.u2.api.apuscn.com/v6/c/u"
                if (XalContext.getApplicationContext() != null) {
                    String serverUrl = CloudPropertyManager.getString(XalContext.getApplicationContext(), "booster_profile.prop",
                            "alex.server", "https://s.hotvideo360.com/v5/r/w");
                    if (DEBUG) {
                        Log.d(TAG, ": " + serverUrl);
                    }
                    return serverUrl;
                }
            }
            return "https://s.hotvideo360.com/v5/r/w";//上传产品打点的地址
        }

    }

    private void initNeptunePlus() {
        Cloud.registerCloudAttributeModule(
                "Trade_Locker_WM",
                "Trade_NotifyBox",
                "Trade_App_Lock",
                "g_trade_splash_v2",
                "Trade_CleanAppV1",
                "trade_recommend",
                "trade_Inters",
                "Trade_SkConfig",
                "trade_score",
                "g_trade_one_tap_clean",
                "g_trade_cleaner_app_v3",
                "g_trade_autoopt",
                "x_odin",
                "thanos_content_sdk"
        );
        NeptuneReporter neptuneReporter = new NeptuneReporter(this);
        AdotoActivateSDK.setAdotoActivateListener(neptuneReporter);
        Cloud.registerAttributeUpdateListener(neptuneReporter);
        Cloud.registerFileUpdateListener(neptuneReporter);
        AdotoActivateSDK.init(new AdotoActivateConfigBuilder() {
            @Override
            public String getUserTagServerHost() {//用户标签的接口
                if (DEBUG) {//"http://test.u2.api.apuscn.com/v6/c/u"
                    return CloudPropertyManager.getString(getApplicationContext(), "booster_profile.prop",
                            "cloud.user.tag", "http://u.hotvideo360.com");
                }
                return "http://u.hotvideo360.com";//r.hotvideo360.com/v2/r/ra
            }

            @Override
            public String getActivateServerHost() {//激活的接口
                if (DEBUG) {//"http://test.u2.api.apuscn.com/v6/c/u"
                    return CloudPropertyManager.getString(getApplicationContext(), "booster_profile.prop",
                            "cloud.active", "http://r.hotvideo360.com");
                }
                return "http://r.hotvideo360.com";
            }
        });


//        Cloud.Companion.init();
        Cloud.init(new AbstractCloudConfig() {//superappmanager.com/v6/c/u
            @Override
            public String getAttributeSyncUrl() {
                if (DEBUG) {//"http://test.u2.api.apuscn.com/v6/c/u"
                    return CloudPropertyManager.getString(getApplicationContext(), "booster_profile.prop",
                            "cloud.att.url", "http://u.hotvideo360.com/v6/c/u");
                }
                return "http://u.hotvideo360.com/v6/c/u";
            }

            @Override
            public String getFileSyncUrl() {
                if (DEBUG) {//"http://test.u2.api.apuscn.com/v6/c/u"
                    return CloudPropertyManager.getString(getApplicationContext(), "booster_profile.prop",
                            "cloud.file.url", "http://u.hotvideo360.com/v6/f/u");
                }
                return "http://u.hotvideo360.com/v6/f/u";
            }
        });


        Task.delay(500).onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) {
                initXALApkUpdate();
                return null;
            }
        }, Task.BACKGROUND_EXECUTOR);
    }


    private void initXALApkUpdate() {
        Nox.init(this, new NoxInitConfig() {
            @Override
            public int getNotificationIconRes() {
                return R.drawable.ic_launcher1; //显示升级通知栏，需要显示的icon图标
            }

            @Override
            public String getOfficialUrl() {
                return null;
            }

            @Override
            protected NeptuneDownloader createDownloader() {
                return null;
            }


            @Override
            public boolean isCurrentCanShowDialog(Context context) {
                boolean res = false;
                if (ProcessUtil.getCurrentProcessName().equals(context.getPackageName())) {
                    res = SharedPref.getBoolean(getApplicationContext(), CommonConstants.KEY_HAS_AGREEMENT_SPLASH, false);
                }
                return res;
            }
        });
    }

    private void initLachesis(String processName) {

//        if (processName != null && processName.equals(getPackageName())) {
        LachesisDaemonSDK.setMainProcessName(processName);

        LachesisDaemonSDK.setAlexListener(new AlexListener() {
            @Override
            public void log(int i, Bundle bundle) {
                StatisticLogger.getLogger().logEvent(i, bundle);
            }
        });
        LachesisDaemonSDK.setRemoteConfigSupplier(new RemoteConfigSupplier.SimpleSupplier() {
            @Override
            public boolean isLogEnabled(String key) {

                int value = CloudPropertyManager.getInt(mContext, CloudPropertyManager.PATH_RUBBISH, "daemon.enable", 1);
                //NeptuneRemoteConfig.getInt(key, 1);
                if (DEBUG) {
                    Log.d(TAG, "isLogEnabled: " + value);
                }
                return value == 1;
            }
        });
        LachesisBuilder builder = new LachesisBuilder()
//                .addKeepLiveService(BackgroundService.class.getName())
//                .addKeepLiveService(MainService.class.getName())
                .addDaemon(new JobSchedulerDaemon.Builder())
                .addDaemon(new AccountLachesisDaemon.Builder())
                .addDaemon(new GcmDaemon.Builder());
        LachesisDaemonSDK.startLachesisDaemon(this, builder);

        //主进程中调用，尽量在靠前的地方开启保活
//        LachesisDaemonSDK.startAllLachesisDaemon(this);
//        }
    }

    private void initAdSDK(final Context context) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtils.LocaleBean _UserLocale = LocaleUtils.getUserLocale(this);
        //系统语言改变了应用保持之前设置的语言
        if (_UserLocale != null) {
            Locale.setDefault(_UserLocale.locale);
            Configuration _Configuration = new Configuration(newConfig);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(_UserLocale.locale);
            } else {
                _Configuration.locale = _UserLocale.locale;
            }
            getResources().updateConfiguration(_Configuration, getResources().getDisplayMetrics());
        }

    }

}
