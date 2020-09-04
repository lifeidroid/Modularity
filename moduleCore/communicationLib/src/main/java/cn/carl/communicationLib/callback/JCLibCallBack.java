package cn.carl.communicationLib.callback;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/11
 * desc   : SocketClient设备监听
 * version: 1.0
 * ==============================================
 */
public interface JCLibCallBack {
    /**
     * 新Client连接
     *
     * @param address
     */
    void onClientAdd(String address);

    /**
     * Client断开连接
     *
     * @param address Client Ip地址
     * @param alias   Client 别名（如果没有设置则为空）
     */
    void onClientRemove(String address, String alias);

    /**
     * 连接失败
     * @param address Server IP地址
     */
    void onConnectFail(String address);
    /**
     * 有消息接收
     *
     * @param isDeal  数据是否已处理
     * @param address Client IP地址
     * @param alias   Client 别名（如果没有设置则为空）
     * @param tag     Client 标签
     * @param data    接收到的数据
     */
    void onMessageRecived(boolean isDeal, String address, String alias, String tag, String data);
}
