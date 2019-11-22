package org.thanos.netcore.bean;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.netcore.internal.MorningDataCore;

import static org.thanos.netcore.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-13.
 */
public class ContentDetail extends ResponseData {
    private static final String TAG = MorningDataCore.LOG_PREFIX + "ContentDetail";
    public final String newsCountry;
    public final ContentItem item;

    @SuppressLint("LongLogTag")
    public ContentDetail(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        if (DEBUG) {
            Log.i(TAG, "ContentDetail: " + jsonObject);
        }
        JSONObject dataObj = jsonObject.getJSONObject("data");
        newsCountry = dataObj.getString("newsCountry");
        JSONArray listArray = dataObj.getJSONArray("list");
        if (listArray.length() > 0) {
            JSONObject obj = listArray.getJSONObject(0);
            item = ContentItem.createFromJSONObject(obj, requestId);
        } else {
            item = null;
        }
    }

    @Override
    public boolean needCache() {
        return item != null;
    }

    @Override
    public String toString() {
        return DEBUG ? "ContentDetail{" +
                "newsCountry='" + newsCountry + '\'' +
                ", item=" + item +
                '}' :"";
    }
}
