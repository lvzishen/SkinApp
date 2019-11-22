package org.thanos.netcore.bean;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.netcore.internal.MorningDataCore;

import java.io.Serializable;
import java.util.ArrayList;

import static org.thanos.netcore.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-11.
 * 服务器返回的频道列表
 */
public class ChannelList extends ResponseData {
    private static final String TAG = MorningDataCore.LOG_PREFIX + "ChannelList";
    /**
     * 服务端下发的新闻国家
     */
    public final String newsCountry;
    /**
     * 服务端下发的新闻语言
     */
    public final String lang;
    /**
     * menu字段的变化时间，时间戳（秒）
     */
    public final int menuUtime;
    public final ArrayList<LangCategoryInfo> langCategoryInfos = new ArrayList<>();
    public final ArrayList<LanguageItem> languageItems = new ArrayList<>();

    public ChannelList(JSONObject jo) throws JSONException {
        super(jo);
        JSONObject dataObj = jo.getJSONObject("data");
        newsCountry = dataObj.getString("newsCountry");
        lang = dataObj.getString("lang");
        menuUtime = dataObj.getInt("menuUtime");
        JSONArray channels = dataObj.getJSONArray("channels");
        for (int i = 0, len = channels.length(); i < len; i++) {
            LangCategoryInfo langCategoryInfo = new LangCategoryInfo(channels.getJSONObject(i));
            if (DEBUG) {
                Log.i(TAG, "ChannelList: " + i + ", " + langCategoryInfo);
            }
            langCategoryInfos.add(langCategoryInfo);
        }
        JSONArray menuArray = dataObj.getJSONArray("menu");
        for (int i = 0, len = menuArray.length(); i < len; i++) {
            JSONObject menuObj = menuArray.getJSONObject(i);
            LanguageItem languageItem = new LanguageItem(menuObj);
            if (DEBUG) {
                Log.i(TAG, "ChannelList: " + i + ", " + languageItem);
            }
            languageItems.add(languageItem);
        }
    }

    static ArrayList<LangCategoryInfo> createLangCategoryInfos(JSONArray langCategoryInfoArray) throws JSONException {
        ArrayList<LangCategoryInfo> langCategoryInfos = new ArrayList<>();
        for (int i = 0, len = langCategoryInfoArray.length(); i < len; i++) {
            LangCategoryInfo langCategoryInfo = new LangCategoryInfo(langCategoryInfoArray.getJSONObject(i));
            if (DEBUG) {
                Log.i(TAG, "createLangCategoryInfos: " + i + ", " + langCategoryInfo);
            }
            langCategoryInfos.add(langCategoryInfo);
        }
        return langCategoryInfos;
    }

    /**
     * 暴露给ThanosSDK的工具方法，创建LanguageItem对象
     *
     * @param newsCountry
     * @param lang
     * @return
     */
    public static LanguageItem createLanguageItem(String newsCountry, String lang) {
        if (TextUtils.isEmpty(newsCountry) || TextUtils.isEmpty(lang)) {
            return null;
        }
        return new LanguageItem(newsCountry, lang);
    }

    @Override
    public boolean needCache() {
        // 至少一种语言里有频道信息才需要缓存
        for (LangCategoryInfo langCategoryInfo : langCategoryInfos) {
            if (!langCategoryInfo.categoryList.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return  DEBUG ?"ChannelList{" +
                "newsCountry='" + newsCountry + '\'' +
                ", lang='" + lang + '\'' +
                ", menuUtime=" + menuUtime +
                ", langCategoryInfos=" + langCategoryInfos +
                ", languageItems=" + languageItems +
                '}' :"";
    }

    /**
     * 单种语言下的分类信息
     */
    public static class LangCategoryInfo {
        /**
         * 语言代码，比如en
         */
        public final String lang;
        /**
         * 语言名称，比如English
         */
        public final String text;
        /**
         * 是否默认
         */
        public final int isDefault;
        /**
         * 客户端是否可以搜索，0不可以，1可以
         */
        public final int search;
        /**
         * 语言风格，1 从左向右读，2 从右向左读
         */
        public final int langStyle;
        /**
         * cates 或者 text字段变化的时间，时间戳（秒）
         */
        public final int catesTextUtime;
        /**
         * 普通频道列表
         */
        public final ArrayList<Category> categoryList = new ArrayList<>();
        /**
         * 视频频道列表
         */
        public final ArrayList<Category> videoCategoryList = new ArrayList<>();

        LangCategoryInfo(JSONObject jo) throws JSONException {
            lang = jo.optString("lang");
            text = jo.optString("text");
            isDefault = jo.optInt("isDefault");
            search = jo.optInt("search");
            langStyle = jo.optInt("langStyle");
            catesTextUtime = jo.optInt("catesTextUtime");

            // 新闻频道列表
            parseCateList(jo.optJSONArray("cates"), categoryList);
            // 视频频道列表
            parseCateList(jo.optJSONArray("videocates"), videoCategoryList);
            // 返回数据里还有photocates/buzzcates这些数据，目前没用
        }

        private static void parseCateList(JSONArray array, ArrayList<Category> categories) throws JSONException {
            if (array != null) {
                for (int i = 0, len = array.length(); i < len; i++) {
                    Category category = new Category(array.getJSONObject(i));
                    if (DEBUG) {
                        Log.i(TAG, "parseCateList: " + i + ", " + category);
                    }
                    categories.add(category);
                }
            }
        }

        @Override
        public String toString() {
            return DEBUG? "LangCategoryInfo{" +
                    "lang='" + lang + '\'' +
                    ", text='" + text + '\'' +
                    ", isDefault=" + isDefault +
                    ", search=" + search +
                    ", langStyle=" + langStyle +
                    ", catesTextUtime=" + catesTextUtime +
                    ", categoryList=" + categoryList +
                    ", videoCategoryList=" + videoCategoryList +
                    '}':"";
        }
    }

    /**
     * 单个分类信息
     */
    public static class Category implements Serializable {
        private static final long serialVersionUID = -6748575921673144218L;
        /**
         * 分类ID
         */
        public final int id;
        /**
         * 展示文本
         */
        public final String text;
        /**
         * 分类图标
         */
        public final String icon;
        /**
         * 分类Banner
         */
        public final String image;
        /**
         * 是否订阅.1是，0否
         */
        public final int isSubscribe;
        public final ArrayList<CategorySubClass> categorySubClassList = new ArrayList<>();

        Category(JSONObject jo) throws JSONException {
            id = jo.getInt("id");
            text = jo.optString("text");
            icon = jo.optString("icon");
            image = jo.optString("image");
            isSubscribe = jo.optInt("isSubscribe");
            JSONArray subclassArray = jo.optJSONArray("subclass");
            if (subclassArray != null) {
                for (int i = 0, len = subclassArray.length(); i < len; i++) {
                    CategorySubClass categorySubClass = new CategorySubClass(subclassArray.getJSONObject(i));
                    if (DEBUG) {
                        Log.i(TAG, "Category: " + i + ", " + categorySubClass);
                    }
                    categorySubClassList.add(categorySubClass);
                }
            }
        }

        @Override
        public String toString() {
            return DEBUG ?"Category{" +
                    "id=" + id +
                    ", text='" + text + '\'' +
                    ", icon='" + icon + '\'' +
                    ", image='" + image + '\'' +
                    ", isSubscribe=" + isSubscribe +
                    ", categorySubClassList=" + categorySubClassList +
                    '}' :"";
        }
    }

    /**
     * 二级分类信息
     */
    public static class CategorySubClass {
        private final int id;
        private final String text;
        private final int checked;

        CategorySubClass(JSONObject jo) throws JSONException {
            id = jo.optInt("id");
            text = jo.optString("text");
            checked = jo.optInt("checked");
        }

        @Override
        public String toString() {
            return DEBUG ? "CategorySubClass{" +
                    "id=" + id +
                    ", text='" + text + '\'' +
                    ", checked=" + checked +
                    '}' :"";
        }
    }

    public static class LanguageItem {
        public final int priority;
        public final String country;
        public final String lang;
        public final String text;


        public LanguageItem(String country, String lang) {
            this.country = country;
            this.lang = lang;
            this.priority = 0;
            this.text = "";
        }

        LanguageItem(JSONObject jo) throws JSONException {
            priority = jo.optInt("priority");
            country = jo.optString("country");
            lang = jo.optString("lang");
            text = jo.optString("text");
        }

        @Override
        public String toString() {
            return DEBUG ?"LanguageItem{" +
                    "priority=" + priority +
                    ", country='" + country + '\'' +
                    ", lang='" + lang + '\'' +
                    ", text='" + text + '\'' +
                    '}' :"";
        }
    }
}
