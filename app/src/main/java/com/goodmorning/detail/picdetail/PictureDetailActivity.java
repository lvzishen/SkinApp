//package com.goodmorning.detail.picdetail;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.os.SystemClock;
//import android.provider.MediaStore;
//import android.text.Html;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//
//import com.creativeindia.goodmorning.R;
//import com.goodmorning.common.BaseActivity;
//import com.goodmorning.common.view.BaseExceptionView;
//import com.goodmorning.config.GlobalConfig;
//import com.goodmorning.utils.MeasureUtil;
//import com.goodmorning.utils.ResourceStringUtils;
//
//import org.interlaken.common.XalContext;
//import org.thanos.core.MorningDataAPI;
//import org.thanos.core.bean.ContentDetail;
//import org.thanos.core.bean.ContentItem;
//import org.thanos.core.bean.ResponseData;
//import org.thanos.core.bean.VideoItem;
//import org.thanos.core.push.ThanosPushConfig;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.concurrent.Callable;
//
//import bolts.Task;
//
//import static org.thanos.core.push.ThanosPushConfig.EXTRA_PUSH_MESSAGE_CONTENT_ID;
//
///**
// * 图集详情页
// */
//public class PictureDetailActivity extends BaseActivity implements View.OnClickListener {
//    public static final String PIC_ITEM = "pic_item";
//    public static final String PIC_INDEX = "pic_index";
//    public static final String PIC_CHANNEL_ID = "pic_channel_id";
//    private static final String SHARE_PIC_DETAIL_PAGE = "detail_page";
//    private String TAG = "Thanos.PictureDetailActivity";
//    private long mTimeStamp = 0; // 算应用时长
//    private LinearLayout shareLayout;
//    private LeftViewPager leftViewPager;
//    private ImageView downImg;
//    private TextView photoCountTv, articleTitleTv, photoTitleTv, likeTv;
//    private ImageView shareImg;
//    private ScrollView scrollView;
//    private BaseExceptionView exceptionView;
//    private PictureAdapter adapter;
//    private ArrayList<VideoItem.PhotoInfo> photoInfos = new ArrayList<>();
//    private ContentItem pushDetailItem;
//    private int screenHeight, imageHeight;
//    private int currentPage; // adapter当前position
//    private int picDetailChannelId;
//    private long contentId;
//    private String from_source;
//    private UIVideoItem uiVideoItem;
//    private int picPosition = -1;
//    private final int SAVE_SUCCESS = 0;//保存图片成功
//    private final int SAVE_FAILURE = 1;//保存图片失败
//    private final int SAVE_BEGIN = 2;//开始保存图片
//
//
//    @SuppressLint("HandlerLeak")
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case SAVE_BEGIN:
//                    if (GlobalConfig.DEBUG) {
//                        Log.d(TAG, "开始保存");
//                    }
//                    Toast.makeText(PictureDetailActivity.this, ResourceStringUtils.getString(mContext, R.string.news_ui__saving), Toast.LENGTH_LONG).show();
//                    downImg.setClickable(false);
//                    break;
//                case SAVE_SUCCESS:
//                    if (GlobalConfig.DEBUG) {
//                        Log.d(TAG, "图片保存成功,请到相册查找");
//                    }
//
//                    Toast.makeText(PictureDetailActivity.this, ResourceStringUtils.getString(mContext, R.string.news_ui__save_successful), Toast.LENGTH_LONG).show();
//                    downImg.setClickable(true);
//                    break;
//                case SAVE_FAILURE:
//                    if (GlobalConfig.DEBUG) {
//                        Log.d(TAG, "图片保存失败,请稍后再试...");
//                    }
//                    Toast.makeText(PictureDetailActivity.this, ResourceStringUtils.getString(mContext, R.string.news_ui__save_failure), Toast.LENGTH_LONG).show();
//
//                    downImg.setClickable(true);
//                    break;
//            }
//        }
//    };
//
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.thanos_picture_detail;
//    }
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.TRANSPARENT);//将状态栏设置成透明色
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);//将导航栏设置为透明色
//        }
//    }
//
//    @Override
//    protected void initView() {
//        ImageView backImg = findViewById(R.id.thanos_picture_detail_back);
//        backImg.setOnClickListener(this);
//        downImg = findViewById(R.id.thanos_picture_detail_download);
//        downImg.setOnClickListener(this);
//        photoCountTv = findViewById(R.id.thanos_picture_detail_count);
//        leftViewPager = findViewById(R.id.thanos_picture_detail_vp);
//        articleTitleTv = findViewById(R.id.thanos_picture_article_title);
//        photoTitleTv = findViewById(R.id.thanos_picture_detail_photo_title);
//        scrollView = findViewById(R.id.thanos_picture_detail_scroll);
//        shareLayout = findViewById(R.id.thanos_picture_detail_bottom);
//
//        likeTv = findViewById(R.id.thanos_picture_big_like_tv);
//        likeTv.setOnClickListener(this);
//        shareImg = findViewById(R.id.thanos_picture_big_share_img);
//        shareImg.setOnClickListener(this);
//        exceptionView = findViewById(R.id.thanos_pic_exception_view);
//
//
//        exceptionView.setTapReload(new BaseExceptionView.ITapReload() {
//
//            @Override
//            public void onTapReload() {
//                if (GlobalConfig.DEBUG) {
//                    Log.d(TAG, "onTapReload click");
//                }
//                if (contentId > 0) {
//                    requestPicDetail(contentId);
//                }
//            }
//        });
//
//        int expandArea = 65;
//        MeasureUtil.expandViewClickArea(shareImg, expandArea);
//        MeasureUtil.expandViewClickArea(likeTv, expandArea);
//        getIntentData();
//    }
//
//    public static Intent createIntent(Context context, UIVideoItem uiVideoItem, int categoryID, int position) {
//        Intent intent = new Intent(context, PictureDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(PIC_ITEM, uiVideoItem);
//        bundle.putInt(PIC_INDEX, position);
//        bundle.putInt(PIC_CHANNEL_ID, categoryID);
//        intent.putExtras(bundle);
//        return intent;
//    }
//
//
//    @Override
//    protected void initData() {
//    }
//
//
//    private void getIntentData() {
//        Intent intent = getIntent();
//        if (intent == null) {
//            return;
//        }
//        String action = intent.getAction();
//        //push
//        if (!TextUtils.isEmpty(action) && action.equals(ThanosPushConfig.ACTION_CLICK_PUSH_NOTIFY)) {
//            from_source = "push";
//            contentId = intent.getLongExtra(EXTRA_PUSH_MESSAGE_CONTENT_ID, 0);
//            if (GlobalConfig.DEBUG) {
//                Log.e(TAG, "推送的内容ID    " + contentId);
//            }
//            if (contentId > 0) {
//                requestPicDetail(contentId);
//            } else {
//                finish();
//            }
//        } else {
//            from_source = "news_center_list";
//            uiVideoItem = (UIVideoItem) intent.getSerializableExtra(PIC_ITEM);
//            picPosition = intent.getIntExtra(PIC_INDEX, -1);
//            contentId = uiVideoItem.contentItem.id;
//            //todo  channleid  正式线 测试线不同
//            int defaultChannelID = GlobalConfig.DEBUG ? MorningDataAPI.CHANNEL_ID_FITNESS_TEST : MorningDataAPI.CHANNEL_ID_FITNESS;
//            if (GlobalConfig.DEBUG) {
//                Log.e(TAG, "defaultChannelID   " + defaultChannelID);
//            }
//            picDetailChannelId = intent.getExtras().getInt(PIC_CHANNEL_ID, defaultChannelID);
//            if (uiVideoItem == null) {
//                //显示异常界面
//                return;
//            }
//            photoInfos = uiVideoItem.contentItem.photoInfos;
//            final String artTitle = uiVideoItem.contentItem.articleTitle;
//            final String photoTitle = photoInfos.get(0).photoTitle;
//
//            final int likeCount = uiVideoItem.contentItem.userLikes;
//            final boolean isLike = uiVideoItem.isLike;
//            initAdapter();
//            adapter.setLoadImgInterface(new PictureAdapter.GlideLoadImgInterface() {
//                @Override
//                public void loadFail() {
//                    if (GlobalConfig.DEBUG) {
//                        Log.d(TAG, "图片加载失败");
//                    }
//                    allowShow(View.INVISIBLE);
//                }
//
//                @Override
//                public void loadSuccess() {
//                    if (GlobalConfig.DEBUG) {
//                        Log.d(TAG, "图片加载成功");
//                    }
//                    allowShow(View.VISIBLE);
//                    setLikeCountText(likeTv, likeCount, isLike);
//                    if (!TextUtils.isEmpty(artTitle)) {
//                        articleTitleTv.setText(artTitle);
//                    }
//                    if (!TextUtils.isEmpty(photoTitle)) {
//                        photoTitleTv.setText(photoTitle);
//                    }
//                }
//            });
//        }
//    }
//
//    private void allowShow(int invisible) {
//        articleTitleTv.setVisibility(invisible);
//        downImg.setVisibility(invisible);
//        shareLayout.setVisibility(invisible);
//        photoCountTv.setVisibility(invisible);
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mTimeStamp = SystemClock.elapsedRealtime();
//        StatisticPageDuration.enterActivity(TAG);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mTimeStamp = SystemClock.elapsedRealtime() - mTimeStamp;
//        if (mTimeStamp > 0 && uiVideoItem != null) {
//            //新闻详情页展示-视频 打点
//            PicItemStatistics.picDetailPageShow(uiVideoItem.contentItem, mTimeStamp, picDetailChannelId, from_source);
//        }
//        StatisticPageDuration.leaveActivity(TAG);
//    }
//
//
//    @SuppressLint("SetTextI18n")
//    private void setLikeCountText(TextView tv, int count, boolean isLike) {
//        if (isLike) {
//            Drawable drawable = mContext.getResources().getDrawable(R.drawable.thanos_video_like_checked_icon);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tv.setCompoundDrawables(drawable, null, null, null);
//        } else {
//            Drawable drawable = mContext.getResources().getDrawable(R.drawable.thanos_video_like_normal_icon);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tv.setCompoundDrawables(drawable, null, null, null);
//        }
//        tv.setSelected(isLike);
//
//        tv.setText(count > 0 ? Utils.division1k(count) : "");
//    }
//
//    private void initAdapter() {
//        adapter = new PictureAdapter(PictureDetailActivity.this, photoInfos, tapPhotoListener);
//        leftViewPager.setAdapter(adapter);
//        leftViewPager.setCurrentItem(0);
//        leftViewPager.addOnPageChangeListener(pageChangeListener);
//        photoCountTv.setText(Html.fromHtml("1<font color='#ffffff'> / </font>" + photoInfos.size()));
//    }
//
//
//    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            if (adapter == null) {
//                return;
//            }
//            currentPage = position;
//            int imageCount = photoInfos.size();
//            VideoItem.PhotoInfo photoInfo;
//            photoInfo = photoInfos.get(position);
//            if (photoInfo != null) {
//                String photoTitle = photoInfo.photoTitle;
//                if (!TextUtils.isEmpty(photoTitle)) {
//                    photoTitleTv.setText(photoTitle);
//                }
//            }
//
//            int index = position + 1;
//            photoCountTv.setText(Html.fromHtml(index + "<font color='#ffffff'> / </font>" + imageCount));
//            ImageUtil.updateVisibility(downImg, View.VISIBLE);
//            updateBottom(true);
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    };
//
//
//    /**
//     * 请求图集详情
//     */
//    private void requestPicDetail(long id) {
//        MorningDataAPI.requestContentDetail(mContext, new MorningDataAPI.ContentDetailRequestParam(id, false, 1), new MorningDataAPI.ThanosDataLoadCallback<ContentDetail>() {
//            @Override
//            public void onSuccess(ContentDetail data) {
//                if (data != null && data.item != null) {
//                    pushDetailItem = data.item;
//                    if (GlobalConfig.DEBUG) {
//                        Log.d(TAG, "有数据--->" + pushDetailItem.type);
//                    }
//                    postUI((VideoItem) pushDetailItem);
//                } else {
//                    if (GlobalConfig.DEBUG) {
//                        Log.d(TAG, "无数据--->");
//                    }
//                    postUI(null);
//                }
//            }
//
//            private void postUI(final VideoItem pictureItem) {
//                Task.call(new Callable<Object>() {
//                    @Override
//                    public Object call() throws Exception {
//                        if (pictureItem != null) {
//                            int picType = pictureItem.type;
//                            if (MorningDataAPI.isPicType(picType)) {
//                                if (GlobalConfig.DEBUG) {
//                                    Log.d(TAG, "postUI 有数据--->" + pictureItem.articleTitle);
//                                }
//                                ArrayList<VideoItem.PhotoInfo> photoInfoArrayList = pictureItem.photoInfos;
//                                if (!photoInfoArrayList.isEmpty()) {
//                                    showDataView(View.VISIBLE, View.GONE);
//                                    adapter.setList(photoInfoArrayList);
//                                    photoCountTv.setText(Html.fromHtml("1<font color='#ffffff'> / </font>" + photoInfoArrayList.size()));
//                                } else {
//                                    showDataView(View.GONE, View.VISIBLE);
//                                }
//                            }
//                        } else {
//                            showDataView(View.GONE, View.VISIBLE);
//                        }
//                        return null;
//                    }
//                }, Task.UI_THREAD_EXECUTOR);
//            }
//
//            @Override
//            public void onLoadFromCache(ContentDetail data) {
//            }
//
//            @Override
//            public void onFail(Exception e) {
//                postUI(null);
//            }
//        });
//    }
//
//
//    /**
//     * 有数据的展示界面
//     */
//    private void showDataView(int invisible, int gone) {
//        articleTitleTv.setVisibility(invisible);
//        downImg.setVisibility(invisible);
//        leftViewPager.setVisibility(invisible);
//        shareLayout.setVisibility(invisible);
//        photoCountTv.setVisibility(invisible);
//        exceptionView.setVisibility(gone);
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.thanos_picture_detail_back) {
//            finish();
//        } else if (id == R.id.thanos_picture_detail_download) {
//            downImg.setClickable(false);//不可重复点击
//            downLoadPic();
//        } else if (id == R.id.thanos_picture_big_share_img) {
//            sharePic();
//        } else if (id == R.id.thanos_picture_big_like_tv) {
//
//            uiVideoItem.isLike = !uiVideoItem.isLike;
//            uiVideoItem.contentItem.userLikes = uiVideoItem.isLike ? (uiVideoItem.contentItem.userLikes + 1) : (uiVideoItem.contentItem.userLikes - 1);
//            int likeCount = uiVideoItem.contentItem.userLikes;
//            boolean isLike = uiVideoItem.isLike;
//            setLikeCountText(likeTv, likeCount, isLike);
//            upLoadLike();
//            //发送广播 通知外面列表刷新点赞数
//            ThanosContentListViewController.notifyPicDataUpdated(mContext, uiVideoItem, picPosition);
//            //点赞打点
//            PicItemStatistics.picDetailLike(uiVideoItem, VIDEO_DETAIL_LIKE_FROM);
//        }
//    }
//
//    /**
//     * 下载图片
//     */
//    private void downLoadPic() {
//        String url;
//        if (currentPage > -1 && adapter != null) {
//            VideoItem.PhotoInfo photoInfo = photoInfos.get(currentPage);
//            if (photoInfo != null) {
//                url = photoInfo.url;
//
//                if (GlobalConfig.DEBUG) {
//                    Log.e(TAG, "图片下载地址l-> " + url);
//                }
//                if (!TextUtils.isEmpty(url)) {
//                    final String finalUrl = url;
//                    //保存图片必须在子线程中操作，是耗时操作;
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Bitmap bp = returnBitMap(finalUrl);
//                            if (bp != null) {
//                                mHandler.obtainMessage(SAVE_BEGIN).sendToTarget();
//                                saveImageToPhotos(mContext, bp);
//                            } else {
//                                mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
//                            }
//                        }
//                    }).start();
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 保存二维码到本地相册
//     */
//    private void saveImageToPhotos(Context context, Bitmap bmp) {
//        // 首先保存图片
//        String appName;
//        appName = XalContext.getAppName();
//        File appDir = new File(Environment.getExternalStorageDirectory(), appName);
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//        String fileName = System.currentTimeMillis() + ".jpg";
//        File file = new File(appDir, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
//            return;
//        }
//        // 最后通知图库更新
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri uri = Uri.fromFile(file);
//        intent.setData(uri);
//        context.sendBroadcast(intent);
//        mHandler.obtainMessage(SAVE_SUCCESS).sendToTarget();
//    }
//
//    /**
//     * 将URL转化成bitmap形式
//     *
//     * @param url
//     * @return bitmap type
//     */
//    public Bitmap returnBitMap(String url) {
//        URL myFileUrl;
//        Bitmap bitmap = null;
//        try {
//            myFileUrl = new URL(url);
//            HttpURLConnection conn;
//            conn = (HttpURLConnection) myFileUrl.openConnection();
//            conn.setDoInput(true);
//            conn.connect();
//            InputStream is = conn.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//
//    /**
//     * 上报图集点赞
//     */
//    private void upLoadLike() {
//        MorningDataAPI.uploadUserBehavior(mContext,
//                new MorningDataAPI.UserBehaviorUploadParam(uiVideoItem.contentItem,
//                        MorningDataAPI.UserBehaviorUploadParam.UserBehavior.LIKE, !(uiVideoItem).isLike, 1), new MorningDataAPI.ThanosDataLoadCallback<ResponseData>() {
//                    @Override
//                    public void onSuccess(ResponseData data) {
//                        if (data != null && data.code == 0) {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "图集点赞行为上报成功" + uiVideoItem.contentItem.articleTitle);
//                            }
//                        } else {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "图集点赞行为上报失败, " + data);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFromCache(ResponseData data) {
//
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        if (GlobalConfig.DEBUG) {
//                            Log.e(TAG, "图集点赞行为上报失败", e);
//                        }
//                    }
//                });
//
//    }
//
//    private void sharePic() {
//        if (uiVideoItem == null) {
//            return;
//        }
//        String url = uiVideoItem.contentItem.shareUrl;
//        String title = uiVideoItem.contentItem.articleTitle;
//        String source = XalContext.getAppName();
//        int type = uiVideoItem.contentItem.type;
//        String[] share = ShareUtils.getShareStringByType(mContext, type);
//        String downloadUrl = ThanosDataCore.getThanosCoreConfig().getShareDownloadUrl();
//        downloadUrl = !TextUtils.isEmpty(downloadUrl) ? "[ " + downloadUrl + " ]" : "";
//        if (GlobalConfig.DEBUG) {
//            Log.d(TAG, "downloadurl->" + downloadUrl);
//        }
//        String content = title + " [ " + source + " ] " + share[0] + "[ " + url + " ]" + share[1] + downloadUrl;
//        XalContext.getAppName();
//        new ShareUtils.Builder(mContext)
//                .setContentType(ShareContentType.TEXT)
//                .setTextContent(content)
//                .setTitle(title)
//                .build()
//                .shareBySystem();
//        PicItemStatistics.picShare((uiVideoItem.contentItem), SHARE_PIC_DETAIL_PAGE);
//
//        //用户分享行为上报
//        MorningDataAPI.uploadUserBehavior(mContext,
//                new MorningDataAPI.UserBehaviorUploadParam((uiVideoItem).contentItem,
//                        MorningDataAPI.UserBehaviorUploadParam.UserBehavior.SHARE, false, 1), new MorningDataAPI.ThanosDataLoadCallback<ResponseData>() {
//                    @Override
//                    public void onSuccess(ResponseData data) {
//                        if (data != null && data.code == 0) {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "图集分享行为上报成功" + (uiVideoItem).contentItem.articleTitle);
//                            }
//                        } else {
//                            if (GlobalConfig.DEBUG) {
//                                Log.i(TAG, "图集分享行为上报失败, " + data + "空数据");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onLoadFromCache(ResponseData data) {
//
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        if (GlobalConfig.DEBUG) {
//                            Log.e(TAG, "图集分享行为上报成功", e);
//                        }
//                    }
//                });
//    }
//
//
//    /**
//     * 更新底部控件
//     *
//     * @param show
//     */
//    private void updateBottom(boolean show) {
//        if (imageHeight == 0) {
//            imageHeight = MeasureUtil.dip2px(getApplicationContext(), 186);
//        }
//        if (screenHeight == 0) {
//            screenHeight = MeasureUtil.getScreenHeight(getApplicationContext());
//        }
//        if (show) {
//            ImageUtil.updateVisibility(scrollView, View.VISIBLE);
//            ImageUtil.updateVisibility(shareLayout, View.VISIBLE);
//        } else {
//            ImageUtil.updateVisibility(scrollView, View.GONE);
//            ImageUtil.updateVisibility(shareLayout, View.GONE);
//        }
//    }
//
//
//    private PictureAdapter.ITapPhotoListener tapPhotoListener = new PictureAdapter.ITapPhotoListener() {
//        @Override
//        public void onTapPhotoListener(int position) {
//            isUpdateBottom(shareLayout.getVisibility() != View.VISIBLE);
//        }
//    };
//
//
//    // 是否显示底部
//    private void isUpdateBottom(boolean show) {
//        updateBottom(show);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (adapter != null) {
//            adapter = null;
//        }
//    }
//
//}
