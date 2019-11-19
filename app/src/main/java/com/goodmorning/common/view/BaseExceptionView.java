package com.goodmorning.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cleanerapp.supermanager.R;


/**
 * Created by hejunge on 2017/11/24.
 * 内容列表加载异常 View
 */

public class BaseExceptionView extends FrameLayout {
    private Context mContext;
    private ITapReload tapReload;

    public BaseExceptionView(@NonNull Context context) {
        this(context, null);
    }

    public BaseExceptionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void setTapReload(ITapReload tapReload) {
        this.tapReload = tapReload;
    }

    private void init() {
        inflate(mContext, R.layout.goodmorning_exception_no_data_layout, this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tapReload != null) {
                    tapReload.onTapReload();
                }
            }
        });
    }

    public interface ITapReload {
        void onTapReload();
    }
}