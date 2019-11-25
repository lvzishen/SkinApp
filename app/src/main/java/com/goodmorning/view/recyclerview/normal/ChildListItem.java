package com.goodmorning.view.recyclerview.normal;

/**
 * Created by yeguolong on 17-8-18.
 */
public abstract class ChildListItem implements ChildItem {

    protected int mGroupPosition;
    protected int mChildPosition;

    public int getChildPosition() {
        return mChildPosition;
    }

    public void setChildPosition(int position) {
        this.mChildPosition = position;
    }

    public int getGroupPosition() {
        return mGroupPosition;
    }

    public void setGroupPosition(int position) {
        this.mGroupPosition = position;
    }

}
