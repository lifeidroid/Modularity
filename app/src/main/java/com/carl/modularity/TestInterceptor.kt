package com.carl.modularity

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor

@Interceptor(priority = 1)
class TestInterceptor :IInterceptor{
     val TAG = "TestInterceptor"
    override fun init(context: Context?) {
        Log.e(TAG, "init: TestInterceptor", )
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        Log.e(TAG, "process: TestInterceptor", )
        callback!!.onContinue(postcard)
    }
}