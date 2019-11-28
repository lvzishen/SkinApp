package com.goodmorning.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.creativeindia.goodmorning.R;
import com.goodmorning.config.GlobalConfig;


public class ImageUtil {
    private static final String TAG = "ImageUtil";
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    // scaleType
    public static void displayImage(Context context, ImageView imageView, String url, int placeHolderResId) {
        displayImage(context, imageView, url, placeHolderResId, new CenterCrop(context));
        if (DEBUG) {
            Log.i(TAG, "displayImage: " + url);
        }
    }

    public static void displayImageWithAdjustImageView(Context context, ImageView imageView, String url, int placeHolderResId, int outWidth, int outHeight) {
        displayImage(context, imageView, url, placeHolderResId, new PortraitCoverBitmapCenterCrop(context, outWidth, outHeight));
        if (DEBUG) {
            Log.i(TAG, "displayImage: " + url);
        }
    }


    /**
     * 支持外部指定输出尺寸的CenterCrop，目前至于短视频列表里的封面图用到，因为其View会自动改变大小。会导致已经设置好的图像被拉伸，所以这里从外部指定View的最终大小
     */
    public static class PortraitCoverBitmapCenterCrop extends CenterCrop {

        private int outWidth;
        private int outHeight;

        PortraitCoverBitmapCenterCrop(Context context, int outWidth, int outHeight) {
            super(context);
            this.outWidth = outWidth;
            this.outHeight = outHeight;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return super.transform(pool, toTransform, this.outWidth, this.outHeight);
        }
    }


    public static void displayImage(Context context, ImageView imageView, String url, int placeHolderResId, BitmapTransformation... transformations) {
        DrawableTypeRequest<String> drawableTypeRequest = Glide.with(context).load(url);
        drawableTypeRequest.placeholder(placeHolderResId);
        drawableTypeRequest.transform(transformations);
        drawableTypeRequest.dontAnimate();
        drawableTypeRequest.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        drawableTypeRequest.into(imageView);
        if (DEBUG) {
            Log.i(TAG, "displayImage: " + url);
        }
    }


    public static void healthAdjustImageView(Context context, ImageView imageView, String url, int placeHolderResId, int outWidth, int outHeight) {
        healthDisplayImage(context, imageView, url, placeHolderResId, outWidth, outHeight);
    }


    private static void healthDisplayImage(Context context, ImageView imageView, String url, int placeHolderResId, int outWidth, int outHeight) {
        DrawableTypeRequest<String> drawableTypeRequest = Glide.with(context).load(url);
        drawableTypeRequest.placeholder(placeHolderResId);
        drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        drawableTypeRequest.transform(new PortraitCoverBitmapCenterCrop(context, outWidth, outHeight), new GlideRoundTransform(context, 10));
        drawableTypeRequest.dontAnimate();
        drawableTypeRequest.into(imageView);
        if (DEBUG) {
            Log.i(TAG, "healthDisplayImage: " + url);
        }
    }


    public static void displayImageView(Context context, ImageView imageView, String url, int placeHolderResId,int width, int heghit) {
        Glide.clear(imageView);
        DrawableTypeRequest<String> drawableTypeRequest = Glide.with(context).load(url);
        drawableTypeRequest.placeholder(placeHolderResId);
        drawableTypeRequest.error(placeHolderResId);
        boolean isGif = isGif(url);
        if (isGif) {
            drawableTypeRequest.asGif();
        }
        if (width > 0 && heghit > 0 && !isGif) {
            drawableTypeRequest.override(width, heghit);//加载特定宽度高度的图片
        }
        drawableTypeRequest.transform(new GlideRoundTransform(context, 6));
        drawableTypeRequest.into(imageView);
        if (DEBUG) {
            Log.i(TAG, "displayImage: " + url);
        }
    }

    public static SimpleTarget<GlideDrawable> getImageDrawable(Context context, String url, final OnImageLoadListener<Drawable> listener) {
        return Glide.with(context).load(url).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (listener != null) {
                    listener.loadSuccess(resource);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                if (listener != null) {
                    listener.loadFail();
                }
            }
        });
    }

//    public static void loadUrlCenterCrop(Context context, String url, int placeHolder, RecognitionCenterImageView imageView, int corner) {
//        try {
//            Glide.with(context).load(url).placeholder(placeHolder).transform(new CenterCrop(context), new GlideRoundTransform(context, corner)).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
//        } catch (Exception e) {
//            if (GlobalConfig.DEBUG) {
//                e.printStackTrace();
//            }
//        }
//    }


    public interface OnImageLoadListener<T> {
        void loadSuccess(T resource);

        void loadFail();
    }


    /**
     * 回收view占用的内存
     *
     * @param view
     */
    public static void unbindDrawables(View view) {
        if (view == null) {
            return;
        }
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }

        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }


    /**
     * 刷新view显示状态
     *
     * @param v
     * @param visibility
     */
    public static void updateVisibility(View v, int visibility) {
        if (v == null) {
            return;
        }
        if (v.getVisibility() == visibility) {
            return;
        }
        v.setVisibility(visibility);
    }





    private static boolean isGif(String url) {
        return getFileExt(url).equals(".gif");
    }

    private static String getFileExt(String path) {
        if (path != null && path.length() != 0) {
            int lastDotIndex = path.lastIndexOf(".");
            if (lastDotIndex >= 0) {
                String ext = path.substring(lastDotIndex);
                if (ext != null) {
                    ext = ext.toLowerCase();
                    if (ext.startsWith(".gif")) {
                        return ".gif";
                    } else if (ext.startsWith(".png")) {
                        return ".png";
                    } else if (ext.startsWith(".jpeg")) {
                        return ".jpeg";
                    } else {
                        return ".jpg";
                    }
                }
            }
        }
        return ".jpg";
    }

}
