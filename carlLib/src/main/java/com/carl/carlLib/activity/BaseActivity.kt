package com.carl.carlLib.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.carl.carlLib.utils.ActivityStack
import java.lang.reflect.Type

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/10/17
 * desc   : Activity基类
 * version: 1.0
 * ==============================================
 */
abstract class BaseActivity : FragmentActivity() {
    private var isCreate = false

    /**
     * 当前页面是否在前面
     *
     * @return
     */
    var isShow = false
        private set
    private var savedInstanceState: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        this.savedInstanceState = savedInstanceState
        ActivityStack.getInstance().addActivity(this)
        setContentView(getLayoutId())
        initVariables()
        initViews(savedInstanceState)
        loadData()
        isCreate = true
    }

    val aContext: Context
        get() = this

    override fun onResume() {
        super.onResume()
        isShow = true
        isCreate = if (isCreate) {
            return
        } else {
            initVariables()
            initViews(savedInstanceState)
            loadData()
            true
        }
    }

    override fun onPause() {
        super.onPause()
        isShow = false
    }

    /**
     * 初始化布局
     *
     * @author blue
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 参数设置
     *
     * @author carl
     */
    protected abstract fun initVariables()

    /**
     * 界面设置
     */
    protected abstract fun initViews(savedInstanceState: Bundle?)

    /**
     * 获取数据
     */
    protected abstract fun loadData()

    override fun onDestroy() {
        ActivityStack.getInstance().removeActivity(this)
        super.onDestroy()
    }

}