import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Vector;

/**
 * @Author xtiwx
 * @Description 消息发送模块
 * @Date 2018.04.17 20:47
 * @Version
 */

public class ModelMsgSend implements Runnable {
    @Override
    public void run() {
        Msg msgCase = null;
        String msg = null;
        Vector<Client> defClients = null;
        boolean msgIsUse = false;
        while (true) {
            // 定义消息实例变量和消息变量
            if (Global.msg.size() > 0) {
                defClients = new Vector<>(Global.clientSockets);
                // 消费掉消息
                msgCase = Global.msg.poll();
                // 获取消息实例中的消息内容
                msg = msgCase.getMessage();
                for (Client socket : defClients) {
                    // 遍历客户群体发送消息
                    if (msgCase.getGoToClient().equals(socket.getCurrentType())) {
                        PrintWriter send = socket.getOut();
                        switch (msgCase.getGoToClient()) {
                            case APP:
                                // socket客户类型与 msg目标是否一致
                                send.println(msg);
                                msgIsUse = true;
                                break;

                            case HAR:
                                /*-----------------时间同步---------------------*/
                                String time = getDate(socket);
                                if (time != null) {

                                    Global.printLog("  <消息模块>\t与客户 <"
                                            + socket.getClientInfo()
                                            + ">\n          >>>>>>>>>>>>>>> " + "正在同步时间");

                                    send.println(panduan(time));
                                }

                                /*---------------------------------------------*/
                                if (msg.startsWith("a")) {
                                    msg = panduan(msg);
                                }
                                send.println(msg);
                                Global.printLog("  <消息模块>\t发送消息 <"
                                        + socket.getClientInfo()
                                        + ">\n          >>>>>>>>>>>>>>> " + msg);
                                msgIsUse = true;
                                break;
                        }
                    }
                }
                // 如果当前客户群体不存在客户，将消息放回消息队列中
                if (msgIsUse) {
                    msgIsUse = false;
                }else {
                    Global.msg.offer(msgCase);
                }
            }

        }
    }

    /**
     * 处理数据为硬件处理需要的格式
     * @param input
     * @return
     */
    public String panduan(String input) {
        StringBuilder stringbuilder = new StringBuilder(input);
        for (int i = 0; i < 7 - input.length(); i++) {
            stringbuilder.insert(1, "0");
        }
        return stringbuilder.toString();
    }

    /**
     * 获取时间字符串
     *
     * @param clientSocket 客户socket对象，从中获取当前客户最后一次同步时间的时间。
     *                     注：如果当前硬件客户需要应该同步，也就是返回值不为null，
     *                     将把当前同步时间设置回客户socket对象中
     * @return
     */
    public String getDate(Client clientSocket) {
        if (System.currentTimeMillis() > clientSocket.getLastSynTime()) {
            clientSocket.setLastSynTime(System.currentTimeMillis());
            return getTimeStr();
        }
        return null;
    }

    /**
     * 创建硬件需要的时间格式
     * @return
     */
    private String getTimeStr() {
        Calendar now = Calendar.getInstance();
        int h = now.get(Calendar.HOUR_OF_DAY);
        int m = now.get(Calendar.MINUTE);
        int s = now.get(Calendar.SECOND);
        int g = h * 3600 + m * 60 + s;
        return "b" + g;
    }
}