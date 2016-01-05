package com.ysy.warrior.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ysy.warrior.R;
import com.ysy.warrior.Util.CollectionUtils;
import com.ysy.warrior.Util.UserDataUtils;
import com.ysy.warrior.bean.User;
import com.ysy.warrior.view.CircularImageView;

import java.util.List;

/**
 * User: ysy
 * Date: 2015/12/31
 * Time: 11:37
 */
public class MenuFragment extends BaseFragment implements View.OnClickListener {
    private CircularImageView iv_photo;
    private TextView tv_nickname;
    private ImageView iv_home_bg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        init(v);
        return v;
    }


    /**
     * 初始化
     *
     * @param v
     */
    private void init(View v) {
        iv_photo = (CircularImageView) v.findViewById(R.id.iv_photo);
        iv_home_bg = (ImageView) v.findViewById(R.id.iv_home_bg);
        tv_nickname = (TextView) v.findViewById(R.id.tv_nickname);
        iv_photo.setOnClickListener(this);
        v.findViewById(R.id.ll_friend).setOnClickListener(this);
        v.findViewById(R.id.ll_all_zixi).setOnClickListener(this);
        v.findViewById(R.id.ll_history).setOnClickListener(this);
        v.findViewById(R.id.ll_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_photo:
                // TODO: 2015/12/31
                // BusProvider.getInstance().post(new MenuPhotoClickEvent());
                break;
            case R.id.ll_friend:
                // TODO: 2015/12/31
                //A.goOtherActivity(context, FriendActivity.class);
                break;
            case R.id.ll_all_zixi:
                // TODO: 2015/12/31
                // A.goOtherActivity(context, AllMyTaskActivity.class);
                break;
            case R.id.ll_history:
                // TODO: 2015/12/31
                //  A.goOtherActivity(context, MyHistoryActivity.class);
                break;
            case R.id.ll_setting:
                // TODO: 2015/12/31
                //A.goOtherActivityFinish(context, SettingActivity.class);
                break;
        }
    }

    @Override
    public void onResume() {
        // TODO: 2015/12/31
        initUserData();
        UserDataUtils.queryUserByUsername(context, currentUser.getUsername(),
                new UserDataUtils.QueryUserDataListener() {
                    @Override
                    public void onSuccess(List<User> arg0) {
                        if (CollectionUtils.isNotNull(arg0)) {
                            currentUser = arg0.get(0);
                            initUserData();
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String msg) {

                    }
                });
        super.onResume();
    }

    public void initUserData() {
        if (currentUser != null) {
            ImageLoader.getInstance().displayImage(currentUser.getAvatar(), iv_photo);
            ImageLoader.getInstance().displayImage(currentUser.getHomeBg(), iv_home_bg);
            tv_nickname.setText(currentUser.getNick());
        }
    }
}
