package com.carl.carlLib.httpframe;

import android.os.Handler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp网络访问工具类
 */
public class OkhttpFrameProcessor implements IHttpProcessor {//实现这个接口
    private OkHttpClient mOkhttClient;
    private Handler mHandler;

    public OkhttpFrameProcessor() {
        mOkhttClient = new OkHttpClient();
        mHandler = new Handler();
    }

    private RequestBody appendBody(Map<String, Object> paramers) {
        FormBody.Builder body = new FormBody.Builder();
        if (paramers == null || paramers.isEmpty()) {
            return body.build();
        }
        for (Map.Entry<String, Object> entry : paramers.entrySet()) {
            body.add(entry.getKey(), entry.getValue().toString());
        }
        return body.build();
    }

    @Override
    public void get(String url, Map<String, Object> parames, final ICallBack callback) {
        final Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        mOkhttClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(result);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(response.message());
                        }
                    });
                }
            }
        });


    }

    @Override
    public void get(String url, Map<String, String> header, Map<String, Object> parames, final ICallBack callback) {
        Request.Builder builder = new Request.Builder();
        builder.get().url(url);
        if (null != header) {
            Iterator<Map.Entry<String, String>> entries = header.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                if (null != entry.getKey() && !"".equals(entry.getKey())) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
        mOkhttClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(result);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(response.message());
                        }
                    });
                }
            }
        });

    }

    @Override
    public void post(String url, Map<String, Object> parames, final ICallBack callback) {
        RequestBody requestBody = appendBody(parames);
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("User-Agent", "a")
                .build();
        mOkhttClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(result);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(response.message().toString());
                        }
                    });
                }

            }
        });
    }

    @Override
    public void post(String url, Map<String, String> header, Map<String, Object> parames, final ICallBack callback) {
        RequestBody requestBody = appendBody(parames);
        Request.Builder builder = new Request.Builder();
        builder.post(requestBody).url(url);
        if (null != header) {
            Iterator<Map.Entry<String, String>> entries = header.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                if (null != entry.getKey() && !"".equals(entry.getKey())) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            builder.addHeader("User-Agent", "a");
        }

        mOkhttClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(result);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(response.message().toString());
                        }
                    });
                }

            }
        });
    }

    @Override
    public void upload(String url, Map<String, Object> parames, Map<String, Object> fileParames, IUploadCallBack iUploadCallBack) {
//TODO 待开发
    }

    @Override
    public void upload(String url, Map<String, String> header, Map<String, Object> parames, Map<String, Object> fileParames, IUploadCallBack iUploadCallBack) {
//TODO 待开发
    }
}
