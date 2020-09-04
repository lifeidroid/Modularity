package cn.carl.communicationLib.utils;

import android.util.Log;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/05
 * desc   :
 * version: 1.0
 * ==============================================
 */

public class SubStringUtils {
    private static String TAG = "SubStringUtils";

    public SubStringUtils() {
    }

    public static String substring(StringBuffer sbMsg, int start) {
        return substring(sbMsg, start, sbMsg.length());
    }

    public static String substring(StringBuffer sbMsg, int start, int end) {
        return substring(sbMsg.toString(), start, end);
    }

    public static String substring(String sbMsg, int start) {
        return substring(sbMsg, start, sbMsg.length());
    }

    public static String substring(String sbMsg, int start, int end) {
        if (sbMsg != null && sbMsg.length() >= start && sbMsg.length() >= end) {
            return sbMsg.substring(start, end);
        } else {
            Log.d(TAG, "截取字符串长度不够:(" + start + "," + end + ")" + sbMsg);
            return "";
        }
    }
}
