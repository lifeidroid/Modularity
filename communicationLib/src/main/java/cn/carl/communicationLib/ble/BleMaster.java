package cn.carl.communicationLib.ble;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;
import android.os.Message;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.carl.communicationLib.constant.JCConst;
import cn.carl.communicationLib.socket.MessageObj;
import cn.carl.communicationLib.utils.DataUtils;
import cn.carl.communicationLib.utils.MyLog;
import cn.carl.communicationLib.utils.StringUtils;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/10
 * desc   : BLE管理类
 * version: 1.0
 * ==============================================
 */
public class BleMaster {
    private static volatile BleMaster instance;
    private Handler handler;
    //已连接服务列表
    private Map<String, PeripheryDevice> serverList = new ConcurrentHashMap<>();
    //写的UUID集合
    private List<BleUUID> writeUUIDList = new ArrayList<>();
    //读的UUID集合
    private List<BleUUID> notifyUUIDList = new ArrayList<>();
    private Thread keepAliveThread = null;//保活线程
    private boolean isKeepAlive = false;//保活线程标识

    public static BleMaster getInstance() {
        if (instance == null) {
            synchronized (BleMaster.class) {
                if (instance == null) {
                    instance = new BleMaster();
                }
            }
        }
        return instance;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    /**
     * 初始化BLE
     */
    public void initBLE(Application application, List<BleUUID> writeUUIDs, List<BleUUID> notifyUUIDs) {
        BleManager.getInstance().init(application);
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);
        writeUUIDList.clear();
        writeUUIDList.addAll(writeUUIDs);
        notifyUUIDList.clear();
        notifyUUIDList.addAll(notifyUUIDs);
        startKeepAlive();
    }

    /**
     * 关闭BLE
     */
    public void stopBLE() {
        stopKeepAlive();
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            BleManager.getInstance().disconnect(item.getValue().getBleDevice());
            send2Handler(item.getValue().getAddress(), item.getValue().getAlias());
        }
        serverList.clear();
        BleManager.getInstance().destroy();
    }


    /**
     * 开启保活心跳检测线程
     */
    private void startKeepAlive() {
        stopKeepAlive();
        isKeepAlive = true;
        keepAliveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //需要刪除的设备
                List<String> removeTemp = new ArrayList<>();
                while (isKeepAlive) {
                    Iterator iterator = serverList.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
                        PeripheryDevice client = item.getValue();
                        if (client.isNeedKeepAlive()) {
                            if (System.currentTimeMillis() - client.getLastHeartBeat() > client.getLimitTime()) {
                                removeTemp.add(client.getAddress());
                                MyLog.d(client.getAddress() + "心跳超时断开连接");
                            }
                        }
                    }
                    //刪除超時的设备
                    if (removeTemp.size() > 0) {
                        for (String item : removeTemp) {
                            disconnectByAddress(item);
                        }
                        removeTemp.clear();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        keepAliveThread.start();
    }

    /**
     * 关闭保活心跳检测线程
     */
    private void stopKeepAlive() {
        isKeepAlive = false;
        if (null != keepAliveThread) {
            keepAliveThread.interrupt();
            keepAliveThread = null;
        }
    }

    /**
     * 设置是否需要保活检测
     *
     * @param address
     * @param needKeepAlive
     * @return
     */
    public void setNeedKeepAlive(String address, boolean needKeepAlive) {
        PeripheryDevice clientThread = serverList.get(address);
        if (null != clientThread) {
            clientThread.setNeedKeepAlive(needKeepAlive);
        }
    }


    /**
     * 通过别名设置是否需要保活检测
     *
     * @param alias
     * @param needKeepAlive
     */
    public void setNeedKeepAliveByAlias(String alias, boolean needKeepAlive) {
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getAlias()) && item.getValue().getAlias().equals(alias)) {
                item.getValue().setNeedKeepAlive(needKeepAlive);
            }
        }
    }

    /**
     * 设置心跳超时时间
     *
     * @param address
     * @param timeOut
     * @return
     */
    public void setTimeOut(String address, long timeOut) {
        PeripheryDevice clientThread = serverList.get(address);
        if (null != clientThread) {
            clientThread.setLimitTime(timeOut);
        }
    }

    /**
     * 通过别名设置是否需要保活检测
     *
     * @param alias
     * @param timeOut
     */
    public void setTimeOutByAlias(String alias, long timeOut) {
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getAlias()) && item.getValue().getAlias().equals(alias)) {
                item.getValue().setLimitTime(timeOut);
            }
        }
    }

    /**
     * 通过蓝牙地址连接蓝牙设备
     *
     * @param address
     */
    public void connect(String address) {
        BleManager.getInstance().connect(address, connectCallBack);
    }

    private BleGattCallback connectCallBack = new BleGattCallback() {
        @Override
        public void onStartConnect() {

        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            if (null != handler) {
                Message message = new Message();
                message.what = JCConst.WHAT_BLE_CONNECT_FAIL;
                message.obj = bleDevice.getMac();
                handler.sendMessage(message);
            }
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            if (null != handler) {
                addPeripheryDevice(bleDevice);
            }
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            if (serverList.containsKey(device.getMac())) {
                send2Handler(serverList.get(device.getMac()).getAddress()
                        , serverList.get(device.getMac()).getAlias());
                serverList.remove(device.getMac());
            }
        }
    };


    /**
     * 添加周边设备
     *
     * @param device
     */
    private void addPeripheryDevice(final BleDevice device) {
        final PeripheryDevice peripheryDevice = new PeripheryDevice();
        peripheryDevice.setAddress(device.getMac());
        peripheryDevice.setBleDevice(device);
        List<BluetoothGattService> services = BleManager.getInstance().getBluetoothGattServices(device);
        if (null != services) {
            for (BluetoothGattService item : services) {
                for (BleUUID bleUUID : writeUUIDList) {
                    if (item.getUuid().equals(bleUUID.getServiceUUID())) {
                        if (null != item.getCharacteristic(bleUUID.getCharacteristicUUID())) {
                            peripheryDevice.setWriteUUID(bleUUID);
                        }
                    }
                }
                for (BleUUID bleUUID : notifyUUIDList) {
                    if (item.getUuid().equals(bleUUID.getServiceUUID())) {
                        if (null != item.getCharacteristic(bleUUID.getCharacteristicUUID())) {
                            peripheryDevice.setNotifyUUID(bleUUID);
                        }
                    }
                }
            }
        }
        if (null != peripheryDevice.getNotifyUUID() && null != peripheryDevice.getWriteUUID()) {
            ReadNotiftCallBack readNotiftCallBack = new ReadNotiftCallBack(device);
            peripheryDevice.setReadNotiftCallBack(readNotiftCallBack);
            BleManager.getInstance().notify(device
                    , peripheryDevice.getNotifyUUID().getServiceUUID().toString()
                    , peripheryDevice.getNotifyUUID().getCharacteristicUUID().toString()
                    , readNotiftCallBack);
            serverList.put(device.getMac(), peripheryDevice);
        } else {
            //FIXME 删除设备
            MyLog.d(device.getMac() + "该设备不符合要求");
        }
    }

    /**
     * 关闭指定地址的周边设备
     *
     * @param address
     */
    public synchronized void disconnectByAddress(String address) {
        if (serverList.containsKey(address)) {
            BleManager.getInstance().disconnect(serverList.get(address).getBleDevice());
            send2Handler(serverList.get(address).getAddress(), serverList.get(address).getAlias());
            serverList.remove(address);
        }
    }

    /**
     * 关闭指定别名的周边设备
     *
     * @param alias
     */
    public void disconnectByAlias(String alias) {
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getAlias()) && item.getValue().getAlias().equals(alias)) {
                disconnectByAddress(item.getValue().getBleDevice().getMac());
                return;
            }
        }
    }

    /**
     * 断开指定标签的周边设备
     *
     * @param tag
     */
    public void disconnectByTag(String tag) {
        Iterator iterator = serverList.entrySet().iterator();
        List<String> removeList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getTag()) && item.getValue().getTag().equals(tag)) {
                removeList.add(item.getValue().getBleDevice().getMac());
            }
        }
        if (!removeList.isEmpty()) {
            for (String item : removeList) {
                disconnectByAddress(item);
            }
        }
    }

    /**
     * 向所有设备发送消息
     *
     * @param data
     */
    public void sendDataAll(String data) {
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            BleManager.getInstance().write(item.getValue().getBleDevice()
                    , item.getValue().getWriteUUID().getServiceUUID().toString()
                    , item.getValue().getWriteUUID().getCharacteristicUUID().toString()
                    , DataUtils.str2Bytes(data.replace(" ", ""))
                    , new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {

                        }

                        @Override
                        public void onWriteFailure(BleException exception) {

                        }
                    });
        }
    }

    /**
     * 向指定地址设备发送消息
     *
     * @param address 地址
     * @param data    消息
     */
    public void sendDataByAddress(String address, String data) {
        if (serverList.containsKey(address)) {
            BleManager.getInstance().write(serverList.get(address).getBleDevice()
                    , serverList.get(address).getWriteUUID().getServiceUUID().toString()
                    , serverList.get(address).getWriteUUID().getCharacteristicUUID().toString()
                    , DataUtils.str2Bytes(data.replace(" ", ""))
                    , new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {

                        }

                        @Override
                        public void onWriteFailure(BleException exception) {

                        }
                    });
        }
    }

    /**
     * 向指定别名设备发送消息
     *
     * @param alias
     * @param data
     */
    public void sendDataByAlias(String alias, String data) {
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getAlias()) && item.getValue().getAlias().equals(alias)) {
                MyLog.d("sendBLEMessageByAlias->alias:" + alias + "   data:" + data);
                BleManager.getInstance().write(item.getValue().getBleDevice()
                        , item.getValue().getWriteUUID().getServiceUUID().toString()
                        , item.getValue().getWriteUUID().getCharacteristicUUID().toString()
                        , DataUtils.str2Bytes(data.replace(" ", ""))
                        , new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(int current, int total, byte[] justWrite) {

                            }

                            @Override
                            public void onWriteFailure(BleException exception) {

                            }
                        });
            }
        }
    }

    /**
     * 向指定标签的设备发送消息
     *
     * @param tag
     * @param data
     */
    public void sendDataByTag(String tag, String data) {
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getTag()) && item.getValue().getTag().equals(tag)) {
                BleManager.getInstance().write(item.getValue().getBleDevice()
                        , item.getValue().getWriteUUID().getServiceUUID().toString()
                        , item.getValue().getWriteUUID().getCharacteristicUUID().toString()
                        , DataUtils.str2Bytes(data.replace(" ", ""))
                        , new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(int current, int total, byte[] justWrite) {

                            }

                            @Override
                            public void onWriteFailure(BleException exception) {

                            }
                        });
            }
        }
    }

    /**
     * 给指定设备设置别名
     *
     * @param address
     * @param alias
     */
    public void setAlias(String address, String alias) {
        //去除相同别名的设备
        String mac = isContainAlias(alias);
        if (!StringUtils.isEmpty(mac)) {
            serverList.get(mac).setAlias("other");
            disconnectByAddress(mac);
        }
        if (serverList.containsKey(address)) {
            serverList.get(address).setAlias(alias);
        }
    }

    /**
     * 设备列表中是否包含指定别名，如果包含返回设备的MAC地址
     *
     * @param alias
     * @return
     */
    public String isContainAlias(String alias) {
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            if (null != item && null != item.getValue()) {
                if (StringUtils.isEqual(item.getValue().getAlias(), alias)) {
                    return item.getValue().getAddress();
                }
            }
        }
        return null;
    }

    /**
     * 给指定设备设置标签
     *
     * @param address
     * @param tag
     */
    public void setTag(String address, String tag) {
        if (serverList.containsKey(address)) {
            serverList.get(address).setTag(tag);
        }
    }


    /**
     * 给指定设备设置解析方法
     *
     * @param address
     * @param decodeInterface
     */
    public void setDecode(String address, BleDecodeInterface decodeInterface) {
        if (serverList.containsKey(address)) {
            serverList.get(address).getReadNotiftCallBack().setBleDecodeInterface(decodeInterface);
        }
    }

    /**
     * 给指定别名的设备设置解析方法
     *
     * @param alias
     * @param decodeInterface
     */
    public void setDecodeByAlias(String alias, BleDecodeInterface decodeInterface) {
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getAlias()) && item.getValue().getAlias().equals(alias))
                item.getValue().getReadNotiftCallBack().setBleDecodeInterface(decodeInterface);
        }
    }

    /**
     * 给指定标签的设备设置解析方法
     *
     * @param tag
     * @param decodeInterface
     */
    public void setDecodeByTag(String tag, BleDecodeInterface decodeInterface) {
        Iterator iterator = serverList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PeripheryDevice> item = (Map.Entry<String, PeripheryDevice>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getTag()) && item.getValue().getTag().equals(tag))
                item.getValue().getReadNotiftCallBack().setBleDecodeInterface(decodeInterface);
        }
    }

    /**
     * 发送断开连接的通知
     *
     * @param address
     * @param alias
     */
    private void send2Handler(String address, String alias) {
        if (null != handler) {
            Message message = new Message();
            message.what = JCConst.WHAT_BLE_REMOVE;
            MessageObj messageObj = new MessageObj();
            messageObj.setAddress(address);
            messageObj.setAlias(alias);
            message.obj = messageObj;
            handler.sendMessage(message);
        }
    }

}
