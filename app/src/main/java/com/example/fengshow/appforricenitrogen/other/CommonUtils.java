package com.example.fengshow.appforricenitrogen.other;

/**
 * Created by fengshow on 2017/5/12.
 */

public class CommonUtils {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
