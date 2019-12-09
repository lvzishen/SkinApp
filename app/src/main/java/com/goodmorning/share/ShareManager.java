package com.goodmorning.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import com.creativeindia.goodmorning.R;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.share.util.FileOpenUtil;
import com.goodmorning.share.util.RFileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * 创建日期：2018/12/26 on 16:55
 * 描述:
 * 作者: lvzishen
 */
public class ShareManager implements ISaveImage {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "ShareManager";
    private List<String> shareApps;


    private static class SingletonHolder {
        private static final ShareManager instance = new ShareManager();
    }

    private List<String> getShareApps() {
        if (shareApps == null) {
            shareApps = new ArrayList<>();
            shareApps.add("com.whatsapp");
            shareApps.add("com.facebook.orca");
            shareApps.add("com.facebook.mlite");
            shareApps.add("com.whatsapp.w4b");
            shareApps.add("com.discord");
            shareApps.add("com.facebook.katana");
            shareApps.add("com.snapchat.android");
            shareApps.add("jp.naver.line.android");
            shareApps.add("com.tencent.mm");
            shareApps.add("com.twitter.android");
            shareApps.add("com.instagram.android");
            shareApps.add("com.google.android.talk");
            shareApps.add("com.viber.voip");
            shareApps.add("kik.android");
            shareApps.add("com.android.mms");
        }
        return shareApps;
    }

    private ShareManager() {
        getShareApps();
    }

//    public String getShareUrl() {
//        return new StringBuffer().append(INSTALL_GOOGLEPLAY_URL)
//                .append(GOOGLE_PLAY_CHANNEL).append(SHARE_CHANNEL_ID).toString();
//    }

    public static ShareManager getInstance() {
        return SingletonHolder.instance;
    }

//

    private Intent getIntent(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        Uri uri;
        String type = FileOpenUtil.getMIMETypeByMap(file);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            uri = FileProvider.getUriForFile(context,
//                    context.getPackageName() + ".FileProvider",
//                    file);
//        } else {
        uri = Uri.fromFile(file);
//        }
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType(type);
        return intent;
    }


    public final Intent getShareIntent(Context context, File file) {
        Intent intent = getIntent(context, file);
        Intent shareIntent = getShareIntent(context, intent, context.getString(R.string.share_message_title));
        if (shareIntent == null) {
            shareIntent = getIntent(context, file);
        }
        return shareIntent;
    }

    @Override
    public String saveImage(Context context, String picName, Bitmap bitmap) {
        //save image
        RFileHelper.deleteExternalShareDirectory(context);
        String path = RFileHelper.saveBitmapToExternalSharePath(context, bitmap);
        RFileHelper.detectFileUriExposure();
        //share
        Uri imageUri = RFileHelper.getExternalSharePathFileUris(context, path).get(0);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("image/*");
        Intent shareIntent = getShareIntent(context, intent, context.getString(R.string.share_message_title));
        if (shareIntent == null) {
            shareIntent = intent;
        }
        context.startActivity(shareIntent);
        return null;
    }


    public final Intent getShareStringIntent(Context context, String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content + ShareTypeManager.URL_TEXT);    //设置要分享的内容
        Intent shareIntent = getShareIntent(context, intent, context.getString(R.string.share_message_title));
        if (shareIntent == null) {
            shareIntent = intent;
        }
        return shareIntent;
    }

    private Intent getShareIntent(Context context, Intent intent, String str) {
        ArrayList<LabeledIntent> arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
//        final List<String> shareApps = getShareApps();
        if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
            if (DEBUG) {
                Log.e(TAG, "query null or empty");
            }
        } else {
            Object obj = null;
            for (ResolveInfo resolveInfo : queryIntentActivities) {
                Intent intent2 = new Intent(intent);
                String packageName = resolveInfo.activityInfo.packageName;
                intent2.setPackage(packageName);
                intent2.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
                if (!packageName.equals(context.getPackageName())) {
                    int i = resolveInfo.activityInfo.applicationInfo.icon;
                    if (hashSet.add(packageName)) {
                        if (DEBUG) {
                            Log.e(TAG, "packageName:-->" + packageName);
                        }
                        arrayList.add(new LabeledIntent(intent2, packageName, resolveInfo.loadLabel(packageManager), i));
                    }
                }
            }
        }
        if (arrayList.isEmpty()) {
            if (DEBUG) {
                Log.e(TAG, "No send options available.");
            }
            return null;
        } else if (arrayList.size() == 1) {
            if (arrayList.get(0).getSourcePackage().equals("com.android.nfc")) {
                return null;
            }
            return (Intent) arrayList.get(0);
        } else {
            Intent intent3;
            String toLowerCase = Build.MANUFACTURER.toLowerCase(Locale.getDefault());
            if (!(Build.VERSION.SDK_INT >= 23) || toLowerCase.contains("xiaomi")) {
                intent3 = (Intent) arrayList.remove(arrayList.size() - 1);
            } else {
                intent3 = new Intent();
            }
            List<LabeledIntent> listSortIntent = getSortList(arrayList);
            Intent createChooser = Intent.createChooser(intent3, str);
            createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", (LabeledIntent[]) listSortIntent.toArray(new LabeledIntent[arrayList.size()]));
            return createChooser;
        }
    }

    private List<LabeledIntent> getSortList(List<LabeledIntent> arrayList) {
        List<String> listName = new ArrayList();
        List<LabeledIntent> listSortIntent = new ArrayList<>();
        for (LabeledIntent labeledIntent : arrayList) {
            listName.add(labeledIntent.getSourcePackage());
        }
        List<String> list = new ArrayList();
        list.addAll(shareApps);
        for (String s : listName) {
            if (!list.contains(s)) {
                list.add(s);
            }
        }

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String packageName = iterator.next();
            if (!listName.contains(packageName)) {
                iterator.remove();
            }
        }

        for (String appName : list) {
            for (LabeledIntent labeledIntent : arrayList) {
                if (appName.equals(labeledIntent.getSourcePackage())) {
                    listSortIntent.add(labeledIntent);
                    break;
                }
            }
        }
        Collections.reverse(listSortIntent);
        return listSortIntent;
    }


}


