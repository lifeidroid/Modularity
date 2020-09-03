package cn.carl.communicationLib.bt;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.carl.communicationLib.constant.JCConst;
import cn.carl.communicationLib.socket.MessageObj;
import cn.carl.communicationLib.utils.DataUtils;
import cn.carl.communicationLib.utils.MyLog;
import cn.carl.communicationLib.utils.StringUtils;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/11
 * desc   : BT Client 线程
 * version: 1.0
 * ==============================================
 */
public class BtClientThread implements Runnable {
    private BluetoothSocket mSocket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private String address = null;
    private Handler handler;
    private BtDecodeInterface decodeInterface;
    //设备别名
    private String alias;
    //设备标签
    private String tag;
    private StringBuilder cacheBuffer = new StringBuilder();
    //上一次心跳时间
    private long lastHeartBeat;
    //心跳超时时间
    private long limitTime = JCConst.HEART_TIMEOUT;
    //是否需要保活检测
    private boolean needKeepAlive = false;


    public BtClientThread(String address, BluetoothSocket socket) {
        this.address = address;
        this.mSocket = socket;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return address;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDecodeInterface(BtDecodeInterface decodeInterface) {
        this.decodeInterface = decodeInterface;
        if (cacheBuffer.length() > 0) {
            decodeInterface.decodeData(this, cacheBuffer.toString(), new BtDecodeInterface.DecodeResult() {
                @Override
                public void onResult(String msg) {
                    cacheBuffer.setLength(0);
                    sendDataBack(true, msg);
                }
            });
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                newDataChuLi(buffer, len);
            }
            if (null != mSocket) {
                BtMaster.getInstance().removeClientByAddress(address);
                mSocket.close();
            }
        } catch (IOException e) {
            MyLog.d("readData:IOException:"+address);
            notifyOffLine();
            e.printStackTrace();
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }


    /**
     * 接收到的数据，转发给Handle处理
     *
     * @param buffer 接收到的数据
     * @param len    接收到的数据长度
     */
    public void newDataChuLi(byte[] buffer, int len) {
        lastHeartBeat = System.currentTimeMillis();
        String data = DataUtils.BytesToHexStr(buffer, 0, len).toString();
        Log.d("lifei", "接收到消息：" + data);
        if (null != decodeInterface) {
            decodeInterface.decodeData(this, data, new BtDecodeInterface.DecodeResult() {
                @Override
                public void onResult(String msg) {
                    sendDataBack(true, msg);
                }
            });
        } else {
            cacheBuffer.append(data);
            sendDataBack(false, DataUtils.BytesToHexStr(buffer, 0, len).toString());
        }
    }

    private void sendDataBack(boolean isDeal, String result) {
        if (null != handler) {
            Message message = new Message();
            message.what = JCConst.WHAT_BT_MESSAGE;
            MessageObj messageObj = new MessageObj();
            messageObj.setDeal(isDeal);
            messageObj.setAddress(address);
            messageObj.setAlias(alias);
            messageObj.setTag(tag);
            messageObj.setDatas(result);
            message.obj = messageObj;
            handler.sendMessage(message);
        }
    }

    /**
     * 发送断开连接的通知
     */
    private void notifyOffLine() {
        BtMaster.getInstance().removeClientByAddress(address);
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            inputStream.close();
            outputStream.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendData(final String msg) {
        if (null != outputStream && !StringUtils.isEmpty(msg)) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        outputStream.write(DataUtils.str2Bytes(msg.replace(" ", "")));
                    } catch (IOException e) {
                        MyLog.d("sendData:IOException"+address);
                        notifyOffLine();
                        e.printStackTrace();
                    }
                }
            };
            BtMaster.getInstance().getmExecutors().execute(runnable);
        }

    }

    public long getLastHeartBeat() {
        return lastHeartBeat;
    }

    public void setLastHeartBeat(long lastHeartBeat) {
        this.lastHeartBeat = lastHeartBeat;
    }

    public long getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(long limitTime) {
        this.limitTime = limitTime;
    }

    public boolean isNeedKeepAlive() {
        return needKeepAlive;
    }

    public void setNeedKeepAlive(boolean needKeepAlive) {
        this.needKeepAlive = needKeepAlive;
    }
}
