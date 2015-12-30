package com.ysy.warrior.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/12/30
 * Time: 11:14
 */
public class NetUtils {
    /**
     * 当前是否有网
     */
    public static boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    /**
     * 检查是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    /**
     * 检查是否是WIFI
     */
    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }

    /**
     * 检查是否是移动网络
     */
    public static boolean isMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    public static long expires(String second) {
        Long l = Long.valueOf(second);
        return l * 1000L + System.currentTimeMillis();
    }

    /**
     * GET请求方式访问api接口
     *
     * @param urlString
     * @param params
     * @return
     */
    public static String getRequest(String urlString, Map<String, String> params) {

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(urlString);
            if (null != params) {
                urlBuilder.append("?");
                Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> param = iterator.next();
                    urlBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8")).append('=')
                            .append(URLEncoder.encode(param.getValue(), "UTF-8"));
                    if (iterator.hasNext()) {
                        urlBuilder.append('&');
                    }
                }
            }
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) new URL(urlBuilder.toString()).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15 * 3000);
            connection.setReadTimeout(15 * 3000);
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream is = connection.getInputStream();
                byte[] buff = new byte[2048];
                int len;
                while ((len = is.read(buff)) != -1) {
                    out.write(buff, 0, len);
                }
                is.close();
                out.flush();
                out.close();
                return new String(out.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
