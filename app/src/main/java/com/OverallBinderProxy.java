package com;

import android.app.Application;
import android.os.IBinder;

import com.clean.binder.mgr.BinderManager;
import com.hotvideo.servicemanager.ServiceManager;


public class OverallBinderProxy implements BinderManager.BinderManangerProxy {

    private Application mApp = null;

    public OverallBinderProxy(Application application) {
        mApp = application;
    }

    @Override
    public IBinder getBinder(String key) {
        if (ServiceManager.SERVICE_KEY.equals(key)) {
            return ServiceManager.getServiceManagerStub(mApp.getApplicationContext());
        }
        return null;
    }

    @Override
    public void onServiceManagerBound() {
        try {
//            ServiceManager.getServiceImplBinder(mApp).startServiceWithIntent(new Intent(mApp, .class));
        } catch (Exception e) {

        }
    }


}
