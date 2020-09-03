package cn.carl.communicationLib;


import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.carl.communicationLib.ble.BleDecodeInterface;
import cn.carl.communicationLib.ble.BleUUID;
import cn.carl.communicationLib.bt.BtDecodeInterface;
import cn.carl.communicationLib.callback.JCLibBleCallBack;
import cn.carl.communicationLib.callback.JCLibBtCallBack;
import cn.carl.communicationLib.callback.JCLibCallBack;
import cn.carl.communicationLib.constant.JCConst;
import cn.carl.communicationLib.service.WorkService;
import cn.carl.communicationLib.socket.DecodeInterface;
import cn.carl.communicationLib.utils.MyLog;

public class JCLibImpl extends JCLib {

    private List<JCLibCallBack> jcLibCallBacks = new ArrayList<>();

    private List<JCLibBleCallBack> jcLibBleCallBacks = new ArrayList<>();

    private List<JCLibBtCallBack> jcLibBtCallBacks = new ArrayList<>();

    private void startService(Intent intent) {
        if (null == getApplication()) {
            Log.e("jc", "jcchengLib error please do function 'JCLib.init(Application app);' first");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplication().startForegroundService(intent);
        } else {
            getApplication().startService(intent);
        }
    }

    @Override
    public void addJCLibCallBack(JCLibCallBack jcLibCallBack) {
        if (null == jcLibCallBack) {
            return;
        }
        synchronized (this) {
            if (!jcLibCallBacks.contains(jcLibCallBack)) {
                jcLibCallBacks.add(jcLibCallBack);
            }
        }
    }

    @Override
    public void removeJCLibCallBack(JCLibCallBack jcLibCallBack) {
        if (null == jcLibCallBack) {
            return;
        }
        synchronized (this) {
            if (jcLibCallBacks.contains(jcLibCallBack)) {
                jcLibCallBacks.remove(jcLibCallBack);
            }
        }
    }

    @Override
    public List<JCLibCallBack> getJCLibCallbacks() {
        return jcLibCallBacks;
    }

    @Override
    public void addJCLibBleCallBack(JCLibBleCallBack jcLibBleCallBack) {
        if (null == jcLibBleCallBack) {
            return;
        }
        synchronized (this) {
            if (!jcLibBleCallBacks.contains(jcLibBleCallBack)) {
                jcLibBleCallBacks.add(jcLibBleCallBack);
            }
        }
    }

    @Override
    public void removeJCLibBleCallBack(JCLibBleCallBack jcLibBleCallBack) {
        if (null == jcLibBleCallBack) {
            return;
        }
        synchronized (this) {
            if (jcLibBleCallBacks.contains(jcLibBleCallBack)) {
                jcLibBleCallBacks.remove(jcLibBleCallBack);
            }
        }
    }

    @Override
    public List<JCLibBleCallBack> getJCLibBleCallbacks() {
        return jcLibBleCallBacks;
    }

    @Override
    public void addJCLibBtCallBack(JCLibBtCallBack jcLibBtCallBack) {
        if (null == jcLibBtCallBack) {
            return;
        }
        synchronized (this) {
            if (!jcLibBtCallBacks.contains(jcLibBtCallBack)) {
                jcLibBtCallBacks.add(jcLibBtCallBack);
            }
        }
    }

    @Override
    public void removeJCLibBtCallBack(JCLibBtCallBack jcLibBtCallBack) {
        if (null == jcLibBtCallBack) {
            return;
        }
        synchronized (this) {
            if (jcLibBtCallBacks.contains(jcLibBtCallBack)) {
                jcLibBtCallBacks.remove(jcLibBtCallBack);
            }
        }
    }

    @Override
    public List<JCLibBtCallBack> getJCLibBtCallbacks() {
        return jcLibBtCallBacks;
    }

    @Override
    public void startSocketServer(int port) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_PORT, port);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_START_SERVER);
        startService(intent);
    }

    @Override
    public void connectSocketServer(String ip, int port) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_PORT, port);
        intent.putExtra(JCConst.EXTRA_ADDRESS, ip);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_CONNECT_SOCKET_SERVER);
        startService(intent);
    }

    @Override
    public void stopSocketServer() {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_STOP_SERVER);
        startService(intent);
    }

    @Override
    public void setAlias(String address, String alias) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_ALIAS);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        startService(intent);
    }

    @Override
    public void setTag(String address, String tag) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_TAG);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        startService(intent);
    }

    @Override
    public void sendMessage(String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_ALL);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void sendMessage(String address, String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void sendMessage(String address, String msg, String descript) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
        MyLog.d("TCP发送消息给:" + address + "【" + descript + "】命令：" + msg);
    }

    @Override
    public void sendMessageByTag(String tag, String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_BT_TAG);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void sendMessageByAlias(String alias, String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_BT_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void sendMessageByAlias(String alias, String msg, String descript) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_BT_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
        MyLog.d("TCP发送消息给:" + alias + "【" + descript + "】命令：" + msg);
    }

    @Override
    public void disConnectClient(String address) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_CLIENT);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        startService(intent);
    }

    @Override
    public void disConnectByAlias(String alias) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        startService(intent);
    }

    @Override
    public void disConnectByTag(String tag) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_BY_TAG);
        intent.putExtra(JCConst.EXTRA_ALIAS, tag);
        startService(intent);
    }

    @Override
    public void disConnectAll() {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_ALL);
        startService(intent);
    }

    @Override
    public void setDecode(String address, DecodeInterface decodeInterface) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_DECODE_BY_ADDRESS);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_DECODE, decodeInterface);
        startService(intent);
    }

    @Override
    public void setDecodeByAlias(String alias, DecodeInterface decodeInterface) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_DECODE_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_DECODE, decodeInterface);
        startService(intent);
    }

    @Override
    public void setDecodeByTag(String tag, DecodeInterface decodeInterface) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_DECODE_BY_TAG);
        intent.putExtra(JCConst.EXTRA_ALIAS, tag);
        intent.putExtra(JCConst.EXTRA_DECODE, decodeInterface);
        startService(intent);
    }

    @Override
    public void setHeartTimeOut(String address, long heartTimeout) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_HEART_TIMEOUT_BY_ADDRESS);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_TIME, heartTimeout);
        startService(intent);
    }

    @Override
    public void setHeartTimeOutByAlias(String alias, long heartTimeout) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_HEART_TIMEOUT_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_TIME, heartTimeout);
        startService(intent);
    }

    @Override
    public void keepALiveByAlias(String alias, boolean needKeepAlive) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_NEED_KEEP_ALIVE_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_KEEP_ALIVE, needKeepAlive);
        startService(intent);
    }


    @Override
    public void keepALive(String address, boolean needKeepAlive) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_NEED_KEEP_ALIVE);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_KEEP_ALIVE, needKeepAlive);
        startService(intent);
    }

    @Override
    public void initBLE(ArrayList<BleUUID> writeUUID, ArrayList<BleUUID> notifyUUID) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_START_BLE);
        Bundle bundle = new Bundle();
        bundle.putSerializable(JCConst.EXTRA_WRITE_UUID, writeUUID);
        bundle.putSerializable(JCConst.EXTRA_NOTIFY_UUID, notifyUUID);
        intent.putExtras(bundle);
        startService(intent);
    }

    @Override
    public void connectBLEServer(String address) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_CONNECT_BLE);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        startService(intent);
    }

    @Override
    public void disConnectBleServer(String address) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_BLE);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        startService(intent);
    }

    @Override
    public void disConnectBleServerByAlias(String alias) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_BLE_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        startService(intent);
    }

    @Override
    public void disConnectBleServerByTag(String tag) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_BLE_BY_TAG);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        startService(intent);
    }

    @Override
    public void stopBLE() {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_STOP_BLE);
        startService(intent);
    }

    @Override
    public void sendBLEMessage(String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BLE_ALL);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void sendBLEMessage(String address, String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BLE);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void sendBLEMessage(String address, String msg, String descript) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BLE);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
        MyLog.d("蓝牙发送消息给:" + address + "【" + descript + "】命令：" + msg);
    }

    @Override
    public synchronized void sendBLEMessageByAlias(String alias, String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BLE_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void sendBLEMessageByAlias(String alias, String msg, String descript) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BLE_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
        MyLog.d("蓝牙发送消息给:" + alias + "【" + descript + "】命令：" + msg);
    }

    @Override
    public void sendBLEMessageByTag(String tag, String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BLE_BY_TAG);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void setBLEAlias(String address, String alias) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BLE_ALIAS);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        startService(intent);
    }

    @Override
    public void setBLETag(String address, String tag) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BLE_TAG);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        startService(intent);
    }

    @Override
    public void setBLEDecode(String address, BleDecodeInterface bleDecodeInterface) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BLE_DECODE);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_DECODE, bleDecodeInterface);
        startService(intent);
    }

    @Override
    public void setBLEDecodeByAlias(String alias, BleDecodeInterface bleDecodeInterface) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BLE_DECODE_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_DECODE, bleDecodeInterface);
        startService(intent);
    }


    @Override
    public void setBLEDecodeByTag(String tag, BleDecodeInterface bleDecodeInterface) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BLE_DECODE_BY_TAG);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        intent.putExtra(JCConst.EXTRA_DECODE, bleDecodeInterface);
        startService(intent);
    }

    @Override
    public void setBLEHeartTimeOut(String address, long heartTimeout) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BLE_HEART_TIMEOUT_BY_ADDRESS);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_TIME, heartTimeout);
        startService(intent);
    }

    @Override
    public void setBLEHeartTimeOutByAlias(String alias, long heartTimeout) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BLE_HEART_TIMEOUT_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_TIME, heartTimeout);
        startService(intent);
    }

    @Override
    public void keepBLEALive(String address, boolean needKeepAlive) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BLE_NEED_KEEP_ALIVE);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_KEEP_ALIVE, needKeepAlive);
        startService(intent);
    }

    @Override
    public void keepBLEALiveByAlias(String alias, boolean needKeepAlive) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BLE_NEED_KEEP_ALIVE_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_KEEP_ALIVE, needKeepAlive);
        startService(intent);
    }

    @Override
    public void connectBtServer(BluetoothDevice address) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_CONNECT_BT_SERVER);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        startService(intent);
    }

    @Override
    public void disConnectBtServer(String address) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_BT);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        startService(intent);
    }

    @Override
    public void disConnectBtServerByAlias(String alias) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_BT_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        startService(intent);
    }

    @Override
    public void disConnectBtServerByTag(String tag) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_DISCONNECT_BT_BY_TAG);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        startService(intent);
    }


    @Override
    public void sendBtMessage(String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BT_ALL);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void sendBtMessage(String address, String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BT);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public synchronized void sendBtMessageByAlias(String alias, String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BT_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void sendBtMessageByAlias(String alias, String msg, String descript) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BT_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
        MyLog.d("蓝牙发送消息给:" + alias + "【" + descript + "】命令：" + msg);
    }

    @Override
    public void sendBtMessageByTag(String tag, String msg) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SEND_DATA_TO_BT_BY_TAG);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        intent.putExtra(JCConst.EXTRA_DATA, msg);
        startService(intent);
    }

    @Override
    public void setBtAlias(String address, String alias) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BT_ALIAS);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        startService(intent);
    }

    @Override
    public void setBtTag(String address, String tag) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BT_TAG);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        startService(intent);
    }

    @Override
    public void setBtDecode(String address, BtDecodeInterface btDecodeInterface) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BT_DECODE);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_DECODE, btDecodeInterface);
        startService(intent);
    }

    @Override
    public void setBtDecodeByAlias(String alias, BtDecodeInterface btDecodeInterface) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BT_DECODE_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_DECODE, btDecodeInterface);
        startService(intent);
    }

    @Override
    public void setBtDecodeByTag(String tag, BtDecodeInterface btDecodeInterface) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BT_DECODE_BY_TAG);
        intent.putExtra(JCConst.EXTRA_TAG, tag);
        intent.putExtra(JCConst.EXTRA_DECODE, btDecodeInterface);
        startService(intent);
    }

    @Override
    public void setBtHeartTimeOut(String address, long heartTimeout) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BT_HEART_TIMEOUT_BY_ADDRESS);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_TIME, heartTimeout);
        startService(intent);
    }

    @Override
    public void setBtHeartTimeOutByAlias(String alias, long heartTimeout) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BT_HEART_TIMEOUT_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_TIME, heartTimeout);
        startService(intent);
    }

    @Override
    public void keepBtALive(String address, boolean needKeepAlive) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BT_NEED_KEEP_ALIVE);
        intent.putExtra(JCConst.EXTRA_ADDRESS, address);
        intent.putExtra(JCConst.EXTRA_KEEP_ALIVE, needKeepAlive);
        startService(intent);
    }

    @Override
    public void keepBtALiveByAlias(String alias, boolean needKeepAlive) {
        Intent intent = new Intent(getApplication(), WorkService.class);
        intent.putExtra(JCConst.EXTRA_COMMAND, JCConst.COMMAND_SET_BT_NEED_KEEP_ALIVE_BY_ALIAS);
        intent.putExtra(JCConst.EXTRA_ALIAS, alias);
        intent.putExtra(JCConst.EXTRA_KEEP_ALIVE, needKeepAlive);
        startService(intent);
    }

}
