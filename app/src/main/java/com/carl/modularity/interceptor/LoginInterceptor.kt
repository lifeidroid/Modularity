package com.carl.modularity.interceptor

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import com.carl.carlLib.widget.T
import com.carl.moduledata.const.PageURL
import java.lang.RuntimeException

/**
 * 测试ARouter的拦截器，拦截需要登录的页面
 */
@Interceptor(priority = 2)
class LoginInterceptor : IInterceptor {
    val TAG = "Test2Interceptor"
    override fun init(context: Context?) {
        Log.e(TAG, "init: Test2Interceptor")
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        Log.e(TAG, "process: Test2Interceptor:${postcard?.extra}")
        if (postcard?.extra == 5) {
            callback?.onInterrupt(RuntimeException("异常情况需谨慎"))
            T.show("请先登录")
            ARouter.getInstance().build(PageURL.ACTIVITY_URL_TESTA).navigation()
        } else {
            callback!!.onContinue(postcard)
        }
    }
}