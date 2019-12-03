package com.goodmorning.manager;

import android.util.Log;

import com.baselib.language.LanguageType;
import com.baselib.language.LanguageUtil;
import com.baselib.sp.SharedPref;

import org.thanos.netcore.bean.ChannelList;
import org.thanos.netcore.helper.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class ContentManager {
    private JsonHelper<ArrayList<ChannelList.LangCategoryInfo>> jsonHelper;
    private JsonHelper<ArrayList<ChannelList.LanguageItem> > jsonLangelper;
    private boolean changeLang = false;
    private boolean isLogin = false;
    private ContentManager(){
        jsonHelper = new JsonHelper<ArrayList<ChannelList.LangCategoryInfo>>() {
        };
        jsonLangelper = new JsonHelper<ArrayList<ChannelList.LanguageItem>>() {
        };
    }

    private static class ContentHolder{
        private static ContentManager contentManager = new ContentManager();
    }

    public static ContentManager getInstance(){
        return ContentHolder.contentManager;
    }

    public ChannelList.LangCategoryInfo getChannelContent(){
        ArrayList<ChannelList.LangCategoryInfo> channellist = jsonHelper.getJsonObject(SharedPref.getString(getApplicationContext(), SharedPref.CHANNEL_CONTENT,""));
        if (channellist == null){
            return null;
        }
        for (ChannelList.LangCategoryInfo langCategoryInfo : channellist){
            if (LanguageUtil.getLanguage().equals(langCategoryInfo.lang)){
                return langCategoryInfo;
            }
        }
        return null;
    }

    public ArrayList<ChannelList.LanguageItem> getLanguageList(){
        ArrayList<ChannelList.LanguageItem> languageItems = jsonLangelper.getJsonObject(SharedPref.getString(getApplicationContext(), SharedPref.LANGUAGE_TYPE,""));
        return languageItems;
    }

    public String getLang(){
        String language = SharedPref.getString(getApplicationContext(),SharedPref.LANGUAGE, LanguageType.ENGLISH.getLanguage());
        ArrayList<ChannelList.LanguageItem> langs = getLanguageList();
        if (langs == null){
            return getLangText(language);
        }
        for (ChannelList.LanguageItem languageItem : langs){
            if (language.equals(languageItem.lang)){
                return languageItem.text;
            }
        }
        return getLangText(language);
    }

    public String getLangText(String lang){
        if (LanguageType.ENGLISH.getLanguage().equals(lang)){
            return "English";
        }else if (LanguageType.HINDI.getLanguage().equals(lang)){
            return "हिन्दी";
        }else if (LanguageType.TAMIL.getLanguage().equals(lang)){
            return "தமிழ";
        }
        return "English";
    }

    public boolean isChangeLang() {
        return changeLang;
    }

    public void setChangeLang(boolean changeLang) {
        this.changeLang = changeLang;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
