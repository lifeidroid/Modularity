package cn.carl.communicationLib.constant;

public class JCConst {
    //心跳超时时间
    public static final long HEART_TIMEOUT = 10 * 1000;
    //命令
    public final static String EXTRA_COMMAND = "extra_command";
    //数据
    public final static String EXTRA_DATA = "extra_data";
    //Ip地址
    public final static String EXTRA_ADDRESS = "extra_address";
    //port
    public final static String EXTRA_PORT = "extra_port";
    //alias
    public final static String EXTRA_ALIAS = "extra_alias";
    //tag
    public final static String EXTRA_TAG = "extra_tag";
    //解析策略
    public final static String EXTRA_DECODE = "extra_decode";
    //时间
    public final static String EXTRA_TIME = "extra_time";
    //需要保活检测
    public final static String EXTRA_KEEP_ALIVE = "extra_keep_alive";
    //ble 服务UUID
    public final static String EXTRA_WRITE_UUID = "extra_service_uuid";
    //ble 特征UUID
    public final static String EXTRA_NOTIFY_UUID = "extra_character_uuid";

    /*******************************************命令**********************************************/
    //打开服务端
    public final static int COMMAND_START_SERVER = 1;
    //关闭服务端
    public final static int COMMAND_STOP_SERVER = 2;
    //连接SocketServer
    public final static int COMMAND_CONNECT_SOCKET_SERVER = 35;
    //发送命令
    public final static int COMMAND_SEND_DATA = 5;
    //给指定TAG的Client 发送消息
    public final static int COMMAND_SEND_DATA_BT_TAG = 9;
    //给指定ALIAS的Client 发送消息
    public final static int COMMAND_SEND_DATA_BT_ALIAS = 10;
    //发送命令到所有设备
    public final static int COMMAND_SEND_DATA_ALL = 6;

    //给Client设置alias
    public final static int COMMAND_SET_ALIAS = 7;
    //给Client设置tag
    public final static int COMMAND_SET_TAG = 8;

    //断开指定的Client
    public final static int COMMAND_DISCONNECT_CLIENT = 11;
    //断开所有Client
    public final static int COMMAND_DISCONNECT_ALL = 12;
    //断开指定别名的Client
    public final static int COMMAND_DISCONNECT_BY_ALIAS = 13;
    //断开指定TAG的Client
    public final static int COMMAND_DISCONNECT_BY_TAG = 14;

    //给制定的Client设置解析策略
    public final static int COMMAND_SET_DECODE_BY_ADDRESS = 15;
    //给指定Alias的Client设置解析策略
    public final static int COMMAND_SET_DECODE_BY_ALIAS = 3;
    //给指定Tag的Client设置解析策略
    public final static int COMMAND_SET_DECODE_BY_TAG = 4;
    //给指定设备设置心跳超时时间
    public final static int COMMAND_SET_HEART_TIMEOUT_BY_ADDRESS = 36;
    //通过别名给指定设备设置心跳超时时间
    public final static int COMMAND_SET_HEART_TIMEOUT_BY_ALIAS = 38;
    //给孩子定设备设置保活检测
    public final static int COMMAND_SET_NEED_KEEP_ALIVE = 37;
    //给孩子定设备设置保活检测
    public final static int COMMAND_SET_NEED_KEEP_ALIVE_BY_ALIAS = 39;
    /***ble***/
    //打开BLE
    public final static int COMMAND_START_BLE = 20;
    //关闭BLE
    public final static int COMMAND_STOP_BLE = 21;
    //连接BLE
    public final static int COMMAND_CONNECT_BLE = 22;
    //关闭BLE连接
    public final static int COMMAND_DISCONNECT_BLE = 23;
    //向某个周边设备发送消息
    public final static int COMMAND_SEND_DATA_TO_BLE = 24;
    //向所有周边设备发送消息
    public final static int COMMAND_SEND_DATA_TO_BLE_ALL = 25;
    //给指定周边设备设置别名
    public final static int COMMAND_SET_BLE_ALIAS = 26;
    //关闭指定别名的连接
    public final static int COMMAND_DISCONNECT_BLE_BY_ALIAS = 27;
    //向指定别名周边设备发送消息
    public final static int COMMAND_SEND_DATA_TO_BLE_BY_ALIAS = 28;
    //给指定设备设置解析方式
    public final static int COMMAND_SET_BLE_DECODE = 29;
    //给指定别名的设备设置解析方式
    public final static int COMMAND_SET_BLE_DECODE_BY_ALIAS = 30;
    //给指定标签的设备设置解析方式
    public final static int COMMAND_SET_BLE_DECODE_BY_TAG = 31;
    //向指定标签的设备发送消息
    public final static int COMMAND_SEND_DATA_TO_BLE_BY_TAG = 32;
    //给指定周边设备设置标签
    public final static int COMMAND_SET_BLE_TAG = 33;
    //断开指定标签的周边设备
    public final static int COMMAND_DISCONNECT_BLE_BY_TAG = 34;
    //给指定设备设置心跳超时时间
    public final static int COMMAND_SET_BLE_HEART_TIMEOUT_BY_ADDRESS = 40;
    //通过别名给指定设备设置心跳超时时间
    public final static int COMMAND_SET_BLE_HEART_TIMEOUT_BY_ALIAS = 41;
    //给孩子定设备设置保活检测
    public final static int COMMAND_SET_BLE_NEED_KEEP_ALIVE = 42;
    //给孩子定设备设置保活检测
    public final static int COMMAND_SET_BLE_NEED_KEEP_ALIVE_BY_ALIAS = 43;
    //连接指定经典蓝牙
    public final static int COMMAND_CONNECT_BT_SERVER = 44;
    //关闭BT连接
    public final static int COMMAND_DISCONNECT_BT = 45;
    //向某个周边设备发送消息
    public final static int COMMAND_SEND_DATA_TO_BT = 46;
    //向所有周边设备发送消息
    public final static int COMMAND_SEND_DATA_TO_BT_ALL = 47;
    //给指定周边设备设置别名
    public final static int COMMAND_SET_BT_ALIAS = 48;
    //关闭指定别名的连接
    public final static int COMMAND_DISCONNECT_BT_BY_ALIAS = 49;
    //向指定别名周边设备发送消息
    public final static int COMMAND_SEND_DATA_TO_BT_BY_ALIAS = 50;
    //给指定设备设置解析方式
    public final static int COMMAND_SET_BT_DECODE = 51;
    //给指定别名的设备设置解析方式
    public final static int COMMAND_SET_BT_DECODE_BY_ALIAS = 52;
    //给指定标签的设备设置解析方式
    public final static int COMMAND_SET_BT_DECODE_BY_TAG = 53;
    //向指定标签的设备发送消息
    public final static int COMMAND_SEND_DATA_TO_BT_BY_TAG = 54;
    //给指定周边设备设置标签
    public final static int COMMAND_SET_BT_TAG = 55;
    //断开指定标签的周边设备
    public final static int COMMAND_DISCONNECT_BT_BY_TAG = 56;
    //给指定设备设置心跳超时时间
    public final static int COMMAND_SET_BT_HEART_TIMEOUT_BY_ADDRESS = 57;
    //通过别名给指定设备设置心跳超时时间
    public final static int COMMAND_SET_BT_HEART_TIMEOUT_BY_ALIAS = 58;
    //给孩子定设备设置保活检测
    public final static int COMMAND_SET_BT_NEED_KEEP_ALIVE = 59;
    //给孩子定设备设置保活检测
    public final static int COMMAND_SET_BT_NEED_KEEP_ALIVE_BY_ALIAS = 60;

    /*****************************************Handler********************************************/
    //客户端新增
    public final static int WHAT_CLIENT_ADD = 1;
    //客户端掉线
    public final static int WHAT_CLIENT_REMOVE = 2;
    //接收到客户端消息
    public final static int WHAT_MESSAGE_RECIVED = 3;
    //新的Ble设备添加
    public final static int WHAT_BLE_ADD = 11;
    //Ble设备移除
    public final static int WHAT_BLE_REMOVE = 12;
    //收到Ble设备发来的消息
    public final static int WHAT_BLE_MESSAGE = 13;
    //连接Ble设备失败
    public final static int WHAT_BLE_CONNECT_FAIL = 14;
    //新的BT设备添加
    public final static int WHAT_BT_ADD = 15;
    //BT设备移除
    public final static int WHAT_BT_REMOVE = 16;
    //收到BT设备发来的消息
    public final static int WHAT_BT_MESSAGE = 17;
    //连接BT设备失败
    public final static int WHAT_BT_CONNECT_FAIL = 18;
    //连接SocketServer设备失败
    public final static int WHAT_CONNECT_FAIL = 19;

}
