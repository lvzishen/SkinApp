package org.thanos.netcore.bean;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.netcore.bean.ContentItem;
import org.thanos.netcore.bean.ResponseData;
import org.thanos.netcore.internal.MorningDataCore;

import static org.thanos.netcore.internal.MorningDataCore.DEBUG;

/**
 * Created by lvzishen on 2019-07-13.
 */
public class CollectDetail extends ResponseData {
    private static final String TAG = MorningDataCore.LOG_PREFIX + "CollectDetail";
    public long _elapse_;

    //{"rqstid":"sdp06nthlrazcwjjbnhw","code":0,"message":"success","_elapse_": 7.156000}

    @SuppressLint("LongLogTag")
    public CollectDetail(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        if (DEBUG) {
            Log.i(TAG, "CollectDetail: " + jsonObject);
        }
        _elapse_ = jsonObject.getLong("_elapse_");
    }

    @Override
    public boolean needCache() {
        return false;
    }

    @Override
    public String toString() {
        return "CollectDetail{" +
                "rqstid='" + requestId + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", _elapse_=" + _elapse_ +
                '}';
    }
}
