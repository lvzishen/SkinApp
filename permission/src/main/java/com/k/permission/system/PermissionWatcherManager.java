package com.k.permission.system;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.hotvideo.config.GlobalConfig;

import java.lang.ref.WeakReference;

public class PermissionWatcherManager {
    private HandlerThread mHt;
    private PermissionHandler mHl;
    private static final int MSG_LOOP = 100;
    private static final int MSG_START = 102;
    private boolean mIsWatching;
    private Context mContext;
    private static final long INTERVAL_TIME = 500;
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "PermissionSettingM";
    private CallBack mCallBack;

    public interface CallBack {
        boolean isHavePermission();
        void opened();
    }

    public PermissionWatcherManager(Context context, CallBack callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
    }


    private class PermissionHandler extends Handler {
        WeakReference<PermissionWatcherManager> managerWeakReference;

        PermissionHandler(PermissionWatcherManager p, Looper looper) {
            super(looper);
            managerWeakReference = new WeakReference<>(p);
        }

        @Override
        public void handleMessage(Message msg) {
            PermissionWatcherManager p = managerWeakReference.get();
            switch (msg.what) {
                case MSG_START:
                    if (DEBUG) {
                        Log.i(TAG, "MSG_START");
                    }
                    p.mIsWatching = true;
                    removeMessages(MSG_LOOP);
                    sendEmptyMessage(MSG_LOOP);
                    break;
                case MSG_LOOP:
                    if (DEBUG) {
                        Log.i(TAG, "MSG_LOOP");
                    }
                    //检测permission
                    if (!p.mIsWatching || mCallBack == null || mContext == null) {
                        return;
                    }

                    if (DEBUG) {
                        Log.i(TAG, "MSG_LOOP"+mCallBack.isHavePermission());
                    }
                    if (mCallBack.isHavePermission()) {
                        //检测到打开权限了,停止轮询,退出设置页面
                        mCallBack.opened();
                        p.mIsWatching = false;
                        removeMessages(MSG_LOOP);
                    } else {
                        sendEmptyMessageDelayed(MSG_LOOP, INTERVAL_TIME);
                    }
                    break;

            }
        }

    }


    public void stopWatching() {
        if (mHl != null) {
            mHl.removeCallbacksAndMessages(null);
        }
        if (mHt != null) {
            mHt.quit();
        }

    }

    public void startWatching() {
        if (mContext == null && mCallBack == null) {
            return;
        }
        if (mHt == null) {
            mHt = new HandlerThread("wm-permission");
            mHt.start();
        }
        if (mHl == null) {
            mHl = new PermissionHandler(this, mHt.getLooper());
        }
        mHl.sendEmptyMessage(MSG_START);
    }
}
