import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author xtiwx
 * @Description 客户socket对象封装
 * @Date 2018.04.17 20:47
 * @Version
 */

public class Client {
    /**
     * 编码方式
     */
    public static String ENCODING_UTF8 = "UTF-8";
    /**
     * 客户端sock连接实例
     */
    private Socket socket;

    /**
     * 当前实体类型
     */
    private EnuClientType currentType;

    /**
     * 硬件socket对象使用。
     * 最后同步时间的时间
     */
    private Long lastSynTime = System.currentTimeMillis();
    /**
     * 构造方法
     *
     * @param socket      客户socke对象
     * @param currentType 当前对象类型
     */
    public Client(Socket socket, EnuClientType currentType) {
        this.socket = socket;
        this.currentType = currentType;
    }

    /**
     * 获取当前对象类型
     *
     * @return
     */
    public EnuClientType getCurrentType() {
        return currentType;
    }

    /**
     * 获取最后同步时间的Long值
     *
     * @return
     */
    public Long getLastSynTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(lastSynTime));
        // 时间加24小时
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        return calendar.getTime().getTime();
    }

    /**
     * 设置最后同步时间
     *
     * @param lastSynTime 时间
     */
    public void setLastSynTime(Long lastSynTime) {
        this.lastSynTime = lastSynTime;
    }

    /**
     * 获取socket输入流
     *
     * @return
     */
    public BufferedReader getRead() {
        BufferedReader input = null;
        try {
            InputStream in = socket.getInputStream();
            input = new BufferedReader(new InputStreamReader(in, ENCODING_UTF8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * 获取socket输出流
     *
     * @return 输出流
     */
    public PrintWriter getOut() {
        return getOut(true);
    }

    /**
     * 获取socket输出流
     *
     * @return 输出流
     */
    public PrintWriter getOut(boolean autoFlush) {
        PrintWriter output = null;
        try {
            output = new PrintWriter(socket.getOutputStream(), autoFlush);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 获取当前对象的IP和端口信息
     *
     * @return IP与端口字符串
     */
    public String getClientInfo() {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }
}
