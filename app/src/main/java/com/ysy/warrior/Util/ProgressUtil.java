package com.ysy.warrior.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * 进度条工具类
 * User: ysy
 * Date: 2016/1/5
 * Time: 11:34
 */
public class ProgressUtil {
    private static Dialog progressDialog;

    public static void showWaitting(Context context) {
        if (progressDialog == null) progressDialog = new ProgressDialog(context);// 创建自定义样式dialog
        progressDialog.setTitle("请稍后...");
        progressDialog.setCancelable(false);// 不可以用“返回键”取消
        progressDialog.show();
    }

    public static void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }
}
