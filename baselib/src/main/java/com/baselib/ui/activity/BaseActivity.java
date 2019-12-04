package com.baselib.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.baselib.LocaleUtils;
import com.baselib.language.LanguageType;
import com.baselib.language.LanguageUtil;
import com.baselib.sp.SharedPref;
import com.creativeindia.goodmorning.baselib.R;

import androidx.appcompat.app.AppCompatActivity;

import org.n.account.core.ui.LoadingDialog;
import org.n.account.core.utils.DialogUtils;

public abstract class BaseActivity extends AppCompatActivity {

    protected LoadingDialog mLoadingDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        //获取我们存储的语言环境 比如 "en","zh",等等
        String language = SharedPref.getString(newBase,SharedPref.LANGUAGE, LanguageType.ENGLISH.getLanguage());
        /**
         * attach对应语言环境下的context
         */
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, language));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (useStartDefaultAnim()) {
            overridePendingTransition(R.anim.slide_right_in, R.anim.no_slide);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * 新开页面从右滑入，之前的页面保持不动
     */
    protected boolean useStartDefaultAnim() {
        return true;
    }

    /**
     * 设置状态栏颜色
     *
     * @param color 颜色值
     */
    protected void setStatusBarColor(int color) {
        getWindow().setStatusBarColor(color);
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return getApplicationContext().getSharedPreferences(name, mode);
    }

    protected void setAndroidNativeLightStatusBar(boolean dark) {
        //6.0以下不起作用
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return;
//        }
        View decor = this.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    /**
     * 当前页面从右滑出，之前的页面保持不动
     */
    protected boolean useFinishDefaultAnim() {
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void reCreateActivity(){
        this.recreate();
    }

    public void dismissLoading() {
        DialogUtils.dismissDialog(mLoadingDialog);
        mLoadingDialog = null;
    }

    public void showLoading(String msg) {
        showLoading(msg,false);
    }

    public void showLoading(String msg,boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, true);
        }
        mLoadingDialog.setMsg(msg);
        mLoadingDialog.setCancelable(cancelable);
        if(cancelable){
            mLoadingDialog.setOnKeyListener(null);
        }else {
            mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == KeyEvent.ACTION_UP) {
                        return true;
                    }
                    return false;
                }
            });
        }
        DialogUtils.showDialog(mLoadingDialog);
    }
}
