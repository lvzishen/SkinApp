package com.goodmorning.view.recyclerview.normal;

public interface SelectableWidget {
    public static final int FULL_CHECKED = 0;
    public static final int PARTIALLY_CHECKED = 1;
    public static final int UNCHECKED = 2;

    public int getState();

    /**
     *
     * @param state
     * @return  if we should refresh the item
     */
    public boolean setState(int state);
}
