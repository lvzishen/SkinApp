package com.goodmorning;


import com.goodmorning.config.GlobalConfig;

public class AppConfig {

    public static final boolean DEBUG = GlobalConfig.DEBUG;
//    public static final boolean CRASH_PROTECTION = true;
//
//    /**
//     * 是否支持apk渠道
//     */
//    public static final boolean SUPPORT_APK_CHANNEL = false;//打apk渠道时，把它值为true
//    /**
//     * 支持launcher 移出系统分区
//     */
//    public static final boolean SUPPORT_LAUNCHER_TRANSFER = false;
    /**
     * productid
     */
    public static final short UIVERSION = 30720;//GP30720    APK30721    PRE30722
    public static final String ACCOUNT_CONFIG_APP_ID = "100010000";//TODO 账号替换 AccountReplace

    /** 透明虚拟按键 */
//    public static final int FLAG_TRANSLUCENT_NAVIGATION = 134217728;
//    /** 透明通知栏 */
//    public static final int FLAG_TRANSLUCENT_STATUS = 67108864;
//    public static final String PACKAGE_NAME = "com.guardian.security.ever";
//    public static final String APUS_PACKAGENAME = "com.creativeindia.goodmorning";
//
//    public static final String MOBV_APP_ID = "21761";
//    public static final String MOBV_APP_KEY = "aed7249ce5d328987f5d0c59433f9987";
//    public static final String MOBV_UNIT_APP_WALL = "78";
//    public static final String MOBV_FACEBOOK_PLACEMENT_ID_APP_WALL = "601220576685544_601422806665321";
//    public static final String MOBV_UNIT_CLEAN_RESULT_NATIVE = "373";
//    public static final String MOBV_FACEBOOK_PLACEMENT_ID_CLEAN_RESULT_NATIVE = "601220576685544_642814772526124";
//
//    public static final String MOBV_UNIT_CPU_COOLER_NATIVE = "374";
//    public static final String MOBV_FACEBOOK_PLACEMENT_ID_CPU_COOLER_NATIVE = "601220576685544_647635842044017";
//
//	private static final int CLOUD_TAG_BASE = 100;
//    public static final int DOWNLOAD_TAG_UPDATE_INFO = CLOUD_TAG_BASE + 1;
//
//
//    /** 攻防用的 jar 包的升级文件名 */
//    public static final String RECOVERY_PREFIX = "APRCVR";
//    /** 攻防用的 jar 包的文件名，故意用 byte [] 表示，避免反编译直接看出来 */
//    public static final byte[] RECOVERY_JAR = { 114, 101, 99, 111, 118, 101, 114, 121, 46, 106, 97, 114 };
//    /** 攻防的入口类名 */
//    public static final String RECOVERY_MAIN_CLASS = "MainApp";
//    /**定期显示升级通知栏的时间间隔 设置亮屏定期显示升级通知栏的时间间隔，默认为15天。<0:代表不启用该功能。0：代表没有时间间隔 。单位为毫秒*/
//    public static final long REGULAR_SHOW_UPGRADE_NOTIFY_INTERVAL = -1;
}
