package com.ysy.warrior.Util;

import android.content.Context;

import com.ysy.warrior.bean.Task;
import com.ysy.warrior.bean.User;
import com.ysy.warrior.config.Constants;
import com.ysy.warrior.db.TaskDao;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 任务的逻辑
 * User: ysy
 * Date: 2016/1/18
 * Time: 17:25
 */
public class TaskUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟


    /**
     * 返回大于当后时间的任务
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static List<Task> getAfterZixi(Context context) {

        List<Task> listTask = new ArrayList<Task>();
        List<Task> temp = new ArrayList<Task>();

        try {
            List<Task> findAll = new TaskDao(context).getAllTask();
            if (findAll != null && findAll.size() > 0) {
                temp.addAll(findAll);
                long curTime = System.currentTimeMillis();
                L.i("大小" + temp.size());
                for (Task task : temp) {
                    if (task.getTime() >= curTime || task.getRepeat() == 1) { //大于当前时间的或者每天重复的
                        listTask.add(task);
                        if (listTask.size() > Constants.MAIN_MYZIXI_LIMIT) break;
                    }
                }
            }
        } catch (Exception e) {
            // if (debugDB) e.printStackTrace();
        }

        return listTask;
    }


    /**
     * 返回网络上大于当后时间的任务
     *
     * @param context
     * @param currentUser
     * @param getZixiCallBack 获取网络数据的回调
     * @throws Exception
     */
    public static void getNetAfterZixi(final Context context, User currentUser, final int limit, final GetZixiCallBack getZixiCallBack) {
        BmobQuery<Task> query = new BmobQuery<Task>();
        // 设置查询条数
        query.setLimit(1000);
        query.addWhereEqualTo("user", currentUser).order("time");
        // 这个查询也包括了用户的已经过时的任务
        query.findObjects(context, new FindListener<Task>() {
            @Override
            public void onSuccess(List<Task> arg0) {
                L.d("网络个数:" + arg0.size());
                // 1.更新本地数据库
                if (arg0.size() > 0) {
                    TaskDao taskDao = new TaskDao(context);
                    taskDao.saveAll(arg0);
                }
                // 2.筛选大于当后时间的
                List<Task> listTask = new ArrayList<Task>();
                long curTime = System.currentTimeMillis();
                for (Task task : arg0) {
                    if (task.getTime() >= curTime || task.getRepeat() == 1) { //大于当前时间的或者每天重复的
                        listTask.add(task);
                    }
                    if (listTask.size() > limit) break;
                }
                getZixiCallBack.onSuccess(listTask);
            }

            @Override
            public void onError(int arg0, String arg1) {
                getZixiCallBack.onError(arg0, arg1);
            }
        });
    }

    public interface GetZixiCallBack {
        void onSuccess(List<Task> list);

        void onError(int errorCode, String msg);
    }

    /**
     * 删除任务
     *
     * @param context
     * @param task
     * @param deleteTaskListener
     */
    public static void deleteTask(Context context, Task task, final DeleteTaskListener deleteTaskListener) {
        new TaskDao(context).deleteTask(task);
//        final DbUtils dbUtils = DbUtils.create(context);
//        try {
//            dbUtils.delete(task);
//        } catch (DbException e) {
        // if (debugDB) e.printStackTrace();
//        }
        task.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {
                L.i("删除任务成功");
                deleteTaskListener.onSuccess();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                L.i("删除任务失败：" + arg0 + arg1);
                deleteTaskListener.onError(arg0, arg1);
            }
        });
    }

    public interface DeleteTaskListener {
        void onSuccess();

        void onError(int errorCord, String msg);
    }

    /**
     * 返回任务的时间字符串 如 14:12
     *
     * @return
     */
    public static String getZixiTimeS(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        DecimalFormat df = new DecimalFormat("00");
        return df.format(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + df.format(calendar.get(Calendar.MINUTE));
    }

    /**
     * 返回zixi的时间距现在还有几分钟
     *
     * @return
     */
    public static int getDurationFromNow(long time) {
        int result = (int) ((time - System.currentTimeMillis()) / 1000 / 60);
        return result;
    }

    /**
     * 返回任务的时间字符串 如 2014-11-20
     */
    public static String getZixiDateS(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        DecimalFormat df = new DecimalFormat("00");
        return calendar.get(Calendar.YEAR) + "-" + df.format(calendar.get(Calendar.MONTH) + 1) + "-"
                + df.format(calendar.get(Calendar.DAY_OF_MONTH));
    }


    /**
     * 上传一个文件
     *
     * @param context
     * @param f
     * @param upLoadListener
     */
    public static void upLoadFile(final Context context, File f, final UpLoadListener upLoadListener) {
        final BmobFile bf = new BmobFile(f);
        bf.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                L.d("上传文件成功" + bf.getFileUrl(context));
                upLoadListener.onSuccess(bf.getFileUrl(context));
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                L.d("上传文件失败" + arg0 + arg1);
                upLoadListener.onFailure(arg0, arg1);
            }
        });
    }


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

    public interface UpLoadListener {
        void onSuccess(String url);

        void onFailure(int error, String msg);
    }
}
