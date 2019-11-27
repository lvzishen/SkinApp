package com.goodmorning.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baselib.bitmap.util.DeviceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.creativeindia.goodmorning.R;
import com.goodmorning.adapter.ShareCommonFactory;
import com.goodmorning.adapter.ShareCommonHolder;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.bean.ShareItem;
import com.goodmorning.share.ShareTypeManager;
import com.goodmorning.utils.ScreenUtils;
import com.goodmorning.view.image.RoundedImageView;
import com.goodmorning.view.recyclerview.normal.CommonRecyclerView;
import com.goodmorning.view.recyclerview.normal.IItem;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

import static com.goodmorning.bean.ShareItem.COPY;
import static com.goodmorning.bean.ShareItem.FACEBOOK;
import static com.goodmorning.bean.ShareItem.INSTAGRAM;
import static com.goodmorning.bean.ShareItem.MESSAGE;
import static com.goodmorning.bean.ShareItem.MORE;
import static com.goodmorning.bean.ShareItem.SAVE;
import static com.goodmorning.bean.ShareItem.WHATSAPP;
import static com.goodmorning.utils.ActivityCtrl.TRANSFER_DATA;

/**
 * 创建日期：2019/11/25 on 10:40
 * 描述:
 * 作者: lvzishen
 */
public abstract class BaseDetailActivity extends AppCompatActivity implements ShareCommonHolder.OnClickListener {

    private static final String TAG = "BaseDetailActivity";
    protected FrameLayout mBackLayout;
    protected TextView mTitleView;
    protected FrameLayout misCollectLayout;
    protected TextView mTextDetailView;
    protected RoundedImageView mImageDetailView;
    protected LinearLayout mVideoLayout;
    protected JzvdStd mVideoView;
    protected CommonRecyclerView mShareRv;
    private List<ShareItem> mDatas;
    protected int mType;
    protected DataListItem mDataItem;
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (useStartDefaultAnim()) {
            overridePendingTransition(com.creativeindia.goodmorning.baselib.R.anim.slide_right_in, com.creativeindia.goodmorning.baselib.R.anim.no_slide);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_detail);
        initIntent();
        mBackLayout = findViewById(R.id.back);
        mTitleView = findViewById(R.id.title);
        misCollectLayout = findViewById(R.id.collect);
        mTextDetailView = findViewById(R.id.text_detail);
        mImageDetailView = findViewById(R.id.image_detail);
        mImageDetailView.setCornerRadius(DeviceUtil.dip2px(getApplicationContext(), 4));
        mVideoLayout = findViewById(R.id.video_detail);
        mVideoView = findViewById(R.id.jz_video);
        mShareRv = findViewById(R.id.rv_list);
        setType();
        mShareRv.setCallback(mCallBack);
        mLayoutManager = new GridLayoutManager(this, 4);
        mShareRv.setLayoutManager(mLayoutManager);
        mTitleView.setText(getTextTitle());
        //设置页面类型
        mBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        misCollectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCollect(v);
            }
        });
        initShareDatas();
    }

    private CommonRecyclerView.Callback mCallBack = new CommonRecyclerView.Callback() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
            return ShareCommonFactory.getHolder(context, parent, viewType, BaseDetailActivity.this);
        }

        @Override
        public void buildList(List<IItem> list) {
            list.addAll(mDatas);
        }
    };

    private void initIntent() {
        if (getIntent() != null && getIntent().getSerializableExtra(TRANSFER_DATA) != null) {
            mDataItem = (DataListItem) getIntent().getSerializableExtra(TRANSFER_DATA);
        } else {
            onBackPressed();
        }
    }

    private void initShareDatas() {
        mDatas = new ArrayList<>();
        switch (mType) {
            case DataListItem.DATA_TYPE_1:
                mDatas.add(new ShareItem(this, WHATSAPP));
                mDatas.add(new ShareItem(this, FACEBOOK));
                mDatas.add(new ShareItem(this, MESSAGE));
                mDatas.add(new ShareItem(this, MORE));
                mDatas.add(new ShareItem(this, COPY));
                changeShareCount();
                break;
            case DataListItem.DATA_TYPE_2:
                mDatas.add(new ShareItem(this, WHATSAPP));
                mDatas.add(new ShareItem(this, FACEBOOK));
                mDatas.add(new ShareItem(this, MORE));
                mDatas.add(new ShareItem(this, SAVE));
                break;
            case DataListItem.DATA_TYPE_3:
                mDatas.add(new ShareItem(this, WHATSAPP));
                mDatas.add(new ShareItem(this, FACEBOOK));
                mDatas.add(new ShareItem(this, INSTAGRAM));
                mDatas.add(new ShareItem(this, MORE));
                mDatas.add(new ShareItem(this, SAVE));
                changeShareCount();
                break;
        }
        mShareRv.reloadData();
    }

    private void changeShareCount() {
        mLayoutManager = new GridLayoutManager(this, 5);
        mShareRv.setLayoutManager(mLayoutManager);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) mShareRv.getLayoutParams();
        layoutParams1.leftMargin = DeviceUtil.dip2px(getApplicationContext(), 10);
        layoutParams1.rightMargin = DeviceUtil.dip2px(getApplicationContext(), 10);
        mShareRv.setLayoutParams(layoutParams1);
    }


    @Override
    public void onClick(ShareItem shareItem) {
        switch (shareItem.type) {
            case WHATSAPP:
                ShareTypeManager.shareWithWhatsapp(mType, BaseDetailActivity.this, mDataItem, mBitmap);
                break;
            case FACEBOOK:
                ShareTypeManager.shareWithFaceBook(mType, BaseDetailActivity.this, mDataItem, mBitmap, TAG);
                break;
            case INSTAGRAM:
                ShareTypeManager.shareWithInstagam(BaseDetailActivity.this, mBitmap);
                break;
            case MESSAGE:
                ShareTypeManager.shareWithMessage(BaseDetailActivity.this, mDataItem.getData());
                break;
            case MORE:
                ShareTypeManager.shareWithMore(mType, BaseDetailActivity.this, mDataItem.getData(), mBitmap, mDataItem.getVideoUrl());
                break;
            case COPY:
                ShareTypeManager.shareWithCopy(BaseDetailActivity.this, mDataItem.getData());
                break;
            case SAVE:
                ShareTypeManager.shareWithImage(mType, BaseDetailActivity.this, mBitmap);
                break;
        }
    }


    /**
     * 新开页面从右滑入，之前的页面保持不动
     */
    protected boolean useStartDefaultAnim() {
        return true;
    }

    protected void setType() {
        this.mType = getType();
        switch (mType) {
            case DataListItem.DATA_TYPE_1:
                mTextDetailView.setVisibility(View.VISIBLE);
                refreshText();
                mImageDetailView.setVisibility(View.GONE);
                mVideoLayout.setVisibility(View.GONE);
                break;
            case DataListItem.DATA_TYPE_2:
                mTextDetailView.setVisibility(View.GONE);
                mImageDetailView.setVisibility(View.VISIBLE);
                refreshImage();
                mVideoLayout.setVisibility(View.GONE);
                break;
            case DataListItem.DATA_TYPE_3:
                mTextDetailView.setVisibility(View.GONE);
                mImageDetailView.setVisibility(View.GONE);
                mVideoLayout.setVisibility(View.VISIBLE);
                refreshVideo();
                break;
        }


    }

    private void refreshVideo() {
        mVideoView.setUp(mDataItem.getVideoUrl()
                , "");
        Glide.with(this).load(mDataItem.getVideoThumbUrl()).into(mVideoView.thumbImageView);
//        mVideoView.post(new Runnable() {
//            @Override
//            public void run() {
//                if (mVideoView.textureView != null) {
//                    mVideoView.textureView.setVideoSize(mVideoView.textureViewContainer.getWidth(), mVideoView.textureViewContainer.getHeight());//视频大小与控件大小一致
//                }
//            }
//        });
    }

    private void refreshText() {
        mTextDetailView.setText(mDataItem.getData());
    }

    private Bitmap mBitmap;

    private void refreshImage() {

        Glide.with(this).load(mDataItem.getPicUrl()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mBitmap = resource;
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = ScreenUtils.screenActualPix(BaseDetailActivity.this)[1] * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = mImageDetailView.getLayoutParams();
                if (para != null) {
                    para.height = height;
                    mImageDetailView.setLayoutParams(para);
                }
                Glide.with(getApplicationContext()).load(mDataItem.getPicUrl()).asBitmap().into(mImageDetailView);
            }
        });
    }

    protected abstract int getType();


    protected abstract String getTextTitle();

    protected abstract void onClickCollect(View v);

    /**
     * 设置状态栏颜色
     *
     * @param color 颜色值
     */
    protected void setStatusBarColor(int color) {
        getWindow().setStatusBarColor(color);
    }


    protected void setAndroidNativeLightStatusBar(boolean dark) {
        //6.0以下不起作用
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        View decor = this.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

}
