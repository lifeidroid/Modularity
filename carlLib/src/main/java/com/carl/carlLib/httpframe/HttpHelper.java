package com.carl.carlLib.httpframe;

import java.util.Map;

/**
 * 网络访问代理类
 */
public class HttpHelper implements IHttpProcessor {
    private static IHttpProcessor mFrameHttpProcessor;
    private static HttpHelper _instance;

    public HttpHelper() {

    }

    public static void init(IHttpProcessor processor) {
        mFrameHttpProcessor = processor;
    }

    public static HttpHelper getInstance() {
        synchronized (HttpHelper.class) {
            if (_instance == null) {
                _instance = new HttpHelper();
            }
            return _instance;
        }
    }

    @Override
    public void get(String url, Map<String, Object> parames, ICallBack callback) {
        //这里我们进行字符串的拼接
        String url_result = appendParamers(url, parames);
        mFrameHttpProcessor.get(url_result, parames, callback);
    }

    @Override
    public void get(String url, Map<String, String> header, Map<String, Object> parames, ICallBack callback) {
        //这里我们进行字符串的拼接
        String url_result = appendParamers(url, parames);
        mFrameHttpProcessor.get(url_result, header, parames, callback);
    }

    @Override
    public void post(String url, Map<String, Object> parames, ICallBack callback) {
        mFrameHttpProcessor.post(url, parames, callback);
    }

    @Override
    public void post(String url, Map<String, String> header, Map<String, Object> parames, ICallBack callback) {
        mFrameHttpProcessor.post(url, header, parames, callback);
    }

    @Override
    public void upload(String url, Map<String, Object> parames, Map<String, Object> fileParames, IUploadCallBack callback) {
        mFrameHttpProcessor.upload(url, parames, fileParames, callback);
    }

    @Override
    public void upload(String url, Map<String, String> header, Map<String, Object> parames, Map<String, Object> fileParames, IUploadCallBack iUploadCallBack) {
        mFrameHttpProcessor.upload(url, header, parames, fileParames, iUploadCallBack);
    }

    /**
     * 拼接Get访问的URL地址
     *
     * @param url
     * @param paramers
     * @return
     */
    static String appendParamers(String url, Map<String, Object> paramers) {
        if (paramers == null || paramers.isEmpty()) {
            return url;
        }
        StringBuilder urlBuider = new StringBuilder(url);
        if (urlBuider.indexOf("?") <= 0) {
            urlBuider.append("?");
        } else {
            if (!urlBuider.toString().endsWith("?")) {
                urlBuider.append("&");
            }
        }
        for (Map.Entry<String, Object> entry : paramers.entrySet()) {
            urlBuider.append(entry.getKey()).append("=").append(entry.getValue().toString());
        }
        return urlBuider.toString();
    }

}