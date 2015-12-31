package com.ysy.warrior.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * User: ysy
 * Date: 2015/12/31
 * Time: 15:44
 */
public class UserDataActivity extends BaseActivity {
    // TODO: 2015/12/31  
    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {

    }

    @Override
    protected View getContentView() {
        TextView v = new TextView(this);
        v.setText("hello");
        return v;
    }

    public static void startUserProfileFromLocation(int[] startingLocation, Activity mainActivity) {
        Intent intent = new Intent(mainActivity, UserDataActivity.class);
        intent.putExtra("startingLocation", startingLocation);
        mainActivity.startActivity(intent);

    }
}
