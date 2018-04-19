import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author xtiwx
 * @Description 全局数据存放与方法类
 * @Date 2018.04.17 20:47
 * @Version
 */

public class Global {
    /**
     * 消息存放区
     */
    public static volatile LinkedBlockingQueue<Msg> msg;

    /**
     * 客户socket
     */
    public static volatile Vector<Client> clientSockets;
    /**
     * 消息发送模块是否启动
     */
    public static boolean MsgSendModelStart;

    /**
     * 是否输出日志
     */
    public static boolean log;

    static {
        clientSockets = new Vector<Client>();
        msg = new LinkedBlockingQueue<Msg>();
        MsgSendModelStart = false;
        log = true;
    }

    /**
     * 获取系统时间
     *
     * @return 时间字符串
     */
    public static String getTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(calendar.HOUR_OF_DAY)
                + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND)
                + " " + calendar.get(Calendar.MILLISECOND);
    }

    /**
     * 打印日志
     * 不想在写类了，就写里面了
     *
     * @param content
     */
    public static void printLog(Object content) {
        if (log) {
            System.out.println(Global.getTime() + content);
        }
    }
}
