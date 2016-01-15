package com.ysy.warrior.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.ysy.warrior.R;
import com.ysy.warrior.view.CircularImageView;
import com.ysy.warrior.view.RevealBackgroundView;
import com.ysy.warrior.view.materialmenu.MaterialMenuView;

/**
 * User: ysy
 * Date: 2015/12/31
 * Time: 15:44
 */
public class UserDataActivity extends BaseActivity {

    private static String photoUrl = "";
    private static String homeUrl = "";

    private ObservableScrollView scrollView;
    private CircularImageView iv_photo;
    private ImageView iv_gender;

    private TextView tv_id, tv_nickname;
    private TextView et_city, et_phone;
    private ViewGroup ll_label, ll_album;
    private ImageView add_pic;
    private ImageView iv_home_bg;

    private int SCROLL_DIS = 180;// 头部滑动检测距离
    private int home_bg_height;

    private View title_bg;
    private View add;
    private View topView;
    private View iv_camera;
    private View data;
    private View album;
    private View bt_eidt;

    private RevealBackgroundView vRevealBackground;
    private MaterialMenuView material_menu;

    private boolean finishRelav;
    private boolean isHide = false;

    // TODO: 2015/12/31  
    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {

    }

    @Override
    protected View getContentView() {
       return View.inflate(context,R.layout.activity_user_data,null);
    }

    public static void startUserProfileFromLocation(int[] startingLocation, Activity mainActivity) {
        Intent intent = new Intent(mainActivity, UserDataActivity.class);
        intent.putExtra("startingLocation", startingLocation);
        mainActivity.startActivity(intent);

    }
}
