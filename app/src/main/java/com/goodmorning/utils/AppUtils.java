package com.goodmorning.utils;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baselib.cloud.CloudPropertyManager;
import com.baselib.language.LanguageUtil;
import com.baselib.sp.SharedPref;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.bean.CheckUpdate;
import com.goodmorning.ui.activity.SettingActivity;
import com.w.sdk.push.PushBindManager;

import org.thanos.netcore.helper.JsonHelper;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class AppUtils {
    /**
     * 切换主题
     * @param activity
     */
    public static void changeTheme(Activity activity){
        boolean isSwitchTheme = SharedPref.getBoolean(getApplicationContext(),SharedPref.IS_SWITCH_THEME,false);
        if (isSwitchTheme() || isSwitchTheme){
            activity.setTheme(R.style.AppThemeNight);
        }else {
            activity.setTheme(R.style.AppTheme);
        }
    }

    /**
     * 切换主题
     * @param activity
     * @param isSwitchTheme
     */
    public static void switchTheme(Activity activity,boolean isSwitchTheme){
        SharedPref.setBoolean(getApplicationContext(),SharedPref.IS_SWITCH_THEME,isSwitchTheme);
        activity.recreate();
    }

    /**
     * 根据主题设置获取状态栏颜色
     * @return
     */
    public static int changeStatusColor(){
        boolean isSwitchTheme = SharedPref.getBoolean(getApplicationContext(),SharedPref.IS_SWITCH_THEME,false);
        if (isSwitchTheme() || isSwitchTheme){
            return ResUtils.getColor(R.color.color_1B0325);
        }else {
            return ResUtils.getColor(R.color.white);
        }
    }

    /**
     * 根据主题获取状态栏是否高亮
     * @return
     */
    public static boolean isStatusLight(){
        boolean isSwitchTheme = SharedPref.getBoolean(getApplicationContext(),SharedPref.IS_SWITCH_THEME,false);
        if (isSwitchTheme() || isSwitchTheme){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 根据时间校验是否切换主题
     * @return
     */
    public static boolean isSwitchTheme(){
        int timeHour = TimeUtils.getHourTime();
        if (timeHour >= 18 || timeHour <= 6){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取版本号
     * @param context 上下文
     * @return 返回版本号
     */
    public static int versionCode(Context context){
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取版本名
     * @param context 上线文
     * @return 返回版本名称
     */
    public static String versionName(Context context){
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    /**
     * 检查App是否更新
     * @param context 上下文
     * @return true：更新，false：不更新
     */
    public static boolean isUpdate(Context context){
        int versionCode = versionCode(context);
        String versionName = versionName(context);
        String cloudData = CloudControlUtils.getCloudData(context, CloudPropertyManager.PATH_CHECK_UPDATE,"check_update");
        JsonHelper<CheckUpdate> jsonHelper = new JsonHelper<CheckUpdate>() {
        };
        CheckUpdate checkUpdate = jsonHelper.getJsonObject(cloudData);
        if (checkUpdate.getVersionCode() !=0 && !TextUtils.isEmpty(checkUpdate.getVersionName())){
            if (checkUpdate.getVersionCode() > versionCode || !checkUpdate.getVersionName().equals(versionName)){
                return true;
            }
        }
        return false;
    }

    /**
     * 如果是7.0以下，我们需要调用changeAppLanguage方法，
     * 如果是7.0及以上系统，直接把我们想要切换的语言类型保存在SharedPreferences中即可
     * 然后重新启动MainActivity
     *
     * @param language
     */
    public static void changeLanguage(Activity activity,String language) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            LanguageUtil.changeAppLanguage(getApplicationContext(), language);
        }
        SharedPref.setString(getApplicationContext(), SharedPref.LANGUAGE, language);
//        if ("VCE-AL00".equals(SystemUtils.getSystemModel())){
            if (activity instanceof SettingActivity){
                activity.finish();
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }else if (activity instanceof MainActivity){
            }
//        }else {
//            activity.recreate();
//        }
        //更新push语言，重新绑定
        try{
            Bundle bundle = new Bundle();
            bundle.putString("ext_locale",language);
            PushBindManager.getInstance().setExtParam(bundle);
        }catch (Exception e){}
    }

    /**
     * 跳转到googleplay应用详情页
     * @param context
     * @param appPkg
     */
    public static void launchAppDetail(Context context, String appPkg) {    //appPkg 是应用的包名
        final String GOOGLE_PLAY = "com.android.vending";//这里对应的是谷歌商店，跳转别的商店改成对应的即可
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + appPkg));
            intent.setPackage(GOOGLE_PLAY);//这里对应的是谷歌商店，跳转别的商店改成对应的即可
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {//没有应用市场，通过浏览器跳转到Google Play
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + appPkg));
                if (intent2.resolveActivity(context.getPackageManager()) != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);
                } else {
                    //没有Google Play 也没有浏览器
                }
            }
        } catch (ActivityNotFoundException activityNotFoundException1) {
            Log.e(AppUtils.class.getSimpleName(), "GoogleMarket Intent not found");
        }
    }

    /**
     * 获取应用包名
     * @param context
     * @return
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
