package com.ysy.warrior.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ysy.warrior.R;
import com.ysy.warrior.config.Constants;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 给一个布局用来播放音频
 * <p>
 * User: ysy
 * Date: 2016/1/18
 * Time: 16:10
 */
public class AudioUtils {
    private static MediaPlayer player; //播放器
    private static Timer timer_play;   //定时器
    private static MyBroadcastReceiver receiver;
    private static AudioUtils audioUtils;
    private static String lastPath;
    private static int lastView;


    private AudioUtils() {
    }

    public static AudioUtils getInstance() {
        if (audioUtils == null) {
            audioUtils = new AudioUtils();
            if (timer_play == null) timer_play = new Timer();
        }
        return audioUtils;
    }


    /**
     * 播放或继续
     *
     * @param context
     * @param view
     * @param recorderPath
     */
    public void play(final Context context, View view, String recorderPath) {
        if (lastPath != null) L.d("lastPath=" + lastPath);
        L.d("lastView=" + lastView);
        L.d("recorderPath=" + recorderPath + ",view=" + view.hashCode());
        // 注册退出广播
        if (receiver == null) {
            L.d("注册音乐停止广播");
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constants.ACTION_DESTORY_PLAYER);
            receiver = new MyBroadcastReceiver();
            context.registerReceiver(receiver, filter);
        }
        // 换了个音频,重新开始
        if (recorderPath == null || !recorderPath.equals(lastPath) || view.hashCode() != lastView) {
            lastPath = recorderPath;
            lastView = view.hashCode();
            // 停止上一个控件状态
            if (pb != null) pb.setProgress(0);
            if (ib_play != null) {
                ib_play.setTag("play");
                ib_play.setImageResource(R.drawable.play_audio);
            }
            // 获取当前的控件
            ib_play = (ImageButton) view.findViewById(R.id.ib_play);
            pb = (ProgressBar) view.findViewById(R.id.pb);
            tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            loading = (ProgressBar) view.findViewById(R.id.loading);
        }

        L.d("播放按钮tag" + ib_play.getTag());
        // 判断播放按钮状态
        if (ib_play.getTag().equals("play")) {
            // 播放操作
            ib_play.setTag("pause");
            ib_play.setImageResource(R.drawable.pause_audio);
            if (player != null) player.release();
            player = new MediaPlayer();
            player.reset();
            try {
                player.setDataSource(recorderPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepareAsync();
            loading.setVisibility(0);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    pb.setMax(player.getDuration());
                    loading.setVisibility(View.GONE);
                    player.start();
                    playingAnim(context);
                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ib_play.setTag("play");
                    ib_play.setImageResource(R.drawable.play_audio);
                    pb.setProgress(0);
                    if (timer_play != null) {
                        timer_play.cancel();
                        timer_play = null;
                    }

                }
            });
        } else if (ib_play.getTag().equals("pause")) {// 暂停操作
            ib_play.setTag("resume");
            ib_play.setImageResource(R.drawable.play_audio);
            player.pause();
            if (timer_play != null) {
                timer_play.cancel();
                timer_play = null;
            }
            stopAnim();
        } else {
            ib_play.setTag("pause");
            ib_play.setImageResource(R.drawable.pause_audio);
            player.start();
            playingAnim(context);
        }

        // try {
        // if (player == null) player = new MediaPlayer();
        // player.setDataSource(recorderPath);
        // player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // player.prepareAsync();
        // loading.setVisibility(0);
        // player.setOnPreparedListener(new OnPreparedListener() {
        // @Override
        // public void onPrepared(MediaPlayer mp) {
        // pb.setMax(player.getDuration());
        // if (isFirst) {
        // isFirst = false;
        // pb.setProgress(0);
        // curPosition = 0;
        // loading.setVisibility(View.GONE);
        // }
        // palying(context);
        // }
        // });
        // player.setOnCompletionListener(new OnCompletionListener() {
        // @Override
        // public void onCompletion(MediaPlayer mp) {
        // ib_play.setTag("play");
        // ib_play.setImageResource(R.drawable.play_audio);
        // pb.setProgress(0);
        // curPosition = 0;
        // stopAnim();
        // releasePlayer();
        // }
        // });
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }


    /**
     * 释放播放器
     */
    private void releasePlayer() {
        if (pb != null) pb.setProgress(0);
        if (ib_play != null) {
            ib_play.setTag("play");
            ib_play.setImageResource(R.drawable.play_audio);
        }
        if (player != null) {
            player.release();
            player = null;
        }
    }

    /**
     * 停止进度，秒动画
     */
    private void stopAnim() {
        if (timer_play != null) {
            timer_play.cancel();
            timer_play = null;
        }
    }


    /**
     * 播放时进度，秒的动画
     */
    private void playingAnim(final Context context) {
        if (timer_play == null) timer_play = new Timer();
        timer_play.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        if (player != null) {
                            tv_duration.setText((player.getCurrentPosition() / 1000) + "秒");
                            pb.setProgress(player.getCurrentPosition());
                        }
                    }
                });
            }
        }, new Date(), 1000);
    }

    private ProgressBar pb;
    private TextView tv_duration;
    private ProgressBar loading;
    private ImageButton ib_play;

    /**
     * Acivitiy 进行Finish的接受者
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Constants.ACTION_DESTORY_PLAYER.equals(intent.getAction())) {
                stopAnim();
                releasePlayer();
                L.d("停止音乐");
                context.unregisterReceiver(this);
                receiver = null;
            }
        }
    }
}
