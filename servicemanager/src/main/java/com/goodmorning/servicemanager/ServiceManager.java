package com.goodmorning.servicemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.clean.binder.mgr.BinderManager;

/**
 * 创建日期：2018/6/22 on 10:50
 * 描述:
 * 作者:lvzishen
 */
public class ServiceManager {

    private static ServiceImpl sServiceImpl = null;
    public static final String SERVICE_KEY = "service_manager";

    /**
     * GDPR启动前，有些服务是不能启动的
     */
    public static interface GDPRServiceFilter{
        public boolean shouldBlockService(Intent intent);
    }

    private static GDPRServiceFilter sGDPRSeviceFilter = null;

    /**
     * 设置服务的过滤器
     * @param filter
     */
    public static void setServiceFilter(GDPRServiceFilter filter){
        sGDPRSeviceFilter = filter;
    }

    public static boolean shouldBlockService(Intent intent){
        if(sGDPRSeviceFilter != null && sGDPRSeviceFilter.shouldBlockService(intent))
            return true;
        return false;
    }

    public synchronized static Binder getServiceManagerStub(Context cxt) {
        if (sServiceImpl == null) {
            sServiceImpl = new ServiceImpl(cxt);
        }
        return sServiceImpl;
    }


    public static ISeviceManager getServiceImplBinder(Context cxt) {
        IBinder binder = BinderManager.getService(cxt, SERVICE_KEY);
        ISeviceManager obj = null;
        if (binder == null){
            obj = null;
        }else {
            obj =  ISeviceManager.Stub.asInterface(binder);
        }
        return new SvcMgrWrapper(obj,cxt);
    }

    public static class SvcMgrWrapper implements ISeviceManager{

        private ISeviceManager mSvcMgr = null;
        private Context mContext = null;
        public SvcMgrWrapper(ISeviceManager inner,Context cxt){
            mSvcMgr = inner;
            mContext = cxt;
        }
        @Override
        public void startServiceWithIntent(Intent intent) throws RemoteException {
            if(shouldBlockService(intent)){
                return;
            }
            if(mSvcMgr == null){
                try{
                    mContext.startService(intent);
                }catch(Exception e){

                }
            }else{
                mSvcMgr.startServiceWithIntent(intent);
            }
        }

        @Override
        public void stopService(Intent intent) throws RemoteException {
            if(mSvcMgr != null){
               // mSvcMgr.startServiceWithIntent(intent);
                mSvcMgr.stopService(intent);
            }else{
                try {
                    mContext.stopService(intent);
                }catch(Exception e){

                }
            }
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    }
}
