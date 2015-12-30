package com.ysy.warrior.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ysy.warrior.R;
import com.ysy.warrior.view.meterialmenu.MaterialMenuView;

public class MainActivity extends BaseActivity {
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private DrawerLayout drawerLayout;
    private MaterialMenuView materialMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {

    }

    @Override
    protected View getContentView() {
        return null;
    }

}
