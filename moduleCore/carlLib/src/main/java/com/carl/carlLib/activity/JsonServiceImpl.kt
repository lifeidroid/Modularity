package com.carl.carlLib.activity

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.alibaba.fastjson.JSON
import java.lang.reflect.Type

/**
 * 本类的添加为了是Activity之间传递自定义实体参数时设置的，根据ARouter官方要求
 */
//此处根据ARouter要求，在传递自定义实体参数时，必须有这个类
// 如果需要传递自定义对象，新建一个类（并非自定义对象类），然后实现 SerializationService,并使用@Route注解标注(方便用户自行选择序列化方式)，例如：
@Route(path = "/yourservicegroupname/json")
class JsonServiceImpl : SerializationService {
    override fun init(context: Context?) {
    }

    override fun <T : Any?> json2Object(input: String?, clazz: Class<T>?): T {
        return JSON.parseObject(input, clazz)
    }

    override fun object2Json(instance: Any?): String {
        return JSON.toJSONString(instance)
    }

    override fun <T : Any?> parseObject(input: String?, clazz: Type?): T {
        return JSON.parseObject(input, clazz)
    }
}