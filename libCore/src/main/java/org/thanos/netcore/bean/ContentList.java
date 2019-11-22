package org.thanos.netcore.bean;

import android.util.Log;

import org.interlaken.common.XalContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.netcore.MorningDataAPI;
import org.thanos.netcore.internal.MorningDataCore;

import java.util.ArrayList;
import java.util.HashSet;

import static org.thanos.netcore.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-12.
 * 列表数据
 */
public class ContentList extends ResponseData {
    private static final String TAG = MorningDataCore.LOG_PREFIX + "ContentList";
    /**
     * 所属国家
     */
    public final String newsCountry;
    /**
     * 来源，可能为null
     */
    public final String powerBy;
    /**
     * 内容集合
     */
    public final ArrayList<ContentItem> items = new ArrayList<>();
    /**
     * 置顶内容集合
     */
    public final ArrayList<ContentItem> topItems = new ArrayList<>();
    /**
     * 推广活动
     */
    public final ArrayList<ContentItem> promotionItems = new ArrayList<>();
    /**
     * 频道列表
     */
    public final ArrayList<ChannelList.LangCategoryInfo> langCategoryInfos = new ArrayList<>();

    public ContentList(JSONObject jo) throws JSONException {
        super(jo);

        JSONObject data = jo.optJSONObject("data");
        if (data != null) {
            newsCountry = data.optString("newsCountry");
            powerBy = data.optString("power_by");

            HashSet<Long> dislikeContentIds = MorningDataCore.getDislikeContentIds(XalContext.getContext());
            JSONArray listArray = data.optJSONArray("list");
            if (listArray != null) {
                for (int i = 0, len = listArray.length(); i < len; i++) {
                    JSONObject jsonObject = listArray.getJSONObject(i);
                    ContentItem contentListItem = ContentItem.createFromJSONObject(jsonObject, requestId);
                    if (DEBUG) {
                        Log.i(TAG, "NewsList: " + i + ", " + contentListItem);
                    }
                    if (contentListItem == null) {
                        continue;
                    }
                    if (dislikeContentIds.contains(contentListItem.id)) {
                        if (DEBUG) {
                            Log.i(TAG, "ContentList: 这条数据已经被此用户标记为不喜欢的了，忽略, " + contentListItem.id);
                        }
                        continue;
                    }
                    items.add(contentListItem);
                }
            }
            parseList(data.optJSONArray("top"), topItems, dislikeContentIds, this.requestId);
            parseList(data.optJSONArray("promotion"), promotionItems, dislikeContentIds, requestId);

            // 频道列表
            JSONArray channelArray = data.optJSONArray("channels");
            if (channelArray != null && channelArray.length() > 0) {
                langCategoryInfos.addAll(ChannelList.createLangCategoryInfos(channelArray.getJSONObject(0).getJSONArray("channels")));
            }
        } else {
            newsCountry = null;
            powerBy = null;
        }
    }

    static boolean isDislikeContent(HashSet<Long> dislikeContentIds, long id) {
        if (dislikeContentIds.contains(id)) {
            if (DEBUG) {
                Log.i(TAG, "这条数据已经被此用户标记为不喜欢的了，忽略, " + id);
            }
            return true;
        }
        return false;
    }

    private static void parseList(JSONArray jsonArray, ArrayList<ContentItem> contentItems, HashSet<Long> dislikeContentIds, String requestId) throws JSONException {
        if (jsonArray != null) {
            for (int i = 0, len = jsonArray.length(); i < len; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentItem contentListItem = ContentItem.createFromJSONObject(jsonObject, requestId);
                if (DEBUG) {
                    Log.i(TAG, "TopList: " + i + ", " + contentListItem);
                }
                if (contentListItem == null) {
                    continue;
                }
                if (MorningDataAPI.isYouTuBeVideo(contentListItem.type)) {
                    if (isDislikeContent(dislikeContentIds, contentListItem.id)) {
                        continue;
                    }
                    contentItems.add(contentListItem);
                }
                switch (contentListItem.type) {
                    case MorningDataAPI.RESOURCE_TYPE_NEWS:// 新闻
                    case MorningDataAPI.RESOURCE_TYPE_MODULE:// 模块
                        ModuleItem moduleItem = (ModuleItem) contentListItem;
                        ArrayList<ContentItem> subList = moduleItem.subList;
                        for (ContentItem contentItem : subList) {
                            if (isDislikeContent(dislikeContentIds, contentItem.id)) {
                                continue;
                            }
                            contentItems.add(contentItem);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public boolean needCache() {
        // 有一个列表不为空，就有必要缓存
        return !this.items.isEmpty() || !this.topItems.isEmpty();
    }
}
