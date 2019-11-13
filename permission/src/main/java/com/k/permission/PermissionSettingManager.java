package com.k.permission;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * 创建日期：2018/5/30 on 10:04
 * 描述:
 * 作者:lvzishen lvzishen
 */
public class PermissionSettingManager {
    private HandlerThread mHt;
    private PermissionHandler mHl;
    private static final int MSG_LOOP = 100;
    private static final int MSG_STOP = 101;
    private static final int MSG_START = 102;
    private boolean mIsWatching;
    private Context mContext;
    private static final long INTERVAL_TIME = 500;
    private IPermissionSettingResult mPermissionSettingListener;
    private static final boolean DEBUG = Configs.DEBUG;
    private static final String TAG = "PermissionSettingM";

    private PermissionSettingManager() {
        if (mHt == null) {
            mHt = new HandlerThread("app-permission");
            mHt.start();
        }
        if (mHl == null) {
            mHl = new PermissionHandler(this, mHt.getLooper());
        }
    }

    private static class SingletonHolder {
        private static final PermissionSettingManager instance = new PermissionSettingManager();

    }

    public static PermissionSettingManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class PermissionHandler extends Handler {
        WeakReference<PermissionSettingManager> managerWeakReference;

        PermissionHandler(PermissionSettingManager p, Looper looper) {
            super(looper);
            managerWeakReference = new WeakReference<>(p);
        }

        @Override
        public void handleMessage(Message msg) {
            PermissionSettingManager p = managerWeakReference.get();
            switch (msg.what) {
                case MSG_START:
                    if (DEBUG) {
                        Log.i(TAG, "MSG_START");
                    }
                    p.mIsWatching = true;
                    String[] permissions = (String[]) msg.obj;
                    removeMessages(MSG_LOOP);

                    Message msgStart = Message.obtain();
                    msgStart.what = MSG_LOOP;
                    msgStart.obj = permissions;
                    sendMessage(msgStart);
                    break;
                case MSG_LOOP:
                    //检测permission
                    if (!p.mIsWatching) {
                        return;
                    }
                    String[] checkPermissions = (String[]) msg.obj;
                    if (PermissionCheck.checkPermission(p.mContext, checkPermissions)) {
                        //检测到打开权限了,停止轮询,退出设置页面
                        if (p.mPermissionSettingListener != null) {
                            if (DEBUG) {
                                Log.i(TAG, "onPermissionIsGrant");
                            }
                            p.mPermissionSettingListener.onPermissionIsGrant(checkPermissions);
                        }
                        p.mIsWatching = false;
                        removeMessages(MSG_LOOP);
                    } else {
                        Message msgLoop = Message.obtain();
                        msgLoop.what = MSG_LOOP;
                        msgLoop.obj = checkPermissions;
                        sendMessageDelayed(msgLoop, INTERVAL_TIME);
                        if (DEBUG) {
                            Log.i(TAG, "MSG_LOOP");
                        }
                    }
                    break;
                case MSG_STOP:
                    if (DEBUG) {
                        Log.i(TAG, "MSG_STOP");
                    }
                    p.mIsWatching = false;
                    removeMessages(MSG_LOOP);
                    break;
            }
        }

    }


    public void stopWatching() {
        if (DEBUG) {
            Log.i(TAG, "stopWatching");
        }
        if (mHl == null) {
            return;
        }
        mHl.sendEmptyMessage(MSG_STOP);
    }

    public void startCheckPermission(Context context, String[] permissions, IPermissionSettingResult permissionSettingResult) {
        if (mHl == null) {
            return;
        }
        if (DEBUG) {
            Log.i(TAG, "startCheckPermission");
        }
        mContext = context;
        mPermissionSettingListener = permissionSettingResult;
        Message message = Message.obtain();
        message.what = MSG_START;
        message.obj = permissions;
        mHl.sendMessage(message);
    }


}
