package cn.carl.communicationLib.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.ArrayList;
import java.util.Map;

import cn.carl.communicationLib.JCLib;
import cn.carl.communicationLib.ble.BleDecodeInterface;
import cn.carl.communicationLib.ble.BleMaster;
import cn.carl.communicationLib.ble.BleUUID;
import cn.carl.communicationLib.bt.BtDecodeInterface;
import cn.carl.communicationLib.bt.BtMaster;
import cn.carl.communicationLib.callback.JCLibBleCallBack;
import cn.carl.communicationLib.callback.JCLibBtCallBack;
import cn.carl.communicationLib.callback.JCLibCallBack;
import cn.carl.communicationLib.constant.JCConst;
import cn.carl.communicationLib.socket.DecodeInterface;
import cn.carl.communicationLib.socket.MessageObj;
import cn.carl.communicationLib.socket.SocketMaster;

import static cn.carl.communicationLib.constant.JCConst.EXTRA_PORT;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/11
 * desc   :
 * version: 1.0
 * ==============================================
 */
public class WorkService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SocketMaster.getInstance().setHandler(handler);
        BleMaster.getInstance().setHandler(handler);
        BtMaster.getInstance().setHandler(handler);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", "workserver", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(getApplicationContext(), "1").build();
            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            int command = intent.getIntExtra(JCConst.EXTRA_COMMAND, -1);
            switch (command) {
                case JCConst.COMMAND_START_SERVER://打开socketServer；
                    int port = intent.getIntExtra(EXTRA_PORT, 1000);
                    SocketMaster.getInstance().startServer(port);
                    break;
                case JCConst.COMMAND_STOP_SERVER:
                    SocketMaster.getInstance().shutdown();
                    break;
                case JCConst.COMMAND_CONNECT_SOCKET_SERVER:
                    String ip = intent.getStringExtra(JCConst.EXTRA_ADDRESS);
                    int port1 = intent.getIntExtra(JCConst.EXTRA_PORT, 1000);
                    SocketMaster.getInstance().connect(ip, port1);
                    break;
                case JCConst.COMMAND_SEND_DATA_ALL:
                    String data = intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", "");
                    SocketMaster.getInstance().sendMsgAll(data);
                    break;
                case JCConst.COMMAND_SET_TAG:
                    String tag = intent.getStringExtra(JCConst.EXTRA_TAG);
                    SocketMaster.getInstance().setTag2Client(intent.getStringExtra(JCConst.EXTRA_ADDRESS), tag);
                    break;
                case JCConst.COMMAND_SET_ALIAS:
                    String alias = intent.getStringExtra(JCConst.EXTRA_ALIAS);
                    SocketMaster.getInstance().setAlias2Client(intent.getStringExtra(JCConst.EXTRA_ADDRESS), alias);
                    break;
                case JCConst.COMMAND_SEND_DATA:
                    SocketMaster.getInstance().sendMsg(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SEND_DATA_BT_TAG:
                    SocketMaster.getInstance().sendMsgByTag(intent.getStringExtra(JCConst.EXTRA_TAG),
                            intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SEND_DATA_BT_ALIAS:
                    SocketMaster.getInstance().sendMsgByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_DISCONNECT_CLIENT:
                    SocketMaster.getInstance().removeClientByAddress(intent.getStringExtra(JCConst.EXTRA_ADDRESS));
                    break;
                case JCConst.COMMAND_DISCONNECT_ALL:
                    SocketMaster.getInstance().removeAll();
                    break;
                case JCConst.COMMAND_DISCONNECT_BY_ALIAS:
                    SocketMaster.getInstance().removeClientByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS));
                    break;
                case JCConst.COMMAND_DISCONNECT_BY_TAG:
                    SocketMaster.getInstance().removeClientByTag(intent.getStringExtra(JCConst.EXTRA_TAG));
                    break;
                case JCConst.COMMAND_SET_DECODE_BY_ADDRESS:
                    SocketMaster.getInstance().setDecode(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            (DecodeInterface) intent.getSerializableExtra(JCConst.EXTRA_DECODE));
                    break;
                case JCConst.COMMAND_SET_DECODE_BY_ALIAS:
                    SocketMaster.getInstance().setDecodeByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            (DecodeInterface) intent.getSerializableExtra(JCConst.EXTRA_DECODE));
                    break;
                case JCConst.COMMAND_SET_DECODE_BY_TAG:
                    SocketMaster.getInstance().setDecodeByTag(intent.getStringExtra(JCConst.EXTRA_TAG),
                            (DecodeInterface) intent.getSerializableExtra(JCConst.EXTRA_DECODE));
                    break;
                case JCConst.COMMAND_SET_HEART_TIMEOUT_BY_ADDRESS:
                    SocketMaster.getInstance().setTimeOut(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            intent.getLongExtra(JCConst.EXTRA_TIME, 10 * 1000));
                    break;
                case JCConst.COMMAND_SET_HEART_TIMEOUT_BY_ALIAS:
                    SocketMaster.getInstance().setTimeOutByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            intent.getLongExtra(JCConst.EXTRA_TIME, 10 * 1000));
                    break;
                case JCConst.COMMAND_SET_NEED_KEEP_ALIVE:
                    SocketMaster.getInstance().setNeedKeepAlive(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            intent.getBooleanExtra(JCConst.EXTRA_KEEP_ALIVE, false));
                    break;
                case JCConst.COMMAND_SET_NEED_KEEP_ALIVE_BY_ALIAS:
                    SocketMaster.getInstance().setNeedKeepAliveByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            intent.getBooleanExtra(JCConst.EXTRA_KEEP_ALIVE, false));
                    break;
                //BLE
                case JCConst.COMMAND_START_BLE:
                    Bundle bundle = intent.getExtras();
                    ArrayList<BleUUID> writeUUIDs = (ArrayList<BleUUID>) bundle.getSerializable(JCConst.EXTRA_WRITE_UUID);
                    ArrayList<BleUUID> notifyUUIDs = (ArrayList<BleUUID>) bundle.getSerializable(JCConst.EXTRA_NOTIFY_UUID);
                    BleMaster.getInstance().initBLE(JCLib.getInstance().getApplication(), writeUUIDs, notifyUUIDs);
                    break;
                case JCConst.COMMAND_STOP_BLE:
                    BleMaster.getInstance().stopBLE();
                    break;
                case JCConst.COMMAND_CONNECT_BLE:
                    BleMaster.getInstance().connect(intent.getStringExtra(JCConst.EXTRA_ADDRESS));
                    break;
                case JCConst.COMMAND_DISCONNECT_BLE:
                    BleMaster.getInstance().disconnectByAddress(intent.getStringExtra(JCConst.EXTRA_ADDRESS));
                    break;
                case JCConst.COMMAND_DISCONNECT_BLE_BY_ALIAS:
                    BleMaster.getInstance().disconnectByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS));
                    break;
                case JCConst.COMMAND_DISCONNECT_BLE_BY_TAG:
                    BleMaster.getInstance().disconnectByTag(intent.getStringExtra(JCConst.EXTRA_TAG));
                    break;
                case JCConst.COMMAND_SEND_DATA_TO_BLE_ALL:
                    BleMaster.getInstance().sendDataAll(intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SEND_DATA_TO_BLE:
                    BleMaster.getInstance().sendDataByAddress(intent.getStringExtra(JCConst.EXTRA_ADDRESS)
                            , intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SEND_DATA_TO_BLE_BY_ALIAS:
                    BleMaster.getInstance().sendDataByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS)
                            , intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SEND_DATA_TO_BLE_BY_TAG:
                    BleMaster.getInstance().sendDataByTag(intent.getStringExtra(JCConst.EXTRA_TAG)
                            , intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SET_BLE_ALIAS:
                    BleMaster.getInstance().setAlias(intent.getStringExtra(JCConst.EXTRA_ADDRESS)
                            , intent.getStringExtra(JCConst.EXTRA_ALIAS));
                    break;
                case JCConst.COMMAND_SET_BLE_TAG:
                    BleMaster.getInstance().setTag(intent.getStringExtra(JCConst.EXTRA_ADDRESS)
                            , intent.getStringExtra(JCConst.EXTRA_TAG));
                    break;
                case JCConst.COMMAND_SET_BLE_DECODE:
                    BleMaster.getInstance().setDecode(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            (BleDecodeInterface) intent.getSerializableExtra(JCConst.EXTRA_DECODE));
                    break;
                case JCConst.COMMAND_SET_BLE_DECODE_BY_ALIAS:
                    BleMaster.getInstance().setDecodeByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            (BleDecodeInterface) intent.getSerializableExtra(JCConst.EXTRA_DECODE));
                    break;
                case JCConst.COMMAND_SET_BLE_DECODE_BY_TAG:
                    BleMaster.getInstance().setDecodeByTag(intent.getStringExtra(JCConst.EXTRA_TAG),
                            (BleDecodeInterface) intent.getSerializableExtra(JCConst.EXTRA_DECODE));
                    break;
                case JCConst.COMMAND_SET_BLE_HEART_TIMEOUT_BY_ADDRESS:
                    BleMaster.getInstance().setTimeOut(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            intent.getLongExtra(JCConst.EXTRA_TIME, 10 * 1000));
                    break;
                case JCConst.COMMAND_SET_BLE_HEART_TIMEOUT_BY_ALIAS:
                    BleMaster.getInstance().setTimeOutByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            intent.getLongExtra(JCConst.EXTRA_TIME, 10 * 1000));
                    break;
                case JCConst.COMMAND_SET_BLE_NEED_KEEP_ALIVE:
                    BleMaster.getInstance().setNeedKeepAlive(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            intent.getBooleanExtra(JCConst.EXTRA_KEEP_ALIVE, false));
                    break;
                case JCConst.COMMAND_SET_BLE_NEED_KEEP_ALIVE_BY_ALIAS:
                    BleMaster.getInstance().setNeedKeepAliveByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            intent.getBooleanExtra(JCConst.EXTRA_KEEP_ALIVE, false));
                    break;
                case JCConst.COMMAND_CONNECT_BT_SERVER:
                    BtMaster.getInstance().connect((BluetoothDevice) intent.getParcelableExtra(JCConst.EXTRA_ADDRESS));
                    break;
                case JCConst.COMMAND_DISCONNECT_BT:
                    BtMaster.getInstance().removeClientByAddress(intent.getStringExtra(JCConst.EXTRA_ADDRESS));
                    break;
                case JCConst.COMMAND_DISCONNECT_BT_BY_ALIAS:
                    BtMaster.getInstance().removeClientByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS));
                    break;
                case JCConst.COMMAND_DISCONNECT_BT_BY_TAG:
                    BtMaster.getInstance().removeClientByTag(intent.getStringExtra(JCConst.EXTRA_TAG));
                    break;
                case JCConst.COMMAND_SEND_DATA_TO_BT_ALL:
                    BtMaster.getInstance().sendMsgAll(intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SEND_DATA_TO_BT:
                    BtMaster.getInstance().sendMsg(intent.getStringExtra(JCConst.EXTRA_ADDRESS)
                            , intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SEND_DATA_TO_BT_BY_ALIAS:
                    BtMaster.getInstance().sendMsgByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS)
                            , intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SEND_DATA_TO_BT_BY_TAG:
                    BtMaster.getInstance().sendMsgByTag(intent.getStringExtra(JCConst.EXTRA_TAG)
                            , intent.getStringExtra(JCConst.EXTRA_DATA).replace(" ", ""));
                    break;
                case JCConst.COMMAND_SET_BT_ALIAS:
                    BtMaster.getInstance().setAlias2Client(intent.getStringExtra(JCConst.EXTRA_ADDRESS)
                            , intent.getStringExtra(JCConst.EXTRA_ALIAS));
                    break;
                case JCConst.COMMAND_SET_BT_TAG:
                    BtMaster.getInstance().setTag2Client(intent.getStringExtra(JCConst.EXTRA_ADDRESS)
                            , intent.getStringExtra(JCConst.EXTRA_TAG));
                    break;
                case JCConst.COMMAND_SET_BT_DECODE:
                    BtMaster.getInstance().setDecode(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            (BtDecodeInterface) intent.getSerializableExtra(JCConst.EXTRA_DECODE));
                    break;
                case JCConst.COMMAND_SET_BT_DECODE_BY_ALIAS:
                    BtMaster.getInstance().setDecodeByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            (BtDecodeInterface) intent.getSerializableExtra(JCConst.EXTRA_DECODE));
                    break;
                case JCConst.COMMAND_SET_BT_DECODE_BY_TAG:
                    BtMaster.getInstance().setDecodeByTag(intent.getStringExtra(JCConst.EXTRA_TAG),
                            (BtDecodeInterface) intent.getSerializableExtra(JCConst.EXTRA_DECODE));
                    break;
                case JCConst.COMMAND_SET_BT_HEART_TIMEOUT_BY_ADDRESS:
                    BtMaster.getInstance().setTimeOut(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            intent.getLongExtra(JCConst.EXTRA_TIME, 10 * 1000));
                    break;
                case JCConst.COMMAND_SET_BT_HEART_TIMEOUT_BY_ALIAS:
                    BtMaster.getInstance().setTimeOutByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            intent.getLongExtra(JCConst.EXTRA_TIME, 10 * 1000));
                    break;
                case JCConst.COMMAND_SET_BT_NEED_KEEP_ALIVE:
                    BtMaster.getInstance().setNeedKeepAlive(intent.getStringExtra(JCConst.EXTRA_ADDRESS),
                            intent.getBooleanExtra(JCConst.EXTRA_KEEP_ALIVE, false));
                    break;
                case JCConst.COMMAND_SET_BT_NEED_KEEP_ALIVE_BY_ALIAS:
                    BtMaster.getInstance().setNeedKeepAliveByAlias(intent.getStringExtra(JCConst.EXTRA_ALIAS),
                            intent.getBooleanExtra(JCConst.EXTRA_KEEP_ALIVE, false));
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JCConst.WHAT_CLIENT_ADD:
                    String address = (String) msg.obj;
                    for (JCLibCallBack jcLibCallBack : JCLib.getInstance().getJCLibCallbacks()) {
                        jcLibCallBack.onClientAdd(address);
                    }
                    break;
                case JCConst.WHAT_CLIENT_REMOVE:
                    MessageObj messageObj = (MessageObj) msg.obj;
                    for (JCLibCallBack jcLibCallBack : JCLib.getInstance().getJCLibCallbacks()) {
                        jcLibCallBack.onClientRemove(messageObj.getAddress()
                                , messageObj.getAlias());
                    }
                    break;
                case JCConst.WHAT_MESSAGE_RECIVED:
                    MessageObj messageObj1 = (MessageObj) msg.obj;
                    for (JCLibCallBack jcLibCallBack : JCLib.getInstance().getJCLibCallbacks()) {
                        jcLibCallBack.onMessageRecived(messageObj1.isDeal()
                                , messageObj1.getAddress()
                                , messageObj1.getAlias()
                                , messageObj1.getTag()
                                , messageObj1.getDatas());
                    }
                    break;
                case JCConst.WHAT_BLE_ADD:
                    Map<String, String> params = (Map<String, String>) msg.obj;
                    for (JCLibBleCallBack jcLibBleCallBack : JCLib.getInstance().getJCLibBleCallbacks()) {
                        jcLibBleCallBack.onClientAdd(params.get("mac"), params.get("name"));
                    }
                    break;
                case JCConst.WHAT_BLE_REMOVE:
                    MessageObj messageObj3 = (MessageObj) msg.obj;
                    for (JCLibBleCallBack jcLibBleCallBack : JCLib.getInstance().getJCLibBleCallbacks()) {
                        jcLibBleCallBack.onClientRemove(messageObj3.getAddress(), messageObj3.getAlias());
                    }
                    break;
                case JCConst.WHAT_BLE_MESSAGE:
                    MessageObj messageObj2 = (MessageObj) msg.obj;
                    for (JCLibBleCallBack jcLibBleCallBack : JCLib.getInstance().getJCLibBleCallbacks()) {
                        jcLibBleCallBack.onMessageRecived(messageObj2.isDeal()
                                , messageObj2.getAddress()
                                , messageObj2.getAlias()
                                , messageObj2.getTag()
                                , messageObj2.getDatas());
                    }
                    break;
                case JCConst.WHAT_BLE_CONNECT_FAIL:
                    String mac = (String) msg.obj;
                    for (JCLibBleCallBack jcLibBleCallBack : JCLib.getInstance().getJCLibBleCallbacks()) {
                        jcLibBleCallBack.onConnectFail(mac);
                    }
                    break;
                case JCConst.WHAT_BT_ADD:
                    for (JCLibBtCallBack jcLibBtCallBack : JCLib.getInstance().getJCLibBtCallbacks()) {
                        jcLibBtCallBack.onClientAdd((String) msg.obj);
                    }
                    break;
                case JCConst.WHAT_BT_REMOVE:
                    MessageObj messageObj4 = (MessageObj) msg.obj;
                    for (JCLibBtCallBack jcLibBtCallBack : JCLib.getInstance().getJCLibBtCallbacks()) {
                        jcLibBtCallBack.onClientRemove(messageObj4.getAddress(), messageObj4.getAlias());
                    }
                    break;
                case JCConst.WHAT_BT_MESSAGE:
                    MessageObj messageObj5 = (MessageObj) msg.obj;
                    for (JCLibBtCallBack jcLibBtCallBack : JCLib.getInstance().getJCLibBtCallbacks()) {
                        jcLibBtCallBack.onMessageRecived(messageObj5.isDeal()
                                , messageObj5.getAddress()
                                , messageObj5.getAlias()
                                , messageObj5.getTag()
                                , messageObj5.getDatas());
                    }
                    break;
                case JCConst.WHAT_BT_CONNECT_FAIL:
                    String mac1 = (String) msg.obj;
                    for (JCLibBtCallBack jcLibBtCallBack : JCLib.getInstance().getJCLibBtCallbacks()) {
                        jcLibBtCallBack.onConnectFail(mac1);
                    }
                    break;
                case JCConst.WHAT_CONNECT_FAIL:
                    String ip = (String) msg.obj;
                    for (JCLibCallBack jcLibCallBack : JCLib.getInstance().getJCLibCallbacks()) {
                        jcLibCallBack.onConnectFail(ip);
                    }
                    break;
            }
        }
    };

}
