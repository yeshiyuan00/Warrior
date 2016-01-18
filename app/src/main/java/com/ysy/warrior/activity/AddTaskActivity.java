package com.ysy.warrior.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysy.warrior.R;
import com.ysy.warrior.Util.AudioUtils;
import com.ysy.warrior.Util.PixelUtil;
import com.ysy.warrior.Util.SP;
import com.ysy.warrior.Util.TaskUtil;
import com.ysy.warrior.Util.TimeUtil;
import com.ysy.warrior.bean.Task;
import com.ysy.warrior.bean.User;
import com.ysy.warrior.view.FlowLayout;
import com.ysy.warrior.view.OpAnimationView;
import com.ysy.warrior.view.RevealBackgroundView;
import com.ysy.warrior.view.SoundWaveView;
import com.ysy.warrior.view.materialmenu.MaterialMenuDrawable;
import com.ysy.warrior.view.materialmenu.MaterialMenuView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * User: ysy
 * Date: 2016/1/18
 * Time: 12:06
 */
public class AddTaskActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener, View.OnClickListener {

    private static final int REQUES_IMG = 0;
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

    private String imgUrl = null;
    private String audioUrl = null;
    private String note = null;

    private ImageView iv;// 添加的图片
    private View ll_audio;
    private FlowLayout ll_at_friend;
    private List<String> atFriends = new ArrayList<String>();
    private List<User> at = new ArrayList<User>();
    private AudioUtils aUtils;

    // 标记第一个的时间基准
    private long headTime;

    private RevealBackgroundView vRevealBackground;

    private MaterialMenuView material_menu;
    private View iv_sort; //title上面的

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

    private Task task = new Task();

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static void startUserProfileFromLocation(int[] startingLocation, Activity mainActivity) {
        Intent intent = new Intent(mainActivity, AddTaskActivity.class);
        intent.putExtra("startingLocation", startingLocation);
        mainActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setupRevealBackground(savedInstanceState);
    }

    private void init() {
        // 播放音频的
        aUtils = AudioUtils.getInstance();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time_tip = (TextView) findViewById(R.id.tv_time_tip);
        tv_date = (TextView) findViewById(R.id.tv_title);
        material_menu = (MaterialMenuView) findViewById(R.id.material_menu);
        iv_sort = findViewById(R.id.iv_sort);
        iv_sort.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);

        ll_audio = findViewById(R.id.ll_audio);
        ll_audio.setVisibility(View.GONE);
        iv = (ImageView) findViewById(R.id.iv);
        iv.setOnClickListener(this);
        material_menu.setState(MaterialMenuDrawable.IconState.BURGER);
        material_menu.setOnClickListener(this);
        ll_at_friend = (FlowLayout) findViewById(R.id.ll_at_friend);


        layout_date = findViewById(R.id.layout_date);
        currentTime = findViewById(R.id.currentTime);

        iv_voice = findViewById(R.id.iv_voice);
        iv_clear = findViewById(R.id.iv_clear);

        et_layout = (ViewGroup) findViewById(R.id.et_layout);
        ll_recording = (ViewGroup) findViewById(R.id.ll_recording);
        soundWaveView = (SoundWaveView) findViewById(R.id.soundWaveView);

        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);

        iv_clear.setOnClickListener(this);

        //右下角
        ib_at = findViewById(R.id.ib_at);
        ib_img = findViewById(R.id.ib_img);
        ib_audio = findViewById(R.id.ib_audio);
        ib_theme = findViewById(R.id.ib_theme);
        tv_tag = (TextView) findViewById(R.id.tv_tag);
        tv_repeat = (TextView) findViewById(R.id.tv_repeat);
        ib_save = (OpAnimationView) findViewById(R.id.ib_save);

        ib_theme.setScaleX(0.8f);
        ib_theme.setScaleY(0.8f);
        ib_at.setScaleX(0.8f);
        ib_at.setScaleY(0.8f);
        ib_audio.setScaleX(0.8f);
        ib_audio.setScaleY(0.8f);
        ib_img.setScaleX(0.8f);
        ib_img.setScaleY(0.8f);

        ib_at.setOnClickListener(this);
        ib_img.setOnClickListener(this);
        ib_audio.setOnClickListener(this);
        ib_theme.setOnClickListener(this);
        ib_save.setOnClickListener(this);
        tv_tag.setOnClickListener(this);
        tv_repeat.setOnClickListener(this);

        wk_0 = (TextView) findViewById(R.id.wk_0);
        wk_1 = (TextView) findViewById(R.id.wk_1);
        wk_2 = (TextView) findViewById(R.id.wk_2);
        wk_3 = (TextView) findViewById(R.id.wk_3);
        wk_4 = (TextView) findViewById(R.id.wk_4);
        wk_5 = (TextView) findViewById(R.id.wk_5);
        wk_6 = (TextView) findViewById(R.id.wk_6);

        day_0 = (TextView) findViewById(R.id.day_0);
        day_1 = (TextView) findViewById(R.id.day_1);
        day_2 = (TextView) findViewById(R.id.day_2);
        day_3 = (TextView) findViewById(R.id.day_3);
        day_4 = (TextView) findViewById(R.id.day_4);
        day_5 = (TextView) findViewById(R.id.day_5);
        day_6 = (TextView) findViewById(R.id.day_6);

        wk_0.setOnClickListener(this);
        wk_1.setOnClickListener(this);
        wk_2.setOnClickListener(this);
        wk_3.setOnClickListener(this);
        wk_4.setOnClickListener(this);
        wk_5.setOnClickListener(this);
        wk_6.setOnClickListener(this);

        day_0.setOnClickListener(this);
        day_1.setOnClickListener(this);
        day_2.setOnClickListener(this);
        day_3.setOnClickListener(this);
        day_4.setOnClickListener(this);
        day_5.setOnClickListener(this);
        day_6.setOnClickListener(this);

        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        headTime = d.getTime();
        initWeekday(c);

        tv_date.setText(new SimpleDateFormat("yyyy/MM/dd").format(d));
        initDatePicker();
        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        //自动补全
       /* String[] course = Tasks.tasks;
        AutoCompleteArrayAdapter<String> adapter = new AutoCompleteArrayAdapter<String>(this,
                R.layout.item_list_simple, course);
        et_name.setAdapter(adapter);
        et_name.setDropDownHeight(metrics.heightPixels / 3);
        et_name.setThreshold(1);
        et_name.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (view.getText().length() > 0 && hasFocus) {
                    view.showDropDown();
                }
            }
        });*/
        et_name.addTextChangedListener(new EditTextChangeListener());
        et_name.setOnEditorActionListener(new SaveEditActionListener());
    }

    /**
     * title的动画
     */
    private void titleAnim() {
        layout_date.setTranslationY(-layout_date.getHeight());
        tv_tag.setTranslationY(tv_tag.getHeight());
        currentTime.setAlpha(0);
        et_name.setVisibility(View.VISIBLE);
        et_layout.setVisibility(View.VISIBLE);
        layout_date.setVisibility(View.VISIBLE);
        currentTime.setVisibility(View.VISIBLE);
        material_menu.animateState(MaterialMenuDrawable.IconState.ARROW);
        iv_sort.animate().rotation(90).setDuration(300).start();
        layout_date.animate().translationY(-PixelUtil.dp2px(3)).setDuration(300).start();
        currentTime.animate().alpha(1).setDuration(300).start();

        int w = (int) (ib_save.getWidth() * 1.7f);
        ib_theme.animate().translationY(-w).rotation(360).setDuration(300).setStartDelay(150).start();
        ib_at.animate().translationY((float) (-w * Math.sqrt(3) / 2)).translationX(-w / 2).rotation(360).setDuration(300).setStartDelay(100).start();
        ib_audio.animate().translationY(-w / 2).translationX((float) (-w * Math.sqrt(3) / 2)).rotation(360).setDuration(300).setStartDelay(50).start();
        ib_img.animate().translationX(-w).rotation(360).setDuration(300).start();
        ib_save.add2right();
        tv_tag.animate().translationY(0).setDuration(300).alpha(1).setStartDelay(300).start();

        task.setNeedAlerted(true);
    }

    /**
     * 初始化头部
     */
    private void initWeekday(Calendar c) {
        Calendar tmp = Calendar.getInstance(Locale.CHINA);
        tmp.setTimeInMillis(c.getTimeInMillis());
        wk_0.setText(TimeUtil.getWeekDayD(tmp.get(Calendar.DAY_OF_WEEK)));
        day_0.setText(tmp.get(Calendar.DAY_OF_MONTH) + "");
        tmp.add(Calendar.DAY_OF_YEAR, 1);
        wk_1.setText(TimeUtil.getWeekDayD(tmp.get(Calendar.DAY_OF_WEEK)));
        day_1.setText(tmp.get(Calendar.DAY_OF_MONTH) + "");
        tmp.add(Calendar.DAY_OF_YEAR, 1);
        wk_2.setText(TimeUtil.getWeekDayD(tmp.get(Calendar.DAY_OF_WEEK)));
        day_2.setText(tmp.get(Calendar.DAY_OF_MONTH) + "");
        tmp.add(Calendar.DAY_OF_YEAR, 1);
        wk_3.setText(TimeUtil.getWeekDayD(tmp.get(Calendar.DAY_OF_WEEK)));
        day_3.setText(tmp.get(Calendar.DAY_OF_MONTH) + "");
        tmp.add(Calendar.DAY_OF_YEAR, 1);
        wk_4.setText(TimeUtil.getWeekDayD(tmp.get(Calendar.DAY_OF_WEEK)));
        day_4.setText(tmp.get(Calendar.DAY_OF_MONTH) + "");
        tmp.add(Calendar.DAY_OF_YEAR, 1);
        wk_5.setText(TimeUtil.getWeekDayD(tmp.get(Calendar.DAY_OF_WEEK)));
        day_5.setText(tmp.get(Calendar.DAY_OF_MONTH) + "");
        tmp.add(Calendar.DAY_OF_YEAR, 1);
        wk_6.setText(TimeUtil.getWeekDayD(tmp.get(Calendar.DAY_OF_WEEK)));
        day_6.setText(tmp.get(Calendar.DAY_OF_MONTH) + "");

        int t = (Integer) SP.get(context, "theme", 0);
        int color = getResources().getColor(R.color.theme_0);
        if (t == 1)
            color = getResources().getColor(R.color.theme_1);
        else if (t == 2)
            color = getResources().getColor(R.color.theme_2);
        else if (t == 3)
            color = getResources().getColor(R.color.theme_3);
        day_0.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_bg));
        day_1.setBackgroundColor(color);
        day_2.setBackgroundColor(color);
        day_3.setBackgroundColor(color);
        day_4.setBackgroundColor(color);
        day_5.setBackgroundColor(color);
        day_6.setBackgroundColor(color);
    }

    /**
     * 初始化日历时间的选择控件
     */
    private void initDatePicker() {
        mCalendar.add(Calendar.MINUTE, 60);  // 设成60分钟后
        setTimeAndTip(new SimpleDateFormat("yyyy/MM/dd").format(mCalendar.getTime()) + ""
                + pad(mCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + pad(mCalendar.get(Calendar.MINUTE)));
        // TODO: 2016/1/18  
    }

    /**
     * 设置tv_time 和 tv_time_tip的文本内容
     *
     * @param string 类似yyyy/MM/dd HH:mm
     */
    private void setTimeAndTip(String string) {
        tv_time.setText(string.substring(string.length() - 5, string.length()));
        try {
            Date d = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(string);
            tv_time_tip.setTextColor(d.getTime() > System.currentTimeMillis() ? Color.GRAY : Color.RED);
            tv_time_tip.setText("(" + TaskUtil.getDescriptionTimeFromTimestamp(d.getTime()) + ")");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground = (RevealBackgroundView) findViewById(R.id.vRevealBackground);
        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra("startingLocation");
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return false;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            titleAnim();
        }
    }

    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {

    }

    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.activity_add_task, null);
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 回车保存
     */
    private class SaveEditActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // TODO: 2016/1/18
                //saveTask();
            }
            return true;
        }
    }

    private class EditTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            iv_clear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
