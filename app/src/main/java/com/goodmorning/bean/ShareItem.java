package com.goodmorning.bean;

import android.content.Context;

import com.creativeindia.goodmorning.R;
import com.goodmorning.view.recyclerview.normal.IItem;

/**
 * 创建日期：2019/11/25 on 14:08
 * 描述:
 * 作者: lvzishen
 */
public class ShareItem implements IItem {
    public int type;
    public String text;
    public int imageResource;
    public static final int WHATSAPP = 1;
    public static final int FACEBOOK = 2;
    public static final int INSTAGRAM = 3;
    public static final int MESSAGE = 4;
    public static final int MORE = 5;
    public static final int COPY = 6;
    public static final int SAVE = 7;

    public ShareItem(Context context, int type) {
        this.type = type;
        switch (type) {
            case WHATSAPP:
                text = context.getResources().getString(R.string.whatsapp);
                imageResource = R.drawable.ic_share_whatsapp;
                break;
            case FACEBOOK:
                text = context.getResources().getString(R.string.facebook);
                imageResource = R.drawable.ic_share_facebook;
                break;
            case INSTAGRAM:
                text = context.getResources().getString(R.string.instagram);
                imageResource = R.drawable.ic_share_ins;
                break;
            case MESSAGE:
                text = context.getResources().getString(R.string.message);
                imageResource = R.drawable.ic_share_message;
                break;
            case MORE:
                text = context.getResources().getString(R.string.more);
                imageResource = R.drawable.ic_share_more;
                break;
            case COPY:
                text = context.getResources().getString(R.string.copy);
                imageResource = R.drawable.ic_share_copy;
                break;
            case SAVE:
                text = context.getResources().getString(R.string.save);
                imageResource = R.drawable.ic_share_save;
                break;
        }
    }

    public static final int NORMAL = 1000;

    @Override
    public int getType() {
        return NORMAL;
    }
}
