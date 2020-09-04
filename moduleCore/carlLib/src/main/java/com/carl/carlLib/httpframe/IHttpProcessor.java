package com.carl.carlLib.httpframe;

import java.util.Map;

/**
 * 网络访问方法
 */
public interface IHttpProcessor {
    void get(String url, Map<String, Object> parames, ICallBack callback);

    void get(String url, Map<String, String> header, Map<String, Object> parames, ICallBack callback);

    void post(String url, Map<String, Object> parames, ICallBack callback);

    void post(String url, Map<String, String> header, Map<String, Object> parames, ICallBack callback);

    void upload(String url, Map<String, Object> parames, Map<String, Object> fileParames, IUploadCallBack iUploadCallBack);

    void upload(String url, Map<String, String> header, Map<String, Object> parames, Map<String, Object> fileParames, IUploadCallBack iUploadCallBack);
}
