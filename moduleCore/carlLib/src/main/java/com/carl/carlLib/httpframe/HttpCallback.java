package com.carl.carlLib.httpframe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carl.carlLib.net.Data;

public abstract class HttpCallback implements ICallBack {

    @Override
    public void onSuccess(String result) {
        JSONObject jsonObj = JSON.parseObject(result);
        if (jsonObj.containsKey("code")) {
            int code = jsonObj.getIntValue("code");
            if (code == 200) {
                onData(new Data(jsonObj));
            } else {
                if (jsonObj.containsKey("msg")) {
                    onError(Integer.parseInt(jsonObj.getString("code")), jsonObj.getString("msg"));
                }
                if (jsonObj.containsKey("message")) {
                    onError(Integer.parseInt(jsonObj.getString("code")), jsonObj.getString("message"));
                }
            }
        } else {
            onError(404, "网络异常，请稍后重试！");
        }
    }

    @Override
    public void onFailure(String result) {
        if (null != result && !"".equals(result)) {
            onError(404, result);
        } else {
            onError(404, "网络异常，请稍后重试！");
        }
    }

    public abstract void onData(Data data);

    public abstract void onError(int code, String msg);

}