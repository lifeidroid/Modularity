package com.carl.carlLib.widget;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2020/04/10
 * desc   : 可控制滚动状态的ViewPager
 * version: 1.0
 * ==============================================
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class NoScrollViewPager extends ViewPager {
    private boolean isScroll;
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScrollViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);   // return true;不行
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isScroll){
            return super.onInterceptTouchEvent(ev);
        }else{
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.
        if (isScroll){
            return super.onTouchEvent(ev);
        }else {
            return true;// 可行,消费,拦截事件
        }
    }
    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }
}
