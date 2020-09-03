package com.carl.moduledata.service

import com.alibaba.android.arouter.facade.template.IProvider
// 声明接口,其他组件通过接口来调用服务
interface HelloService : IProvider {
    fun sayHello(name: String): String
}