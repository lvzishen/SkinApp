package com.goodmorning.view.recyclerview.normal;


import java.util.List;

public interface GroupItem<C extends ChildItem> extends IItem {
	
	boolean isExpand();

	void setIsExpand(boolean isExpand);

	List<C> getChildrenList();

	int getType();

}
