package com.carl.carlLib.widget;

import android.graphics.Color;

import com.blankj.utilcode.util.ToastUtils;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2019/01/02
 * desc   : Toast
 * version: 1.0
 * ==============================================
 */
public class T {
    public static void show(String msg) {
        ToastUtils.setBgColor(Color.parseColor("#90000000"));
        ToastUtils.setMsgColor(Color.WHITE);
        ToastUtils.setMsgTextSize(26);
        ToastUtils.showLong(msg);
    }
}
