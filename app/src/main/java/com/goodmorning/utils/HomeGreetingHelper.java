package com.goodmorning.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baselib.cloud.CloudPropertyManager;
import com.baselib.cloud.CloudPropertyManagerBridge;
import com.baselib.sp.SharedPref;
import com.goodmorning.config.GlobalConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class HomeGreetingHelper {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "HomeGreetingHelper";

    public static String showText(Context context) {
        String langSP = SharedPref.getString(context, SharedPref.LANGUAGE, "en");
        String dayRule = CloudPropertyManager.getString(context, CloudPropertyManager.PATH_HOME_GREETING,
                "h.h.t.g." + langSP, null);
        if (TextUtils.isEmpty(dayRule) && !TextUtils.isEmpty(langSP) && !"en".equalsIgnoreCase(langSP)) {
            dayRule = CloudPropertyManager.getString(context, CloudPropertyManager.PATH_HOME_GREETING,
                    "h.h.t.g.en", null);
        }
        if (TextUtils.isEmpty(dayRule)) {
            return null;
        }
        try {
            JSONArray jsonArray = new JSONArray(dayRule);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                int from = jsonObject.optInt("from");
                int to = jsonObject.optInt("to");
                String content = jsonObject.optString("content");
                Calendar calendar = Calendar.getInstance();
                int h = calendar.get(Calendar.HOUR_OF_DAY);
                int m = calendar.get(Calendar.MINUTE);
                int curr = h * 60 + m;


                if ((curr >= from && curr <= to)) {
                    if (DEBUG) {
                        Log.i(TAG, "showText: "+jsonObject.toString());
                    }
                    return content;
                }
            }
        } catch (JSONException e) {
            if (DEBUG) {
                Log.e(TAG, "isDayLimit: ", e);
            }
        }
        return null;
    }
}
