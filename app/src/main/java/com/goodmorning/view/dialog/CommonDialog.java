package com.goodmorning.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.creativeindia.goodmorning.R;
import com.goodmorning.utils.ResUtils;

public class CommonDialog extends Dialog {
    TextView title;
    TextView content;
    TextView leftBtn;
    TextView rightBtn;

    public CommonDialog(@NonNull Context context) {
        super(context);
    }

    public CommonDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        View view = ResUtils.getInflater().inflate(R.layout.dialog_common, null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView(view);
    }

    private void initView(View view){
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        leftBtn = view.findViewById(R.id.left_btn);
        rightBtn = view.findViewById(R.id.right_btn);
    }


    private void apply(final DialogParams params) {
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (params.commonDialogListener != null) {
                    params.commonDialogListener.onClickRight();
                }
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (params.commonDialogListener != null) {
                    params.commonDialogListener.onClickLeft();
                }
            }
        });

        if (TextUtils.isEmpty(params.content)) {
            content.setVisibility(View.GONE);
        } else {
            content.setText(params.content);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                content.setTextAlignment(params.contentAlign);
            }
        }

        rightBtn.setText(params.rightBtnStr);
        leftBtn.setText(params.leftBtnStr);

        if (!TextUtils.isEmpty(params.title)) {
            title.setText(params.title);
        } else {
            title.setVisibility(View.GONE);
        }
    }

    /**
     * builder for CommonDialog
     */
    public static final class Builder {
        private DialogParams P;


        public Builder(Context context) {
            P = new DialogParams();
            P.context = context;
        }

        public Builder setTitle(String title) {
            P.title = title;
            return this;
        }

        public Builder setContent(String content) {
            P.content = content;
            return this;
        }

        public Builder setLeftBtnStr(String leftBtnStr) {
            P.leftBtnStr = leftBtnStr;
            return this;
        }

        public Builder setRightBtnStr(String rightBtnStr) {
            P.rightBtnStr = rightBtnStr;
            return this;
        }

        public Builder setDialogCancelable(boolean cancelable) {
            P.cancelable = cancelable;
            return this;
        }

        public Builder addClickListener(CommonDialogClickListener listener) {
            P.commonDialogListener = listener;
            return this;
        }

        public Builder setContentAlign(int contentAlign) {
            P.contentAlign |= contentAlign;
            return this;
        }

        private CommonDialog create() {
            CommonDialog dialog = new CommonDialog(P.context, P.themeResId);
            dialog.apply(P);
            return dialog;
        }

        public CommonDialog createAndShow() {
            return show();
        }

        public CommonDialog show() {
            CommonDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
