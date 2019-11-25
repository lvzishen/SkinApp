package com.goodmorning.view.recyclerview.normal;

import java.util.List;

/**
 * 组
 * Created by yeguolong on 17-8-18.
 */
public abstract class GroupListItem implements GroupItem {

    protected int mGroupPosition;
   // public  List<GroupListItem> groupListItems;

    public boolean ispreGroupExpend = false  ;


    public int getGroupPosition() {
        return mGroupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.mGroupPosition = groupPosition;
    }

    public int getChildCount() {
        List childrenList = getChildrenList();
        if (childrenList != null)
            return childrenList.size();
        return 0;
    }

}
