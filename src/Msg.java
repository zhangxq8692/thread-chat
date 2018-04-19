/**
 * @Author xtiwx
 * @Description 消息封装
 * @Date 2018.04.17 20:47
 * @Version
 */

public class Msg {
    /**
     * 消息去向
     */
    private EnuClientType goToClient;
    /**
     * 消息
     */
    private String message;

    public Msg(EnuClientType toClient, String message) {
        this.goToClient = toClient;
        this.message = message;
    }

    public EnuClientType getGoToClient() {
        return goToClient;
    }

    public String getMessage() {
        return message;
    }

}
