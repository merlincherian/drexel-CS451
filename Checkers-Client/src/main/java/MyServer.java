import javafx.application.Platform;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.function.Consumer;

public class MyServer extends NetworkConnection {
    private int port;

    public MyServer(int port, Consumer<Serializable> onReceiveCallBack) {
        super(onReceiveCallBack);
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            Checkers.gHostIpAddress = InetAddress.getLocalHost().getHostAddress();
            Checkers.gHostPortNumber = Integer.toString(port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }
}