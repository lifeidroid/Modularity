package com.carl.carlLib.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carl.carlLib.presenter.BasePresenter

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/10/17
 * desc   : MVP框架下的BaseFragment
 * version: 1.0
 * ==============================================
 */
abstract class MvpBaseFragment<V, T : BasePresenter<V>> : BaseFragment() {
    lateinit var mPresenter: T
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mPresenter = createPresenter()
        mPresenter.attachView(this as V)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    abstract fun createPresenter(): T
}