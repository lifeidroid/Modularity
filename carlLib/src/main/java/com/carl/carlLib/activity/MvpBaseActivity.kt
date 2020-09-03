package com.carl.carlLib.activity

import android.os.Bundle
import com.carl.carlLib.presenter.BasePresenter

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/10/17
 * desc   : MVP框架下的BaseActivity
 * version: 1.0
 * ==============================================
 */
abstract class MvpBaseActivity<V, T : BasePresenter<V>> : BaseActivity() {
    lateinit var mPresenter: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = createPresenter()
        mPresenter!!.attachView(this as V)
    }

    override fun onDestroy() {
        mPresenter!!.detachView()
        super.onDestroy()
    }

    abstract fun createPresenter(): T
}