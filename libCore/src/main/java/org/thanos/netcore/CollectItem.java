package org.thanos.netcore;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 创建日期：2019/11/27 on 18:03
 * 描述:
 * "id":1531816782958026,
 * "collect":1
 * 作者: lvzishen
 */
public class CollectItem implements Serializable {

    public int id;
    public int collect;

    public static CollectItem createFromJSONObject(JSONObject jsonObject, String requestId) throws JSONException {
        CollectItem collectItem = new CollectItem();
        collectItem.id = jsonObject.getInt("id");
        collectItem.collect = jsonObject.getInt("collect");
        return collectItem;
    }

}
