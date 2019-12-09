package com.goodmorning.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.creativeindia.goodmorning.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.goodmorning.share.RWhatsAppManager;
import com.goodmorning.share.ShareTypeManager;
import com.goodmorning.utils.ResUtils;

/**
 * @author dinghao
 */
public class ShareDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "ShareDialog";
    private TextView tvShareWhatsApp;
    private TextView tvShareFaceBook;
    private TextView tvShareCope;
    private RelativeLayout llShareDialog;
    private Activity mActivity;
    private String ShareUrl = "https://play.google.com/store/apps/details?id=com.creativeindia.goodmorning";
    public ShareDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        View view = ResUtils.getInflater().inflate(R.layout.dialog_share_layout, null);
        setContentView(view);
        initView();
        initDialog();
        mActivity = (Activity) context;
    }

    private void initView(){
        tvShareWhatsApp = findViewById(R.id.tv_share_whatsapp);
        tvShareFaceBook = findViewById(R.id.tv_share_facebook);
        tvShareCope = findViewById(R.id.tv_share_copy);
        llShareDialog = findViewById(R.id.rl_share_dialog);
        tvShareWhatsApp.setOnClickListener(this);
        tvShareFaceBook.setOnClickListener(this);
        tvShareCope.setOnClickListener(this);
        llShareDialog.setOnClickListener(this);
    }

    private void initDialog(){
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wmlp);
        window.setWindowAnimations(R.style.DialogAnimation);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_share_whatsapp:
                RWhatsAppManager.getInstance().shareText(getContext(),getShareMsg());
                dismiss();
                break;
            case R.id.tv_share_facebook:
                shareFaceBook(mActivity);
                dismiss();
                break;
            case R.id.tv_share_copy:
                ShareTypeManager.shareWithCopy(mActivity, getShareMsg());
                dismiss();
                break;
            case R.id.rl_share_dialog:
                dismiss();
                break;
        }
    }

    private String getShareMsg(){
        String shareMsg = "Sunny Day： Kahe apno ko Namaste!\n" +new String(Character.toChars(128591))+"https://play.google.com/store/apps/details?id=com.creativeindia.goodmorning";
        return shareMsg;
    }

    private void shareFaceBook(Activity activity){
        CallbackManager callbackManager = CallbackManager.Factory.create();
        FacebookCallback<Sharer.Result> callback =
                new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onCancel() {
                        Log.i(TAG, "Canceled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.i(TAG, String.format("Error: %s", error.toString()));
                    }

                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Log.i(TAG, "Success!");
                    }
                };
        com.facebook.share.widget.ShareDialog shareDialog = new com.facebook.share.widget.ShareDialog(activity);
        shareDialog.registerCallback(callbackManager, callback);
        ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse(ShareUrl)).build();
        shareDialog.show(content, com.facebook.share.widget.ShareDialog.Mode.AUTOMATIC);
    }
}
