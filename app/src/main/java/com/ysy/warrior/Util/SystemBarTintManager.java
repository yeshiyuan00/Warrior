package com.ysy.warrior.Util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * User: ysy
 * Date: 2015/12/29
 * Time: 16:36
 * <p>
 * Class to manage status and navigation bar tint effects when using KitKat
 * translucent system UI modes.
 */
public class SystemBarTintManager {

    static {
        // Android allows a system property to override the presence of the
        // navigation bar.
        // Used by the emulator.
        // See
        // https://github.com/android/platform_frameworks_base/blob/master/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java#L1076
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                sNavBarOverride = null;
            }
        }
    }

    /**
     * The default system bar tint color value.
     */
    public static final int DEFAULT_TINT_COLOR = 0x99000000;

    private static String sNavBarOverride;

    private final SystemBarConfig mConfig;
    private boolean mStatusBarAvailable;
    private boolean mNavBarAvailable;
    private boolean mStatusBarTintEnabled;
    private boolean mNavBarTintEnabled;
    private View mStatusBarTintView;
    private View mNavBarTintView;

    /**
     * Constructor. Call this in the host activity onCreate method after its
     * content view has been set. You should always create new instances when
     * the host activity is recreated.
     *
     * @param activity The host activity.
     */
    @TargetApi(19)
    public SystemBarTintManager(Activity activity) {

        Window win = activity.getWindow();
        ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //check theme attr
            int attrs[] = {android.R.attr.windowTranslucentStatus,
                    android.R.attr.windowTranslucentNavigation};
            TypedArray a = activity.obtainStyledAttributes(attrs);

            try {
                mStatusBarAvailable = a.getBoolean(0, false);
                mNavBarAvailable = a.getBoolean(1, false);
            } finally {
                a.recycle();
            }
            // check window flags
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((winParams.flags & bits) != 0) {
                mStatusBarAvailable = true;
            }
            bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if ((winParams.flags & bits) != 0) {
                mNavBarAvailable = true;
            }
        }

        mConfig = new SystemBarConfig(activity, mStatusBarAvailable, mNavBarAvailable);

        // device might not have virtual navigation keys

    }

    public void setStatusBarTintEnabled(boolean b) {

    }

    public void setStatusBarTintColor(int color) {

    }


    /**
     * Class which describes system bar sizing and other characteristics for the
     * current device configuration.
     */
    public static class SystemBarConfig {
        private SystemBarConfig(Activity activity, boolean translucentStatusBar, boolean traslucentNavBar) {

        }
    }
}
