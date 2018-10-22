package com.gz.audio.ui;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.gz.audio.R;
import com.gz.audio.utils.IntentUtil;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
//    private com.gz.audio.MainBinding binding;

    View setting,history,collect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initEasypermissions();

        initView();
        initListener();
    }

    private void initView() {
        setting = findViewById(R.id.setting);
        history = findViewById(R.id.history);
        collect = findViewById(R.id.collect);
    }

    private void initListener() {
       setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(MainActivity.this, SettingActivity.class);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(MainActivity.this, HistoryActivity.class);
            }
        });
       collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(MainActivity.this, CollectActivity.class);
            }
        });
    }

    private void initEasypermissions() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(this, permissions)) {
            Log.e("Liuyz", "..........hasPermissions................");
        } else {
            Log.e("Liuyz", "..........requestPermissions................");
            EasyPermissions.requestPermissions(this, "点击确定应用需要获取的权限", 101, permissions);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms != null && perms.size() > 0) {
            for (int i = 0; i < perms.size(); i++) {
                Log.e("Liuyz", "...ok...perms......." + i + "....." + perms.get(i));
            }
            Log.e("Liuyz", "..........onPermissionsGranted................");
        }
    }

    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms != null && perms.size() > 0) {
            for (int i = 0; i < perms.size(); i++) {
                Log.e("Liuyz", "..faild....perms......." + i + "....." + perms.get(i));
            }
            Log.e("Liuyz", "..........onPermissionsDenied................");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
