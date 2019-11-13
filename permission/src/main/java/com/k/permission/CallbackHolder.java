package com.k.permission;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 单例对象，用于保存传递的回调对象
 * Created by libo on 2018/3/19.
 */
public class CallbackHolder {
    public static final String KEY_ACTIVITY_INTENT_DATA = "key_activity_intent_data";

    public Map<String, CheckCallback> holderData;

    private CallbackHolder() {
        holderData = new HashMap<>();
    }

    public String put(CheckCallback obj) {
        String key = UUID.randomUUID().toString();
        holderData.put(key, obj);
        return key;
    }

    public String put(String key, CheckCallback obj) {
        if(TextUtils.isEmpty(key)) {
            throw new RuntimeException("param error");
        }

        holderData.put(key, obj);
        return key;
    }

    /**
     * 取完数据以后就清掉的方式，避免忘记删除数据导致一直在内存中
     * @param key
     * @return
     */
    public CheckCallback get(String key) {
        CheckCallback obj = holderData.get(key);
        holderData.remove(key);

        return obj;
    }

    public void remove(String key) {
        holderData.remove(key);
    }

    private void clear() {
        holderData = new HashMap<>();
    }

    public static CallbackHolder getInstance() {
        return ActivityIntentDataHolder_.instance;
    }

    private static class ActivityIntentDataHolder_ {
        private static final CallbackHolder instance = new CallbackHolder();
    }
}
