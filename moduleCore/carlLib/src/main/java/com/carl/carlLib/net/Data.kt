package com.carl.carlLib.net

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject

class Data(var json: JSONObject) {
    val msg: String
        get() {
            return if (json.containsKey("msg")) {
                json.getString("msg")
            } else {
                ""
            }
        }

    fun <T> getList(listname: String, classOfT: Class<T>): List<T>? {
        return JSON.parseArray(json.getJSONObject("data").getString(listname), classOfT)
    }

    fun <T> getModel(classOfT: Class<T>): T? {
        return JSON.parseObject(json.getString("data"), classOfT)
    }

    fun <T> getList(classOfT: Class<T>): List<T>? {
        return JSON.parseArray(json.getString("data"), classOfT)
    }

    fun <T> getModel(name: String, classOfT: Class<T>): T? {
        return JSON.parseObject(json.getJSONObject("data").getString(name), classOfT)
    }

    fun getInt(name: String): Int {
        return json.getJSONObject("data").getIntValue(name)
    }

    fun getString(name: String): String? {
        return json.getJSONObject("data").getString(name)
    }

    override fun toString(): String {
        return json.getString("data")
    }
}