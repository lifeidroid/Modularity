package com.carl.carlLib.httpframe;

/**
 * 网络访问通用返回方法
 */
public interface ICallBack {
    //成功时候回调
    void onSuccess(String result);
    //失败时候回调
    void onFailure(String result);
}