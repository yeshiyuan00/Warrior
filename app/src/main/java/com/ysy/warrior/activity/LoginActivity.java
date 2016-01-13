package com.ysy.warrior.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.squareup.otto.Subscribe;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.ysy.warrior.R;
import com.ysy.warrior.Util.A;
import com.ysy.warrior.Util.L;
import com.ysy.warrior.Util.NetUtils;
import com.ysy.warrior.Util.PixelUtil;
import com.ysy.warrior.Util.T;
import com.ysy.warrior.config.Constants;
import com.ysy.warrior.otto.BusProvider;
import com.ysy.warrior.otto.FinishActivityEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;

/**
 * User: ysy
 * Date: 2015/12/30
 * Time: 11:28
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, IUiListener {

    //    private MyBroadcastReceiver receiver;

    private String nickName;
    private String photoUrl;
    private String gender;
    private String city;

    private View welcome;
    private View line;
    private View bottom;
    private View bg;
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        findViewById(R.id.bt_qq).setOnClickListener(this);
        findViewById(R.id.bt_sina).setOnClickListener(this);

        BusProvider.getInstance().register(this);
        bindViews();
        showAnim();
    }

    /**
     * 设置为全屏显示
     */
    private void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);
    }

    private void bindViews() {
        bg = findViewById(R.id.bg);
        welcome = findViewById(R.id.welcome);
        line = findViewById(R.id.line);
        bottom = findViewById(R.id.bottom);
    }

    private void showAnim() {
        int DURATION = 400;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //首先是背景
        bg.animate().translationY(-metrics.heightPixels + PixelUtil.dp2px(220)).setDuration(DURATION).start();
        line.animate().scaleX(1).setDuration(DURATION).setStartDelay(DURATION).start();

        welcome.animate().alpha(1).setDuration(DURATION).setStartDelay(DURATION).start();

        bottom.animate().alpha(1).setDuration(DURATION).setStartDelay(DURATION).start();
    }

    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {
        tv_title.setText(getString(R.string.login));
    }

    @Override

    protected View getContentView() {
        return View.inflate(context, R.layout.activity_login, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onFinishListen(FinishActivityEvent event) {
        L.i("finish");
        event.finish(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_qq:
                LoginQQ();
                break;
            case R.id.bt_sina:
                LoginSina();
                break;
            // case R.id.bt_weixin:
            // LoginWeiXin();
            // break;
        }
    }

    /**
     * QQ授权登录
     */
    private void LoginQQ() {
        Tencent mTencent = Tencent.createInstance(Constants.QQ_KEY, this.getApplicationContext());
        mTencent.login(this, "all", this);
    }


    /**
     * 新浪授权登录
     */
    private void LoginSina() {

    }


    @Override
    public void onComplete(Object o) {
        try {
            JSONObject jsonObject = new JSONObject(o.toString());
            String userId = jsonObject.getString("openid");
            String expiresIn = jsonObject.getString("expires_in");
            String accessToken = jsonObject.getString("access_token");
            BmobUser.BmobThirdUserAuth authInfo = new
                    BmobUser.BmobThirdUserAuth("qq", accessToken, expiresIn, userId);
            BmobUser.loginWithAuthData(context, authInfo, new OtherLoginListener() {
                @Override
                public void onSuccess(JSONObject userAuth) {
                    L.i("QQ授权成功去校验" + userAuth.toString());
                    getQQInfo(userAuth);
                }

                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
                    Log.i("smile", "第三方登陆失败：" + +code +"-" +msg);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(UiError uiError) {
        L.i("QQ第三方登陆失败：" + uiError.errorMessage);
        T.show(context, "QQ授权失败");
    }

    @Override
    public void onCancel() {
        L.i("QQ第三方登陆取消");
        T.show(context, "QQ授权取消");
    }

    /**
     * 获取QQ的信息
     */
    private void getQQInfo(final JSONObject obj) {
        // 若更换为自己的APPID后，仍然获取不到自己的用户信息，则需要
        // 根据http://wiki.connect.qq.com/get_user_info提供的API文档，想要获取QQ用户的信息，则需要自己调用接口，传入对应的参数
        new Thread() {
            @Override
            public void run() {
                try {
                    Map<String, String> params = new HashMap<String, String>();
                    // 下面则是返回的json字符
                    // {
                    // "qq": {
                    // "openid": "B4F5ABAD717CCC93ABF3BF28D4BCB03A",
                    // "access_token": "05636ED97BAB7F173CB237BA143AF7C9",
                    // "expires_in": 7776000
                    // }
                    // }
                    if (obj != null) {
                        // params.put("access_token", obj.getJSONObject("qq")
                        // .getString("access_token"));//
                        // 此为微博登陆成功之后返回的access_token
                        // params.put("uid",
                        // obj.getJSONObject("weibo").getString("uid"));//
                        // 此为微博登陆成功之后返回的uid
                        params.put("access_token", obj.getJSONObject("qq").getString("access_token"));// 此为QQ登陆成功之后返回access_token
                        params.put("openid", obj.getJSONObject("qq").getString("openid"));
                        params.put("oauth_consumer_key", Constants.QQ_KEY);// oauth_consumer_key为申请QQ登录成功后，分配给应用的appid
                        params.put("format", "json");// 格式--非必填项
                    }
                    String result = NetUtils.getRequest("https://graph.qq.com/user/get_user_info", params);
                    L.i("QQ的个人信息：" + result);
                    JSONObject json = new JSONObject(result);
                    nickName = json.getString("nickname");
                    gender = json.getString("gender");
                    photoUrl = json.getString("figureurl_qq_2").replace("\\", "");
                    city = json.getString("province") + " " + json.getString("city");
                    goDialogActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 必须跳到一个界面处理一下获取到的信息
     */
    protected void goDialogActivity() {
        Intent i = new Intent(context, DialogActivity.class);
        i.putExtra("nickName", nickName);
        i.putExtra("gender", gender);
        i.putExtra("photoUrl", photoUrl);
        i.putExtra("city", city);
        A.goOtherActivityNoAnim(context, i);
    }
}
