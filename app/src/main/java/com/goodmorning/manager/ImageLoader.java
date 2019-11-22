package com.goodmorning.manager;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;

public class ImageLoader {
    public static void displayImageByName(Context context, String url, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        Glide.with(context).load(url).placeholder(placeholder).error(error).into(imageView);
    }
}
