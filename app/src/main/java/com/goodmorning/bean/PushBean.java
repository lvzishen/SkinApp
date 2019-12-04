package com.goodmorning.bean;

/**
 * 创建日期：2019/12/4 on 12:07
 * 描述:
 * 作者: lvzishen
 */

public class PushBean {
    /**
     * action_main : xapplink://com.augeapps.locker/credit/order?order_id=200040289523149201
     * sub_title : Typin
     * action_button :
     * feedback_prob : 0
     * icon :
     * description : 10013
     * message_type : 10013
     * title : 10013
     * button :
     * card_style : 2
     * replace_old :
     * extra :
     * big_image :
     * tag :
     * id :
     */

    public String action_main;
    public String sub_title;
    public String action_button;
    public String feedback_prob;
    public String icon;
    public String description;
    public String message_type;
    public String title;
    public String button;
    public String card_style;
    public String replace_old;
    public DayPicture extra;
    public String big_image;
    public String tag;
    public String id;

    @Override
    public String toString() {
        return "PushBean{" +
                "action_main='" + action_main + '\'' +
                ", sub_title='" + sub_title + '\'' +
                ", action_button='" + action_button + '\'' +
                ", feedback_prob='" + feedback_prob + '\'' +
                ", icon='" + icon + '\'' +
                ", description='" + description + '\'' +
                ", message_type='" + message_type + '\'' +
                ", title='" + title + '\'' +
                ", button='" + button + '\'' +
                ", card_style='" + card_style + '\'' +
                ", replace_old='" + replace_old + '\'' +
                ", extra=" + extra.toString() +
                ", big_image='" + big_image + '\'' +
                ", tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
