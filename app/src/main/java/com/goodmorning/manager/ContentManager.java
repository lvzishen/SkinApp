package com.goodmorning.manager;

import com.baselib.language.LanguageUtil;
import com.baselib.sp.SharedPref;

import org.thanos.netcore.bean.ChannelList;
import org.thanos.netcore.helper.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class ContentManager {
    private JsonHelper<ArrayList<ChannelList.LangCategoryInfo>> jsonHelper;
    private ContentManager(){
        jsonHelper = new JsonHelper<ArrayList<ChannelList.LangCategoryInfo>>() {
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
        for (ChannelList.LangCategoryInfo langCategoryInfo : channellist){
            if (LanguageUtil.getLanguage().equals(langCategoryInfo.lang)){
                return langCategoryInfo;
            }
        }
        return null;
    }

}
