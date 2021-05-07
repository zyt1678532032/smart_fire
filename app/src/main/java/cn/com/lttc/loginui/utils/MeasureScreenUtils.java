package cn.com.lttc.loginui.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 获取屏幕尺寸参数工具类
 */
public class MeasureScreenUtils {

    private MeasureScreenUtils() {
    }

    /**
     * 获取屏幕倍数
     *
     * @param activity
     * @return
     */
    public static float getDensity(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        float density = displayMetrics.density;//屏幕密度/160（0.75、1、1.5、2、3、4）
        return density;
    }

    /**
     * 获取屏幕密度
     *
     * @param activity
     * @return
     */
    public static int getDensityDpi(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int densityDpi = displayMetrics.densityDpi;//屏幕密度
        return densityDpi;
    }

    /**
     * 获取屏幕宽度方向分辨率
     *
     * @param activity
     * @return
     */
    public static int getWidthPixels(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;//宽度方向分辨率
        return widthPixels;
    }

    /**
     * 获取屏幕高度方向分辨率
     *
     * @param activity
     * @return
     */
    public static int getHeightPixels(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int heightPixels = displayMetrics.heightPixels;//高度方向分辨率
        return heightPixels;
    }

    /**
     * 获取文字放大系数
     *
     * @param activity
     * @return
     */
    public static float getScaledDensity(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        float scaledDensity = displayMetrics.scaledDensity;//文字放大系数
        return scaledDensity;
    }

    /**
     * 获取宽度方向屏幕密度
     *
     * @param activity
     * @return
     */
    public static float getXdpi(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        float xdpi = displayMetrics.xdpi;//宽度方向屏幕密度
        return xdpi;
    }

    /**
     * 获取高度方向屏幕密度
     *
     * @param activity
     * @return
     */
    public static float getYdpi(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        float ydpi = displayMetrics.ydpi;//高度方向屏幕密度
        return ydpi;
    }

    /**
     * 获取屏幕尺寸
     *
     * @param activity
     * @return
     */
    public static float getScreenSize(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;//宽度方向分辨率
        int heightPixels = displayMetrics.heightPixels;//高度方向分辨率
        float xdpi = displayMetrics.xdpi;//宽度方向屏幕密度
        float ydpi = displayMetrics.ydpi;//高度方向屏幕密度
        float screenSize = (float) Math.sqrt(Math.pow(widthPixels / xdpi, 2) + Math.pow(heightPixels / ydpi, 2));//屏幕物理尺寸
        return screenSize;
    }

}
