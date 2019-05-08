/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package us.pinguo.foundation.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;


/**
 * Collection of utility functions used in this package.
 */
public class Util {
    // Orientation hysteresis amount used in rounding, in degrees
    public static final int ORIENTATION_HYSTERESIS = 20;
    public static final int[] COUNT_DOWN_TIME = {3, 5, 10};
    private static final String TAG = Util.class.getSimpleName();
    public static String PACKAGE_NAME = "";
    public static String VERSION_CODE = "";
    public static String VERSION_NAME = "";
    public static String PHONE_LOCALE = "";
    // 上次点击的时间
    private static long lastClickTime;
    private static int SCREEN_HEIGHT = 0;
    private static int SCREEN_WIDTH = 0;
    private static float sPixelDensity = 3;
    private static int versionCode = -1;

    private Util() {
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public static int getScreenWidth() {
        if (SCREEN_WIDTH == 0) {
            throw new RuntimeException("Must call initialize method first !!!");
        }
        return SCREEN_WIDTH;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    public static int getScreenHeight() {
        if (SCREEN_HEIGHT == 0) {
            throw new RuntimeException("Must call initialize method first !!!");
        }
        return SCREEN_HEIGHT;
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public static float getPixelDensity() {
        if (sPixelDensity == -1) {
            throw new RuntimeException("Must call initialize method first !!!");
        }
        return sPixelDensity;
    }

    public static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    /**
     * 转换rect
     *
     * @param rectF 原始rect范围
     * @param rect  转换后rect范围
     */
    public static void rectFToRect(RectF rectF, Rect rect) {
        rect.left = Math.round(rectF.left);
        rect.top = Math.round(rectF.top);
        rect.right = Math.round(rectF.right);
        rect.bottom = Math.round(rectF.bottom);
    }

    /**
     * 验证URI是否有效
     *
     * @param uri
     * @param resolver
     * @return
     */
    public static boolean isUriValid(Uri uri, ContentResolver resolver) {
        if (uri == null) {
            return false;
        }

        try {
            ParcelFileDescriptor pfd = resolver.openFileDescriptor(uri, "r");
            if (pfd == null) {
                return false;
            }
            pfd.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    /**
     * 关闭文件流
     *
     * @param c
     */
    public static void closeSilently(Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }

    /**
     * 初始化工具类
     *
     * @param context
     */
    public static void initialize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        sPixelDensity = metrics.density;
        SCREEN_HEIGHT = metrics.heightPixels;
        SCREEN_WIDTH = metrics.widthPixels;

        try {
            PACKAGE_NAME = context.getPackageName();
            PackageInfo manager = context.getPackageManager()
                    .getPackageInfo(PACKAGE_NAME, PackageManager.GET_CONFIGURATIONS);
            VERSION_CODE = String.valueOf(manager.versionCode);
            VERSION_NAME = manager.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isTabletDivice() {
        int screenSize = SCREEN_WIDTH;
        int dpSize = Util.pixelTodp(screenSize);
        if (dpSize >= 600) {
            return true;
        }
        return false;
    }

    public static boolean isSupportTiltshift() {
        return sPixelDensity > 1;
    }

    /**
     * 获取当前app版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        if (versionCode != -1) {
            return versionCode;
        }
        if (context == null) {
            versionCode = -1;
        }

        PackageInfo manager;
        try {
            manager = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionCode = manager.versionCode;
        } catch (NameNotFoundException e) {
            versionCode = -1;
        }
        return versionCode;
    }

    public static boolean isSupported(String value, List<String> supported) {
        return supported != null && supported.indexOf(value) >= 0;
    }

    /**
     * dp转像素
     *
     * @param dp
     * @return
     */
    public static float dpToPixel(float dp) {
        return sPixelDensity * dp;
    }

    public static int dpToPixel(int dp) {
        return Math.round(dpToPixel((float) dp));
    }

    public static int pixelTodp(int pixel) {
        return Math.round(pixel / sPixelDensity);
    }

    public static int meterToPixel(float meter) {
        // 1 meter = 39.37 inches, 1 inch = 160 dp.
        return Math.round(dpToPixel(meter * 39.37f * 160));
    }

    public static int dpToPx(float dp) {
        return (int) (sPixelDensity * dp + 0.5f);
    }

    public static void Assert(boolean cond) {
        if (!cond) {
            throw new AssertionError();
        }
    }

    public static <T> T checkNotNull(T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a == null ? false : a.equals(b));
    }

    public static int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation = false;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }

    public static int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation, int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }

    /**
     * 获取当前语言环境
     *
     * @param context
     * @return
     */
    public static String getLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }


    public static boolean isZh(String language) {
        return "zh".equals(language.toLowerCase(Locale.ENGLISH));
    }

    public static PackageInfo getMyPackageInfo(Context context) throws NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
    }

    /**
     * 产生0~99999999的随机数,不足的用0补充
     *
     * @return 产生0~99999999的随机数,不足的用0补充
     * @author liubo
     */
    public static String getRandomString() {
        final int max = 100000000;
        int random = new Random().nextInt(max);
        String str = String.format(Locale.ENGLISH, "%8d", random);
        return str.replace(' ', '0');
    }

    /**
     * 是否是快速点击
     *
     * @return true 快速点击 false 常规点击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 400) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    public static <T> int indexOf(T[] array, T s) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isPhoneNum(String value) {
        if (TextUtils.isEmpty(value) || value.length() != 11) {
            return false;
        }
        String reg = "1\\d{10}";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(value).matches();
    }


    /**
     * 产生[0,n)的不重复的随机数
     *
     * @param n
     * @return
     */
    public static int[] randomIntWithoutRepeat(final int n) {
        int[] randomInts = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            if (i == n - 1) {
                //最后一个直接找,提高效率
                randomInts[i] = findLastRandom(randomInts, n);
            } else {
                randomInts[i] = random.nextInt(n);
            }
            for (int j = 0; j < i; j++) {
                if (randomInts[j] == randomInts[i]) {
                    randomInts[i] = random.nextInt(n);
                    j = -1;
                }
            }
        }
        return randomInts;
    }

    private static int findLastRandom(int[] randomInts, int n) {
        boolean[] booleanArray = new boolean[n];
        for (int i = 0; i < n; i++) {
            booleanArray[i] = false;
        }
        for (int i = 0; i < n - 1; i++) {
            booleanArray[randomInts[i]] = true;
        }
        for (int i = 0; i < n; i++) {
            if (!booleanArray[i]) {
                return i;
            }
        }
        return 0;
    }

    private static long appOpenTime = 0L;
    public static int getDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public static int getDayOfMonth(long currentTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = simpleDateFormat.format(new Date(currentTime));
        String[] datas = data.split("-");
        int day = Integer.valueOf(datas[2]);
        return day;
    }

}
