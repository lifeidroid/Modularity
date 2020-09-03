package com.carl.carlLib.httpframe;

import android.app.Application;
import android.util.Log;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class XUtilsProcessor implements IHttpProcessor {
    public XUtilsProcessor(Application app) {
        x.Ext.init(app);
        x.Ext.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    @Override
    public void get(final String url, Map<String, Object> parames, final ICallBack callback) {
        final RequestParams requestParams = new RequestParams(url);
        requestParams.setConnectTimeout(1000 * 60);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("XUtilsProcessor", "GET请求\n请求地址：" + url + "\n成功返回:" + result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("XUtilsProcessor", "GET请求\n请求地址：" + url  + "\n失败原因:" + ex.getMessage());
                callback.onFailure(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void get(final String url, Map<String, String> header, Map<String, Object> parames,final ICallBack callback) {
        final RequestParams requestParams = new RequestParams(url);
        requestParams.setConnectTimeout(1000 * 60);
        if (null != header) {
            Iterator<Map.Entry<String, String>> entries = header.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                if (null != entry.getKey() && !"".equals(entry.getKey())) {
                    requestParams.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("XUtilsProcessor", "GET请求\n请求地址：" + url + "\n成功返回:" + result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("XUtilsProcessor", "GET请求\n请求地址：" + url  + "\n失败原因:" + ex.getMessage());
                callback.onFailure(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void post(final String url, Map<String, Object> parames, final ICallBack callback) {
        final RequestParams requestParams = new RequestParams(url);
        requestParams.setAsJsonContent(true);
        if (null != parames) {
            Iterator<Map.Entry<String, Object>> entries = parames.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                if (null != entry.getKey() && !"".equals(entry.getKey())) {
                    requestParams.addParameter(entry.getKey(), entry.getValue());
                } else {
                    requestParams.setBodyContent((String) entry.getValue());
                    Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url + "    \n参数:" +entry.getValue());
                }
            }
        }
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url + "    \n参数:" + requestParams.toJSONString() + "\n成功返回:" + result);
                } catch (JSONException e) {
                    Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url +  "\n成功返回:" + result);
                    e.printStackTrace();
                }
                Log.d("XUtilsProcessor", "-------------------------------------------");
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException){
                    try {
                        Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url + "    \n参数:" + requestParams.toJSONString() + "\n失败原因:" + ex.toString());
                    } catch (JSONException e) {
                        Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url  + "\n失败原因:" + ex.getMessage());
                        e.printStackTrace();
                    }
                    callback.onFailure(ex.getMessage());
                }
                Log.d("XUtilsProcessor", "-------------------------------------------");

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Override
    public void post(final String url, Map<String, String> header, Map<String, Object> parames, final ICallBack callback) {
        final RequestParams requestParams = new RequestParams(url);
        requestParams.setAsJsonContent(true);
        if (null != header) {
            Iterator<Map.Entry<String, String>> entries = header.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                if (null != entry.getKey() && !"".equals(entry.getKey())) {
                    requestParams.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
        if (null != parames) {
            Iterator<Map.Entry<String, Object>> entries = parames.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                if (null != entry.getKey() && !"".equals(entry.getKey())) {
                    requestParams.addParameter(entry.getKey(), entry.getValue());
                } else {
                    requestParams.setBodyContent((String) entry.getValue());
                    Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url + "    \n头部参数:" +entry.getValue());
                }
            }
        }
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url + "    \n参数:" + requestParams.toJSONString() + "\n成功返回:" + result);
                } catch (JSONException e) {
                    Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url +  "\n成功返回:" + result);
                    e.printStackTrace();
                }
                Log.d("XUtilsProcessor", "-------------------------------------------");
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException){
                    try {
                        Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url + "    \n参数:" + requestParams.toJSONString() + "\n失败原因:" + ex.toString());
                    } catch (JSONException e) {
                        Log.d("XUtilsProcessor", "POST请求\n请求地址：" + url  + "\n失败原因:" + ex.getMessage());
                        e.printStackTrace();
                    }
                    callback.onFailure(ex.getMessage());
                }
                Log.d("XUtilsProcessor", "-------------------------------------------");

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void upload(String url, Map<String, Object> parames, Map<String, Object> fileParames, final IUploadCallBack callBack) {
        RequestParams params = new RequestParams(url);
        if (null != parames) {
            for (Map.Entry<String, Object> entry : parames.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        params.setConnectTimeout(1000 * 60);
        if (null != fileParames) {
            for (Map.Entry<String, Object> entry : fileParames.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue(), null);
            }
        }
        try {
            Log.d("androidlib", "params:" + params.toJSONString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("androidlib", "fileParams:" + fileParames.toString());
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long l, long l1, boolean b) {
                callBack.onProgress(l1, l);
            }

            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!isOnCallback) {
                    callBack.onFailure("网络错误！");
                }
            }


            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void upload(String url, Map<String, String> header, Map<String, Object> parames, Map<String, Object> fileParames,final IUploadCallBack iUploadCallBack) {
        RequestParams params = new RequestParams(url);
        if (null != header) {
            Iterator<Map.Entry<String, String>> entries = header.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                if (null != entry.getKey() && !"".equals(entry.getKey())) {
                    params.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
        if (null != parames) {
            for (Map.Entry<String, Object> entry : parames.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        params.setConnectTimeout(1000 * 60);
        if (null != fileParames) {
            for (Map.Entry<String, Object> entry : fileParames.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue(), null);
            }
        }
        try {
            Log.d("androidlib", "params:" + params.toJSONString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("androidlib", "fileParams:" + fileParames.toString());
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long l, long l1, boolean b) {
                iUploadCallBack.onProgress(l1, l);
            }

            @Override
            public void onSuccess(String result) {
                iUploadCallBack.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!isOnCallback) {
                    iUploadCallBack.onFailure("网络错误！");
                }
            }


            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
