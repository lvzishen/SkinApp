package com.clean.binder.mgr;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MultiProcessBinderProvider extends ContentProvider {
	
	private static final String TAG = "BinderManager";
    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        return null;
    }

    @Override
    public boolean onCreate() {
    	if(ModuleConfig.DEBUG){
    		Log.v(TAG, "BinderManager started on process "+android.os.Process.myPid());
    	}
        return true;
    }

    @Override
    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {
        return null;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        return 0;
    }

    public Bundle call(String method, String arg, Bundle extras) {
        Bundle bundle = new Bundle();
        BinderParcel bp = new BinderParcel();
        bp.mProxy = BinderManager.getServerBinderMgr();
        bundle.putParcelable("rt", bp);
        return bundle;
    }
}
