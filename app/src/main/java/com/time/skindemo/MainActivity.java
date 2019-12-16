package com.time.skindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.time.skindemo.skin.SkinManager;
import com.time.skindemo.skin.SkinResource;

import java.io.File;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 运行时权限申请（6.0+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skin();
            }
        });
        findViewById(R.id.button_restore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().restoreDefault();
            }
        });

    }

    @Override
    public void changeSkin(SkinResource skinResource) {
        super.changeSkin(skinResource);
        //做一些第三方View的改变
//        skinResource.getDrawableByName()
    }

    public void skin() {
        //应该从服务器上下载
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "myskin.skin";
        //换肤
        SkinManager.getInstance().loadSkin(skinPath);
    }

}
