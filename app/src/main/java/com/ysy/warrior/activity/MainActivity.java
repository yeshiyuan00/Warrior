package com.ysy.warrior.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ysy.warrior.R;
import com.ysy.warrior.Util.A;
import com.ysy.warrior.Util.PixelUtil;
import com.ysy.warrior.Util.SP;
import com.ysy.warrior.fragment.MenuFragment;
import com.ysy.warrior.fragment.MyTaskFragment;
import com.ysy.warrior.fragment.OtherTaskFragment;
import com.ysy.warrior.otto.BusProvider;
import com.ysy.warrior.otto.MenuPhotoClickEvent;
import com.ysy.warrior.view.OpAnimationView;
import com.ysy.warrior.view.materialmenu.MaterialMenuDrawable;
import com.ysy.warrior.view.materialmenu.MaterialMenuView;

import cn.bmob.v3.datatype.BmobGeoPoint;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private DrawerLayout drawerLayout;
    private MaterialMenuView materialMenu;
    private MenuFragment menuFragment;// 侧滑菜单Fragment
    private ImageButton iv_sort;
    private View iv_search;
    private View toolbar;
    private TextView mTitle;
    private PopupWindow popWin;
    private SwipeRefreshLayout refreshLayout;
    private OpAnimationView addButtom;
    private MyTaskFragment myTaskFragment;
    private OtherTaskFragment otherTaskFragment;
    private Fragment currentFragment;
    private boolean isFirst = true;
    private boolean userDataFlag;

    private RelativeLayout layout_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materialMenu = (MaterialMenuView) findViewById(R.id.material_menu);
        toolbar = findViewById(R.id.title);
        mTitle = (TextView) findViewById(R.id.tv_title);
        iv_sort = (ImageButton) findViewById(R.id.iv_sort);
        addButtom = (OpAnimationView) findViewById(R.id.iv_add);
        iv_search = findViewById(R.id.iv_search);
        iv_search.setVisibility(View.VISIBLE);
        iv_search.setOnClickListener(this);
        materialMenu.setOnClickListener(this);
        iv_sort.setOnClickListener(this);
        addButtom.setOnClickListener(this);
        initUserLocation();
        initDrawerMenu();
        initMain();

        BusProvider.getInstance().register(this);
        layout_main = (RelativeLayout) findViewById(R.id.id_layout_main);
    }

    @Subscribe
    public void goUserData(MenuPhotoClickEvent event) {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            //A.goOtherActivity(context, UserDataActivity.class);
            userDataFlag = true;
        }
    }

    private void initMain() {
        myTaskFragment = new MyTaskFragment();
        otherTaskFragment = new OtherTaskFragment();
        currentFragment = myTaskFragment;
        if (isFirst) {
            startIntroAnimation();
            isFirst = false;
        }
    }

    /**
     * 侧滑抽屉界面
     */
    private void initDrawerMenu() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置抽屉菜单左边剩余的空间的阴影
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                layout_main.setTranslationX(drawerView.getWidth() * slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                materialMenu.animatePressedState(MaterialMenuDrawable.IconState.X);
                materialMenu.animateState(MaterialMenuDrawable.IconState.ARROW);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                materialMenu.animateState(MaterialMenuDrawable.IconState.BURGER);
                if (userDataFlag) {
                    int[] location = new int[]{PixelUtil.dp2px(20), PixelUtil.dp2px(56)};
                    UserDataActivity.startUserProfileFromLocation(location, MainActivity.this);
                    overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        // 侧滑菜单
        menuFragment = new MenuFragment();
        changeFramgnt(R.id.left_drawer, menuFragment);
    }

    private void initUserLocation() {
        double longitude = Double.parseDouble((String) SP.get(context.getApplicationContext(), "longitude", "0"));
        double latitude = Double.parseDouble((String) SP.get(context.getApplicationContext(), "latitude", "0"));
        currentUser.setLocation(new BmobGeoPoint(longitude, latitude));
        currentUser.update(context.getApplicationContext());
    }

    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {

    }

    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.layout_main, null);
    }

    /**
     * 切换侧滑菜单布局打开或关闭
     */
    public void toggle() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            materialMenu.animateState(MaterialMenuDrawable.IconState.BURGER);
        } else {
            // materialMenu.animateState(MaterialMenuDrawable.IconState.X);
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.material_menu:
                toggle();
                break;
            case R.id.iv_search:
                A.goOtherActivityNoAnim(context, SearchResultActivity.class);
                break;
            case R.id.iv_add:
                launcherAddTaskActivity(v);
                break;
        }
    }

    public void launcherAddTaskActivity(View v) {
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        startingLocation[0] += v.getWidth() / 2;
        AddTaskActivity.startUserProfileFromLocation(startingLocation, this);
        overridePendingTransition(0, 0);
    }

    private void startIntroAnimation() {
        addButtom.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        int actionbarSize = PixelUtil.dp2px(57);
        toolbar.setTranslationY(-actionbarSize);
        mTitle.setTranslationY(-actionbarSize);
        iv_sort.setTranslationY(-actionbarSize);

        toolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        mTitle.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        iv_sort.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();

    }

    private void startContentAnimation() {
        addButtom.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(400)
                .start();
        changeFramgnt(R.id.layout_content, currentFragment);
    }
}
