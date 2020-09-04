package cn.carl.communicationLib.ble;

import android.os.Message;

import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.util.HashMap;
import java.util.Map;

import cn.carl.communicationLib.constant.JCConst;
import cn.carl.communicationLib.socket.MessageObj;
import cn.carl.communicationLib.utils.DataUtils;
import cn.carl.communicationLib.utils.MyLog;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/11
 * desc   :
 * version: 1.0
 * ==============================================
 */
public class ReadNotiftCallBack extends BleNotifyCallback {
    private BleDecodeInterface bleDecodeInterface;
    private String alias;
    private String tag;
    private String address;
    private BleDevice device;
    private StringBuilder cacheBufffur = new StringBuilder();
    //上一次心跳时间
    private long lastHeartBeat;

    public ReadNotiftCallBack(BleDevice device) {
        this.address = device.getMac();
        this.device = device;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

//    public void setHandler(Handler handler) {
//        this.handler = handler;
//    }

    public void setBleDecodeInterface(BleDecodeInterface bleDecodeInterface) {
        this.bleDecodeInterface = bleDecodeInterface;
        if (cacheBufffur.length() > 0) {
            bleDecodeInterface.decodeData(cacheBufffur.toString(), new BleDecodeInterface.DecodeResult() {
                @Override
                public void onResult(String msg) {
                    cacheBufffur.setLength(0);
                    sendDataBack(true, msg);
                }
            });
        }
    }

    @Override
    public void onNotifySuccess() {
        Message message = new Message();
        message.what = JCConst.WHAT_BLE_ADD;
        Map<String,String> params = new HashMap<String,String>();
        params.put("mac",device.getMac());
        params.put("name",device.getName());
        message.obj = params;
        if (null != BleMaster.getInstance().getHandler()){
            BleMaster.getInstance().getHandler().sendMessage(message);
        }
    }

    @Override
    public void onNotifyFailure(BleException exception) {
        //FIXME 设置notify失败
    }

    @Override
    public void onCharacteristicChanged(byte[] data) {
        lastHeartBeat = System.currentTimeMillis();
        String newMsg = DataUtils.BytesToHexStr(data).toString();
        MyLog.d("onCharacteristicChanged：" + newMsg);
        if (null != bleDecodeInterface) {
            bleDecodeInterface.decodeData(newMsg, new BleDecodeInterface.DecodeResult() {
                @Override
                public void onResult(String msg) {
                    sendDataBack(true, msg);
                }
            });
        } else {
            cacheBufffur.append(newMsg);
            sendDataBack(false, DataUtils.BytesToHexStr(data).toString());
        }
    }

    private void sendDataBack(boolean isDeal, String result) {
        if (null != BleMaster.getInstance().getHandler()){
            Message message = new Message();
            message.what = JCConst.WHAT_BLE_MESSAGE;
            MessageObj messageObj = new MessageObj();
            messageObj.setDeal(isDeal);
            messageObj.setAddress(address);
            messageObj.setAlias(alias);
            messageObj.setTag(tag);
            messageObj.setDatas(result);
            message.obj = messageObj;
            BleMaster.getInstance().getHandler().sendMessage(message);
        }
    }

    public long getLastHeartBeat() {
        return lastHeartBeat;
    }

    public void setLastHeartBeat(long lastHeartBeat) {
        this.lastHeartBeat = lastHeartBeat;
    }
}
