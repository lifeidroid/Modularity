package com.carl.carlLib.widget;

import android.view.View;

import java.util.Calendar;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2019/05/13
 * desc   : 防止多次点击的点击事件
 * version: 1.0
 * ==============================================
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {
    //最小点击间隔
    public static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);
}
