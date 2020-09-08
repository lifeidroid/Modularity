package com.carl.moduledata.base

import com.carl.carlLib.activity.IBaseModel
import com.carl.carlLib.httpframe.HttpCallback
import com.carl.carlLib.httpframe.HttpHelper

open class BaseModelImpl() : IBaseModel {

    /**
     * 已经认证过的GET请求(带默认参数的GET请求)
     *
     * @param url
     * @param map
     * @param httpCallBack
     */
    fun doAuthGet(url: String, map: HashMap<String, Any>?, httpCallBack: HttpCallback) {
        var header = HashMap<String, String>()
//        header["authorization"] = AppConfig.TOKEN
        HttpHelper.getInstance().get(url, header, map, httpCallBack)
    }

    /**
     * 已经认证过的POST请求(带默认参数的POST)
     *
     * @param url
     * @param map
     * @param httpCallBack
     */
    fun doAuthPost(url: String, map: HashMap<String, Any>?, httpCallBack: HttpCallback) {
        var header = HashMap<String, String>()
//        header["authorization"] = AppConfig.TOKEN
        HttpHelper.getInstance().post(url, header, map, httpCallBack)
    }

}