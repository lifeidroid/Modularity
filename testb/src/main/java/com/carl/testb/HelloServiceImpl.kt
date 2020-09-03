package com.carl.testb

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.carl.moduledata.service.HelloService

@Route(path = "/service/hello", name = "测试服务")
class HelloServiceImpl:HelloService {
    override fun sayHello(name: String): String {

        return "hello,$name"
    }

    override fun init(context: Context?) {
    }
}