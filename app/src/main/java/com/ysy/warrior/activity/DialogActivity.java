package com.ysy.warrior.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ysy.warrior.CustomApplication;
import com.ysy.warrior.R;
import com.ysy.warrior.Util.A;
import com.ysy.warrior.Util.L;
import com.ysy.warrior.Util.T;
import com.ysy.warrior.bean.MaxNumber;
import com.ysy.warrior.otto.BusProvider;
import com.ysy.warrior.otto.FinishActivityEvent;

import java.util.ArrayList;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * User: ysy
 * Date: 2015/12/30
 * Time: 15:57
 */
public class DialogActivity extends BaseActivity {

    protected Integer temp;
    private String nickName;
    private String photoUrl;
    private String gender;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nickName = getIntent().getStringExtra("nickName");
        photoUrl = getIntent().getStringExtra("photoUrl");
        gender = getIntent().getStringExtra("gender");
        city = getIntent().getStringExtra("city");
        L.i("nickName:" + nickName + ",photoUrl:" + photoUrl + ",gender:" + gender + ",city" + city);
        // 判断用户是否为空
        if (currentUser != null) {
            L.i("用户非空");
            if (currentUser.getUsername().length() >= 15) {
                // qq第一次注册，绑定username,和一大堆默认信息
                currentUser.setNick(nickName);
                currentUser.setAvatar(photoUrl);
                currentUser.setCity(city);
                currentUser.setHomeBg("http://file.bmob.cn/M01/BC/D4/oYYBAFVj1TuAXDU6AAA0P-InLD0537.jpg");
                currentUser.setPhoneNum("57575777");
                ArrayList<String> list = new ArrayList<String>();
                list.add("http://file.bmob.cn/M00/D6/4E/oYYBAFR9ZMuAI5HVAAAG8DKoaHY038.png");
                currentUser.setAlbum(list);
                currentUser.setMale((gender.equals("男") || gender.equals("m")) ? true : false);
                L.i("用户绑定id");
                bindUserName();
            } else {
                // 已经注册过了，更新用户信息去主界面(换个手机登录)
                // 需要綁定设备Id
                currentUser.setDeviceType("android");
                currentUser.setInstallId(BmobInstallation.getInstallationId(context));
                currentUser.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        goMainActivity();
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        L.i(arg0 + "更新用户信息去主界面失败" + arg1);
                        CustomApplication.getInstance().logout();
                        A.finishSelf(context);
                    }
                });
            }
        } else {
            finish();
            T.show(context, "用户为空,授权失败");
        }
    }

    private void bindUserName() {
        /** 从服务器获取一个可用的用户名 */
        BmobQuery<MaxNumber> query = new BmobQuery<MaxNumber>();
        query.getObject(this, "ZvX5LLLU", new GetListener<MaxNumber>() {
            @Override
            public void onSuccess(MaxNumber maxNumber) {
                temp++;
                L.i("temp=" + temp + ",用户名:" + currentUser.getUsername());
                // 重新设置用户名（数字串）
                currentUser.setUsername(temp + "");
                currentUser.setDeviceType("android");
                currentUser.setInstallId(BmobInstallation.getInstallationId(context));
                /* 必须调用update，不能用save */
                currentUser.update(DialogActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        goMainActivity();
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {
                        L.i(arg0 + "," + arg1);
                        CustomApplication.getInstance().logout();
                        A.finishSelf(context);
                    }
                });
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                L.i(arg0 + ",update," + arg1);
                CustomApplication.getInstance().logout();
                A.finishSelf(context);
            }
        });
    }

    /**
     * 去主界面
     */
    private void goMainActivity() {
        // 将设备与username进行绑定
        userManager.bindInstallationForRegister(currentUser.getUsername());
        updateUserInfos();
        BusProvider.getInstance().post(new FinishActivityEvent());
        A.goOtherActivityFinishNoAnim(context, MainActivity.class);
    }

    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {
        tv_title.setText("完善信息");
    }

    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.activity_dialog, null);
    }
}
