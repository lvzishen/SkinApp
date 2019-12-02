package com.goodmorning.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.baselib.bitmap.util.DeviceUtil;
import com.baselib.cloud.CloudPropertyManager;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.bean.DayPicture;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.ui.activity.MyCollectActivity;
import com.goodmorning.ui.activity.PicDetailActivity;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.utils.CheckUtils;
import com.goodmorning.utils.CloudControlUtils;
import com.w.sdk.push.api.IPushExtension;
import com.w.sdk.push.api.PushManager;
import com.w.sdk.push.model.PushMessage;

import org.njord.push.sdk.extdefault.PushMessageBody;
import org.njord.push.sdk.extdefault.PushMessageParser;
import org.thanos.netcore.helper.JsonHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.goodmorning.utils.ActivityCtrl.TRANSFER_DATA;
import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

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
        PushMessageBody messageBody = PushMessageParser.getMessageBody(pushMessage.mMessageBody);
        if (messageBody == null) {
            return;
        }
        showNotification(messageBody, context);
    }

    private void showNotification(PushMessageBody messageBody, Context context) {
        NotificationManager nm = getNotificationManager(context);
        if (nm == null)
            return;
        bindNotificationChannel(context, nm);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "goodmorning_notification_channelid");

        //拼装DataListItem对象
        String cloudData = CloudControlUtils.getCloudData(getApplicationContext(), CloudPropertyManager.PATH_EVERYDAY_PIC, "day_pic");
        JsonHelper<DayPicture> jsonHelper = new JsonHelper<DayPicture>() {
        };
        DayPicture dayPicture = jsonHelper.getJsonObject(cloudData);
        DataListItem dataListItem = new DataListItem();
        dataListItem.setType(DataListItem.DATA_TYPE_2);
        dataListItem.setPicUrl(dayPicture.getPicUrl());
        dataListItem.setResourceId(dayPicture.getId());

        Intent intent = null;
        if (CheckUtils.isShowPic(dayPicture.getStartTime(), dayPicture.getEndTime())) {
            //展示弹窗 跳首页
            intent = new Intent(context, MainActivity.class);
            intent.putExtra("is_from_noti", true);
            intent.putExtra(TRANSFER_DATA, dataListItem);
            context.startActivity(intent);
        } else {
            //跳详情页
            intent = new Intent(context, PicDetailActivity.class);
            intent.putExtra(TRANSFER_DATA, dataListItem);
            context.startActivity(intent);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_REQUEST_PIC_BROADCAST, intent, FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, NOTIFICATION_REQUEST_PIC_BROADCAST,// requestCode是0的时候三星手机点击通知栏通知不起作用
                new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_pic_notification);
        //设置大标题
        mRemoteViews.setTextViewText(R.id.title, messageBody.getTitle());
        mRemoteViews.setTextViewText(R.id.desc, messageBody.getDescription());
        mRemoteViews.setBoolean(R.id.title, "setSingleLine", false);
        mRemoteViews.setInt(R.id.title, "setMaxLines", 2);
        //设置小标题
        mRemoteViews.setViewVisibility(R.id.desc, View.GONE);
        //设置图像
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
//        StatisticLoggerX.logShow(StatisticConstants.NOTIFICATION_BROWSER_CLOUDURL, StatisticConstants.FROM_NOTIFICATION, StatisticConstants.FROM_NOTIFICATION);
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
