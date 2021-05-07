package cn.com.lttc.loginui.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 屏幕适配工具类
 */
public class ScreenAdaptUtils {

    private static float sNoncompatDesity;
    private static float sNoncompatScaledDesity;

    private ScreenAdaptUtils() {
    }

    public static void setCustomDesity(Activity activity, final Application application, int designPicture_dp) {

        final DisplayMetrics appdisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDesity == 0) {
            sNoncompatDesity = appdisplayMetrics.density;
            sNoncompatScaledDesity = appdisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration configuration) {
                    if (configuration != null && configuration.fontScale > 0) {
                        sNoncompatScaledDesity = application.getResources().getDisplayMetrics().scaledDensity;
                    }

                }

                @Override
                public void onLowMemory() {

                }
            });

        }

        final float targetDesity = (float) appdisplayMetrics.widthPixels / designPicture_dp;//designPicture_dp为设计图的宽度dp
        final float targetScaleDesity = targetDesity * (sNoncompatScaledDesity / sNoncompatDesity);
        final int targetDesityDpi = (int) (160 * targetDesity);

        appdisplayMetrics.density = targetDesity;
        appdisplayMetrics.scaledDensity = targetScaleDesity;
        appdisplayMetrics.densityDpi = targetDesityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDesity;
        activityDisplayMetrics.scaledDensity = targetScaleDesity;
        activityDisplayMetrics.densityDpi = targetDesityDpi;
    }


}
