package com.goodmorning.ui.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baselib.bitmap.util.DeviceUtil;
import com.baselib.ui.activity.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.creativeindia.goodmorning.R;
import com.goodmorning.utils.ScreenUtils;
import com.goodmorning.view.image.RoundedImageView;

import org.n.account.ui.view.AccountUIHelper;


public class LoginGuideActivity extends BaseActivity {
    private RoundedImageView mImageDetailView;
    private RoundedImageView mImageBg;
    private View mLoginView;

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
        Glide.with(this).load(picUrl).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = ScreenUtils.screenActualPix(LoginGuideActivity.this)[1] * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = mImageDetailView.getLayoutParams();
                if (para != null) {
                    para.height = height;
                    mImageDetailView.setLayoutParams(para);
                }
                Glide.with(getApplicationContext()).load(picUrl).asBitmap().into(mImageDetailView);
            }
        });
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountUIHelper.startLogin(LoginGuideActivity.this);
            }
        });
        findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
