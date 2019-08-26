import java.io.Serializable;
import java.util.function.Consumer;

public class MyClient extends NetworkConnection {

    private String ip;
    private int port;

    public MyClient(String ip, int port, Consumer<Serializable> onReceiveCallBack){
        super(onReceiveCallBack);
        System.out.println("Client Connected!");
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
