package com.baselib.cloud;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;

import org.interlaken.common.env.BasicProp;

import java.util.HashMap;

public class CloudPropertyImpl {
    private Context mContext;

    public CloudPropertyImpl(Context cxt) {
        mContext = cxt;
    }


    public float getFloat(String path, String key, float def)
            throws RemoteException {

        float val;
        String sVal = getString(path, key, null);
        try {
            val = Float.parseFloat(sVal);
        } catch (Exception e) {
            val = def;
        }
        return val;
    }


    public int getInt(String path, String key, int def) throws RemoteException {
        int val;
        String sVal = getString(path, key, null);
        try {
            val = Integer.parseInt(sVal);
        } catch (Exception e) {
            val = def;
        }
        return val;
    }

    private static HashMap<String, BasicProp> sPropInstance = new HashMap<String, BasicProp>();


    public String getString(String path, String key, String def)
            throws RemoteException {
        BasicProp prop = null;
        synchronized (sPropInstance) {
            prop = sPropInstance.get(path);
            if (prop == null) {
                prop = new BasicProp(mContext, path);
                sPropInstance.put(path, prop);
            }
        }
        /*if (CloudPropertyManager.PATH_CONFIG.equals(path)) {
            prop = ConfigManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_CONFIG2.equals(path)) {
            prop = ConfigManager2.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_BLOG.equals(path)) {
            prop = BlogConfigManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_CHARGER_LOCKER.equals(path)) {
            prop = ChargerLockerConfigManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_EMERGENCY_ADS.equals(path)) {
            prop = EmergencyAdsManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_RUBBISH.equals(path)) {
            prop = RubbishManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_BOOST_CLOUD.equals(path)) {
            prop = CommonPropManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_APPLOCK_CLOUD.equals(path)) {
            prop = AppLockPropManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_ANTI_VIRUS.equals(path)) {
		    prop = AntiVirusPropertyManager.getInstance(mContext);
	    } else if(CloudPropertyManager.PATH_GLOBAL_URL.equals(path)) {
            prop = GlobalPropertyManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_RESULT_ADS_CONFIG.equals(path)) {
            prop = ResultAdsPropManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_AV_ADS_CONFIG.equals(path)) {
            prop = AvAdsPropManager.getInstance(mContext);
        } else if (CloudPropertyManager.PATH_ONE_TAP_BOOST_ADS_CONFIG.equals(path)) {
            prop = OneTapBoostAdsConfigManager.getInstance(mContext);
        }*/

        if (prop == null)
            return def;

        String val = prop.get(key);
        if (val == null)
            return def;
        return val;
    }


    public void reload(String path) throws RemoteException {
        if (TextUtils.isEmpty(path))
            return;
        synchronized (sPropInstance) {
            sPropInstance.remove(path);
        }

        /*if (CloudPropertyManager.PATH_CONFIG.equals(path)) {
            ConfigManager.reload(mContext);
        } else if (CloudPropertyManager.PATH_CONFIG2.equals(path)) {
            ConfigManager2.reload(mContext);
        } else if (CloudPropertyManager.PATH_BLOG.equals(path)) {
            BlogConfigManager.reload(mContext);
        } else if (CloudPropertyManager.PATH_EMERGENCY_ADS.equals(path)) {
            EmergencyAdsManager.reload(mContext);
        } else if (CloudPropertyManager.PATH_CHARGER_LOCKER.equals(path)) {
            ChargerLockerConfigManager.reload(mContext);
        } else if (CloudPropertyManager.PATH_RUBBISH.equals(path)) {
            RubbishManager.reload(mContext);
        } else if (CloudPropertyManager.PATH_BOOST_CLOUD.equals(path)) {
            CommonPropManager.reload(mContext);
        } else if (CloudPropertyManager.PATH_APPLOCK_CLOUD.equals(path)) {
            AppLockPropManager.reload(mContext);
        } else if (CloudPropertyManager.PATH_ANTI_VIRUS.equals(path)) {
		    AntiVirusPropertyManager.reload(mContext);
	    } else if (CloudPropertyManager.PATH_RESULT_ADS_CONFIG.equals(path)) {
            ResultAdsPropManager.reload(mContext);
        } else if (CloudPropertyManager.PATH_AV_ADS_CONFIG.equals(path)) {
            AvAdsPropManager.reload(mContext);
        } else if (CloudPropertyManager.PATH_ONE_TAP_BOOST_ADS_CONFIG.equals(path)) {
            OneTapBoostAdsConfigManager.reload(mContext);
        }*/
    }


    public boolean isDirty(String[] pathes) throws RemoteException {
        if (pathes == null)
            return false;
        synchronized (sPropInstance) {
            for (int i = 0; i < pathes.length; i++) {
                if (!sPropInstance.containsKey(pathes[i]))
                    return true;
            }
        }
        return false;
    }


    public long getLong(String path, String key, long def)
            throws RemoteException {
        long val;
        String sVal = getString(path, key, null);
        try {
            val = Long.parseLong(sVal);
        } catch (Exception e) {
            val = def;
        }
        return val;
    }

}
