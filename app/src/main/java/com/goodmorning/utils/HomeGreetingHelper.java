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
    public static final int MORNING = 1;
    public static final int AFTERNOON = 2;
    public static final int NIGHT = 3;

    public static String showText(Context context) {
        String dayRule = getGreetingInfo(context);
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
//                int m = calendar.get(Calendar.MINUTE);
//                int curr = h * 60 + m;


                if ((h >= from && h < to)) {
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

    /**
     * 获取每日时间状态
     * @param context
     * @return
     */
    public static int dayTimeStatus(Context context){
        String dayRule = getGreetingInfo(context);
        if (TextUtils.isEmpty(dayRule)) {
            return 0;
        }
        try {
            JSONArray jsonArray = new JSONArray(dayRule);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                int from = jsonObject.optInt("from");
                int to = jsonObject.optInt("to");
                Calendar calendar = Calendar.getInstance();
                int h = calendar.get(Calendar.HOUR_OF_DAY);
//                int m = calendar.get(Calendar.MINUTE);
//                int curr = h * 60 + m;


                if ((h >= from && h < to)) {
                    return ++i;
                }
            }
        } catch (JSONException e) {
            if (DEBUG) {
                Log.e(TAG, "isDayLimit: ", e);
            }
        }
        return 0;
    }

    private static String getGreetingInfo(Context context) {
        String langSP = SharedPref.getString(context, SharedPref.LANGUAGE, "en");
        String dayRule = CloudPropertyManager.getString(context, CloudPropertyManager.PATH_HOME_GREETING,
                "h.h.t.g." + langSP, null);
        if (TextUtils.isEmpty(dayRule) && !TextUtils.isEmpty(langSP) && !"en".equalsIgnoreCase(langSP)) {
            dayRule = CloudPropertyManager.getString(context, CloudPropertyManager.PATH_HOME_GREETING,
                    "h.h.t.g.en", null);
        }
        return dayRule;
    }
}
