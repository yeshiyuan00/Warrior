package com.ysy.warrior.Util;

import android.annotation.SuppressLint;

/**
 * User: ysy
 * Date: 2016/1/18
 * Time: 17:10
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    /**
     * 返回星期汉字串 一 二 三 四 五 六 日
     *
     * @param i
     * @return
     */
    public static String getWeekDayD(int i) {
        String[] sb = new String[]{"日", "一", "二", "三", "四", "五", "六", "日"};
        return sb[i - 1];
    }
}
