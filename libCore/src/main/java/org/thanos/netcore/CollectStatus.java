package org.thanos.netcore;

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
public class CollectStatus extends ResponseData {
    private static final String TAG = MorningDataCore.LOG_PREFIX + "CollectDetail";
    public long _elapse_;
    public CollectItem item;

    @SuppressLint("LongLogTag")
    public CollectStatus(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        if (DEBUG) {
            Log.i(TAG, "CollectDetail: " + jsonObject);
        }
        _elapse_ = jsonObject.getLong("_elapse_");
        JSONArray listArray = jsonObject.getJSONArray("data");
        if (listArray.length() > 0) {
            JSONObject obj = listArray.getJSONObject(0);
            item = CollectItem.createFromJSONObject(obj, requestId);
        } else {
            item = null;
        }
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
