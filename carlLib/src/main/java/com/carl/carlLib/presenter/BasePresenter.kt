package com.carl.carlLib.presenter

import java.lang.ref.WeakReference

abstract class BasePresenter<V> {
    /**
     * 当内存不足时释放内存
     */
    var mViewRef: WeakReference<V>? = null


    val view: V?
        get() = mViewRef?.get()

    /**
     * bind presenter with view
     * @param view
     */
    fun attachView(view: V) {
        mViewRef = WeakReference<V>(view)
    }

    /**
     * detach view manager
     */
    fun detachView() {
        if (mViewRef != null) {
            mViewRef?.clear()
            mViewRef = null
        }
    }

}
