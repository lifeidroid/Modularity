package cn.carl.communicationLib.ble;


import com.clj.fastble.data.BleDevice;

import cn.carl.communicationLib.constant.JCConst;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/11
 * desc   : BLE周边设备
 * version: 1.0
 * ==============================================
 */
public class PeripheryDevice {
    //设备地址
    private String address;
    //设备标签
    private String tag;
    //设备别名
    private String alias;
    //周边设备
    private BleDevice bleDevice;
    //解析回调
    private ReadNotiftCallBack readNotiftCallBack;
    //write的UUID
    private BleUUID writeUUID;
    //notify的UUID
    private BleUUID notifyUUID;
    //心跳超时时间
    private long limitTime = JCConst.HEART_TIMEOUT;
    //是否需要保活检测
    private boolean needKeepAlive = false;


    public void setAlias(String alias) {
        this.alias = alias;
        readNotiftCallBack.setAlias(alias);
    }

    public String getAlias() {
        return alias;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
        readNotiftCallBack.setTag(tag);
    }

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public ReadNotiftCallBack getReadNotiftCallBack() {
        return readNotiftCallBack;
    }

    public void setReadNotiftCallBack(ReadNotiftCallBack readNotiftCallBack) {
        this.readNotiftCallBack = readNotiftCallBack;
    }

    public BleUUID getWriteUUID() {
        return writeUUID;
    }

    public void setWriteUUID(BleUUID writeUUID) {
        this.writeUUID = writeUUID;
    }

    public BleUUID getNotifyUUID() {
        return notifyUUID;
    }

    public void setNotifyUUID(BleUUID notifyUUID) {
        this.notifyUUID = notifyUUID;
    }

    public long getLastHeartBeat() {
        if (null != readNotiftCallBack){
            return readNotiftCallBack.getLastHeartBeat();
        }else{
            return System.currentTimeMillis();
        }
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
