package com.ysy.warrior.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lidroid.xutils.DbUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ysy.warrior.CustomApplication;
import com.ysy.warrior.R;
import com.ysy.warrior.Util.SystemBarTintManager;
import com.ysy.warrior.bean.User;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;

/**
 * User: ysy
 * Date: 2015/12/29
 * Time: 16:24
 */

/**
 * Activity的父类,做一些公共的初始化,变量,方法
 * Created by Hanks on 2015/5/17.
 */
public class BaseActivity extends FragmentActivity {
    protected Context context;
    protected BmobUserManager userManager;
    protected BmobChatManager manager;
    protected CustomApplication mApplication;
    protected User currentUser;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected ImageLoader imageLoader;
    protected DisplayImageOptions option_photo, option_pic;
    DbUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initStatusBar();
    }

    /**
     * 沉浸状态栏
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initStatusBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.title));
    }
}
