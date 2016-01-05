package com.ysy.warrior.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ysy.warrior.R;
import com.ysy.warrior.Util.L;
import com.ysy.warrior.Util.SP;
import com.ysy.warrior.bean.User;

import cn.bmob.im.BmobUserManager;

/**
 * User: ysy
 * Date: 2016/1/5
 * Time: 10:41
 */
public class AlertService extends Service {

    public static final String ACTION = "app.hanks.com.conquer.service.AlertService";
    private SoundPool pool;
    private int id;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (pool == null) {
            pool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
            String defaultAudioUri = (String) SP.get(this, "alert_audio", "");
            L.d("默认铃声：" + defaultAudioUri);
            if (!TextUtils.isEmpty(defaultAudioUri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(Uri.parse(defaultAudioUri), proj,
                        null, null, null);
                int actual_image_column_index = cursor.getColumnIndexOrThrow(
                        MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(actual_image_column_index);
                id = pool.load(path, 1);
            } else {
                id = pool.load(this, R.raw.notify, 1);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        User user = BmobUserManager.getInstance(this).getCurrentUser(User.class);
        if (user != null) {
            // 获取今天为被提醒任务
            //List<Task> list = TaskUtil.getTodayAfterZixi(this);
            int defaultAlertTime = (Integer) SP.get(this, "alert_time", 0);
            long alertTime = 1000 * 60 * 10;
            if (defaultAlertTime == 1) {
                alertTime = 1000 * 60 * 20;
            } else if (defaultAlertTime == 2) {
                alertTime = 1000 * 60 * 30;
            } else if (defaultAlertTime == 3) {
                alertTime = 1000 * 60 * 60;
            }


            //  L.d("今天未被提醒的任务:" + task.getId() + task.getName() + "," + task.getTime() + "提醒间隔：" + alertTime);
            // 10分钟提前提醒
           /* if (task.getTime() - System.currentTimeMillis() < alertTime) {
                Card card = new Card();
                card.setType(0);// 0。提醒卡
                card.setFid("1234567");
                card.setFusername("12345678");
                card.setFnick("Conquer");
                card.setZixiName(task.getName());
                card.setTime(task.getTime());
                card.settId(user.getObjectId());
                card.setZixiId(task.getId());
                card.setFavatar("http://file.bmob.cn/M00/D7/E0/oYYBAFSER8OAM-OhAAAC5aqrGKs048.png");
                int streamID = pool.play(id, 1, 1, 0, 0, 1);
                pool.setVolume(streamID, 1, 1);
                TaskUtil.setZixiHasAlerted(this, task.getId());
                // 提醒用户
                NotifyUtils.showZixiAlertToast(this, card);
            }
        }*/
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
