package com.carl.carlLib.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/10/17
 * desc   : Fragment基类
 * version: 1.0
 * ==============================================
 */
abstract class BaseFragment : Fragment() {
    var mBundle: Bundle? = null
    var savedInstanceState: Bundle? = null
    var isShow = false
        private set

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
    }

    val aContext: Context?
        get() = activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.savedInstanceState = savedInstanceState
        val view = inflater.inflate(getLayoutId(), container, false)
        mBundle = savedInstanceState
        initBase()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariables()
        initViews(savedInstanceState)
        loadData()
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isShow = true
        }
    }

    /**
     * 初始化布局
     *
     * @author carl
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 自定义封装(在AppBaseActivity中实例化)
     *
     * @author carl
     */
    protected abstract fun initBase()

    /**
     * 参数设置
     *
     * @author carl
     */
    protected abstract fun initVariables()

    /**
     * 视图设置
     *
     * @author carl
     */
    protected abstract fun initViews(savedInstanceState: Bundle?)

    /**
     * 获取网络数据
     */
    protected abstract fun loadData()
    override fun onDestroy() {
        isShow = false
        super.onDestroy()
    }

    fun finish() {
        if (null != activity) {
            activity!!.finish()
        }
    }

    override fun startActivity(intent: Intent) {
        if (null != context) {
            context!!.startActivity(intent)
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        if (null != activity) {
            activity!!.startActivityForResult(intent, requestCode)
        }
    }

    fun sendBroadcast(intent: Intent?) {
        if (null != context) {
            context!!.sendBroadcast(intent)
        }
    }
}