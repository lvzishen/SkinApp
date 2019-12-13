package com.time.skindemo;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.time.skindemo.skin.SkinAttrsSupport;
import com.time.skindemo.skin.SkinManager;
import com.time.skindemo.skin.attr.SkinAttr;
import com.time.skindemo.skin.attr.SkinView;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期：2019/12/11 on 16:05
 * 描述: 换肤BaseActivity
 * 作者: lvzishen
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }

            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                //拦截到View的创建 获取View之后要去解析
                //1.创建View
                //getDelegate().createView 主要是去兼容AppCompat
                View view = getDelegate().createView(parent, name, context, attrs);
                Log.i(TAG, view + "");
                //2.解析属性 src textColor background 自定义属性
                if (view != null) {
                    List<SkinAttr> skinAttrs = SkinAttrsSupport.getSkinAttrs(context, attrs);
                    SkinView skinView = new SkinView(view, skinAttrs);
                    //3.统一交给SkinManager管理
                    managerSkinView(skinView);
                    //4.判断一下是否换肤
                    SkinManager.getInstance().checkChangeSkin(skinView);
                }

                return view;
            }

        });
        super.onCreate(savedInstanceState);

    }

    /**
     * 统一交给SkinManager管理
     *
     * @param skinView
     */
    private void managerSkinView(SkinView skinView) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if (skinViews == null) {
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(this, skinViews);
        }
        skinViews.add(skinView);
    }

}
