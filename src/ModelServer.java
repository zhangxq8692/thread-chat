import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author xtiwx
 * @Description 服务器核心调度类
 * @Date 2018.04.17 20:47
 * @Version
 */

public class ModelServer implements Runnable {
    private ServerSocket serverSocket;
    private EnuClientType currentType;
    private EnuClientType messageToClientType;
    private Integer port;

    /**
     * 构造方法
     *
     * @param port                服务器端口
     * @param currentType         当前服务器服务群体
     * @param messageToClientType 消息发送群体
     */
    public ModelServer(Integer port, EnuClientType currentType, EnuClientType messageToClientType) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.currentType = currentType;
        this.messageToClientType = messageToClientType;
        this.port = port;
    }

    /**
     * 监听客户进入
     */
    public void waitClient() {
        Global.printLog( "  <" + currentType + ":" + port + ">服务器\t启动...");
        try {
            while (true) {
                Global.printLog("  <" + currentType + ">" + "服务器\t等待客户...");

                // 监听端口
                Socket Socket = serverSocket.accept();

                // 创建客户socket封闭对象
                Client clientSocket = new Client(Socket, currentType);

                Global.printLog("  <" + currentType + ">" + "服务器\t客户进入 <"
                        + clientSocket.getClientInfo() + ">");

                // 加入客户到客户列表
                Global.clientSockets.add(clientSocket);

                Global.printLog( "  <" + currentType + ">" + "服务器\t启动客户 <"
                        + clientSocket.getClientInfo() + "> 消息接收模块");

                // 创建客户消息接收对象模块
                ModelCollect instance = new ModelCollect(clientSocket, messageToClientType);

                // 创建新线程启动对刚进入的客户发过消息的接收
                new Thread(instance,"client_"+ clientSocket.getClientInfo()).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //如果消息转发模块未启动就启动，与之相反
        if (!Global.MsgSendModelStart) {
            synchronized (Global.class) {
                if (!Global.MsgSendModelStart) {
                    Global.printLog( "  <消息转发>服务启动...");
                    new Thread(new ModelMsgSend(), "msg_send_model").start();
                    Global.MsgSendModelStart = true;
                }
            }
        }
        waitClient();
    }
}
