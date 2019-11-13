package com.hotvideo.servicemanager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.hotvideo.config.GlobalConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static android.content.Context.BIND_AUTO_CREATE;


public class ServiceImpl extends ISeviceManager.Stub {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "ServiceImpl";
    private Context mContext;
    private ConcurrentHashMap<String, ServiceConnection> mServiceConntection;
    private ConcurrentHashMap<String, BinderBean> mIBinderMap;
    private ConcurrentHashMap<String, List<Intent>> mIntents;
    private ConcurrentHashMap<String, Boolean> mBinderStatus;
    //binder asBinder asInterFace 支持跨进程
    private Handler mUIHandler = new Handler(Looper.getMainLooper());


    public ServiceImpl(Context cxt) {
        mContext = cxt.getApplicationContext();
        mServiceConntection = new ConcurrentHashMap<>();
        mIBinderMap = new ConcurrentHashMap<>();
        mIntents = new ConcurrentHashMap<>();
        mBinderStatus = new ConcurrentHashMap<>();
    }

    public void startService(Intent dataIntent) {
        String serviceKey = dataIntent.getComponent().getClassName();
        if (!GlobalConfig.isOreoAndAbove()) {
            mContext.startService(dataIntent);
            if (DEBUG) {
                Log.i(TAG, "<TargetO:service->启动service:" + serviceKey);
            }
            return;
        }
        if (DEBUG) {
            Log.i(TAG, ">TargetO:service->启动service:" + serviceKey);
        }
        BinderBean binderBean = mIBinderMap.get(serviceKey);
        if (binderBean != null) {
            if (binderBean.iBinder != null) {
                IDefaultBinder binder = binderBean.iBinder;
                try {
                    if (binder.isRunning(serviceKey)) {
                        startServiceByBinder(dataIntent, serviceKey, binder);
                    } else {
                        bindService(dataIntent, serviceKey);
                    }
                } catch (Exception e) {
                }
            }
        } else {
            bindService(dataIntent, serviceKey);
        }
    }

    @Override
    public void startServiceWithIntent(final Intent dataIntent) throws RemoteException {
        if (mUIHandler != null) {
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    startService(dataIntent);
                }
            });
        }
    }

    private void startServiceByBinder(Intent dataIntent, String serviceKey, IDefaultBinder binder) {
        if (binder == null) {
            return;
        }
        if (DEBUG) {
            Log.i(TAG, ">TargetO:绑定完成后:准备执行onstartcommand:" + serviceKey);
        }
        List<Intent> mTemp = mIntents.get(serviceKey);
        if (mTemp != null) {
            if (DEBUG) {
                Log.i(TAG, ">TargetO:开始处理多个intent:" + serviceKey + "共需要处理:" + mTemp.size());
            }
            for (int i = 0; i < mTemp.size(); i++) {
                if (DEBUG) {
                    Log.i(TAG, ">TargetO:处理完一个intent,总共需要处理:" + serviceKey + "共需要处理:" + mTemp.size());
                }
                Intent intent = mTemp.get(i);
                try {
                    binder.startCommand(intent);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mIntents.remove(serviceKey);
        }
        if (dataIntent != null) {
            if (DEBUG) {
                Log.i(TAG, ">TargetO:处理单个intent:" + serviceKey);
            }
            try {
                binder.startCommand(dataIntent);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void bindService(Intent dataIntent, String serviceKey) {
        if (DEBUG) {
            Log.i(TAG, ">TargetO:service->准备绑定service:" + serviceKey);
        }
        //删除无用的binder
        if (mIBinderMap.get(serviceKey) != null) {
            mIBinderMap.remove(serviceKey);
        }
        //是否正在bind
        boolean isBinding = false;
        if (mBinderStatus.get(serviceKey) != null && mBinderStatus.get(serviceKey)) {
            isBinding = true;
        } else {
            mBinderStatus.put(serviceKey, true);
        }
        //如果没有正在bind 或者需要重新bind 删掉缓存的脏数据
        List<Intent> mTemp = mIntents.get(serviceKey);
        if (mTemp == null) {
            mTemp = new ArrayList<>();
        }
        if (!isBinding) {
            mTemp.clear();
        }
        mTemp.add(dataIntent);
        mIntents.put(serviceKey, mTemp);
        if (DEBUG) {
            Log.i(TAG, ">TargetO:intent队列中现在有几个待处理intent:" + serviceKey + mIntents.get(serviceKey).size());
        }
        if (!isBinding) {
            if (DEBUG) {
                Log.i(TAG, ">TargetO:service->正在绑定service:" + serviceKey);
            }
            ServiceConnection serviceConnection = getServiceConnection(serviceKey);
            mContext.bindService(dataIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void stopService(Intent intent) throws RemoteException {
        String serviceKey = intent.getComponent().getClassName();
        if (!GlobalConfig.isOreoAndAbove()) {
            //Intent intent = ServiceModule.getExternalConfig().getIntent(serviceKey);
            if (intent != null) {
                mContext.stopService(intent);
            }
            if (DEBUG) {
                Log.i(TAG, "<TargetO:service->停止service" + serviceKey);
            }
            return;
        }
        ServiceConnection serviceConnection = mServiceConntection.get(serviceKey);
        if (serviceConnection != null) {
            if (DEBUG) {
                Log.i(TAG, ">TargetO:service->停止service" + serviceKey);
            }
            removeMapData(serviceKey);
            mContext.unbindService(serviceConnection);
        }
    }

    private void removeMapData(String serviceKey) {
        mIBinderMap.remove(serviceKey);
        mServiceConntection.remove(serviceKey);
        mIntents.remove(serviceKey);
        mBinderStatus.remove(serviceKey);
    }

    private ServiceConnection getServiceConnection(final String serviceKey) {
        ServiceConnection serviceConnection = mServiceConntection.get(serviceKey);
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    if (mIBinderMap.get(serviceKey) == null) {
                        try {
                            service.linkToDeath(new IBinder.DeathRecipient() {
                                @Override
                                public void binderDied() {
                                    if (DEBUG) {
                                        Log.i(TAG, ">TargetO:binderDied异常销毁:" + serviceKey);
                                    }
                                    removeMapData(serviceKey);
                                }
                            }, 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        BinderBean binderBean = new BinderBean();
                        binderBean.iBinder = IDefaultBinder.Stub.asInterface(service);
                        binderBean.componentName = name;
                        mIBinderMap.put(serviceKey, binderBean);
                        //已经连接了处理下队列里的intent
                        if (DEBUG) {
                            Log.i(TAG, ">TargetO:绑定完成service:" + serviceKey);
                        }
                        startServiceByBinder(null, serviceKey, IDefaultBinder.Stub.asInterface(service));
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    if (DEBUG) {
                        Log.d(TAG, "onServiceDisconnected: " + name);
                    }
                }
            };
            mServiceConntection.put(serviceKey, serviceConnection);
        }
        return serviceConnection;
    }

}
