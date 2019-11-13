package com.baselib.bitmap.image;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface IImageInfo {
	public String getKey();
	public void setIcon(Drawable dr);
	public Drawable loadIcon(Context cxt);
}
