package com.ysy.warrior.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysy.warrior.R;
import com.ysy.warrior.bean.User;
import com.ysy.warrior.view.FlowLayout;
import com.ysy.warrior.view.OpAnimationView;
import com.ysy.warrior.view.RevealBackgroundView;
import com.ysy.warrior.view.SoundWaveView;
import com.ysy.warrior.view.materialmenu.MaterialMenuView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * User: ysy
 * Date: 2016/1/18
 * Time: 12:06
 */
public class AddTaskActivity extends BaseActivity {

    private static final int REQUES_IMG    = 0;
    private static final int REQUES_FRIEND = 1;

    boolean isFirst = true;
    private EditText et_name;
    private TextView tv_time, tv_time_tip;
    private TimePickerDialog timePickerDialog24h;
    private DatePickerDialog datePickerDialog;

    private TextView tv_date;
    private TextView wk_0, wk_1, wk_2, wk_3, wk_4, wk_5, wk_6;
    private TextView day_0, day_1, day_2, day_3, day_4, day_5, day_6;
    private final Calendar mCalendar = Calendar.getInstance();

    private String imgUrl   = null;
    private String audioUrl = null;
    private String note     = null;

    private ImageView iv;// 添加的图片
    private View       ll_audio;
    private FlowLayout ll_at_friend;
    private List<String> atFriends = new ArrayList<String>();
    private List<User>   at        = new ArrayList<User>();
    //private AudioUtils aUtils;

    // 标记第一个的时间基准
    private long headTime;

    private RevealBackgroundView vRevealBackground;

    private MaterialMenuView material_menu;
    private View             iv_sort; //title上面的

    private TextView tv_repeat, tv_tag;

    private View layout_date;//日期选择
    private View currentTime; //显示选择的时间
    private View iv_voice, iv_clear; //语音，清除
    private View ib_audio, ib_theme, ib_img, ib_at;

    private ViewGroup ll_recording;
    private ViewGroup et_layout;

    private SoundWaveView soundWaveView;
    private TextInputLayout textInputLayout;
    private OpAnimationView ib_save;


    public static void startUserProfileFromLocation(int[] startingLocation, Activity mainActivity) {
        Intent intent = new Intent(mainActivity, AddTaskActivity.class);
        intent.putExtra("startingLocation", startingLocation);
        mainActivity.startActivity(intent);
    }

    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {

    }

    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.activity_add_task, null);
    }
}
