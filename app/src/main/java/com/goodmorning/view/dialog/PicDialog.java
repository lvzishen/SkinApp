package com.goodmorning.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.creativeindia.goodmorning.R;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.ui.activity.PicDetailActivity;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.utils.ImageUtil;
import com.goodmorning.utils.ResUtils;

public class PicDialog extends Dialog implements View.OnClickListener {
    private ImageView ivPic;
    private ImageButton btnClose;
    private Button btnShare;
    private DataListItem dataListItem;
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
        ivPic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btnClose.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }

    private void initDialog(){
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.gravity = Gravity.TOP;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    private void addData(){
        ViewGroup.LayoutParams layoutParams =  ivPic.getLayoutParams();
        ImageUtil.displayImageView(getContext(),ivPic,dataListItem.getPicUrl(),R.drawable.shape_list_item_default,layoutParams.width, layoutParams.height);
    }

    public void setDataListItem(DataListItem dataListItem){
        this.dataListItem = dataListItem;
        addData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_share:
                if (dataListItem == null){
                    dataListItem = new DataListItem();
                }
                ActivityCtrl.gotoOpenActivity(getContext(), PicDetailActivity.class, dataListItem);
                dismiss();
                break;
        }
    }
}
