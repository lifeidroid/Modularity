package cn.carl.communicationLib.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cn.carl.communicationLib.utils.MyLog;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2018/12/11
 * desc   : SocketServer 监听线程
 * version: 1.0
 * ==============================================
 */
public class ServerThread extends Thread {
    private boolean isExit = false;
    private ServerSocket server;


    public ServerThread(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!isExit && null != server) {
                // 进入等待环节
                final Socket socket = server.accept();
                // 获取手机连接的地址及端口号
                final String address = socket.getRemoteSocketAddress().toString();
                MyLog.d("ServerThread-> 连接成功address：" + address);
                ClientThread clientThread = new ClientThread(address, socket);
                clientThread.setHandler(SocketMaster.getInstance().getHandler());
                SocketMaster.getInstance().getmExecutors().execute(clientThread);
                SocketMaster.getInstance().addClient(address, clientThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Stop() {
        isExit = true;
        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}