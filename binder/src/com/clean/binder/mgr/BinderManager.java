package com.clean.binder.mgr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class BinderManager {
	
	private static final boolean DEBUG = ModuleConfig.DEBUG;
	private static final String TAG = "BinderManager";
	public static String getAuthority(Context cxt) {
		String pkg = cxt.getPackageName();
		return "com.binder.provider_"+pkg;
	}

	public static Uri getProviderUri(Context cxt) {
		String authority = getAuthority(cxt);
		return Uri.parse("content://" + authority);
	}
	
	public static interface BinderManangerProxy{
		public IBinder getBinder(String key);
		public void onServiceManagerBound();
	}

	private static BinderManangerProxy sBinderManagerProxy = null;
	
	public static void setBinderManagerProxy(BinderManangerProxy proxy){
		sBinderManagerProxy = proxy;
	}

	private static boolean sIsHostProcess = false;
	public static void setIsHostProcess(boolean isHost){
		sIsHostProcess = isHost;
		if(sIsHostProcess){
			sClient = (ISvcManager) getServerBinderMgr();
		}
		if(DEBUG){
			Log.v(TAG,"sIsHostProcess = "+sIsHostProcess+" , sClient = "+sClient);
		}
	}
	/**
	 * 
	 * ʵ��һ��binder�ı��ع�����
	 */
	public static class SvcMgrImpl extends ISvcManager.Stub {
		private static HashMap<String,IBinder> sSvrContainer = new HashMap<String,IBinder>();
		@Override
		public void addService(String name, IBinder binder)
				throws RemoteException {
			if(TextUtils.isEmpty(name))
				return;
			synchronized(sSvrContainer){
				if(binder != null){
					try{
						binder.linkToDeath(new SvrDeathLinker(name), 0);
					}catch(Exception e){
						
					}
				}
				sSvrContainer.put(name, binder);
			}

		}

		@Override
		public IBinder getService(String name) throws RemoteException {
			IBinder binder = null;
			synchronized(sSvrContainer){
				binder =  sSvrContainer.get(name);
			}
			if(binder == null && sBinderManagerProxy != null){
				binder = sBinderManagerProxy.getBinder(name);
			}
			return binder;
		}
		
		/**
		 *���ڼ�ͦbinderʧЧ
		 */
		private static class SvrDeathLinker implements  DeathRecipient{
			private String mKey = null;
			public SvrDeathLinker(String key){
				mKey = key;
			}
			@Override
			public void binderDied() {
				synchronized(sSvrContainer){
					sSvrContainer.remove(mKey);
				}
			}
		}
	}

	private static ISvcManager.Stub sProxy = null;

	static IBinder getServerBinderMgr() {
		synchronized (BinderManager.class) {
			if (sProxy == null) {
				sProxy = new SvcMgrImpl();
			}
		}
		return sProxy;
	}

	private static ISvcManager sClient = null;
	private static DeathRecipient sDeathLinker = new DeathRecipient() {
		@Override
		public void binderDied() {
			if(DEBUG){
				Log.v(TAG,"sClient is reset!");
			}
			sClient = null;
			synchronized (sSvcCache){
				sSvcCache.clear();
			}
			sServiceBound = false;
		}

	};

	/**
	 * ���пͻ���Ҫʹ�õ�binder �����ڴ˻�ȡ
	 * 
	 * @param cxt
	 * @return
	 */
	public static ISvcManager getClientBinderMgr(Context cxt) {
		synchronized (BinderManager.class) {
			if (sClient != null)
				return sClient;

			try{
				bindBinderServiceIfNecessary(cxt);
				Bundle bundle = cxt.getContentResolver().call(getProviderUri(cxt),
						"rtClient", null, null);
				bundle.setClassLoader(BinderParcel.class.getClassLoader());
				BinderParcel bp = (BinderParcel) bundle.getParcelable("rt");
				IBinder binder = bp.mProxy;
				if (binder == null)
					return null;
				else {
					sClient = ISvcManager.Stub.asInterface(binder);
					try {
						((IBinder) sClient.asBinder()).linkToDeath(sDeathLinker, 0);
					} catch (Exception e) {
						sClient = null;
					}
					if(DEBUG){
						Log.v(TAG,"set bindmgr from provider");
					}
					return sClient;
			}
			}catch(Exception e){
				if(DEBUG){
					Log.e(TAG, "",e);
				}
				return null;
			}
		}
	}
	//用作测试
	public static boolean testCanFectchBinderProvider(Context cxt) {
		try {
			Bundle bundle = cxt.getContentResolver().call(getProviderUri(cxt),
					"rtClient", null, null);
			if(bundle == null)
				return false;
			return true;
		} catch (Exception e) {
			if (DEBUG) {
				Log.e(TAG, "", e);
			}
			return false;
		}
	}


	private static boolean sServiceBound = false;
	//如果无法用provider的方式拿到binder管理器就应该使用Service的形式再尝试一次，
	private static ServiceConnection sServiceConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			sServiceBound = false;
			if(DEBUG){
				Log.v(TAG,"fetch bindmgr from service "+binder);
			}
			try{
				if(binder != null){
					synchronized (BinderManager.class){
						if(sClient == null) {
							sClient = ISvcManager.Stub.asInterface(binder);
							if (DEBUG)
								Log.v(TAG, "set binder in servcie connected");
							try {
								((IBinder) sClient.asBinder()).linkToDeath(sDeathLinker, 0);
							} catch (Exception e) {
								sClient = null;
							}
						}
					}
				}
			}catch (Exception e){
				if(DEBUG){
					Log.e(TAG, "",e);
				}
			}
			if(sBinderManagerProxy != null){
				sBinderManagerProxy.onServiceManagerBound();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	public static void bindBinderServiceIfNecessary(Context cxt){
		if(sServiceBound)
			return;
		sServiceBound = true;
		try{
			ComponentName cn = new ComponentName(cxt,BinderService.class);
			Intent intent = new Intent();
			intent.setComponent(cn);
			cxt.bindService(intent,sServiceConn,Context.BIND_AUTO_CREATE);
		}catch(Exception e){
			if(DEBUG){
				Log.e(TAG,"",e);
			}
		}
	}
	
	
	/**
	 * ���е�binder�ڿͻ��˶����л���
	 */
	private static HashMap<String,IBinder> sSvcCache = new HashMap<String,IBinder>();
	
	/**
	 * ע��Ӧ���ڵ�һ��ȫ�ַ���
	 * @param cxt
	 * @param key
	 * @param binder
	 * @return
	 */
	public static boolean addService(Context cxt, String key, IBinder binder) {
		if (TextUtils.isEmpty(key))
			return false;

		if (binder == null)
			return false;

		ISvcManager svcmgr = getClientBinderMgr(cxt);
		if (svcmgr == null)
			return false;
		
		try {
			svcmgr.addService(key, binder);
			synchronized(sSvcCache){
				sSvcCache.remove(key);
			}
			return true;
		} catch (Exception e) {
		}
		
		return false;
	}
	
	
	/**
	 * ��ȡһ��ȫ�ֵķ���
	 * @param cxt
	 * @param key
	 * @return
	 */
	public static IBinder getService(Context cxt,String key){
		IBinder binder = null;
		synchronized(sSvcCache){
			binder = sSvcCache.get(key);
		}
		if(binder != null)
			return binder;
		ISvcManager svcmgr = getClientBinderMgr(cxt);
		if (svcmgr == null)
			return null;
		
		try {
			binder = svcmgr.getService(key);
			if(binder != null){
				binder.linkToDeath(new CliDeathLinker(key), 0);
				synchronized(sSvcCache){
					sSvcCache.put(key, binder);
				}
			}
		} catch (Exception e) {
			if(DEBUG)
				Log.e(TAG,"",e);
		}
		
		return binder;
	}
	
	
	/**
	 * 
	 *��Щ����������ÿ��ʹ��binder�Ŀͻ��˽����
	 */
	private static class CliDeathLinker implements DeathRecipient{
		
		private String mKey = null;
		public CliDeathLinker(String key){
			mKey = key;
		}
		@Override
		public void binderDied() {
			synchronized(sSvcCache){
				sSvcCache.remove(mKey);
			}
		}
		
	}
	

}
