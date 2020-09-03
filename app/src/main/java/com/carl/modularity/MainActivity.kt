package com.carl.modularity

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.carl.carlLib.activity.BaseActivity
import com.carl.carlLib.manage.DeviceManager
import com.carl.moduledata.const.PageURL
import com.carl.moduledata.model.LoginModel
import com.carl.moduledata.service.HelloService
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = PageURL.ACTIVITY_URL_MAIN)
class MainActivity : BaseActivity() {
    val TAG = "MainActivity"
    
    @Autowired
    lateinit var helloService:HelloService

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun initVariables() {
    }

    override fun initViews(savedInstanceState: Bundle?) {
        var student = LoginModel()
        student.name = "张三"
        student.age = 12
        btnTest.setOnClickListener {
            ARouter.getInstance().build(PageURL.ACTIVITY_URL_TESTB)
                .withString("name","lifeidroid")
                .withInt("age",7)
                .withObject("student",student)
                .navigation(this, object : NavigationCallback {
                    override fun onFound(postcard: Postcard?) {
                        Log.e(TAG, "onFound: group：${postcard?.group}  path:${postcard?.path}")
                    }

                    override fun onLost(postcard: Postcard?) {
                        Log.e(TAG, "onLost: group：${postcard?.group}  path:${postcard?.path}")
                    }

                    override fun onArrival(postcard: Postcard?) {
                        Log.e(TAG, "onArrival: group：${postcard?.group}  path:${postcard?.path}")
                    }

                    override fun onInterrupt(postcard: Postcard?) {
                        Log.e(TAG, "onInterrupt: group：${postcard?.group}  path:${postcard?.path}")
                    }
                })
        }

        btnTestService.setOnClickListener {
            Log.d(TAG, "initViews: ${helloService.sayHello("张三")}")
        }
    }

    override fun loadData() {
    }
}