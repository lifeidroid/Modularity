package cn.carl.communicationLib.socket;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.carl.communicationLib.constant.JCConst;
import cn.carl.communicationLib.utils.MyLog;
import cn.carl.communicationLib.utils.StringUtils;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/11
 * desc   : Socket 管理类(当前App为SocketServer端，其他设备连接当前设备)
 * version: 1.0
 * ==============================================
 */
public class SocketMaster {
    private static Map<String, ClientThread> clientList = new ConcurrentHashMap<>();
    private static ServerThread serverThread = null;
    private static volatile SocketMaster instance;
    private Handler handler;
    private ExecutorService mExecutors = null; // 线程池对象
    private Thread keepAliveThread = null;//保活线程
    private boolean isKeepAlive = false;//保活线程标识

    public SocketMaster() {
        mExecutors = Executors.newCachedThreadPool(); // 创建线程池
    }

    public static SocketMaster getInstance() {
        if (instance == null) {
            synchronized (SocketMaster.class) {
                if (instance == null) {
                    instance = new SocketMaster();
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
     * 添加客户端
     *
     * @param address
     * @param socket
     */

    public void addClient(String address, ClientThread socket) {
        clientList.put(address, socket);
        Message message = new Message();
        message.what = JCConst.WHAT_CLIENT_ADD;
        message.obj = address;
        handler.sendMessage(message);
    }

    /**
     * 关闭所有server socket 和 清空Map
     */
    public void shutdown() {
        stopKeepAlive();
        if (null != serverThread) {
            serverThread.interrupt();
            serverThread = null;
        }
        removeAll();
    }

    /**
     * 通过地址发送消息
     *
     * @param address
     * @param msg
     * @return
     */
    public boolean sendMsg(String address, String msg) {
        ClientThread clientThread = clientList.get(address);
        if (null != clientThread) {
            clientThread.sendData(msg);
            return true;
        }
        return false;
    }

    /**
     * 通过别名发送消息
     *
     * @param alias
     * @param msg
     */
    public void sendMsgByAlias(String alias, String msg) {
        Iterator iterator = clientList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getAlias()) && item.getValue().getAlias().equals(alias)) {
                item.getValue().sendData(msg);
            }
        }
    }

    /**
     * 通过标签发送消息
     *
     * @param tag
     * @param msg
     */
    public void sendMsgByTag(String tag, String msg) {
        Iterator iterator = clientList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getTag()) && item.getValue().getTag().equals(tag)) {
                item.getValue().sendData(msg);
            }
        }
    }

    /**
     * 群发消息
     *
     * @param msg
     * @return
     */
    public void sendMsgAll(String msg) {
        Iterator iterator = clientList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            item.getValue().sendData(msg);
        }
    }

    /**
     * 开启SocketServer
     */
    public void startServer(int port) {
        if (serverThread != null) {
            shutdown();
        }
        serverThread = new ServerThread(port);
        serverThread.start();

        startKeepAlive();
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
                    Iterator iterator = clientList.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
                        ClientThread client = item.getValue();
                        if (client.isNeedKeepAlive()) {
                            if (System.currentTimeMillis() - client.getLastHeartBeat() > client.getLimitTime()) {
                                removeTemp.add(client.getAddress());
                                MyLog.d(client.getAddress()+"心跳超时断开连接");
                            }
                        }
                    }
                    //刪除超時的设备
                    if (removeTemp.size() > 0){
                        for (String item:removeTemp){
                            removeClientByAddress(item);
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
        ClientThread clientThread = clientList.get(address);
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
        Iterator iterator = clientList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
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
        ClientThread clientThread = clientList.get(address);
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
        Iterator iterator = clientList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getAlias()) && item.getValue().getAlias().equals(alias)) {
                item.getValue().setLimitTime(timeOut);
            }
        }
    }

    /**
     * 设置最新更新时间
     *
     * @param address
     * @param heartTime
     */
    public void updateHeartTime(String address, long heartTime) {
        ClientThread clientThread = clientList.get(address);
        if (null != clientThread) {
            clientThread.setLastHeartBeat(heartTime);
        }
    }


    /**
     * 通过IP地址和端口号进行连接
     *
     * @param ip
     * @param port
     */
    public void connect(final String ip, final int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket mSocket = new Socket(ip, port);
                    if (mSocket.isConnected()) {
                        MyLog.d("SocketMaster.connect 连接成功->ip:" + ip + "   port:" + port);
                        ClientThread clientThread = new ClientThread(ip, mSocket);
                        clientThread.setLastHeartBeat(System.currentTimeMillis());
                        clientThread.setHandler(handler);
                        mExecutors.execute(clientThread);
                        addClient(ip, clientThread);
                        startKeepAlive();
                    } else {
                        Message message = new Message();
                        message.what = JCConst.WHAT_CONNECT_FAIL;
                        message.obj = ip;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    Message message = new Message();
                    message.what = JCConst.WHAT_CONNECT_FAIL;
                    message.obj = ip;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 去除某个客户端
     *
     * @param address
     */
    public void removeClientByAddress(String address) {
        if (clientList.containsKey(address)) {
            send2Handler(address, clientList.get(address).getAlias());
        }
    }

    /**
     * 断开指定别名的Client
     *
     * @param alias
     */
    public void removeClientByAlias(String alias) {
        Iterator iterator = clientList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getAlias()) && item.getValue().getAlias().equals(alias)) {
                removeClientByAddress(item.getKey());
                return;
            }
        }
    }

    /**
     * 断开指定Tag的Client
     *
     * @param tag
     */
    public void removeClientByTag(String tag) {
        Iterator iterator = clientList.entrySet().iterator();
        List<String> removeList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getTag()) && item.getValue().getTag().equals(tag)) {
                removeList.add(item.getValue().getAddress());
            }
        }
        if (!removeList.isEmpty()) {
            for (String item : removeList) {
                removeClientByAddress(item);
            }
            removeList.clear();
        }
    }

    /**
     * 清空所有已连接Client
     */
    public void removeAll() {
        Iterator iterator = clientList.entrySet().iterator();
        List<String> removeList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            removeList.add(item.getKey());
        }
        if (!removeList.isEmpty()) {
            for (String item : removeList) {
                removeClientByAddress(item);
            }
            removeList.clear();
        }
        clientList.clear();
    }

    public Map<String, ClientThread> getClientList() {
        return clientList;
    }

    /**
     * 给Client设置tag
     *
     * @param address
     * @param tag
     */
    public void setTag2Client(String address, String tag) {
        if (null != clientList.get(address)) {
            clientList.get(address).setTag(tag);
        }
    }

    /**
     * 给Client设置alias
     *
     * @param address
     * @param alias
     */
    public void setAlias2Client(String address, String alias) {
        //去除相同别名的设备
        String mac = isContainAlias(alias);
        if (!StringUtils.isEmpty(mac)) {
            clientList.get(mac).setAlias("other");
            removeClientByAddress(mac);
        }
        if (null != clientList.get(address)) {
            clientList.get(address).setAlias(alias);
        }
    }

    /**
     * 设备列表中是否包含指定别名，如果包含返回设备的MAC地址
     *
     * @param alias
     * @return
     */
    public String isContainAlias(String alias) {
        Iterator iterator = clientList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            if (null != item && null != item.getValue()) {
                if (StringUtils.isEqual(item.getValue().getAlias(), alias)) {
                    return item.getValue().getAddress();
                }
            }
        }
        return null;
    }

    /**
     * 给指定设备设置解析策略
     *
     * @param address
     * @param decodeInterface
     */
    public void setDecode(String address, DecodeInterface decodeInterface) {
        if (null != clientList.get(address)) {
            clientList.get(address).setDecodeInterface(decodeInterface);
        }
    }

    /**
     * 给指定别名的设备设置解析策略
     *
     * @param alias
     * @param decodeInterface
     */
    public void setDecodeByAlias(String alias, DecodeInterface decodeInterface) {
        Iterator iterator = clientList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getAlias()) && item.getValue().getAlias().equals(alias)) {
                item.getValue().setDecodeInterface(decodeInterface);
            }
        }
    }

    /**
     * 给指定标签的设备设置解析策略
     *
     * @param tag
     * @param decodeInterface
     */
    public void setDecodeByTag(String tag, DecodeInterface decodeInterface) {
        Iterator iterator = clientList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ClientThread> item = (Map.Entry<String, ClientThread>) iterator.next();
            if (!StringUtils.isEmpty(item.getValue().getTag()) && item.getValue().getTag().equals(tag)) {
                item.getValue().setDecodeInterface(decodeInterface);
            }
        }
    }

    /**
     * 发送断开连接的通知
     *
     * @param address
     * @param alias
     */
    private synchronized void send2Handler(String address, String alias) {
        if (clientList.containsKey(address)) {
            clientList.get(address).close();
            clientList.remove(address);
            if (null != handler) {
                Message message = new Message();
                message.what = JCConst.WHAT_CLIENT_REMOVE;
                MessageObj messageObj = new MessageObj();
                messageObj.setAddress(address);
                messageObj.setAlias(alias);
                message.obj = messageObj;
                handler.sendMessage(message);
            }
        }
    }

    public ExecutorService getmExecutors() {
        return mExecutors;
    }
}
