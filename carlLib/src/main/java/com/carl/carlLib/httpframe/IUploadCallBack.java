package com.carl.carlLib.httpframe;

/**
 * 上传通用返回方法
 */
public interface IUploadCallBack {
    //成功时候回调
    void onSuccess(String result);

    //失败时候回调
    void onFailure(String result);

    //上传进度回调
    void onProgress(Long progress, Long total);
}
