package com.goodmorning.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.baselib.bitmap.util.DeviceUtil;
import com.baselib.statistic.StatisticConstants;
import com.baselib.statistic.StatisticLoggerX;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.bean.PushBean;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.ui.activity.PicDetailActivity;
import com.goodmorning.utils.CheckUtils;
import com.w.sdk.push.api.IPushExtension;
import com.w.sdk.push.api.PushManager;
import com.w.sdk.push.model.PushMessage;

import org.thanos.netcore.helper.JsonHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.goodmorning.utils.ActivityCtrl.TRANSFER_DATA;

/**
 * 创建日期：2019/11/29 on 20:30
 * 描述:
 * 作者: lvzishen
 */
public class MorningPushExtension extends IPushExtension {
    private static final String TAG = "MorningPushExtension";
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final int NOTIFICATION_REQUEST_PIC_BROADCAST = 10000;
    private static final int NOTIFICATION_PIC_DETAIL = 1001;
    public static DataListItem pushDataListItem = null;

    @Override
    public boolean handleMessage(PushMessage message, Context context) throws Exception {
        if (message != null && !TextUtils.isEmpty(message.mMessageBody)
                && message.mMessageType == this.messageType) {
            if (DEBUG) {
                Log.d(TAG, message.constructMessageString());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault());
                Log.d(TAG, "local time: " + format.format(new Date(message.mRemoteMessageTime)));
            }
            dispatchMessage(message, context);
            PushManager.markMessageRead(context, message);
        }
        return false;
    }

    private void dispatchMessage(PushMessage pushMessage, Context context) {
        JsonHelper<PushBean> jsonHelper = new JsonHelper<PushBean>() {
        };
        PushBean pushBean = jsonHelper.getJsonObject(pushMessage.mMessageBody);
        if (pushBean == null) {
            return;
        }
        showNotification(pushBean, context);
    }

    private void showNotification(PushBean pushBean, Context context) {
        NotificationManager nm = getNotificationManager(context);
        if (nm == null)
            return;
        if (DEBUG) {
            Log.i(TAG, "pushBean:---->" + pushBean.toString());
        }
        bindNotificationChannel(context, nm);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "goodmorning_notification_channelid");

        DataListItem dataListItem = new DataListItem();
        dataListItem.setType(DataListItem.DATA_TYPE_2);
        dataListItem.setPicUrl(pushBean.big_image);
        String str[] = pushBean.action_main.split(",");
        dataListItem.setResourceId(Long.valueOf(str[0]));
        dataListItem.setWidth(Integer.valueOf(str[1]));
        dataListItem.setHeight(Integer.valueOf(str[2]));
        dataListItem.setChannelName("push");
        if (DEBUG) {
            Log.i(TAG, "dataListItem:---->" + dataListItem.toString());
        }
        Intent intent = null;
        if (CheckUtils.isShowPic(Long.valueOf(pushBean.start_time), Long.valueOf(pushBean.end_time))) {
            //展示弹窗 跳首页
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("is_from_noti", true);
//            intent.putExtra(TRANSFER_DATA, dataListItem);
            pushDataListItem = dataListItem;
        } else {
            //跳详情页
            intent = new Intent(context, PicDetailActivity.class);
            intent.putExtra(TRANSFER_DATA, dataListItem);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_REQUEST_PIC_BROADCAST, intent, FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, NOTIFICATION_REQUEST_PIC_BROADCAST,// requestCode是0的时候三星手机点击通知栏通知不起作用
                new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_pic_notification);
        //设置大标题
        mRemoteViews.setTextViewText(R.id.title, pushBean.title);
        mRemoteViews.setTextViewText(R.id.desc, pushBean.description);
//        mRemoteViews.setBoolean(R.id.title, "setSingleLine", true);
//        mRemoteViews.setInt(R.id.title, "setMaxLines", 2);
        //设置图像
//        Glide.with(context)
//                .load(pushBean.extra.getPicUrl())
//                .asBitmap()
//                .placeholder(R.drawable.ic_launcher1)
//                .error(R.drawable.ic_launcher1)
//                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        mRemoteViews.setImageViewBitmap(R.id.notify_big_image, resource);
//                    }
//                });
        mRemoteViews.setImageViewBitmap(R.id.notify_big_image, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher1));
        mBuilder.setContent(mRemoteViews)
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher1);

        if (DeviceUtil.isAboveLollipop(true)) {
            mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            mBuilder.setPriority(NotificationCompat.DEFAULT_ALL);
            mBuilder.setFullScreenIntent(pendingIntent1, true);// 横幅
        }
        Notification notify = mBuilder.build();
        StatisticLoggerX.logShow(StatisticConstants.NOTIFICATION_BROWSER_CLOUDURL, StatisticConstants.FROM_NOTIFICATION, StatisticConstants.FROM_NOTIFICATION);
        nm.notify(NOTIFICATION_PIC_DETAIL, notify);
    }

    private NotificationManager getNotificationManager(Context cxt) {
        return (NotificationManager) cxt.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static void bindNotificationChannel(Context context, NotificationManager notificationManager) {
        bindNotificationChannel(context, notificationManager, NotificationManager.IMPORTANCE_LOW);
    }

    private static void bindNotificationChannel(Context context, NotificationManager notificationManager, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channel_name = context.getString(R.string.app_name);
            String channel_des = context.getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel("goodmorning_notification_channelid", channel_name, importance);
            mChannel.setDescription(channel_des);
            notificationManager.createNotificationChannel(mChannel);
        }

    }

}
