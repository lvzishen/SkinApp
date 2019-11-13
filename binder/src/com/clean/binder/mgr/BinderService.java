package com.clean.binder.mgr;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by tangchun on 2017/4/6.
 */

public class BinderService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return BinderManager.getServerBinderMgr();
    }

}
