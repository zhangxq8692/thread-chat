import java.io.BufferedReader;
import java.io.IOException;


/**
 * @Author xtiwx
 * @Description 客户消息接收模块
 * @Date 2018.04.17 20:47
 * @Version
 */

public class ModelCollect implements Runnable {
    /**
     * 客户socket封装对象
     */
    private Client socket;
    /**
     * 接收消息的客户类型
     */
    private EnuClientType messageToClientType;

    /**
     * 构造方法
     *
     * @param socket              客户socket封装对象
     * @param messageToClientType 接收消息的客户类型
     */
    public ModelCollect(Client socket, EnuClientType messageToClientType) {
        this.socket = socket;
        this.messageToClientType = messageToClientType;
    }

    /**
     * 消息接收处理方法
     */
    public void msgReception() {
        BufferedReader read = socket.getRead();
        while (true) {
            try {
                // 阻塞读取客户端消息
                String msg = read.readLine();
                if (msg == null) {
                    throw new IOException("客户断开连接");
                }
                //两个服务端口这里不同，第一参数表示消息去哪里
                Global.msg.offer(new Msg(messageToClientType, msg));

                //日志输出
                Global.printLog("  <" + socket.getCurrentType()
                        + ">" + "服务器\t收到信息 <"
                        + socket.getClientInfo()
                        + ">\n          >>>>>>>>>>>>>>> "
                        + msg);

            } catch (IOException e) {
                // 客户端离开，移出客户
                Global.clientSockets.remove(socket);
                // 结束线程
                break;
            }
        }
    }

    @Override
    public void run() {
        msgReception();     //消息接收处理方法
    }
}
