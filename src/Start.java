import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author xtiwx
 * @Description 测试类
 * @Date 2018.04.17 20:47
 * @Version
 */
public class Start {
    public static void main(String[] args) {
        ModelServer app = new ModelServer(6379, EnuClientType.APP, EnuClientType.HAR);
        Thread run = new Thread(app,"app_Core");
        run.start();

        ModelServer har = new ModelServer(6636, EnuClientType.HAR, EnuClientType.APP);
        Thread run1 = new Thread(har,"hard_Core");
        run1.start();



        new Thread("client_hard") {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("127.0.0.1", 6636);
                    Client client = new Client(socket, EnuClientType.HAR);
                    final PrintWriter out = client.getOut(true);
                    out.println("我是hardware");
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            out.println("你好app,我是hard " + Global.getTime());
                        }
                    },8000,8000);
                    while (true) {
                        System.out.println(Global.getTime() + " ================>>hardware收到消息：" + client.getRead().readLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread("client_app") {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("127.0.0.1", 6379);
                    Client client = new Client(socket, EnuClientType.APP);
                    final PrintWriter out = client.getOut(true);
                    out.println("a1088");
                    out.println("我是app");
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            out.println("你好hard,我是app " + Global.getTime());
                        }
                    },5000,5000);
                    while (true) {
                        System.out.println(Global.getTime() + " ================>>app收到消息：" + client.getRead().readLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
