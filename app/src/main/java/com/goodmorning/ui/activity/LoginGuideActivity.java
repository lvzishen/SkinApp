package com.goodmorning.ui.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.baselib.bitmap.util.DeviceUtil;
import com.baselib.ui.activity.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.creativeindia.goodmorning.R;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.utils.ImageUtilHandle;
import com.goodmorning.utils.ScreenUtils;
import com.goodmorning.view.image.RoundedImageView;

import org.n.account.core.AccountSDK;
import org.n.account.core.constant.Constant;
import org.n.account.core.contract.ILoginCallback;
import org.n.account.core.contract.LoginApi;
import org.n.account.core.exception.NotAllowLoginException;
import org.n.account.core.model.Account;
import org.n.account.net.NetCode;
import org.n.account.ui.view.AccountUIHelper;


public class LoginGuideActivity extends BaseActivity {
    private static final String TAG = "LoginGuideActivity";
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private RoundedImageView mImageDetailView;
    private RoundedImageView mImageBg;
    private View mLoginView;
    private LoginApi mLoginApi;
    private String mLoadingStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guide);
        mImageDetailView = findViewById(R.id.image_detail);
        mImageDetailView.setCornerRadius(DeviceUtil.dip2px(getApplicationContext(), 6));
        mLoginView = findViewById(R.id.login_rl);
        mImageBg = findViewById(R.id.roundimage);
        mImageBg.setImageResource(R.drawable.share_shadow);
        mImageBg.setCornerRadius(DeviceUtil.dip2px(getApplicationContext(), 6), DeviceUtil.dip2px(getApplicationContext(), 6), 0, 0);
        String picUrl = getIntent().getStringExtra("picUrl");


        Glide.with(this).asBitmap().load(picUrl).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = ScreenUtils.screenActualPix(LoginGuideActivity.this)[1] * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = mImageDetailView.getLayoutParams();
                if (para != null) {
                    para.height = height;
                    mImageDetailView.setLayoutParams(para);
                }
                //增加产品水印
                Bitmap bitmapWater = BitmapFactory.decodeResource(getResources(), R.drawable.share_watermark);
                Bitmap watermarkBitmap = ImageUtilHandle.createWaterMaskRightBottom(resource, bitmapWater, (int) getResources().getDimension(R.dimen.qb_px_2), (int) getResources().getDimension(R.dimen.qb_px_2));
                mImageDetailView.setImageBitmap(watermarkBitmap);
//                Glide.with(getApplicationContext()).asBitmap().load(picUrl).into(mImageDetailView);
            }
        });
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyLogin(LoginGuideActivity.this);
//                AccountUIHelper.startLogin(LoginGuideActivity.this);
            }
        });
        findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void startMyLogin(Activity activity) {
        try {
            mLoginApi = LoginApi.Factory.create(activity, Constant.LoginType.FACEBOOK);
        } catch (NotAllowLoginException e) {
            if (GlobalConfig.DEBUG) {
                throw new IllegalArgumentException(e);
            }
//                        finish();
            return;
        }

        mLoginApi.login(mLoginCallback);
        if (mLoadingDialog != null) {
            mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
//                                finish();
                    if (DEBUG) {
                        Log.i(TAG, "mLoadingDialog onCancel: ");
                    }
                }
            });
        }
    }

    private ILoginCallback mLoginCallback = new ILoginCallback() {

        @Override
        public void onPrePrepare(@Constant.LoginType int type) {
            if (type == Constant.LoginType.GOOGLE) {
                showLoading(mLoadingStr, true);
            }
            if (DEBUG) {
                Log.i(TAG, "onPrePrepare: ");
            }
        }

        @Override
        public void onPrepareFinish() {

            if (DEBUG) {
                Log.i(TAG, "onPrepareFinish: ");
            }
            dismissLoading();
        }

        @Override
        public void onPreLogin(@Constant.LoginType int type) {
            if (DEBUG) {
                Log.i(TAG, "onPreLogin: ");
            }
            showLoading(mLoadingStr);
        }

        @Override
        public void onLoginSuccess(Account account) {
            if (DEBUG) {
                Log.i(TAG, "onLoginSuccess: ");
            }
            dismissLoading();
            if (account != null) {
//                mImageBg.setVisibility(View.GONE);
//                showAccountInfo(account);
                finish();
            }
//            initShareLink();
//            if(UIController.getInstance().getAlexLogWatcher() != null){
//                Bundle alexBundle = new Bundle();
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_NAME_STRING, CreditStatistics.Alex.INVITE_FRIEND_OPERATION_NEW);
//                alexBundle.putString(AlexEventsConstant.XALEX_OPERATION_ACTION_STRING, "share");
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_CATEGORY_STRING, mTargetPackage);
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_FLAG_STRING, "logined");
//                UIController.getInstance().getAlexLogWatcher().log(AlexEventsConstant.XALEX_CLICK,alexBundle);
//            }
        }

        @Override
        public void onLoginFailed(int error, String msg) {
            dismissLoading();
            if (DEBUG) {
                Log.i(TAG, "onLoginFailed: ");
            }
            if (AccountSDK.getExceptionHandler() != null) {
                AccountSDK.getExceptionHandler().handleException(getApplicationContext(),
                        NetCode.NEED_TOAST, TextUtils.concat(getString(R.string.common_unknown_error
                                , getString(R.string.login_network_failed)), "(", String.valueOf(error), ")").toString());
            }
//            if(UIController.getInstance().getAlexLogWatcher() != null){
//                Bundle alexBundle = new Bundle();
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_NAME_STRING, CreditStatistics.Alex.INVITE_FRIEND_OPERATION_NEW);
//                alexBundle.putString(AlexEventsConstant.XALEX_OPERATION_ACTION_STRING, "share");
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_CATEGORY_STRING, mTargetPackage);
//                alexBundle.putString(AlexEventsConstant.XALEX_CLICK_FLAG_STRING, "login_fail");
//                UIController.getInstance().getAlexLogWatcher().log(AlexEventsConstant.XALEX_CLICK,alexBundle);
//            }
//            finish();
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mLoginApi != null) mLoginApi.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 64206 && resultCode == 0) {
//            finish();//Facebook取消
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mLoginApi != null)
            mLoginApi.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        if (mLoginApi != null) mLoginApi.onDestroy();
        super.onDestroy();
    }
}
