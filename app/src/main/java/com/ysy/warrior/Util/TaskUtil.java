package com.ysy.warrior.Util;

import java.text.SimpleDateFormat;

/**
 *  任务的逻辑
 * User: ysy
 * Date: 2016/1/18
 * Time: 17:25
 */
public class TaskUtil {

    private static SimpleDateFormat sdf    = new SimpleDateFormat();
    private static final int              YEAR   = 365 * 24 * 60 * 60;// 年
    private static final int              MONTH  = 30 * 24 * 60 * 60;// 月
    private static final int              DAY    = 24 * 60 * 60;// 天
    private static final int              HOUR   = 60 * 60;// 小时
    private static final int              MINUTE = 60;// 分钟

    /**
     * 根据时间戳获取描述性时间，如3分钟后，1天后
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {

        /*if (timestamp <= System.currentTimeMillis()) {
            return TimeUtil.getDescriptionTimeFromTimestamp(timestamp);
        }

        Calendar curC = Calendar.getInstance();
        Calendar zixiC = Calendar.getInstance();
        zixiC.setTimeInMillis(timestamp);

        int curYear = curC.get(Calendar.YEAR);
        int curMonth = curC.get(Calendar.MONTH);
        int curDay = curC.get(Calendar.DAY_OF_YEAR);// 这一年的第几天
        int curHour = curC.get(Calendar.HOUR_OF_DAY);
        int curMin = curC.get(Calendar.MINUTE);

        int zixiYear = zixiC.get(Calendar.YEAR);
        int zixiMonth = zixiC.get(Calendar.MONTH);
        int zixiDay = zixiC.get(Calendar.DAY_OF_YEAR);// 这一年的第几天
        int zixiHour = zixiC.get(Calendar.HOUR_OF_DAY);
        int zixiMin = zixiC.get(Calendar.MINUTE);

        String result = "未知";
        if (curYear == zixiYear) {
            if (curMonth == zixiMonth) {
                if (curDay == zixiDay) {
                    if (zixiHour - curHour <= 1) {// 小于120分钟
                        if (zixiMin + 60 * (zixiHour - curHour) - curMin <= 3) {// 小于3分钟
                            result = "马上";
                        } else result = (zixiMin + 60 * (zixiHour - curHour) - curMin) + "分钟后";
                    } else result = (zixiHour - curHour) + "小时后";
                } else result = (zixiDay - curDay) + "天后";
            } else result = (zixiMonth - curMonth) + "个月后";
        } else result = (zixiYear - curYear) + "年后";
        curC = null;
        zixiC = null;
        return result;*/

        long currentTime = System.currentTimeMillis();
        long timeGap = (timestamp - currentTime) / 1000;// 与现在时间相差秒数
        String timeStr = "N/A";
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年后";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月后";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天后";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时后";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟后";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }
}
