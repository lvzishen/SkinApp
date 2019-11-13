package com.baselib.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.baselib.statistic.StatisticConstants;
import com.baselib.statistic.StatisticLoggerX;


public abstract class BaseEventLoggerActivity extends BaseActivity {

    private static final String FUNCTION_FROM_SOURCE = "function_from_source";
    private static final String FUNCTION_STYLE = "function_style";
    private static final String FUNCTION_FROM_CONTAINER = "function_from_container";

    protected String mLoggerStyle = null;
    protected String mFromContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (TextUtils.isEmpty(mFromContainer)) {
            mFromContainer = getContainerName();
        }
        super.onCreate(savedInstanceState);

//        StatisticLoggerX.logShow(getName()/*"ScanningPage"*/, JUNK_FILES, getFromSource(),null);
        if (!isCustomLogger()) {
            statisticLogger();
        }
    }

    protected String getContainerName() {
        return null;
    }

    protected void statisticLogger() {
        StatisticLoggerX.logShowTypeStyle(getName(), getFromContainer(), getFromSource(), getLoggerStyle(), getLoggerType(), StatisticConstants.LOGGER_NEW);
    }

    protected String getFromContainer() {
        String container = getIntent().getStringExtra(StatisticConstants.KEY_FROM_CONTAINER);
        if (!TextUtils.isEmpty(container)) {
            mFromContainer = container;
        }
        if (TextUtils.isEmpty(mFromContainer)) {
            if (TextUtils.isEmpty(mFromContainer)) {
                mFromContainer = getIntent().getStringExtra(FUNCTION_FROM_CONTAINER);
            }
        }
        if (TextUtils.isEmpty(mFromContainer)) {
            return getContainerName();
        }
        return mFromContainer;
    }

    protected boolean isCustomLogger() {
        return false;
    }

    protected String getLoggerType() {
        return null;
    }

    protected String getLoggerStyle() {
        if (TextUtils.isEmpty(mLoggerStyle)) {
            return getIntent().getStringExtra(FUNCTION_STYLE);
        }
        return mLoggerStyle;
    }

    protected abstract String getName();

    protected String getFromSource() {
        String fromSource = getIntent().getStringExtra(StatisticConstants.KEY_FROM_SOURCE);
        if (TextUtils.isEmpty(fromSource)) {
            fromSource = getIntent().getStringExtra(FUNCTION_FROM_SOURCE);
        }
        return fromSource;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {
            intent = new Intent();
        }
        intent.putExtra(FUNCTION_FROM_SOURCE, getFromSource());
        intent.putExtra(FUNCTION_FROM_CONTAINER, getFromContainer());
        intent.putExtra(FUNCTION_STYLE, getLoggerStyle());
        super.startActivityForResult(intent, requestCode);
    }


}
