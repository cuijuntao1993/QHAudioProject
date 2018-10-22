package com.gz.audio.ui.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.gz.audio.R;
import com.gz.audio.ui.BaseActivity;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity1 extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private FrameLayout frameLayout;
    private RadioGroup radioGroup;
    private Fragment[] mFragments;
    private int mIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initEasypermissions();

        initFragment();
        setRadioGroupListener();


    }

    private void initFragment() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        frameLayout = (FrameLayout) findViewById(R.id.fl_content);
        HomeFragment homeFragment = new HomeFragment();

        MineFragment mineFragment = new MineFragment();
        //添加到数组
        mFragments = new Fragment[]{homeFragment, mineFragment};
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //添加首页
        ft.add(R.id.fl_content, homeFragment).commit();
        //默认设置为第0个
        setIndexSelected(0);
    }

    private void setIndexSelected(int index) {
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.fl_content, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }
        ft.commit();
        //再次赋值
        mIndex = index;

    }

    private void setRadioGroupListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_home:
                        setIndexSelected(0);
                        break;

                    case R.id.rb_mine:
                        setIndexSelected(1);
                        break;
                    default:
                        setIndexSelected(0);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //仅当activity为task根（即首个启动activity）时才生效,这个方法不会改变task中的activity状态，
            // 按下返回键的作用跟按下HOME效果一样；重新点击应用还是回到应用退出前的状态；
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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


}
