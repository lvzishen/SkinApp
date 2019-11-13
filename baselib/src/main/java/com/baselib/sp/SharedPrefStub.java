package com.baselib.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Set;

/**
 * Created by tangchun on 2017/3/22.extends ISharedPref.Stub
 */

public class SharedPrefStub {

    private static SharedPrefStub sInstance = null;

    public static SharedPrefStub getInstance(Context cxt) {
        synchronized (SharedPrefStub.class) {
            if (sInstance == null)
                sInstance = new SharedPrefStub(cxt);
        }
        return sInstance;
    }

    private Context mContext = null;

    private SharedPrefStub(Context cxt) {
        mContext = cxt.getApplicationContext();
    }

    public void putLong(String name, String key, long value) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public void putInteger(String name, String key, int value) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public void putString(String name, String key, String value) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public void putBoolean(String name, String key, boolean value) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public void clear(String name) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
        }
    }

    public void clearKey(String name, String key) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public long getLong(String name, String key, long def) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            return pref.getLong(key, def);
        }
        return def;
    }

    public int getInteger(String name, String key, int def) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            return pref.getInt(key, def);
        }
        return def;
    }

    public String getString(String name, String key, String def) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            return pref.getString(key, def);
        }
        return def;
    }

    public boolean getBoolean(String name, String key, boolean def) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            return pref.getBoolean(key, def);
        }
        return def;
    }

    public String getStrings(String name, String key, String def) {
        if (TextUtils.isEmpty(name))
            name = SharedPref.NAME;
        SharedPreferences pref = mContext.getSharedPreferences(name, 0);
        if (pref != null) {
            Set<String> res = pref.getStringSet(key, null);
            if (res == null || res.isEmpty())
                return def;
            StringBuilder sb = new StringBuilder();
            for (String val : res) {
                sb.append(val);
                sb.append(",");
            }
            return sb.toString();
        }
        return def;
    }
}
