package cn.carl.communicationLib;


import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import cn.carl.communicationLib.ble.BleDecodeInterface;
import cn.carl.communicationLib.ble.BleUUID;
import cn.carl.communicationLib.bt.BtDecodeInterface;
import cn.carl.communicationLib.callback.JCLibBleCallBack;
import cn.carl.communicationLib.callback.JCLibBtCallBack;
import cn.carl.communicationLib.callback.JCLibCallBack;
import cn.carl.communicationLib.service.WorkService;
import cn.carl.communicationLib.socket.DecodeInterface;
import cn.carl.communicationLib.utils.MyLog;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/11
 * desc   : JCLib
 * version: 1.0
 * ==============================================
 */
public abstract class JCLib {
    private static JCLib instance;
    private static Application application;

    public static JCLib getInstance() {
        if (instance == null) {
            synchronized (JCLib.class) {
                if (instance == null) {
                    instance = new JCLibImpl();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化JCLib
     *
     * @param context
     * @param showLog
     * @param saveLog
     */
    public static void init(Application context, Boolean showLog, Boolean saveLog) {
        application = context;
        MyLog.init();
        MyLog.isDebug = showLog;
        MyLog.isPrintingLog = saveLog;
    }

    /**
     * 关闭JCLib
     */
    public void destroy() {
        if (null == application) {
            return;
        }
        stopSocketServer();
        stopBLE();
        application.stopService(new Intent(application, WorkService.class));
    }

    public Application getApplication() {
        return application;
    }

    /**
     * 添加Socket Server监听回调
     *
     * @param jcLibCallBack
     */
    public abstract void addJCLibCallBack(JCLibCallBack jcLibCallBack);

    /**
     * 取消Socket Server监听回调
     *
     * @param jcLibCallBack
     */
    public abstract void removeJCLibCallBack(JCLibCallBack jcLibCallBack);

    public abstract List<JCLibCallBack> getJCLibCallbacks();

    /**
     * 添加BLE设备监听回调
     *
     * @param jcLibBleCallBack
     */
    public abstract void addJCLibBleCallBack(JCLibBleCallBack jcLibBleCallBack);

    /**
     * 取消BLE 设备监听回调
     *
     * @param jcLibBleCallBack
     */
    public abstract void removeJCLibBleCallBack(JCLibBleCallBack jcLibBleCallBack);

    public abstract List<JCLibBleCallBack> getJCLibBleCallbacks();

    /**
     * 添加经典蓝牙设备监听回调
     *
     * @param jcLibBtCallBack
     */
    public abstract void addJCLibBtCallBack(JCLibBtCallBack jcLibBtCallBack);

    /**
     * 取消经典蓝牙设备监听回调
     *
     * @param jcLibBtCallBack
     */
    public abstract void removeJCLibBtCallBack(JCLibBtCallBack jcLibBtCallBack);

    public abstract List<JCLibBtCallBack> getJCLibBtCallbacks();

    /**
     * 开启SocketServer
     *
     * @param port 端口号
     */
    public abstract void startSocketServer(int port);

    /**
     * 连接SocketServer
     *
     * @param ip
     * @param port
     */
    public abstract void connectSocketServer(String ip, int port);

    /**
     * 停止SocketServer
     */
    public abstract void stopSocketServer();

    /**
     * 给Client指定别名(规定别名是唯一的)
     *
     * @param address
     * @param alias
     */
    public abstract void setAlias(String address, String alias);

    /**
     * 给Client设置Tag
     *
     * @param address
     * @param tag
     */
    public abstract void setTag(String address, String tag);

    /**
     * 发送消息给所有Client
     *
     * @param msg
     */
    public abstract void sendMessage(String msg);

    /**
     * 给指定address的Client发送消息
     *
     * @param address Client的IP
     * @param msg
     */
    public abstract void sendMessage(String address, String msg);

    /**
     * 给指定address的Client发送消息
     *
     * @param address  Client的IP
     * @param msg
     * @param descript 消息描述
     */
    public abstract void sendMessage(String address, String msg, String descript);

    /**
     * 给指定Tag的Client发送消息
     *
     * @param tag
     * @param msg
     */
    public abstract void sendMessageByTag(String tag, String msg);

    /**
     * 给指定别名的Client发送消息
     *
     * @param alias
     * @param msg
     */
    public abstract void sendMessageByAlias(String alias, String msg);

    /**
     * 给指定别名的Client发送消息
     *
     * @param alias
     * @param msg
     * @param descript
     */
    public abstract void sendMessageByAlias(String alias, String msg, String descript);

    /**
     * 断开指定地址的Client
     *
     * @param address
     */
    public abstract void disConnectClient(String address);

    /**
     * 断开指定别名的Client
     *
     * @param alias
     */
    public abstract void disConnectByAlias(String alias);

    /**
     * 断开指定tag的Client
     *
     * @param tag
     */
    public abstract void disConnectByTag(String tag);

    /**
     * 断开所有Client
     */
    public abstract void disConnectAll();

    /**
     * 给指定设备设置数据包解析策略
     *
     * @param address
     * @param decodeInterface
     */
    public abstract void setDecode(String address, DecodeInterface decodeInterface);

    /**
     * 给指定别名的设备设置数据包解析策略
     *
     * @param alias
     * @param decodeInterface
     */
    public abstract void setDecodeByAlias(String alias, DecodeInterface decodeInterface);

    /**
     * 给指定标签的设备设置数据包解析策略
     *
     * @param tag
     * @param decodeInterface
     */
    public abstract void setDecodeByTag(String tag, DecodeInterface decodeInterface);

    /**
     * 给指定设备设置心跳超时时间
     *
     * @param address
     * @param heartTimeout
     */
    public abstract void setHeartTimeOut(String address, long heartTimeout);

    /**
     * 通过别名给指定设备设置心跳超时时间
     *
     * @param alias
     * @param heartTimeout
     */
    public abstract void setHeartTimeOutByAlias(String alias, long heartTimeout);

    /**
     * 通过别名设置设备是否需要保活
     *
     * @param address
     * @param needKeepAlive
     */
    public abstract void keepALive(String address, boolean needKeepAlive);

    /**
     * 通过别名设置设备是否需要保活
     *
     * @param alias
     * @param needKeepAlive
     */
    public abstract void keepALiveByAlias(String alias, boolean needKeepAlive);

    /*****************************************BLE**************************************************/

    /**
     * 初始化BLE
     */
    public abstract void initBLE(ArrayList<BleUUID> writeUUID, ArrayList<BleUUID> notifyUUID);

    /**
     * 关闭BLE
     */
    public abstract void stopBLE();

    /**
     * 通过连接BLEServer
     *
     * @param address
     */
    public abstract void connectBLEServer(String address);

    /**
     * 断开指定地址的周边设备
     *
     * @param address
     */
    public abstract void disConnectBleServer(String address);

    /**
     * 断开制定别名的周边设备
     *
     * @param alias
     */
    public abstract void disConnectBleServerByAlias(String alias);

    /**
     * 断开指定标签的周边设备
     *
     * @param tag
     */
    public abstract void disConnectBleServerByTag(String tag);

    /**
     * 发送消息给所有周边设备
     *
     * @param msg
     */
    public abstract void sendBLEMessage(String msg);

    /**
     * 发送消息给指定地址的设备
     *
     * @param address
     * @param msg
     */
    public abstract void sendBLEMessage(String address, String msg);

    /**
     * 发送消息给指定地址的设备
     *
     * @param address
     * @param msg
     */
    public abstract void sendBLEMessage(String address, String msg, String descript);

    /**
     * 给指定别名的BLE周边设备发送消息
     *
     * @param alias
     * @param msg
     */
    public abstract void sendBLEMessageByAlias(String alias, String msg);

    /**
     * 给指定别名的BLE周边设备发送消息
     *
     * @param alias
     * @param msg
     * @param descript
     */
    public abstract void sendBLEMessageByAlias(String alias, String msg, String descript);

    /**
     * 给指定标签的BLE周边设备发送消息
     *
     * @param tag
     * @param msg
     */
    public abstract void sendBLEMessageByTag(String tag, String msg);

    /**
     * 给指定设备设置别名
     *
     * @param address
     * @param alias
     */
    public abstract void setBLEAlias(String address, String alias);

    /**
     * 给指定设备设置标签
     *
     * @param address
     * @param tag
     */
    public abstract void setBLETag(String address, String tag);

    /**
     * 给指定设备设置解析
     *
     * @param address
     * @param bleDecodeInterface
     */
    public abstract void setBLEDecode(String address, BleDecodeInterface bleDecodeInterface);

    /**
     * 给指定别名的设备设置解析
     *
     * @param alias
     * @param bleDecodeInterface
     */
    public abstract void setBLEDecodeByAlias(String alias, BleDecodeInterface bleDecodeInterface);

    /**
     * 给指定标签的设备设置解析
     *
     * @param tag
     * @param bleDecodeInterface
     */
    public abstract void setBLEDecodeByTag(String tag, BleDecodeInterface bleDecodeInterface);

    /**
     * 给指定设备设置心跳超时时间
     *
     * @param address
     * @param heartTimeout
     */
    public abstract void setBLEHeartTimeOut(String address, long heartTimeout);

    /**
     * 通过别名给指定设备设置心跳超时时间
     *
     * @param alias
     * @param heartTimeout
     */
    public abstract void setBLEHeartTimeOutByAlias(String alias, long heartTimeout);

    /**
     * 通过别名设置设备是否需要保活
     *
     * @param address
     * @param needKeepAlive
     */
    public abstract void keepBLEALive(String address, boolean needKeepAlive);

    /**
     * 通过别名设置设备是否需要保活
     *
     * @param alias
     * @param needKeepAlive
     */
    public abstract void keepBLEALiveByAlias(String alias, boolean needKeepAlive);

    /*****************************************经典蓝牙**************************************************/
    /**
     * 通过蓝牙地址连接经典蓝牙
     *
     * @param address
     */
    public abstract void connectBtServer(BluetoothDevice address);

    /**
     * 断开指定地址的周边设备
     *
     * @param address
     */
    public abstract void disConnectBtServer(String address);

    /**
     * 断开制定别名的周边设备
     *
     * @param alias
     */
    public abstract void disConnectBtServerByAlias(String alias);

    /**
     * 断开指定标签的周边设备
     *
     * @param tag
     */
    public abstract void disConnectBtServerByTag(String tag);

    /**
     * 发送消息给所有周边设备
     *
     * @param msg
     */
    public abstract void sendBtMessage(String msg);

    /**
     * 发送消息给指定地址的设备
     *
     * @param address
     * @param msg
     */
    public abstract void sendBtMessage(String address, String msg);

    /**
     * 给指定别名的BLE周边设备发送消息
     *
     * @param alias
     * @param msg
     */
    public abstract void sendBtMessageByAlias(String alias, String msg);

    /**
     * 给指定别名的BLE周边设备发送消息
     *
     * @param alias
     * @param msg
     * @param descript
     */
    public abstract void sendBtMessageByAlias(String alias, String msg, String descript);

    /**
     * 给指定标签的BLE周边设备发送消息
     *
     * @param tag
     * @param msg
     */
    public abstract void sendBtMessageByTag(String tag, String msg);

    /**
     * 给指定设备设置别名
     *
     * @param address
     * @param alias
     */
    public abstract void setBtAlias(String address, String alias);

    /**
     * 给指定设备设置标签
     *
     * @param address
     * @param tag
     */
    public abstract void setBtTag(String address, String tag);

    /**
     * 给指定设备设置解析
     *
     * @param address
     * @param btDecodeInterface
     */
    public abstract void setBtDecode(String address, BtDecodeInterface btDecodeInterface);

    /**
     * 给指定别名的设备设置解析
     *
     * @param alias
     * @param btDecodeInterface
     */
    public abstract void setBtDecodeByAlias(String alias, BtDecodeInterface btDecodeInterface);

    /**
     * 给指定标签的设备设置解析
     *
     * @param tag
     * @param btDecodeInterface
     */
    public abstract void setBtDecodeByTag(String tag, BtDecodeInterface btDecodeInterface);

    /**
     * 给指定设备设置心跳超时时间
     *
     * @param address
     * @param heartTimeout
     */
    public abstract void setBtHeartTimeOut(String address, long heartTimeout);

    /**
     * 通过别名给指定设备设置心跳超时时间
     *
     * @param alias
     * @param heartTimeout
     */
    public abstract void setBtHeartTimeOutByAlias(String alias, long heartTimeout);

    /**
     * 通过别名设置设备是否需要保活
     *
     * @param address
     * @param needKeepAlive
     */
    public abstract void keepBtALive(String address, boolean needKeepAlive);

    /**
     * 通过别名设置设备是否需要保活
     *
     * @param alias
     * @param needKeepAlive
     */
    public abstract void keepBtALiveByAlias(String alias, boolean needKeepAlive);

}
