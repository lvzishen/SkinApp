package org.thanos.netcore.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.netcore.internal.MorningDataCore;

import java.io.Serializable;
import java.util.ArrayList;

import static org.thanos.netcore.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-19.
 * 服务器返回的"模块"
 */
public class ModuleItem extends ContentItem implements Serializable {
    private static final long serialVersionUID = -5106451287528375178L;
    private static final String TAG = MorningDataCore.LOG_PREFIX + "ModuleItem";
    /**
     * 模块数据集合
     */
    public final ArrayList<ContentItem> subList = new ArrayList<>();
    private final String title;
    private final String iconUrl;
    private final int jumpChannel;

    ModuleItem(JSONObject jsonObject, String requestId) throws JSONException {
        super(jsonObject, requestId);
        status = jsonObject.optInt("status");
        title = jsonObject.optString("title");
        iconUrl = jsonObject.optString("icon_url");
        jumpChannel = jsonObject.optInt("jump_channel");
        JSONArray subListArray = jsonObject.optJSONArray("sub_list");
        if (subListArray != null) {
            for (int i = 0, len = subListArray.length(); i < len; i++) {
                ContentItem object = ContentItem.createFromJSONObject(subListArray.getJSONObject(i), requestId);
                if (DEBUG) {
                    Log.i(TAG, "ModuleItem: ");
                }
                if (object == null) {
                    continue;
                }
                subList.add(object);
            }
        }
    }

    @Override
    public String toString() {
        return DEBUG ? "ModuleItem{" +
                "subList=" + subList +
                ", type=" + type +
                ", id=" + id +
                ", category=" + category +
                '}' : "";
    }
}
