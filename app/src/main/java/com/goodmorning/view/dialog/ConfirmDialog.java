package com.goodmorning.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.creativeindia.goodmorning.R;
import com.goodmorning.utils.ResUtils;

public class ConfirmDialog extends Dialog {
    private TextView titleText;
    private TextView contentText;
    private TextView confirmBtn;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        contentText = findViewById(R.id.content);
        titleText = findViewById(R.id.title);
        confirmBtn = findViewById(R.id.confirm);
    }

    private void apply(final DialogParams params) {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (params.confirmDialogListener != null) {
                    params.confirmDialogListener.onConfirm();
                } else {
                    ConfirmDialog.this.dismiss();
                }
            }
        });

        titleText.setText(params.title);
        titleText.setTextAlignment(params.contentAlign);

        if (TextUtils.isEmpty(params.content)) {
            contentText.setVisibility(View.GONE);
        } else {
            contentText.setText(params.content);
            contentText.setTextAlignment(params.contentAlign);
        }

        if (TextUtils.isEmpty(params.confirmStr)) {
            confirmBtn.setText(ResUtils.getString(R.string.btn_confirm));
        } else {
            confirmBtn.setText(params.confirmStr);
        }
        this.setOnCancelListener(params.mCancelListener);
    }

    /**
     * builder for CommonDialog
     */
    public static final class Builder {
        protected DialogParams P;

        public Builder(Context context) {
            P = new DialogParams();
            P.context = context;
        }

        public Builder setContent(String content) {
            P.title = content;
            return this;
        }

        public Builder setInfo(String content) {
            P.content = content;
            return this;
        }

        public Builder setConfirmStr(String confirmStr) {
            P.confirmStr = confirmStr;
            return this;
        }

        public Builder setDialogCancelable(boolean cancelable) {
            P.cancelable = cancelable;
            return this;
        }

        public Builder setDialogCancelListener(OnCancelListener listener) {
            P.mCancelListener = listener;
            return this;
        }


        public Builder addSigleClickListener(ConfirmDialogClickListener listener) {
            P.confirmDialogListener = listener;
            return this;
        }

        private ConfirmDialog create() {
            return new ConfirmDialog(P.context, P.themeResId);
        }

        public ConfirmDialog createAndShow() {
            return show();
        }

        public ConfirmDialog show() {
            ConfirmDialog dialog = create();
            dialog.show();
            dialog.apply(P);
            return dialog;
        }
    }
}
