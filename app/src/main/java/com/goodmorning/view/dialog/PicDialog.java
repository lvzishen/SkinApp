package com.goodmorning.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baselib.sp.SharedPref;
import com.baselib.statistic.StatisticLoggerX;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.creativeindia.goodmorning.R;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.ui.activity.PicDetailActivity;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.utils.CheckUtils;
import com.goodmorning.utils.GlideRoundTransform;
import com.goodmorning.utils.ImageUtil;
import com.goodmorning.utils.ImageUtilHandle;
import com.goodmorning.utils.ResUtils;
import com.goodmorning.view.image.NiceImageView;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class PicDialog extends Dialog implements View.OnClickListener {
    private ImageView ivPic;
    private ImageButton btnClose;
    private Button btnShare;
    private DataListItem dataListItem;
    private TextView imgDayTitle,imgDaytxt;
    public PicDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        View view = ResUtils.getInflater().inflate(R.layout.pic_day_layout, null);
        setContentView(view);
        initView();
        initDialog();
    }

    private void initView(){
        ivPic = findViewById(R.id.iv_pic);
        btnClose = findViewById(R.id.btn_close);
        btnShare = findViewById(R.id.btn_share);
        imgDayTitle = findViewById(R.id.img_day_title);
        imgDaytxt = findViewById(R.id.img_day_txt);
        btnClose.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnShare.setText(getContext().getString(R.string.share));
        imgDayTitle.setText(getContext().getString(R.string.img_day));
        imgDaytxt.setText(getContext().getString(R.string.img_share_day));

    }

    private void initDialog(){
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.gravity = Gravity.TOP;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    private void addData(){
        ViewGroup.LayoutParams layoutParams =  ivPic.getLayoutParams();
        Glide.with(getContext())
                .load(dataListItem.getPicUrl())//dataListItem.getPicUrl()
                .placeholder(R.drawable.shape_list_item_default)
                .error(R.drawable.shape_list_item_default)
                .override(720, 900)
                .apply(RequestOptions.bitmapTransform(new GranularRoundedCorners(25,25,0,0)))
                .into(ivPic);
        StatisticLoggerX.logShowUpload("","pic popup","","","");
    }

    public void setDataListItem(DataListItem dataListItem){
        this.dataListItem = dataListItem;
        addData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_close:
                StatisticLoggerX.logClickUpload("","pic popup","close","","");
                dismiss();
                break;
            case R.id.btn_share:
                StatisticLoggerX.logClickUpload("","pic popup","share","","");
                if (dataListItem == null){
                    dataListItem = new DataListItem();
                }
                ActivityCtrl.gotoOpenActivity(getContext(), PicDetailActivity.class, dataListItem);
                dismiss();
                break;
        }
    }
}
