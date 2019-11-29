package com.goodmorning.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baselib.bitmap.util.DeviceUtil;
import com.baselib.cloud.CloudPropertyManager;
import com.baselib.sp.SharedPref;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.creativeindia.goodmorning.R;
import com.goodmorning.adapter.ShareCommonFactory;
import com.goodmorning.adapter.ShareCommonHolder;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.bean.ShareItem;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.share.ShareTypeManager;
import com.goodmorning.utils.BitmapUtils;
import com.goodmorning.utils.ImageUtilHandle;
import com.goodmorning.utils.ScreenUtils;
import com.goodmorning.view.image.RoundedImageView;
import com.goodmorning.view.recyclerview.normal.CommonRecyclerView;
import com.goodmorning.view.recyclerview.normal.IItem;

import org.n.account.core.api.NjordAccountManager;
import org.n.account.core.model.Account;
import org.thanos.netcore.CollectStatus;
import org.thanos.netcore.MorningDataAPI;
import org.thanos.netcore.ResultCallback;
import org.thanos.netcore.bean.CollectDetail;
import org.thanos.netcore.internal.requestparam.CollectRequestParam;
import org.thanos.netcore.internal.requestparam.CollectStatusRequestParam;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

import static com.baselib.cloud.CloudPropertyManager.PATH_SHAREGUIDE_PROP;
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
    private static final int MESSAGE_TO_SHARE = 1;
    protected FrameLayout mBackLayout;
    protected TextView mTitleView;
    protected FrameLayout misCollectLayout;
    protected TextView mTextDetailView;
    protected ImageView mImageCollect;
    protected RoundedImageView mImageDetailView;
    protected LinearLayout mVideoLayout;
    protected JzvdStd mVideoView;
    protected CommonRecyclerView mShareRv;
    private List<ShareItem> mDatas;
    protected int mType;
    protected DataListItem mDataItem;
    private GridLayoutManager mLayoutManager;
    protected ShareItem mShareItem;
    protected boolean isGoLogin;
    private boolean isNotHaveWaterMark;
    private boolean isCollect;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TO_SHARE:
                    switch (mShareItem.type) {
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
                    break;
            }
        }
    };

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
        mImageCollect = findViewById(R.id.iscollect);
        mImageDetailView = findViewById(R.id.image_detail);
        mImageDetailView.setCornerRadius(DeviceUtil.dip2px(getApplicationContext(), 6));
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
                if (mDataItem.getStatus() == DataListItem.STATUS_TYPE_1 && !isCollect) {
                    if (GlobalConfig.DEBUG) {
                        Log.i(TAG, "内容失效不能取消收藏");
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.content_offline), Toast.LENGTH_SHORT).show();
                    return;
                }
                MorningDataAPI.requestCollectUpLoad(getApplicationContext(),
                        new CollectRequestParam(mDataItem.getResourceId(),
                                isCollect, false, 1), new ResultCallback<CollectDetail>() {
                            @Override
                            public void onSuccess(CollectDetail data) {
                                if (data != null && data.code == 0) {
                                    if (GlobalConfig.DEBUG) {
                                        Log.i(TAG, "用户收藏上报成功");
                                    }
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            isCollect = !isCollect;
                                            mImageCollect.setSelected(isCollect);
                                        }
                                    });
                                } else {
                                    if (GlobalConfig.DEBUG) {
                                        Log.i(TAG, "用户收藏上报失败");
                                    }
                                }
                            }

                            @Override
                            public void onLoadFromCache(CollectDetail data) {

                            }

                            @Override
                            public void onFail(Exception e) {
                                if (GlobalConfig.DEBUG) {
                                    Log.d(TAG, "用户收藏上报失败, [" + e + "]");
                                }
                            }
                        });


            }
        });
        initShareDatas();
        if (mDataItem.getStatus() == DataListItem.STATUS_TYPE_1) {
            isCollect = true;
            mImageCollect.setSelected(true);
            findViewById(R.id.ll_no_collect).setVisibility(View.VISIBLE);
            findViewById(R.id.content_detail_ll).setVisibility(View.GONE);
        } else {
            getCollectStatus();
        }
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
        this.mShareItem = shareItem;
        //引导登录
        //获取已登录的账号
        Account account = NjordAccountManager.getCurrentAccount(this);
        if (account != null) {
            if (mType == DataListItem.DATA_TYPE_2 && account.isGuest() && isShowLoginGuide()) {
                Intent intent = new Intent(BaseDetailActivity.this, LoginGuideActivity.class);
                intent.putExtra("picUrl", mDataItem.getPicUrl());
                startActivity(intent);
                isGoLogin = true;
                return;
            }//NjordAccountManager.isLogined(getApplicationContext())
            if (mType == DataListItem.DATA_TYPE_2 && (!account.isGuest() && NjordAccountManager.isLogined(getApplicationContext())) && !isNotHaveWaterMark) {
                makeWaterMark();
            } else {
                mHandler.sendEmptyMessage(MESSAGE_TO_SHARE);
            }
        }
    }

    private void makeWaterMark() {
        //生成水印
        Account account = NjordAccountManager.getCurrentAccount(getApplicationContext());
        Glide.with(this).load(account.mPictureUrl).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                //增加产品水印
                Bitmap bitmapWater = BitmapFactory.decodeResource(getResources(), R.drawable.share_watermark);
                Bitmap watermarkBitmap = ImageUtilHandle.createWaterMaskRightBottom(mBitmap, bitmapWater, (int) getResources().getDimension(R.dimen.qb_px_2), (int) getResources().getDimension(R.dimen.qb_px_2));
                //增加头像水印
                Bitmap circleBitmap = BitmapUtils.createCircleBitmap(resource);
                Bitmap avatarBitmap = BitmapUtils.getbitmap(circleBitmap, DeviceUtil.dip2px(getApplicationContext(), 36), DeviceUtil.dip2px(getApplicationContext(), 36));
                watermarkBitmap = ImageUtilHandle.createWaterMaskLeftTop(watermarkBitmap, avatarBitmap, (int) getResources().getDimension(R.dimen.qb_px_2), (int) getResources().getDimension(R.dimen.qb_px_2));
                //增加文字水印
                TextView textView = new TextView(BaseDetailActivity.this);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setText("Wish you Babe");
                textView.setTextSize(18f);
                Bitmap textBitmap = ImageUtilHandle.viewToBitmap(textView);
                mBitmap = ImageUtilHandle.createWaterMaskLeftTop(watermarkBitmap, textBitmap, DeviceUtil.px2dip(getApplicationContext(), avatarBitmap.getWidth()) + (int) getResources().getDimension(R.dimen.qb_px_4), DeviceUtil.px2dip(getApplicationContext(), avatarBitmap.getWidth() - textView.getHeight()) / 2 + (int) getResources().getDimension(R.dimen.qb_px_2));
//                    mImageDetailView.setImageBitmap(watermarkBitmap);
                isNotHaveWaterMark = true;
                mHandler.sendEmptyMessage(MESSAGE_TO_SHARE);
            }
        });
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

    public static final String IS_SHOW_SHARE = "isshowshare";
    public static final String IS_SHOW_TIMES = "isshowtimes";

    public boolean isShowLoginGuide() {
        int showTimes = SharedPref.getInt(getApplicationContext(), IS_SHOW_SHARE, 0);
        int arriveTimes = CloudPropertyManager.getInt(getApplicationContext(), PATH_SHAREGUIDE_PROP, "show_times", 2);
        long firstShowTimes = SharedPref.getLong(getApplicationContext(), IS_SHOW_TIMES, 0);
        int day = CloudPropertyManager.getInt(getApplicationContext(), PATH_SHAREGUIDE_PROP, "show_day", 3);
        //已超过最大次数,超过三天，清空次数
        if ((arriveTimes > showTimes) && (System.currentTimeMillis() - firstShowTimes > day * 1000 * 60 * 60 * 24)) {
            SharedPref.setInt(getApplicationContext(), IS_SHOW_SHARE, 0);
            SharedPref.setLong(getApplicationContext(), IS_SHOW_TIMES, 0);
        }
        if (showTimes < arriveTimes) {
            //增加次数
            SharedPref.setInt(getApplicationContext(), IS_SHOW_SHARE, showTimes++);
            //到达次数，记录时间
            if (showTimes == arriveTimes) {
                SharedPref.setLong(getApplicationContext(), IS_SHOW_TIMES, System.currentTimeMillis());
            }
            return true;
        }
        return false;
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


    private void getCollectStatus() {
        //获取收藏状态
        if (GlobalConfig.DEBUG) {
            Log.i(TAG, "用户ID" + mDataItem.getResourceId());
        }
        MorningDataAPI.requestCollectStatus(getApplicationContext(),
                new CollectStatusRequestParam(mDataItem.getResourceId(),
                        false, 1), new ResultCallback<CollectStatus>() {
                    @Override
                    public void onSuccess(CollectStatus data) {
                        if (data != null && data.code == 0) {
                            if (GlobalConfig.DEBUG) {
                                Log.i(TAG, "用户收藏状态" + data.item.collect);
                            }
                            if (data.item.collect == 1) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        isCollect = true;
                                        mImageCollect.setSelected(true);
                                    }
                                });
                            }
                        } else {
                            if (GlobalConfig.DEBUG) {
                                Log.i(TAG, "用户收藏状态失败");
                            }
                        }
                    }

                    @Override
                    public void onLoadFromCache(CollectStatus data) {

                    }

                    @Override
                    public void onFail(Exception e) {
                        if (GlobalConfig.DEBUG) {
                            Log.d(TAG, "用户收藏状态失败, [" + e + "]");
                        }
                    }
                });
    }

    protected abstract int getType();


    protected abstract String getTextTitle();


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
